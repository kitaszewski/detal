package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Sms {
    private String number;
    private String message;
    private String sender;
    private Integer id_customer;

    public Sms(String number, String message, String sender) {
        this.number = number;
        this.message = message;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "number='" + number + '\'' +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", id_customer=" + id_customer +
                '}';
    }
}
