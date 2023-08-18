package com.api.onepiece.service;

import org.springframework.stereotype.Service;

import com.api.onepiece.entity.Volume;

@Service
public interface VolumeService {
    
    public Iterable<Volume> getAllVolumes();

    public Volume getVolumeById(Long id) throws Exception;

    public Volume createVolume(Volume volume) throws Exception;

    public Volume updateVolume(Volume fromVolume) throws Exception;

    public void deleteVolume(Long id) throws Exception;

}
