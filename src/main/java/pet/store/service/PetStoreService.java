package pet.store.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;


@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	
	@Transactional(readOnly=false)
	

	public PetStoreData savePetStore(PetStoreData petStoreData) {
    	Long petStoreId = petStoreData.getPetStoreId();
    	System.out.println(petStoreId);
    	PetStore petStore = findOrCreatePetStore(petStoreId);
    	
    	copyPetStoreFields(petStore, petStoreData);
    	 return new PetStoreData(petStoreDao.save(petStore));
    	
    }

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
	
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStoreNumber(petStoreData.getPetStoreNumber());
	}

	private PetStore findOrCreatePetStore(long petStoreId) {
	
		PetStore petStore;
		if(petStoreId == 0) {
			
		 petStore = new PetStore();
	    	}else {
	    		
			petStore = findPetStoreById(petStoreId);
			
	    	}
		
		return petStore;
	}

	private PetStore findPetStoreById(long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(() -> new NoSuchElementException("no element found with id: " + petStoreId));
		
	}

	
	@Autowired 
	private EmployeeDao employeeDao;
	@Transactional (readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore =findPetStoreById(petStoreId); 
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);		
		employee.setPetStore(petStore);	
		petStore.getEmployees().add(employee);	

		System.out.println( "HHHHHHHHHHH" +petStore.getEmployees() +"HHHHHHHHHHH");
		//petStoreDao.save(petStore);
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
	employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
	employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
	employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	employee.setEmployeeId(petStoreEmployee.getEmployeeId());
	
		
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		Employee employee;
		if (employeeId == 0) {			
			employee= new Employee(); 
		}else {			
			employee = findEmployeeById(petStoreId, employeeId);
		}
		return employee;
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(() -> new NoSuchElementException("no element found with id: " + employeeId));	 
		if(petStoreId == employee.getPetStore().getPetStoreId()) {		
			return employee;
		}else {	
			throw new IllegalArgumentException("Pet Store Id's do not match");
		}	
	}
	@Autowired 
	private CustomerDao customerDao;
	@Transactional 
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		
		copyCustomerFields(customer, petStoreCustomer);	
		Set<PetStore> petStores = new HashSet<>();
		petStores.add(petStore);
		customer.setPetStore(petStores);		
		Set<Customer> customers = new HashSet<>();		
		customers.add(customer);	
		petStore.setCustomers(customers);
		
		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);
		
		
	}

private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		// TODO Auto-generated method stub
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());


}

private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
	Customer customer;
	if(customerId == 0) {
		customer = new Customer();
	}else {
		customer = findCustomerById(petStoreId, customerId);
	}
	return customer;
}

private Customer findCustomerById(Long petStoreId, Long customerId) {
	Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementException("no element found with id: " + customerId));
	Set<PetStore> petStores = new HashSet<>();
	PetStore petStore = findPetStoreById(petStoreId);
	petStores.addAll(customer.getPetStore());
	boolean isThere = false;
	for(int i =0; i <petStores.size(); i++) {
		if(petStores.contains(petStore)) {
			isThere = true;
			
		}
	}
	if(isThere) {
		return customer;
	}
	else {
		throw new IllegalArgumentException("Eat dirt please");
	}

	
}
@Transactional 

public List<PetStoreData> listAllPetStores() {
	List<PetStore> petStores = petStoreDao.findAll();
	
	List<PetStoreData> result = new LinkedList<>();
	for(PetStore petStore : petStores) {
		PetStoreData psd = new PetStoreData(petStore);
		
		psd.getCustomers().clear();
		psd.getEmployees().clear();
		
		result.add(psd);
	}
	
	return result;
}
@Transactional 
public PetStoreData listPetStoreById(Long petStoreId) {
	PetStore petStore = findPetStoreById(petStoreId);
	PetStoreData psd = new PetStoreData(petStore);
	return psd;
}

public Map<String, String> DeleteStoreById(Long petStoreId) {
	PetStore ps = findPetStoreById(petStoreId);
	petStoreDao.delete(ps);
	Map <String, String> MeSG = new HashMap<String, String>();
	String Msg = ("Store to be deleted" + petStoreId) ;
	MeSG.put(Msg, "Deletion successfull");

	return MeSG;
}

	
}
