package pl.rawinet.detal.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Page<Customer> findAllByOrderById(Pageable pageable);
    Customer findByPhone(String phone);
}
