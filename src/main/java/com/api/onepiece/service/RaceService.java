package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Race;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.RaceRepository;

@Service
public class RaceService implements GenericService<Race>{

    @Autowired
    RaceRepository raceRepository;

    @Override
    public Iterable<Race> getAll() {
        return raceRepository.findAll();
    }

    @Override
    public Race getById(Long id) throws Exception {
        return raceRepository.findById(id).orElseThrow(() 
                    -> new MyEntityNotFoundException("Raza/especie no encontrado"));
    }

    @Override
    public Race create(Race race) throws Exception {
        if(raceRepository.existsByName(race.getName())){
            throw new CustomFieldValidationException("Raza/especie ya registrada", "name");
        } else{
            return raceRepository.save(race);
        }
    }

    @Override
    public Race update(Race from) throws Exception {
        Race to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&raceRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Raza/especie ya registrada", "name");
        } else{
            to.setName(from.getName());
            return raceRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Race delete = getById(id);
        raceRepository.delete(delete);
    }
    
}
