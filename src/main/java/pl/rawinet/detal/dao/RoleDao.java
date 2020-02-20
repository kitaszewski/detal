package pl.rawinet.detal.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByRole(String role);
}
