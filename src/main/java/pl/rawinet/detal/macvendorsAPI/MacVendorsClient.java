package pl.rawinet.detal.macvendorsAPI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class MacVendorsClient extends MacAddress {
    @Value("${macvendors.api.path}")
    private String endpointPath;
    private HttpHeaders headers;

    public MacVendorsClient() {
        this.headers = getHttpHeaderWithToken(readToken());
    }

    private String readToken(){
        String token = "";
        try {
            InputStream fs = this.getClass().getClassLoader().getResourceAsStream("macvendors.token");
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            token = br.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    private HttpHeaders getHttpHeaderWithToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);
        return headers;
    }

    public void checkMacVendor() throws IOException {
        HttpEntity httpEntity = new HttpEntity(this.headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = endpointPath+ this.getMac();

        ResponseEntity<String> result = null;
        try {
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            this.vendor = result.getBody();
        } catch (Exception ignored) {
        }
    }
}
