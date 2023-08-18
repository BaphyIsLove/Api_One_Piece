package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Arc;
import com.api.onepiece.entity.Saga;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.repository.SagaRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class SagaServiceImpl implements SagaService{

    @Autowired
    SagaRepository sagaRepository;

    @Override
    public Iterable<Saga> getAllSagas() {
        return sagaRepository.findAll();
    }

    @Override
    public Saga getSagaById(Long id) throws MyEntityNotFoundException {
        return sagaRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Saga no encontrada") );
    }

    @Override
    public Saga createSaga(Saga saga) throws Exception {
        if(sagaRepository.existsByName(saga.getName())){
            throw new CustomFieldValidationException("Ya existe una saga con ese nombre", "name");
        } else{
            Long lastNum = sagaRepository.findMaxNumberByPrefix("S");
            saga.setUniqueKey(UniqueKeyGenerator.generateUniqueKey("S", lastNum));
            return sagaRepository.save(saga);
        }
    }

    @Override
    public Saga updateSaga(Saga fromSaga) throws Exception {
        Saga toSaga = getSagaById(fromSaga.getId());
        if(!toSaga.getUniqueKey().equals(fromSaga.getUniqueKey()) && sagaRepository.existsByUniqueKey(fromSaga.getUniqueKey())){
            throw new CustomFieldValidationException("Ya Existe esa clave", "uniqueKey");
        } else if(!toSaga.getName().equals(fromSaga.getName())&&sagaRepository.existsByName(fromSaga.getName())){
            throw new CustomFieldValidationException("Ya Existe una saga con ese nombre", "name");
        } else{
            mapSaga(fromSaga, toSaga);
            return sagaRepository.save(toSaga);
        }
    }
    
    private void mapSaga(Saga from, Saga to) throws CustomFieldValidationException{
        to.setUniqueKey(from.getUniqueKey());
        to.setName(from.getName());
    }

    @Override
    public void deleteSaga(Long id) throws MyEntityNotFoundException {
        Saga deleteSaga = sagaRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Saga no encontrada"));
        for (Arc arc : deleteSaga.getArcs()) {
            arc.setSaga(null);
        }
        sagaRepository.delete(deleteSaga);
    }

}
