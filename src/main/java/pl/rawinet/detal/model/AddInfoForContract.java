package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class AddInfoForContract {
    private int customerId;
    private String installationDate;
    private int installationFee;
    private int discount;
}
