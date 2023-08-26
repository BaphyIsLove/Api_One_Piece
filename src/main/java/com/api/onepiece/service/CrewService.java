package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Crew;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.CrewRepository;

@Service
public class CrewService implements GenericService<Crew>{

    @Autowired
    CrewRepository crewRepository;

    @Override
    public Iterable<Crew> getAll() {
        Iterable<Crew> allCrews = crewRepository.findAll();
        for(Crew crew:allCrews){
            crew.setCrewBounty();
            crewRepository.save(crew);
        }
        return allCrews;
    }

    @Override
    public Crew getById(Long id) throws Exception {
        return crewRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Agrupación no encontrado"));
    }

    @Override
    public Crew create(Crew crew) throws Exception {
        if(crewRepository.existsByName(crew.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else{
            return crewRepository.save(crew);
        }
    }
    
    @Override
    public Crew update(Crew from) throws Exception {
        Crew to = getById(from.getId());
        if(!to.getName().equals(from.getName())&&crewRepository.existsByName(from.getName())){
            throw new CustomFieldValidationException("Agrupación ya registrada", "name");
        } else {
            to.setName(from.getName());
            return crewRepository.save(to);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Crew deleteCrew = getById(id);
        crewRepository.delete(deleteCrew);
    }
    
}
