package com.example.anniee.customer;

import com.example.anniee.utilities.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/Customer")

public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/add")
    public EntityResponse create(@RequestBody Customer customer) {
        return customerService.create(customer);
   }
    @GetMapping("/getAll")
    public EntityResponse fetchAll() {
        return customerService.fetchAll();
    }

    @DeleteMapping("/delete")
    public EntityResponse delete(@RequestParam Long id) {
        return customerService.delete(id);
    }

    @GetMapping("/findById")
    public EntityResponse findById(@RequestParam Long id) {
        return customerService.findById(id);
    }
    @PutMapping("/update/{customerId}")
    public EntityResponse<Customer> updateCustomer(@RequestBody Customer updatedCustomer){
        return customerService.update(updatedCustomer);
    }
    @PostMapping("/addbulk")
    public EntityResponse addBulk(@RequestBody java.util.List<Customer> customerList) {
        return customerService.addBulkCustomers(customerList);

   }
    @PutMapping("/approveOrReject")
    public EntityResponse approveOrReject(@RequestBody List<ChangeOfStatusDTO> changeOfStatusDTOList, @RequestParam String remarks){
        return customerService.approveOrReject(changeOfStatusDTOList,remarks);
    }
}
