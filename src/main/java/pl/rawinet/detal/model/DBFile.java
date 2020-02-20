package pl.rawinet.detal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "files")
public class DBFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer customerId;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    public DBFile(int cid, String fileName,  byte[] data, String fileType) {
        this.customerId = cid;
        this.fileName = fileName;
        this.data = data;
        this.fileType = fileType;
    }
}
