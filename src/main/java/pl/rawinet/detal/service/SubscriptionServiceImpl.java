package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rawinet.detal.dao.SubscriptionDao;
import pl.rawinet.detal.model.Subscription;
import pl.rawinet.detal.service.Interfaces.SubscriptionService;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    SubscriptionDao subscriptionDao;

    @Override
    public Subscription getSubscriptionById(Integer id) {
        return subscriptionDao.findById(id).get();
    }

    @Override
    public Subscription getSubscriptionByCustomerId(Integer id) {
        if(subscriptionDao.existsSubscriptionByCustomerId(id)){
            return subscriptionDao.findByCustomerId(id);
        }
        return new Subscription();
    }

    @Override
    public void deleteSubscriptionByCustomerId(Integer id) {
        if(subscriptionDao.existsSubscriptionByCustomerId(id)){
            subscriptionDao.delete(subscriptionDao.findByCustomerId(id));
        }
    }

    @Override
    public void saveOrUpdate(Subscription subscription) {
        subscriptionDao.save(subscription);
    }
}
