package pl.rawinet.detal.service.Interfaces;


import pl.rawinet.detal.model.Subscription;

public interface SubscriptionService {
    public Subscription getSubscriptionById(Integer id);
    public Subscription getSubscriptionByCustomerId(Integer id);
    public void deleteSubscriptionByCustomerId(Integer id);
    public void saveOrUpdate(Subscription subscription);
}
