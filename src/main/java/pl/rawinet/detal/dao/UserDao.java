package pl.rawinet.detal.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    User findByLogin(String login);
}
