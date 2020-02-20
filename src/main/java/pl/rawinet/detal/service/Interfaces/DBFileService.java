package pl.rawinet.detal.service.Interfaces;

import pl.rawinet.detal.model.DBFile;

public interface DBFileService {
    public DBFile storeFile(DBFile inputFile);
    public DBFile getFile(Integer fileId);
    public void deleteFileByCustomerId(Integer id);
}
