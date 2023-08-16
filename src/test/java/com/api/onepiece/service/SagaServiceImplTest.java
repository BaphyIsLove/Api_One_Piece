package com.api.onepiece.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.api.onepiece.entity.Saga;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.repository.SagaRepository;

@SpringBootTest
@ActiveProfiles("test")
public class SagaServiceImplTest {
    
    @Autowired
    SagaRepository sagaRepository;

    @Autowired
    SagaService sagaService;

    @BeforeEach
    void setUp() {
        sagaRepository.deleteAll();
        Saga saga = Saga.builder().name("test name").uniqueKey("S-001").build();
        Saga saga2 = Saga.builder().name("test name 2").uniqueKey("S-002").build();
        sagaRepository.save(saga);
        sagaRepository.save(saga2);
    }
    
    @Test
    void testCreateNewSaga() throws Exception {
        Saga saga = Saga.builder().name("acept name").build();
        sagaService.createSaga(saga);

        boolean exist = sagaRepository.existsByName("acept name");
        assertTrue(exist);
    }

    @Test
    void testCreateSagaDuplicateName() throws Exception {
        assertThrows(CustomFieldValidationException.class, () -> 
                    sagaService.createSaga(Saga.builder()
                            .name("test name").build()));
    }

    @Test
    void testDeleteSaga() throws Exception {
        assertEquals(2, sagaRepository.count());
        Saga deleteSaga = sagaRepository.findByName("test name").orElse(null);
        String idDeleteSaga = deleteSaga.getUniqueKey();
        sagaService.deleteSaga(idDeleteSaga);
        assertEquals(1, sagaRepository.count());
    }
    
    @Test
    void testGetAllSagas() {
        Iterable<Saga> sagas = sagaService.getAllSagas();
        List<Saga> sagaList = new ArrayList<>();
        sagas.iterator().forEachRemaining(sagaList::add);
        
        assertEquals(2, sagaList.size());
    }
    
    @Test
    void testGetSagaById() throws Exception {
        Saga saga = Saga.builder().name("find saga").uniqueKey("S-003").build();
        sagaRepository.save(saga);
        Saga findSaga = sagaRepository.findByName("find saga").orElse(null);
        assertEquals(saga.getUniqueKey(), findSaga.getUniqueKey());
    }
    
    @Test
    void testUpdateSaga() throws Exception {
        Saga saga = sagaRepository.findByName("test name").orElse(null);
        saga.setName("update name");
        sagaService.updateSaga(saga);
        assertTrue(sagaRepository.existsByName("update name"));
    }
}