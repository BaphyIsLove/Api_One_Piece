package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Arc;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.repository.SagaRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class ArcServiceImpl implements ArcService{

    @Autowired
    private ArcRepository arcRepository;

    @Autowired
    private SagaRepository sagaRepository;

    @Override
    public Iterable<Arc> getAllArcs(){
        return arcRepository.findAll();
    }

    @Override
    public Arc getArcByUniqueKey(String uniqueKey) throws Exception {
        return arcRepository.findByUniqueKey(uniqueKey).orElseThrow(() -> new MyEntityNotFoundException("Id no valido para los arcos"));
    }

    @Override
    public Arc createArc(Arc arc) throws Exception {
        if(arcRepository.existsByName(arc.getName())){
            throw new CustomFieldValidationException("ya existe un arco con ese nombre", "name");
        } else {
            Long lastNum = arcRepository.findMaxNumberByPrefix("A");
            arc.setUniqueKey(UniqueKeyGenerator.generateUniqueKey("A", lastNum));
            arc.setSaga(sagaRepository.findByUniqueKey(arc.getSagaKey()).orElseThrow(() -> new CustomFieldValidationException("Seleccione una saga", "sagaKey")));
            return arcRepository.save(arc);
        }
    }

    @Override
    public Arc updateArc(Arc fromArc) throws Exception {
        Arc toArc = getArcByUniqueKey(fromArc.getUniqueKey());
        if(!toArc.getName().equals(fromArc.getName())&&arcRepository.existsByName(fromArc.getName())){
            throw new CustomFieldValidationException("Ya Existe un arco con ese nombre", "name");
        } else{
            toArc.setName(fromArc.getName());
            mapArc(fromArc, toArc);
            return arcRepository.save(toArc);
        }
    }

    private void mapArc(Arc from, Arc to) throws CustomFieldValidationException{
        to.setSaga(sagaRepository.findByUniqueKey(from.getSagaKey()).orElseThrow(() -> new CustomFieldValidationException("Seleccione una saga", "sagaKey")));
    }

    @Override
    public void deleteArc(String uniqueKey) throws Exception {
        Arc deleteArc = arcRepository.findByUniqueKey(uniqueKey).orElseThrow(() -> new MyEntityNotFoundException("Arco no encontrado"));
        arcRepository.delete(deleteArc); 
    }

}
