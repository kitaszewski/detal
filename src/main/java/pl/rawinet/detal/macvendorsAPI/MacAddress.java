package pl.rawinet.detal.macvendorsAPI;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MacAddress{
    private String mac;
    protected String vendor;

    public void setMac(String mac) {
        this.mac = mac;
        this.vendor = null;
    }
}
