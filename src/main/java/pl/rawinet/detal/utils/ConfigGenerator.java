package pl.rawinet.detal.utils;

import lombok.extern.log4j.Log4j2;
import pl.rawinet.detal.model.Subscription;
import pl.rawinet.detal.model.WifiSettings;

@Log4j2
public class ConfigGenerator {
    private StringBuilder sb;

    public ConfigGenerator() {
        sb = new StringBuilder();
    }

    public String generateDhcpMtk(String customerDescription, Subscription subscription){
        sb.append("/ip/dhcp-server/lease/add address=").append(subscription.getIp())
                .append(" comment=\"").append(customerDescription).append("\" mac-address=").append(subscription.getMacId())
                .append(" server=dhcp-v").append(subscription.getVlan());
        return sb.toString();
    }

    public String generateGpon(int oltPort, int onuId, String ontSn, int lineProfile, int serviceProfile, String mngtIp){
        sb.append("interface gpon-olt 1/").append(oltPort).append("<br>");
        sb.append("create gpon-onu ").append(onuId).append(" sn ").append(ontSn).append(" line-profile-id ")
                .append(lineProfile).append(" service-profile-id ").append(serviceProfile).append("<br>");
        sb.append("quit<br>");
        sb.append("!<br>");
        sb.append("gpon-onu 1/").append(oltPort).append("/").append(onuId).append("<br>");
        sb.append("iphost 1 static address ").append(mngtIp).append(" mask 255.255.255.0  default-gw 10.0.11.1<br>");
        sb.append("quit<br>");
        sb.append("!<br>");
        return sb.toString();
    }

    public String generateOntWifi(Subscription s, WifiSettings w){
        sb.append("gpon-onu 1/").append(s.getOltPort()).append("/").append(s.getOnuId()).append("<br>");
        sb.append("wifi admin disable").append("<br>");
        sb.append("wifi country code 616").append("<br>");
        sb.append("wifi-ap 1 ssid-name ").append(w.getSsid()).append("<br>");
        sb.append("wifi-ap 1 auth mode wpa2").append("<br>");
        sb.append("wifi-ap 1 wpa encryptionmode aes").append("<br>");
        sb.append("wifi-ap 1 wpa key ").append(w.getPassword()).append("<br>");
        sb.append("wifi admin enable").append("<br>");
        sb.append("end<br>");
        sb.append("!<br>");
        return sb.toString();
    }

    public String ontStickerMac(String mac){
        log.info("MAC sticker convert: "+mac);
        mac=mac.toLowerCase();
        int len = mac.length();
        String prefix = mac.substring(0,len -2);
        String lastTwoChars = mac.substring(len - 2, len);
        int mac_value = Integer.decode("0x"+lastTwoChars);
        mac_value+=5;
        sb.setLength(0);
        sb.append(prefix).append(Integer.toHexString(mac_value));
        return formatMac(sb.toString());
    }

    public String formatMac(String mac){
        log.info("MAC format: "+mac);
        sb.setLength(0);
        sb.append(mac.toLowerCase());
        sb.insert(10,':');
        sb.insert(8,':');
        sb.insert(6,':');
        sb.insert(4,':');
        sb.insert(2,':');
        return sb.toString();
    }
}
