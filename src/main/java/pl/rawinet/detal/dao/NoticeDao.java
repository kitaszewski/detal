package pl.rawinet.detal.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.Notice;

import java.util.List;

@Repository
public interface NoticeDao extends CrudRepository<Notice, Integer> {
    boolean existsNoticeByCustomerId(Integer id);
    List<Notice> findAllByCustomerIdOrderByIdDesc(Integer id);
}
