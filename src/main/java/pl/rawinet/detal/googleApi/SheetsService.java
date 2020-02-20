package pl.rawinet.detal.googleApi;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class SheetsService {
    @Autowired
    GoogleSheetsAccess sheetsAccess;

    public List<List<Object>> getValuesFromRange(String range) throws IOException, GeneralSecurityException {
        Sheets sheets = sheetsAccess.getSheetsService();
        ValueRange response = sheets.spreadsheets().values().get(sheetsAccess.getSpreadsheetId(), range).execute();
        return response.getValues();
    }

    public void writeDataInRange(String range, ValueRange data) throws IOException, GeneralSecurityException {
        Sheets sheets = sheetsAccess.getSheetsService();
        UpdateValuesResponse result = sheets.spreadsheets().values()
                .update(sheetsAccess.getSpreadsheetId(), range, data)
                .setValueInputOption("RAW")
                .execute();
    }
}
