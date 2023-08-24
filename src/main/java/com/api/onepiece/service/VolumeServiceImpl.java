package com.api.onepiece.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Arc;
import com.api.onepiece.entity.MangaChapter;
import com.api.onepiece.entity.Volume;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.VolumeRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class VolumeServiceImpl implements GenericService<Volume> {

    @Autowired
    VolumeRepository volumeRepository;

    @Override
    public Iterable<Volume> getAll() {
        return volumeRepository.findAll();
    }

    @Override
    public Volume getById(Long id) throws Exception {
        Volume volume = volumeRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Tomo no encontrado"));
        return volume;
    }

    @Override
    public Volume create(Volume volume) throws Exception {
        if(volumeRepository.existsByName(volume.getName())){
            throw new CustomFieldValidationException("El nombre del tomo ya existe", "name");
        } else if(volume.getArc()==null) {
            throw new CustomFieldValidationException("Selecciona al menos un arco", "arc");
        } else{
            Long lastNum = volumeRepository.findMaxNumberByPrefix("V");
            volume.setUniqueKey(UniqueKeyGenerator.generateUniqueKey("V", lastNum));
            List<Arc> selectedArcs = volume.getArc();
            for (Arc arc : selectedArcs) {
                arc.getVolumes().add(volume);
            }
            return volumeRepository.save(volume);
        }
    }

    @Override
public Volume update(Volume fromVolume) throws Exception {
    Volume toVolume = getById(fromVolume.getId());
    
    if (!toVolume.getName().equals(fromVolume.getName()) && volumeRepository.existsByName(fromVolume.getName())) {
        throw new CustomFieldValidationException("El nombre ya existe", "name");
    } else if (!toVolume.getUniqueKey().equals(fromVolume.getUniqueKey()) && volumeRepository.existsByUniqueKey(fromVolume.getUniqueKey())) {
        throw new CustomFieldValidationException("Ya existe un tomo con esa clave", "uniqueKey");
    } else{
        toVolume.setName(fromVolume.getName());
        toVolume.setUniqueKey(fromVolume.getUniqueKey());
        
        toVolume.getArc().clear();
        for (Arc arc : fromVolume.getArc()) {
            toVolume.getArc().add(arc);
            arc.getVolumes().add(toVolume);
        }
        
        return volumeRepository.save(toVolume);
    }
}

    @Override
    public void delete(Long id) throws Exception {
        Volume deleteVolume = volumeRepository.findById(id).orElseThrow(() ->
                        new MyEntityNotFoundException("Tomo no encontrado"));
        for (Arc arc : deleteVolume.getArc()) {
            arc.setSaga(null);
        }
        for(MangaChapter mangaChapter:deleteVolume.getMangaChapters()){
            mangaChapter.setVolume(null);
        }
        volumeRepository.delete(deleteVolume);
    }
    
}
