package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String surname;
    private String address;
    private String installationAddress = "";
    private String email;
    private String phone;
    private String pesel;
    private String idCardNumber;
    @Column(nullable = false)
    private boolean business=false;
}
