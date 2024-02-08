package com.example.anniee.customer;
import com.example.anniee.utilities.EntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public EntityResponse<Customer> create(Customer customer) {
        EntityResponse<Customer> entityResponse = new EntityResponse<>();
        try {
            customer.setPostedBy("SYSTEM");
            customer.setPostedTime(LocalDateTime.now());
            customer.setPostedFlag('Y');
            Customer savedCustomer = customerRepository.save(customer);
            entityResponse.setMessage("Customer Created Successfully");
            entityResponse.setStatusCode(HttpStatus.CREATED.value());
            entityResponse.setEntity(savedCustomer);
        } catch (Exception e) {
            log.error("An error occurred while creating a customer", e);
            entityResponse.setMessage("An error occurred while creating a customer");
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return entityResponse;
    }

    @Transactional(readOnly = true)
    public EntityResponse<List<Customer>> fetchAll() {
        EntityResponse<List<Customer>> entityResponse = new EntityResponse<>();
        try {
            List<Customer> existingCustomers = customerRepository.findAllByDeletedFlag('N');
            if (!existingCustomers.isEmpty()) {
                entityResponse.setEntity(existingCustomers);
                entityResponse.setMessage("Customers retrieved successfully");
                entityResponse.setStatusCode(HttpStatus.OK.value());
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No customers available");
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while fetching customers", e);
            entityResponse.setMessage("An unexpected error occurred while fetching customers");
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return entityResponse;
    }

    @Transactional
    public EntityResponse<Customer> delete(Long id) {
        EntityResponse<Customer> entityResponse = new EntityResponse<>();
        try {
            Optional<Customer> existingCustomerOptional = customerRepository.findById(id);
            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();
                existingCustomer.setDeletedBy("SYSTEM");
                existingCustomer.setDeletedTime(LocalDateTime.now());
                existingCustomer.setDeletedFlag('Y');
                customerRepository.save(existingCustomer);
                entityResponse.setMessage("Customer deleted successfully");
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setEntity(existingCustomer);
            } else {
                entityResponse.setMessage("Failed to delete customer. Customer not found.");
                entityResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting a customer: {}", e.getMessage());
            entityResponse.setMessage("An error occurred while deleting a customer");
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return entityResponse;
    }

    public EntityResponse<Customer> findById(Long id) {
        EntityResponse<Customer> entityResponse = new EntityResponse<>();

        try {
            Optional<Customer> existingCustomerOptional = customerRepository.findById(id);

            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();
                entityResponse.setMessage("Customer found successfully");
                existingCustomer.setVerifiedBy("SYSTEM");
                existingCustomer.setVerifiedTime(LocalDateTime.now());
                existingCustomer.setVerifiedFlag('Y');
                customerRepository.save(existingCustomer);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setEntity(existingCustomer);
            } else {
                entityResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
                entityResponse.setMessage("Customer not found");
                entityResponse.setEntity(null);
            }
        } catch (Exception e) {
            log.error("An error occurred while finding a customer: " + e.getMessage());
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("An error occurred while finding a customer");
            entityResponse.setEntity(null);
        }

        return entityResponse;
    }

    public EntityResponse update(Customer updatedCustomer) {
        EntityResponse entityResponse = new EntityResponse<>();
        try {
            Optional<Customer> existingCustomerOptional = customerRepository.findById(updatedCustomer.getId());
            if (existingCustomerOptional != null) {
                Long customerId = updatedCustomer.getId();
                if (existingCustomerOptional.isPresent()) {
                    Customer existingCustomer = existingCustomerOptional.get();
                    existingCustomer.setAddress(updatedCustomer.getAddress());
                    existingCustomer.setFirstName(updatedCustomer.getFirstName());
                    existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
                    existingCustomer.setSecondName(updatedCustomer.getSecondName());
                    existingCustomer.setEmail(updatedCustomer.getEmail());
                    existingCustomer.setIdNumber(updatedCustomer.getIdNumber());
                    customerRepository.save(existingCustomer);
                    entityResponse.setMessage("Customer updated successfully");
                    entityResponse.setStatusCode(HttpStatus.OK.value());
                    existingCustomer.setModifiedBy("ADMIN");
                    existingCustomer.setModifiedTime(LocalDateTime.now());
                    existingCustomer.setModifiedFlag('Y');
                    customerRepository.save(existingCustomer);
                    entityResponse.setEntity(existingCustomer);
                } else {
                    entityResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
                    entityResponse.setMessage("Customer not found");
                }
            } else {
                entityResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                entityResponse.setMessage("Updated customer is null");
            }
        } catch (Exception e) {
            log.error("An error occurred while updating the customer: " + e.getMessage());
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("An error occurred while updating the customer");
        }
        return entityResponse;
    }

    public EntityResponse addBulkCustomers(List<Customer> customerList) {
        EntityResponse entityResponse = new EntityResponse<>();
        try {
            List<Customer> existingCustomerOptional = customerRepository.saveAll(customerList);
            entityResponse.setEntity(existingCustomerOptional);
            entityResponse.setMessage("Customers added successfully");
            entityResponse.setStatusCode(HttpStatus.CREATED.value());

        } catch (Exception e) {
            log.error("error{}", e);

        }
        return entityResponse;
    }

    public EntityResponse<List<Customer>> approveOrReject(List<ChangeOfStatusDTO> changeOfStatusDTOList, String remarks) {
        EntityResponse<List<Customer>> response = new EntityResponse<>();
        try {
            if (changeOfStatusDTOList.isEmpty()) {
                response.setMessage("You must provide at least one Customer for approval or rejection");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return response;
            }

            List<Customer> updatedCustomers = new ArrayList<>();
            for (ChangeOfStatusDTO changeOfStatusDTO : changeOfStatusDTOList) {
                Optional<Customer> optionalCustomer = customerRepository.findById(changeOfStatusDTO.getId());
                if (optionalCustomer.isPresent()) {
                    Customer customer = optionalCustomer.get();
                    String status = changeOfStatusDTO.getStatus().toUpperCase();
                    switch (status) {
                        case "APPROVED":
                            customer.setStatus("APPROVED");
                            customer.setHrApprovedBy("SYSTEM");
                            customer.setHrApprovedOn(LocalDateTime.now());
                            customer.setHrApprovedFlag('Y');
                            customer.setRemarks(remarks);
                            break;
                        case "REJECTED":
                            customer.setStatus("REJECTED");
                            customer.setHrApprovedFlag('N');
                            customer.setHrApprovedBy(null);
                            customer.setHrApprovedOn(null);
                            break;
                        case "RETURNED":
                            customer.setStatus("RETURNED");
                            break;
                        default:
                            response.setMessage("Invalid Status provided: " + status);
                            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                            return response;
                    }
                    updatedCustomers.add(customerRepository.save(customer));
                } else {
                    response.setMessage("No Customer found with such an Id: " + changeOfStatusDTO.getId());
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    return response;
                }
            }
            response.setMessage("Customer Status updated Successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setEntity(updatedCustomers);
        } catch (Exception e) {
            log.error(e.toString());
            response.setMessage("An unexpected error occurred while updating status");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }
}
