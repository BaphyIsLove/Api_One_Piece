package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.MangaChapter;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.MangaChapterRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class MangaChapterServiceImpl implements GenericService<MangaChapter> {

    @Autowired
    private MangaChapterRepository mangaChapterRepository;

    @Override
    public Iterable<MangaChapter> getAll() {
        return mangaChapterRepository.findAll();
    }

    @Override
    public MangaChapter getById(Long id) throws Exception {
        return mangaChapterRepository.findById(id).orElseThrow(()-> new MyEntityNotFoundException("Capitulo no encontrado"));
    }

    @Override
    public MangaChapter create(MangaChapter mangaChapter) throws Exception {
        if(mangaChapterRepository.existsByName(mangaChapter.getName())){
            throw new CustomFieldValidationException("Nombre duplicado", "name");
        } else if(mangaChapter.getVolume()==null){
            throw new CustomFieldValidationException("Selecciona un tomo", "volume");
        } else {
            Long lastNum = mangaChapterRepository.findMaxNumberByPrefix("MC");
            mangaChapter.setUniqueKey(UniqueKeyGenerator.generateChapterUniqueKey("MC", lastNum));
            return mangaChapterRepository.save(mangaChapter);
        }
    }

    @Override
    public MangaChapter update(MangaChapter fromMangaChapter) throws Exception {
        MangaChapter toMangaChapter = getById(fromMangaChapter.getId());
        if(!toMangaChapter.getName().equals(fromMangaChapter.getName())&&mangaChapterRepository.existsByName(fromMangaChapter.getName())){
            throw new CustomFieldValidationException("Ya existe un capitulo con ese nombre", "name");
        } else if(!toMangaChapter.getUniqueKey().equals(fromMangaChapter.getUniqueKey())&&mangaChapterRepository.existsByUniqueKey(fromMangaChapter.getUniqueKey())){
            throw new CustomFieldValidationException("ya existe un capitulo con esa clave", "uniqueKey");
        } else if(fromMangaChapter.getVolume()==null){
            throw new CustomFieldValidationException("Selecciona un tomo", "volume");
        } else {
            toMangaChapter.setName(fromMangaChapter.getName());
            toMangaChapter.setUniqueKey(fromMangaChapter.getUniqueKey());
            toMangaChapter.setVolume(fromMangaChapter.getVolume());
            toMangaChapter.setMangaArc(fromMangaChapter.getMangaArc());
            return mangaChapterRepository.save(fromMangaChapter);            
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        mangaChapterRepository.deleteById(id);
    }
    
}
