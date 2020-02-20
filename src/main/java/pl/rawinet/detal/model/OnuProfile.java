package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class OnuProfile {

    @Id
    private int id;
    private int idProfile;
    private String name;
    private boolean publicIp;
    private boolean wifi;
    private boolean iptv;
    private String speed;
    private String type;
    private int tvPort;
    private int vlan;
}
