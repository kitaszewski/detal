package pl.rawinet.detal.controller;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.googleApi.SheetsService;
import pl.rawinet.detal.model.Subscription;
import pl.rawinet.detal.service.ConfigVarServiceImpl;
import pl.rawinet.detal.service.CustomerServiceImpl;
import pl.rawinet.detal.service.SubscriptionServiceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Controller
public class GoogleApiController {

    @Autowired
    SheetsService sheet;

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubscriptionServiceImpl subscriptionService;
    @Autowired
    ConfigVarServiceImpl configVarService;

    @GetMapping("/assignip")
    public ModelAndView assignIp(int id) throws IOException, GeneralSecurityException{

        Subscription subscription = subscriptionService.getSubscriptionByCustomerId(id);
        String range = configVarService.getVal("assignip.range."+subscription.getVlan());
        String subnetMask = configVarService.getVal("assignip.subnetmask."+subscription.getVlan());
        String gateway = configVarService.getVal("assignip.gateway."+subscription.getVlan());
        String dstRangeCell = configVarService.getVal("assignip.dst.range.cell."+subscription.getVlan()); //sheet!column for insert client name
        int offset = Integer.parseInt(configVarService.getVal("assignip.offset."+subscription.getVlan()));

        String subnetPrefix = gateway.substring(0,gateway.lastIndexOf('.')+1);

        List<List<Object>> values = sheet.getValuesFromRange(range);

        if(values == null || values.isEmpty()){
            System.out.println("No data found!");
        } else {
            for (List row : values) {
                if (row.size() == 1) {
                    int dstRow = Integer.parseInt(row.get(0).toString());
                    dstRow = dstRow + offset;
                    dstRangeCell = dstRangeCell+dstRow;
                    ValueRange body = new ValueRange()
                            .setValues(Arrays.asList(
                                    Arrays.asList(customerService.getCustomerById(id).getSurname()+' '+customerService.getCustomerById(id).getName())));
                    sheet.writeDataInRange(dstRangeCell, body);

                    subscription.setIp(subnetPrefix+row.get(0));
                    subscription.setSubnetMask(subnetMask);
                    subscription.setGateway(gateway);
                    subscriptionService.saveOrUpdate(subscription);
                    break;
                }
            }
        }
        ModelAndView m = new ModelAndView();
        m.setViewName("redirect:/customer?id="+id);
        return m;
    }
}
