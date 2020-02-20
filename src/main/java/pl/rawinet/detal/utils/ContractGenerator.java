package pl.rawinet.detal.utils;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.*;
import pl.rawinet.detal.model.AddInfoForContract;
import pl.rawinet.detal.model.Customer;
import pl.rawinet.detal.model.Subscription;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@NoArgsConstructor
@Setter
public class ContractGenerator {

    private Map<String, String> replaceMap;

    public Map<String, String> prepareContractDetails(Customer customer, Subscription subscription, AddInfoForContract info){
        Map<String, String> detailsMap = new HashMap<String, String>();
        detailsMap.put("xname", customer.getName());
        detailsMap.put("xsurname", customer.getSurname());
        detailsMap.put("xaddress", customer.getAddress());
        if (customer.getInstallationAddress().isEmpty()) {
            detailsMap.put("xinstalladdress", customer.getAddress());
        } else {
            detailsMap.put("xinstalladdress", customer.getInstallationAddress());
        }
        detailsMap.put("xphone", customer.getPhone());
        if(customer.isBusiness()){
            detailsMap.put("xidcardnumber", "nie dotyczy");
            detailsMap.put("xpeselnip", "NIP");
        } else {
            detailsMap.put("xidcardnumber", "dowód osobisty "+customer.getIdCardNumber());
            detailsMap.put("xpeselnip", "PESEL");
        }

        detailsMap.put("xpesel", customer.getPesel());
        detailsMap.put("xmail", customer.getEmail());
        detailsMap.put("xcustomerip", subscription.getIp());
        detailsMap.put("xsubnetmask", subscription.getSubnetMask());
        detailsMap.put("xgateway", subscription.getGateway());
        detailsMap.put("xonttype", subscription.getOntType());
        detailsMap.put("xontmodel", subscription.getOntType());
        detailsMap.put("xontsn", subscription.getOntSn());
        detailsMap.put("xinstallationdate", info.getInstallationDate());

        String isPublicIp;
        if(subscription.isPublicIp()) {isPublicIp="TAK";} else {isPublicIp="NIE";}
        detailsMap.put("xispublicip", isPublicIp);

        String isWifi;
        if(subscription.isWifi()) {isWifi="TAK";} else {isWifi="NIE";}
        detailsMap.put("xiswifi", isWifi);

        String xspeed1="", xspeed2="", xspeed3="";
        int xactivationfee=0, xonetimefee=0;
        double xtotalsubscriptionvalue=0, publicIpFee=0, subscriptionFee=0;
        switch (subscription.getSpeed()){
            case "50/15":
                xspeed1 = "X";
                xactivationfee = 299;
                subscriptionFee = 39.99;
                publicIpFee = 9.99;
                break;
            case "150/20":
                xspeed2 = "X";
                xactivationfee = 249;
                subscriptionFee = 59.99;
                publicIpFee = 4.99;
                break;
            case "300/30":
                xspeed3 = "X";
                xactivationfee = 149;
                subscriptionFee = 89.99;
                publicIpFee = 4.99;
                break;
        }
        detailsMap.put("xspeed1", xspeed1);
        detailsMap.put("xspeed2", xspeed2);
        detailsMap.put("xspeed3", xspeed3);

        if(subscription.isPublicIp()){
            xtotalsubscriptionvalue = subscriptionFee + publicIpFee;
        } else {
            xtotalsubscriptionvalue = subscriptionFee;
        }

        xonetimefee = xactivationfee + info.getInstallationFee() - info.getDiscount();

        DecimalFormat df2 = new DecimalFormat("#.##");
        detailsMap.put("xtotalsubscriptionvalue", df2.format(xtotalsubscriptionvalue));
        detailsMap.put("xactivationfee", Integer.toString(xactivationfee));
        detailsMap.put("xinstallationfee", Integer.toString(info.getInstallationFee()));
        detailsMap.put("xdiscount", Integer.toString(info.getDiscount()));
        detailsMap.put("xonetimefee", Integer.toString(xonetimefee));

        return detailsMap;
    }

    public String generateProtocol(String path, String docType) throws IOException {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        PolishCharsRemover remover = new PolishCharsRemover();
        String customerName = remover.removePlChars(replaceMap.get("xsurname")+'_'+replaceMap.get("xname"));

        String outputName = customerName+'_'+docType+"_"+dateFormat.format(date)+".docx";

        FileInputStream inStream = new FileInputStream(path+"template_"+docType+".docx");
        FileOutputStream outStream = new FileOutputStream(path+outputName);
        XWPFDocument docx = new XWPFDocument(inStream);

        // tresc dokumentu
        for (XWPFParagraph p : docx.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if(text != null){
                        for(Map.Entry<String,String> entry : replaceMap.entrySet() ){
                            if (text.contains(entry.getKey())) {
                                text = text.replace(entry.getKey(), entry.getValue());
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }

        //zawartosc tabel
        for (XWPFTable tbl : docx.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        List<XWPFRun> runs = p.getRuns();
                        if (runs != null) {
                            for (XWPFRun r : runs) {
                                String text = r.getText(0);
                                if(text != null){
                                    for(Map.Entry<String,String> entry : replaceMap.entrySet() ){
                                        if (text.contains(entry.getKey())) {
                                            text = text.replace(entry.getKey(), entry.getValue());
                                            r.setText(text, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        docx.write(outStream);
        docx.close();

        inStream.close();
        outStream.close();
        return outputName;
    }
}
