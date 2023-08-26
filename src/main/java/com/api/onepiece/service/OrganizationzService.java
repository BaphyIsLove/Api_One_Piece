package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Organizationz;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.OrganizationzRepository;

@Service
public class OrganizationzService implements GenericService<Organizationz>{
    
    @Autowired
    OrganizationzRepository organizationzRepository;

    @Override
    public Iterable<Organizationz> getAll() {
        return organizationzRepository.findAll();
    }

    @Override
    public Organizationz getById(Long id) throws Exception {
        return organizationzRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Agrupación no encontrado"));
    }

    @Override
    public Organizationz create(Organizationz organizationz) throws Exception {
        if(organizationzRepository.existsByName(organizationz.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else{
            return organizationzRepository.save(organizationz);
        }
    }
    
    @Override
    public Organizationz update(Organizationz from) throws Exception {
        Organizationz to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&organizationzRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else {
            to.setName(from.getName());
            return organizationzRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Organizationz deleteOrganizationz = getById(id);
        organizationzRepository.delete(deleteOrganizationz);
    }
    
}