package pl.rawinet.detal.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@NoArgsConstructor
public class TelnetConnection {

    @Value("${device.olt.ip}")
    private String ip;
    @Value("${device.olt.port}")
    private Integer port;
    @Value("${device.olt.user}")
    private String user;
    @Value("${device.olt.password}")
    private String password;

    public String sendToOlt(String cmd) throws IOException {

        TelnetClient tc = new TelnetClient();
        System.out.println(cmd);

        try {
            tc.connect(ip, port);
        } catch (Exception e) {
            return "Połączenie: "+e.getMessage();
        }

//        InputStream in = tc.getInputStream();
        OutputStream out = tc.getOutputStream();

        String loginCmd = user+"\r\n"+password+"\r\nen\r\nminolta\r\nconf t\r\n";
        try {
            out.write(loginCmd.getBytes());
        } catch (Exception e) {
            return "Logowanie: "+e.getMessage();
        }

        try {
            out.write(cmd.getBytes());
        } catch (Exception e) {
            return "Komenda: "+e.getMessage();
        }

        String logoutCmd = "end\r\nwrite startup-config\r\nexit\r\nexit\r\n";
        try {
            out.write(logoutCmd.getBytes());
//            tc.disconnect();
        } catch (Exception e) {
            return "Zamknięcie: "+e.getMessage();
        }

        return "Wysłano do OLT.";
    }

//    odczyt z bufora
//    byte[] buff = new byte[1024];
//    int ret_read = 0;
//    do
//    {
//        ret_read = in.read(buff);
//        if(ret_read > 0)
//        {
//            System.out.print(new String(buff, 0, ret_read));
//        }
//    }
//    while (ret_read >= 0);
}

