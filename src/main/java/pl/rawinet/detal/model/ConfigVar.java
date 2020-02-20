package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ConfigVar {

    @Id
    @Column(nullable = false, unique = true, columnDefinition="VARCHAR(64)")
    private String varKey;
    private String varValue;
}
