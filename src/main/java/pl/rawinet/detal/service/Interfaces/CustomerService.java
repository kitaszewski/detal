package pl.rawinet.detal.service.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.rawinet.detal.model.Customer;
import java.util.List;

public interface CustomerService {
    public List<Customer> getAllCustomers();
    public Customer getCustomerById(Integer id);
    public Customer getCustomerByPhone(String phone);
    public Customer saveOrUpdate(Customer Customer);
    public void deleteCustomer(Integer id);
    public Page<Customer> getAllCustomers(Pageable pageable);
}
