package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Occupation;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.OccupationRepository;

@Service
public class OccupationService implements GenericService<Occupation>{

    @Autowired
    OccupationRepository occupationRepository;

    @Override
    public Iterable<Occupation> getAll() {
        return occupationRepository.findAll();
    }

    @Override
    public Occupation getById(Long id) throws Exception {
        return occupationRepository.findById(id).orElseThrow(() 
                    -> new MyEntityNotFoundException("Ocupacion ya registrada"));
    }

    @Override
    public Occupation create(Occupation occupation) throws Exception {
        if(occupationRepository.existsByName(occupation.getName())){
            throw new CustomFieldValidationException(
                    "Ocupacion ya registrada", "name");
                } else{
                    return occupationRepository.save(occupation);
                }
            }
            
    @Override
    public Occupation update(Occupation from) throws Exception {
        Occupation to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&occupationRepository.existsByName(from.getName())){  
            throw new CustomFieldValidationException(
                    "Ocupacion ya registrada", "name");
        } else{
            to.setName(from.getName());
            return occupationRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Occupation delete = getById(id);
        occupationRepository.delete(delete);
    }
    
}
