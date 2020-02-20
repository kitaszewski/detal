package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rawinet.detal.dao.ConfigVarDao;
import pl.rawinet.detal.model.ConfigVar;
import pl.rawinet.detal.service.Interfaces.ConfigVarService;

@Service
public class ConfigVarServiceImpl implements ConfigVarService {

    @Autowired
    ConfigVarDao configVarDao;

    @Override
    public String getVal(String key) {
        return configVarDao.findById(key).get().getVarValue();
    }

    @Override
    public void setVal(String key, String val) {
        ConfigVar property = configVarDao.findById(key).get();
        property.setVarValue(val);
        configVarDao.save(property);
    }

    @Override
    public void intPlusOne(String key) {
        ConfigVar property = configVarDao.findById(key).get();
        Integer a = Integer.parseInt(property.getVarValue());
        a++;
        property.setVarValue(a.toString());
        configVarDao.save(property);

    }
}
