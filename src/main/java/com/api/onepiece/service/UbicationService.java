package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Ubication;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.UbicationRepository;

@Service
public class UbicationService implements GenericService<Ubication>{

    @Autowired
    UbicationRepository ubicationRepository;

    @Override
    public Iterable<Ubication> getAll() {
        return ubicationRepository.findAll();
    }

    @Override
    public Ubication getById(Long id) throws Exception {
        return ubicationRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Agrupación no encontrado"));
    }

    @Override
    public Ubication create(Ubication ubication) throws Exception {
        if(ubicationRepository.existsByName(ubication.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else{
            return ubicationRepository.save(ubication);
        }
    }
    
    @Override
    public Ubication update(Ubication from) throws Exception {
        Ubication to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&ubicationRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else {
            to.setName(from.getName());
            return ubicationRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Ubication deleteUbication = getById(id);
        ubicationRepository.delete(deleteUbication);
    }
    
}