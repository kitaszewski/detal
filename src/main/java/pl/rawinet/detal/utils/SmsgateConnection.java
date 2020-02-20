package pl.rawinet.detal.utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.rawinet.detal.model.Response;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsgateConnection {

    @Value("${smsgate.host}")
    private String smsgateHost;
    @Value("${smsgate.http}")
    private String smsgateHttp;
    @Value("${smsgate.username}")
    private String user;
    @Value("${smsgate.password}")
    private String password;

    RestTemplate restTemplate;
    JSONObject credentialsJson;

    public SmsgateConnection() {
        this.restTemplate = new RestTemplate();
    }

    public HttpHeaders LoginSmsgate(){
        SetCredentials();

        ResponseEntity<Response> resultLogin = restTemplate.postForEntity(GetEndpointUrl("login"), credentialsJson.toString(), Response.class);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", resultLogin.getHeaders().getFirst("Set-Cookie"));
        return requestHeaders;
    }

    private void SetCredentials(){
        Map<String, String> cridentials = new HashMap<>();
        cridentials.put("username", user);
        cridentials.put("password", password);
        this.credentialsJson = new JSONObject(cridentials);
    }

    public String GetEndpointUrl(){
        return smsgateHttp+"://"+smsgateHost+"/smsgate/";
    }

    public String GetEndpointUrl(String endpoint){
        return smsgateHttp+"://"+smsgateHost+"/smsgate/"+endpoint;
    }
}
