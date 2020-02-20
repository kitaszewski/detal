package pl.rawinet.detal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.rawinet.detal.model.Customer;
import pl.rawinet.detal.service.CustomerServiceImpl;

import java.util.List;

@RestController
public class ApiController {

    @Autowired
    CustomerServiceImpl customerService;

    @GetMapping("/custlist")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/custlist/{id}")
    public Customer getCustomerById(@PathVariable("id") int id){
        return customerService.getCustomerById(id);
    }
}
