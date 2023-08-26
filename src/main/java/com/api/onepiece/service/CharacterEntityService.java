package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.CharacterEntity;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.CharacterEntityRepository;

@Service
public class CharacterEntityService implements GenericService<CharacterEntity>{

    @Autowired
    CharacterEntityRepository characterRepository;

    @Override
    public Iterable<CharacterEntity> getAll() {
        return characterRepository.findAll();
    }

    @Override
    public CharacterEntity getById(Long id) throws Exception {
        return characterRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Personaje no encontrado"));
    }

    @Override
    public CharacterEntity create(CharacterEntity character) throws Exception {
        if(characterRepository.existsByName(character.getName())){
            throw new CustomFieldValidationException("El personaje ya se encuentra registrado", "name");
        } else {
            return characterRepository.save(character);
        }
    }

    @Override
    public CharacterEntity update(CharacterEntity fromCharacter) throws Exception {
        CharacterEntity toCharacter = getById(fromCharacter.getId());
        if(!toCharacter.getName().equals(fromCharacter.getName())&&characterRepository.existsByName(fromCharacter.getName())){
            throw new CustomFieldValidationException("Personaje ya registrado", "name");
        } else{
            mapCharacter(fromCharacter, toCharacter);
            return characterRepository.save(toCharacter);

        }
    }

    private void mapCharacter(CharacterEntity from, CharacterEntity to){
        to.setName(from.getName());
        to.setRace(from.getRace());
        to.setOccupation(from.getOccupation());
        to.setBounty(from.getBounty());
        to.setCrew(from.getCrew());
        to.setAge(from.getAge());
        to.setSize(from.getSize());
        to.setBirthdate(from.getBirthdate());
        to.setFirstApparitionManga(from.getFirstApparitionManga());
        to.setFirstApparitionAnime(from.getFirstApparitionAnime());
    }

    @Override
    public void delete(Long id) throws Exception {
        CharacterEntity deleteCharacter = getById(id);
        characterRepository.delete(deleteCharacter);
    }
    
}
