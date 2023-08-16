package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Saga getSagaByUniqueKey(String uniqueKey) throws MyEntityNotFoundException {
        return sagaRepository.findByUniqueKey(uniqueKey).orElseThrow(() -> new MyEntityNotFoundException("Saga no encontrada") );
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
        if(sagaRepository.existsByName(fromSaga.getName())){
            throw new CustomFieldValidationException("Ya Existe una saga registrada con ese nombre", "name");
        } else{
            Saga toSaga = getSagaByUniqueKey(fromSaga.getUniqueKey());
            mapSaga(fromSaga, toSaga);
            return sagaRepository.save(toSaga);
        }
    }

    private void mapSaga(Saga from, Saga to){
        to.setName(from.getName());
        to.setArcs(from.getArcs());
    }

    @Override
    public void deleteSaga(String uniqueKey) throws MyEntityNotFoundException {
        Saga deleteSaga = sagaRepository.findByUniqueKey(uniqueKey).orElseThrow(() -> new MyEntityNotFoundException("Saga no encontrada"));
        sagaRepository.delete(deleteSaga);
    }

}
