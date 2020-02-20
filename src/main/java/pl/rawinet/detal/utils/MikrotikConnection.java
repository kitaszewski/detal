package pl.rawinet.detal.utils;

import lombok.NoArgsConstructor;
import me.legrange.mikrotik.ApiConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MikrotikConnection {

    @Value("${device.mikrotik.ip}")
    private String ip;
    @Value("${device.mikrotik.user}")
    private String user;
    @Value("${device.mikrotik.password}")
    private String password;

    public String sendToMtk(String cmd){
        ApiConnection con;
        try {
            con = ApiConnection.connect(ip); // connect to router
            con.login(user,password); // log in to router
        } catch (Exception e) {
            return "Polaczenie "+e.getMessage();
        }
        try {
            con.execute(cmd); // execute a command
        } catch (Exception e) {
            return "Komenda "+e.getMessage();
        }
        try {
            con.close();
        } catch (Exception e) {
            return "Zamkniecie "+e.getMessage();
        }
        return "Wysłano poprawnie";
    }
}
