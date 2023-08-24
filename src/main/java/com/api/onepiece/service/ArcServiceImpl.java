package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.AnimeChapter;
import com.api.onepiece.entity.Arc;
import com.api.onepiece.entity.MangaChapter;
import com.api.onepiece.entity.Volume;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class ArcServiceImpl implements GenericService<Arc> {

    @Autowired
    private ArcRepository arcRepository;

    @Override
    public Iterable<Arc> getAll(){
        return arcRepository.findAll();
    }

    @Override
    public Arc getById(Long id) throws Exception {
        return arcRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Id no encontrado"));
    }

    @Override
    public Arc create(Arc arc) throws Exception {
        if(arcRepository.existsByName(arc.getName())){
            throw new CustomFieldValidationException("ya existe un arco con ese nombre", "name");
        } else if(arc.getSaga()==null) {
            throw new CustomFieldValidationException("Selecciona una saga", "saga");
        } else {
            Long lastNum = arcRepository.findMaxNumberByPrefix("A");
            arc.setUniqueKey(UniqueKeyGenerator.generateUniqueKey("A", lastNum));
            return arcRepository.save(arc);
        }
    }

    @Override
    public Arc update(Arc fromArc) throws Exception {
        Arc toArc = getById(fromArc.getId());
        if(!toArc.getUniqueKey().equals(fromArc.getUniqueKey()) && arcRepository.existsByUniqueKey(fromArc.getUniqueKey())){
            throw new CustomFieldValidationException("Ya Existe esa clave", "uniqueKey");
        } else if(!toArc.getName().equals(fromArc.getName())&&arcRepository.existsByName(fromArc.getName())){
            throw new CustomFieldValidationException("Ya Existe un arco con ese nombre", "name");
        } else{
            mapArc(fromArc, toArc);
            return arcRepository.save(toArc);
        }
    }
    
    private void mapArc(Arc from, Arc to) throws CustomFieldValidationException{
        to.setUniqueKey(from.getUniqueKey());
        to.setName(from.getName());
        to.setSaga(from.getSaga());
    }

    @Override
    public void delete(Long id) throws Exception {
        Arc deleteArc = arcRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Arco no encontrado"));
        deleteArc.setSaga(null);
        for(Volume volume:deleteArc.getVolumes()){
            volume.setArc(null);
        }
        for(AnimeChapter animeChapter:deleteArc.getAnimeChapters()){
            animeChapter.setAnimeArc(null);
        }
        for(MangaChapter mangaChapter:deleteArc.getMangaChapters()){
            mangaChapter.setMangaArc(null);
        }
        arcRepository.delete(deleteArc); 
    }

}
