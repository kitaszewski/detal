package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rawinet.detal.dao.DBFileDao;
import pl.rawinet.detal.model.DBFile;
import pl.rawinet.detal.service.Interfaces.DBFileService;

@Service
public class DBFileServiceImpl implements DBFileService {

    @Autowired
    DBFileDao dbFileDao;

    @Override
    public DBFile storeFile(DBFile inputFile) {
        return dbFileDao.saveAndFlush(inputFile);
    }

    @Override
    public DBFile getFile(Integer fileId) {
        return dbFileDao.findById(fileId).get();
    }

    @Override
    public void deleteFileByCustomerId(Integer id) {
        if (dbFileDao.existsDbfileByCustomerId(id)){
            dbFileDao.findAllByCustomerId(id).forEach(e -> dbFileDao.delete(e));
        }
    }
}
