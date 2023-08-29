package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Location;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.LocationRepository;

@Service
public class LocationService implements GenericService<Location>{

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Iterable<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location getById(Long id) throws Exception {
        return locationRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Agrupación no encontrado"));
    }

    @Override
    public Location create(Location location) throws Exception {
        return locationRepository.save(location);
    }
    
    @Override
    public Location update(Location from) throws Exception {
        Location to = getById(from.getId());        
        to.setName(from.getName());
        to.setUbication(from.getUbication());
        return locationRepository.save(to);
    }

    @Override
    public void delete(Long id) throws Exception {
        Location deleteLocation = getById(id);
        locationRepository.delete(deleteLocation);
    }
    
}
