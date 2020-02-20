package pl.rawinet.detal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.model.Customer;
import pl.rawinet.detal.model.Response;
import pl.rawinet.detal.model.Sms;
import pl.rawinet.detal.service.CustomerServiceImpl;
import pl.rawinet.detal.utils.SmsgateConnection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SmsController {
    @Autowired
    CustomerServiceImpl customerService;

    @Value("${smsgate.host}")
    private String smsgateHost;
    @Value("${smsgate.http}")
    private String smsgateHttp;

    @Autowired
    SmsgateConnection smsgate;

    @GetMapping(path = {"/smsbox", "/smsbox/{box}"})
    public ModelAndView showSmsBoxes(@PathVariable(name = "box", required = false) String boxParam) {
        String box = StringUtils.isEmpty(boxParam) ? "sent" : boxParam;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView m = new ModelAndView();
        m.setViewName("smsbox");
        m.addObject("sms2send", new Sms(null, " Wiadomosc automatyczna Rawinet", "Detal_"+authentication.getName()));
        m.addObject("view", box);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity requestEntity =  new HttpEntity(null, smsgate.LoginSmsgate());

        String urlSent=smsgate.GetEndpointUrl("sentitems/"+authentication.getName());
        ResponseEntity<Sms[]> resultSent = restTemplate.exchange(urlSent, HttpMethod.GET, requestEntity, Sms[].class);
        m.addObject("sent", resultSent.getBody().length);

        String urlInbox=smsgate.GetEndpointUrl("inbox");
        ResponseEntity<Sms[]> resultInbox = restTemplate.exchange(urlInbox, HttpMethod.GET, requestEntity, Sms[].class);
        m.addObject("inbox", resultInbox.getBody().length);

        String urlGetAlarms=smsgate.GetEndpointUrl("alarms");
        ResponseEntity<Response> resultAlarms = restTemplate.exchange(urlGetAlarms, HttpMethod.GET, requestEntity, Response.class);
        m.addObject("alarms", resultAlarms.getBody().getStatus());

        switch (box){
            case "inbox": m.addObject("smslist", convertResultToList(resultInbox)); break;
            case "sent":
            default: m.addObject("smslist", convertResultToList(resultSent));
        }

        return m;
    }

    @GetMapping("/cleanalarms")
    public ModelAndView showSmsBoxes() {
        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/smsbox/sent");

        HttpEntity requestEntity =  new HttpEntity(null, smsgate.LoginSmsgate());
        RestTemplate restTemplate = new RestTemplate();
        String url=smsgate.GetEndpointUrl("clear");
        ResponseEntity<Response> result = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Response.class);
        return m;
    }

    @PostMapping("/newsms")
    public ModelAndView sendoutSms(Sms sms){
        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/smsbox/sent");

        HttpEntity requestEntity =  new HttpEntity(sms, smsgate.LoginSmsgate());
        RestTemplate restTemplate = new RestTemplate();
        String url=smsgate.GetEndpointUrl("new");
        ResponseEntity<Response> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        return m;
    }

    private List<Sms> convertResultToList(ResponseEntity<Sms[]> result){
        List<Sms> smsList = Arrays.stream(result.getBody()).map(e -> {
            Customer c = customerService.getCustomerByPhone(e.getNumber());
            try{
                e.setNumber(c.getSurname()+' '+c.getName());
                e.setId_customer(c.getId());
            } catch (Exception exp){
                e.setId_customer(0);
            }
            return e;
        }).collect(Collectors.toList());
        return smsList;
    }
}

//        Sms sms = new Sms("506477890", "test post api "+dateFormat.format(date), "Detal_piotr");
//        ResponseEntity<Response> result = restTemplate.postForEntity(url, sms, Response.class);
//        Response response = result.getBody();
//        ObjectMapper mapper = new ObjectMapper();
//        Sms sms1 = mapper.readValue(mapper.writeValueAsString(response.getData()), Sms.class);