package com.api.onepiece.service;

import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Arc;

@Service
public interface ArcService {
    
    public Iterable<Arc> getAllArcs();

    public Arc getArcByUniqueKey(String uniqueKey) throws Exception;

    public Arc createArc(Arc arc) throws Exception;

    public Arc updateArc(Arc arc) throws Exception;

    public void deleteArc(String uniqueKey) throws Exception;

}
