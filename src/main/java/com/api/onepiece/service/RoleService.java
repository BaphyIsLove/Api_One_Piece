package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Role;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.RoleRepository;

@Service
public class RoleService implements GenericService<Role>{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Iterable<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getById(Long id) throws Exception {
        return roleRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Role no existente"));
    }

    @Override
    public Role create(Role role) throws Exception {
        if(roleRepository.existsByName(role.getName())){
            throw new CustomFieldValidationException("Ya existe ese rol en la base de datos", "role");
        } else{
            return roleRepository.save(role);
        }
    }
    
    @Override
    public Role update(Role from) throws Exception {
        Role to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&roleRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Ya existe ese rol en la base de datos", "role");
        } else{
            to.setName(from.getName());
            return roleRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Role deleteRole = getById(id);
        roleRepository.delete(deleteRole);
    }
    
}
