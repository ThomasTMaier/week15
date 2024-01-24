package pet.store.service;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

}
