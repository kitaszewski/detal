package pl.rawinet.detal.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.model.Customer;
import pl.rawinet.detal.model.Notice;
import pl.rawinet.detal.model.Subscription;
import pl.rawinet.detal.model.WifiSettings;
import pl.rawinet.detal.service.*;
import pl.rawinet.detal.utils.ConfigGenerator;
import pl.rawinet.detal.utils.MikrotikConnection;
import pl.rawinet.detal.utils.PolishCharsRemover;
import pl.rawinet.detal.utils.TelnetConnection;

import java.io.IOException;

@Controller
@Log4j2
public class SubscriptionController {

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubscriptionServiceImpl subscriptionService;
    @Autowired
    NoticeServiceImpl noticeService;
    @Autowired
    OnuProfileServiceImpl onuProfileService;
    @Autowired
    ConfigVarServiceImpl configVarService;

    @Autowired
    MikrotikConnection mtkConnection;
    @Autowired
    TelnetConnection telnetConnection;

    @GetMapping("/addsubscription/{id}")
    public ModelAndView addSubscriptionForm(@PathVariable("id") int id) {
        ModelAndView m = new ModelAndView();
        m.addObject("customer", customerService.getCustomerById(id));
        m.addObject("subscription", subscriptionService.getSubscriptionByCustomerId(id));
        m.setViewName("addsubscription");
        return m;
    }

    @PostMapping("/addsubscription")
    public ModelAndView saveSubscription(Subscription subscription) {
        ModelAndView m = new ModelAndView();
        if(subscription.getTvPort() == 0 && subscription.isIptv()){
            subscription.setTvPort(1);
        }

        if(subscription.isPublicIp()){
            subscription.setVlan(151);
        }
        else{
            switch (subscription.getOltPort()){
                case 1: subscription.setVlan(161); break;
                case 2: subscription.setVlan(162); break;
                case 3: subscription.setVlan(164); break;
                case 4: subscription.setVlan(164); break;
            }
        }

        subscriptionService.saveOrUpdate(subscription);
        int id = subscription.getCustomerId();
        m.addObject("customer", customerService.getCustomerById(id));
        m.setViewName("redirect:/customer/"+id);
        log.info("Zapis uslug dla klienta id: "+id+' '+subscription.toString());
        return m;
    }

    @GetMapping("/macconvert/{sid}")
    public ModelAndView macConvert(@PathVariable("sid") int sid){
        ModelAndView m = new ModelAndView();
        Subscription s = subscriptionService.getSubscriptionById(sid);
        ConfigGenerator cg = new ConfigGenerator();
        log.info("Przeliczenie MAC z naklejki: "+s.getMacId());
        s.setMacId(cg.ontStickerMac(s.getMacId()));
        m.addObject("customer", customerService.getCustomerById(s.getCustomerId()));
        m.setViewName("redirect:/customer/"+s.getCustomerId());
        return m;
    }

    @GetMapping("/macformat/{sid}")
    public ModelAndView macFormat(@PathVariable("sid") int sid){
        ModelAndView m = new ModelAndView();
        Subscription s = subscriptionService.getSubscriptionById(sid);
        ConfigGenerator cg = new ConfigGenerator();
        log.info("Formatowanie MAC: "+s.getMacId());
        s.setMacId(cg.formatMac(s.getMacId()));
        m.addObject("customer", customerService.getCustomerById(s.getCustomerId()));
        m.setViewName("redirect:/customer/"+s.getCustomerId());
        return m;
    }

    @GetMapping("/getconfdhcp/{id}")
    public ModelAndView getConfigurationDhcp(@PathVariable("id") int id) throws IOException {
        Notice note = new Notice();
        Customer customer = customerService.getCustomerById(id);
        PolishCharsRemover remover = new PolishCharsRemover();
        ConfigGenerator cg = new ConfigGenerator();

        note.setTitle("Wygenerowana konfiguracja DHCP");
        note.setCustomerId(id);

        String customerName = remover.removePlChars(customer.getSurname()+'-'+customer.getName());

        String cfg = cg.generateDhcpMtk(customerName, subscriptionService.getSubscriptionByCustomerId(id));

        note.setMessage(cfg);
        //put config to router
        String routerResult = mtkConnection.sendToMtk(cfg);

        note.setMessage(cfg+"<br>"+routerResult);
        noticeService.saveOrUpdate(note);

        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/customer/"+id);
        log.info("Generowanie DHCP: "+routerResult);
        return m;
    }

    @GetMapping("/getconfgpon/{id}")
    public ModelAndView getConfigurationGpon(@PathVariable("id") int id) throws IOException {
        Notice note = new Notice();
        ConfigGenerator cg = new ConfigGenerator();
        Subscription s = subscriptionService.getSubscriptionById(id);
        int onuNextId;

        if(s.getOnuId() == 0){
            onuNextId = Integer.parseInt(configVarService.getVal("onu.next.id"));
            configVarService.intPlusOne("onu.next.id");
            s.setMngtIp(configVarService.getVal("onu.subnet.prefix")+onuNextId);
            s.setOnuId(onuNextId);
        } else {
            onuNextId = s.getOnuId();
        }

        note.setTitle("Wygenerowana konfiguracja GPON");
        note.setCustomerId(s.getCustomerId());

        String cfg = cg.generateGpon(s.getOltPort()
                ,onuNextId
                ,s.getOntSn()
                ,onuProfileService.bindOnuLineProfile(s).getIdProfile()
                ,onuProfileService.bindOnuServiceProfile(s).getIdProfile()
                ,s.getMngtIp());
        //put config to GPON OLT
        String telnetResult = telnetConnection.sendToOlt(cfg.replace("<br>", "\r\n"));

        note.setMessage(cfg+"<br>"+telnetResult);

        subscriptionService.saveOrUpdate(s);
        noticeService.saveOrUpdate(note);

        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/customer/"+s.getCustomerId());
        log.info("Generowanie GPON: "+telnetResult);
        return m;
    }

    @GetMapping("/unregisteronu/{id}")
    public ModelAndView unregisterOnu(@PathVariable("id") int id) throws IOException {
        Notice note = new Notice();
        Subscription s = subscriptionService.getSubscriptionById(id);

        StringBuilder sb = new StringBuilder();
        sb.append("interface gpon-olt 1/").append(s.getOltPort()).append("<br>");
        sb.append("no create gpon-onu ").append(s.getOnuId()).append("<br>");
        String cfg = sb.toString();

        //put config to GPON OLT
        String telnetResult = telnetConnection.sendToOlt(cfg.replace("<br>", "\r\n"));

        note.setTitle("Wyrejestrowano ONU");
        note.setCustomerId(s.getCustomerId());
        note.setMessage(cfg+"<br>"+telnetResult);
        noticeService.saveOrUpdate(note);

        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/customer/"+s.getCustomerId());
        log.info("Wyrejestrowano ONU");
        return m;
    }

    @PostMapping("/wifisetup")
    public ModelAndView wifiSetUp(WifiSettings wifiSettings) throws IOException {
        Notice note = new Notice();
        ConfigGenerator cg = new ConfigGenerator();
        Subscription s = subscriptionService.getSubscriptionById(wifiSettings.getSubscriptionId());
        log.info("Konfiguracja WiFi id klienta: "+s.getId()+" dane: "+wifiSettings.toString());

        note.setTitle("Ustawienie sieci WiFi");
        note.setCustomerId(s.getCustomerId());

        String cfg = cg.generateOntWifi(s, wifiSettings);
        //put config to GPON OLT
        String telnetResult = telnetConnection.sendToOlt(cfg.replace("<br>", "\r\n"));

        note.setMessage(cfg+"<br>"+telnetResult);
        noticeService.saveOrUpdate(note);

        ModelAndView m = new ModelAndView();
        m.addObject("customer", customerService.getCustomerById(s.getCustomerId()));
        m.setViewName("redirect:/customer/"+s.getCustomerId());
        return m;
    }
}
