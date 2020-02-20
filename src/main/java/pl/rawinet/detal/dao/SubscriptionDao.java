package pl.rawinet.detal.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.Subscription;

@Repository
public interface SubscriptionDao extends CrudRepository<Subscription, Integer> {
    Subscription findByCustomerId(Integer id);
    boolean existsSubscriptionByCustomerId(Integer id);
}
