package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer customerId;
    private String speed;
    private boolean publicIp;
    @Column(nullable = false)
    private boolean wifi = true;
    private boolean iptv;
    private String ontType = "";
    private String ontSn = "";
    private int oltPort;
    private int onuId;
    private String mngtIp = "";
    private String ip = "";
    private String subnetMask = "";
    private String gateway = "";
    private String macId = "";
    private int tvPort;
    private int vlan;

}