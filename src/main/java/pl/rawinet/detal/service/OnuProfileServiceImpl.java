package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rawinet.detal.dao.OnuProfileDao;
import pl.rawinet.detal.model.OnuProfile;
import pl.rawinet.detal.model.Subscription;
import pl.rawinet.detal.service.Interfaces.OnuProfileService;

@Service
public class OnuProfileServiceImpl implements OnuProfileService {
    @Autowired
    OnuProfileDao onuProfileDao;

    @Override
    public OnuProfile bindOnuLineProfile(Subscription s) {
        return onuProfileDao.bindOnuLineProfile(s.isPublicIp(), s.isIptv(), s.getSpeed(), s.getVlan());
    }

    @Override
    public OnuProfile bindOnuServiceProfile(Subscription s) {
        return onuProfileDao.bindOnuServiceProfile(s.isPublicIp(), s.isIptv(), s.isWifi(), s.getTvPort(), s.getVlan());
    }
}
