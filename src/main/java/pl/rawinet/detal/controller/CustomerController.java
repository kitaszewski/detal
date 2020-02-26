package pl.rawinet.detal.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.macvendorsAPI.MacVendorsClient;
import pl.rawinet.detal.model.*;
import pl.rawinet.detal.service.*;
import pl.rawinet.detal.utils.ContractGenerator;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class CustomerController {

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubscriptionServiceImpl subscriptionService;
    @Autowired
    NoticeServiceImpl noticeService;
    @Autowired
    ConfigVarServiceImpl configVarService;
    @Autowired
    AddInfoForContract addInfoForContract;
    @Autowired
    DBFileServiceImpl dbFileService;
    @Autowired
    WifiSettings wifiSettings;
    @Autowired
    MacVendorsClient mac;

    @GetMapping("/customers")
    public ModelAndView showCustomerList() {
        ModelAndView m = new ModelAndView();
        m.setViewName("customers");
        return m;
    }

    @GetMapping("/customer/{id}")
    public ModelAndView showCustomerDetails(@PathVariable("id") int id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView m = new ModelAndView();
        Subscription subscription = subscriptionService.getSubscriptionByCustomerId(id);
        mac.setMac(subscription.getMacId());
        mac.checkMacVendor();

        m.addObject("subscription", subscription);
        m.addObject("emptynotice", new Notice());
        m.addObject("notices", noticeService.getAllCustomersNotices(id));
        m.addObject("customer", customerService.getCustomerById(id));
        m.addObject("addinfoforcontract", addInfoForContract);
        m.addObject("wifisettings", wifiSettings);
        m.addObject("sms2send", new Sms(null, " Wiadomosc automatyczna Rawinet", "Detal_"+authentication.getName()));
        m.addObject("macvendor", mac.getVendor());

        m.setViewName("customer");
        return m;
    }

    @GetMapping("/addcustomer")
    public ModelAndView addCustomerForm() {
        ModelAndView m = new ModelAndView();
        m.addObject("customer", new Customer());
        m.setViewName("addcustomer");
        return m;
    }

    @PostMapping("/addcustomer")
    public ModelAndView saveCustomer(@Valid Customer customer, BindingResult result) {
        ModelAndView m = new ModelAndView();
        if(result.hasErrors()){
            m.addObject("customer", customer);
            m.setViewName("addcustomer");
            List<String> errors = result.getFieldErrors().stream().map(r -> r.getDefaultMessage()).collect(Collectors.toList());
            m.addObject("errors", errors);
        } else {
            Customer addedCustomer = customerService.saveOrUpdate(customer);
            m.addObject("customer", addedCustomer);
            m.setViewName("redirect:/customer/" + addedCustomer.getId());
        }
        log.info("Zapis klienta: "+customer.toString());
        return m;
    }

    @GetMapping("/editcustomer/{id}")
    public ModelAndView editCustomerForm(@PathVariable("id") int id) {
        ModelAndView m = new ModelAndView();
        m.addObject("customer", customerService.getCustomerById(id));
        m.setViewName("addcustomer");
        return m;
    }

    @GetMapping("/delcustomer/{id}")
    public ModelAndView delCustomer(@PathVariable("id") int id) {
        ModelAndView m = new ModelAndView();
        noticeService.deleteAllCustomersNotices(id);
        subscriptionService.deleteSubscriptionByCustomerId(id);
        dbFileService.deleteFileByCustomerId(id);
        customerService.deleteCustomer(id);
        m.addObject("customers", customerService.getAllCustomers());
        m.setViewName("redirect:/customers");
        return m;
    }

    @PostMapping("/addnotice")
    public ModelAndView saveNotice(Notice notice) {
        noticeService.saveOrUpdate(notice);
        ModelAndView m = new ModelAndView();
        int id = notice.getCustomerId();
        m.addObject("customers", customerService.getCustomerById(id));
        m.setViewName("redirect:/customer/" + id);
        return m;
    }

    @PostMapping("/gencontract")
    public ModelAndView genContract(AddInfoForContract info) throws IOException {
        log.info("Generowanie dokumentów, dane do umowy: "+info.toString());
        int id = info.getCustomerId();

        Notice notice = new Notice();
        notice.setTitle("Wygenerowane dokumenty");
        notice.setCustomerId(id);

        ContractGenerator cg = new ContractGenerator();
        cg.setReplaceMap(cg.prepareContractDetails(customerService.getCustomerById(id),
                subscriptionService.getSubscriptionByCustomerId(id),
                info
        ));
        String path = configVarService.getVal("contract.path.template");
        String contractFileName = cg.generateProtocol(path, "umowa");
        String protocolFileName = cg.generateProtocol(path, "protokol");

        DBFile contractToDb = new DBFile(id, contractFileName, Files.readAllBytes(Paths.get(path + contractFileName)), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contractToDb = dbFileService.storeFile(contractToDb);
        DBFile protocolToDb = new DBFile(id, protocolFileName, Files.readAllBytes(Paths.get(path + protocolFileName)), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        protocolToDb = dbFileService.storeFile(protocolToDb);

        StringBuilder message = new StringBuilder();
        message.append("Wygenerowana umowa: <a href='/detal/download/").append(contractToDb.getId()).append("'>").append(contractFileName).append("</a><br>");
        message.append("Wygenerowany prokół: <a href='/detal/download/").append(protocolToDb.getId()).append("'>").append(protocolFileName).append("</a><br>");

        notice.setMessage(message.toString());
        noticeService.saveOrUpdate(notice);

        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/customer/" + id);
        return m;
    }
}
