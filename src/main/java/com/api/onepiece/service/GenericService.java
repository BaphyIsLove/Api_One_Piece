package com.api.onepiece.service;

import org.springframework.stereotype.Service;

@Service
public interface GenericService<T>{
    
    public Iterable<T> getAll();

    public T getById(Long id) throws Exception;

    public T create(T entity) throws Exception;

    public T update(T from) throws Exception;

    public void delete(Long id) throws Exception;

}
