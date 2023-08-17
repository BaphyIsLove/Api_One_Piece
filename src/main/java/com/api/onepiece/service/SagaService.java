package com.api.onepiece.service;

import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Saga;

@Service
public interface SagaService {
    
    public Iterable<Saga> getAllSagas();

    public Saga getSagaById(Long id) throws Exception;

    public Saga createSaga(Saga saga) throws Exception;

    public Saga updateSaga(Saga saga) throws Exception;

    public void deleteSaga(Long id) throws Exception;

}
