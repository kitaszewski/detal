package pl.rawinet.detal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.DBFile;

import java.util.List;

@Repository
public interface DBFileDao extends JpaRepository<DBFile, Integer> {
    boolean existsDbfileByCustomerId(Integer id);
    List<DBFile> findAllByCustomerId(Integer id);
}
