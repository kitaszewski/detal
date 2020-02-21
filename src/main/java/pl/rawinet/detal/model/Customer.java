package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @NotBlank(message = "Wprowadź imię nazwisko / nazwę firmy.")
    private String surname;
    private String address;
    private String installationAddress = "";
    @Pattern(regexp="^$|.+@.+\\...+", message="Niepoprawny format adresu email.")
    private String email;
    @Pattern(regexp="^$|^[1-9][0-9]{8}$", message="Niepoprawny format telefonu.")
    private String phone;
    private String pesel;
    private String idCardNumber;
    @Column(nullable = false)
    private boolean business=false;
}
