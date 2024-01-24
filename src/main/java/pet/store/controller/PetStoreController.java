package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.entity.PetStore;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
private	PetStoreService petStoreService ;
//log.info("This is a log line");
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData httpPostRequestMap(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store ()", petStoreData);
		petStoreService.savePetStore(petStoreData);
		return petStoreData;
		
	}
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData UpdatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating PetStore {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	@PostMapping("/pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Adding Employee {}", petStoreEmployee);
		System.out.println("THERERERER");
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
		
	}
	@PostMapping("/pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Adding a customer {}", petStoreCustomer);
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	
	}
	@GetMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<PetStoreData> retrievePetStores(){
		log.info("retrieving pet stores {}");
		return petStoreService.listAllPetStores();
	}
	@GetMapping("/pet_store/{petStoreId}")
	@ResponseStatus(code = HttpStatus.FOUND)
	public PetStoreData listPetStore(@PathVariable Long petStoreId) {
		log.info("listing a pet store {}", petStoreId);
		return petStoreService.listPetStoreById(petStoreId);
	}
	@DeleteMapping("/pet_store/{petStoreId}")
	@ResponseStatus(code = HttpStatus.GONE)
	public Map<String, String> deletePetStore(@PathVariable Long petStoreId){
		log.info("Deleting pet Store with id {}", petStoreId );
		return petStoreService.DeleteStoreById(petStoreId);
		
	}
}
