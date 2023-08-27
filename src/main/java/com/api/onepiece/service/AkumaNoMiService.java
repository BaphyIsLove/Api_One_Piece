package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.AkumaNoMi;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.AkumaNoMiRepository;

@Service
public class AkumaNoMiService implements GenericService<AkumaNoMi>{

    @Autowired
    AkumaNoMiRepository akumaNoMiRepository;

    @Override
    public Iterable<AkumaNoMi> getAll() {
        return akumaNoMiRepository.findAll();
    }

    @Override
    public AkumaNoMi getById(Long id) throws Exception {
        return akumaNoMiRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("AkumaNoMi no existente"));
    }

    @Override
    public AkumaNoMi create(AkumaNoMi akumaNoMi) throws Exception {
        if(akumaNoMiRepository.existsByName(akumaNoMi.getName())){
            throw new CustomFieldValidationException("Ya existe ese rol en la base de datos", "akumaNoMi");
        } else{
            return akumaNoMiRepository.save(akumaNoMi);
        }
    }
    
    @Override
    public AkumaNoMi update(AkumaNoMi from) throws Exception {
        AkumaNoMi to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&akumaNoMiRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Ya existe ese rol en la base de datos", "akumaNoMi");
        } else{
            to.setName(from.getName());
            return akumaNoMiRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        AkumaNoMi deleteAkumaNoMi = getById(id);
        akumaNoMiRepository.delete(deleteAkumaNoMi);
    }
    
}

