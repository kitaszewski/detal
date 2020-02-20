package pl.rawinet.detal.googleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Repository
public class GoogleSheetsAccess {
    private static final String APPLICATION_NAME = "Rawinet Detal";
    private String SPREADSHEET_ID = "1yU3kITv2crChz1_ifG9pJyCO1Kti5rchGXPrbtT4V54";   //adresacja IP

    public static Credential credential;

    public String getSpreadsheetId() {
        return SPREADSHEET_ID;
    }

    public static Credential getCredential() throws IOException {
        if (credential == null) {
            InputStream is = GoogleSheetsAccess.class
                    .getResourceAsStream("/detal-servicekey.json");
            credential = GoogleCredential.fromStream(is)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        }
        return credential;
    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException{
        credential=getCredential();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
