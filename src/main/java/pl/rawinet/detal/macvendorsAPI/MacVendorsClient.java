package pl.rawinet.detal.macvendorsAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;

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

    public String checkFullInfo() {
        HttpEntity httpEntity = new HttpEntity(this.headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = endpointPath+ this.getMac();

        ResponseEntity<JsonNode> result = null;
        try {
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JsonNode.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(result.getBody().toString());
            return node.at("/data/organization_name").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Not found";
        }
    }
}
