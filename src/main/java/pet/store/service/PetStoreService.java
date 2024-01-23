package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.entity.PetStore;
import pet.store.controller.model.PetStoreData;
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

	
}
