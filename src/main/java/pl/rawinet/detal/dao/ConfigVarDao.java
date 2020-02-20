package pl.rawinet.detal.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.ConfigVar;

@Repository
public interface ConfigVarDao extends CrudRepository<ConfigVar, String> {
}
