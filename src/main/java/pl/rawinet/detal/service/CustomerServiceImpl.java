package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rawinet.detal.dao.CustomerDao;
import pl.rawinet.detal.model.Customer;
import pl.rawinet.detal.service.Interfaces.CustomerService;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerDao customerDao;

    @Override
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerDao.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customerDao.findById(id).get();
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        return customerDao.findByPhone(phone);
    }

    @Override
    public Customer saveOrUpdate(Customer customer) {
        return customerDao.saveAndFlush(customer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerDao.delete(customerDao.findById(id).get());
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {return customerDao.findAllByOrderById(pageable);}
}
