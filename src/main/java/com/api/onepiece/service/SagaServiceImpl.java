package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Saga;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.repository.SagaRepository;

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
            return sagaRepository.save(saga);
        }
    }

    @Override
    public Saga updateSaga(Saga fromSaga) throws Exception {
        if(sagaRepository.existsByName(fromSaga.getName())){
            throw new CustomFieldValidationException("Ya Existe una saga registrada con ese nombre", "name");
        } else{
            Saga toSaga = getSagaById(fromSaga.getId());
            mapSaga(fromSaga, toSaga);
            return sagaRepository.save(toSaga);
        }
    }

    

    private void mapSaga(Saga from, Saga to){
        to.setName(from.getName());
    }

    @Override
    public void deleteSaga(Long id) throws MyEntityNotFoundException {
        Saga deleteSaga = sagaRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Saga no encontrada"));
        sagaRepository.delete(deleteSaga);
    }
    
}
