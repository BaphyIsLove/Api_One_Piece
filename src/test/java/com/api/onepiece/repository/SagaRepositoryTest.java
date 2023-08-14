package com.api.onepiece.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.api.onepiece.entity.Saga;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SagaRepositoryTest {

    @Autowired
    SagaRepository sagaRepository;    
    
    @BeforeAll
    void setUp() {
        sagaRepository.deleteAll();
        
        Saga saga = Saga.builder()
                    .name("test name")
                    .uniqueKey("S-001")
                    .build();
        sagaRepository.save(saga);
    }

    @AfterAll
    void tearDown() {
        sagaRepository.deleteAll();
    }

    @Test
    void testExistsByNameExist() {
        boolean existsByName = sagaRepository.existsByName("test name");
        assertTrue(existsByName);
    }
    
    @Test
    void testExistsByNameNotExist() {
        boolean existsByName = sagaRepository.existsByName("test name not exist");
        assertFalse(existsByName);
    }

    @Test
    void testFindMaxNumberByPrefixRegister() {
        String prefix = "S";
        Long expectedResult = 1L;
        
        Long actualResult = sagaRepository.findMaxNumberByPrefix(prefix);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindByName() {
        Saga findsaga = sagaRepository.findByName("test name").orElse(null);
        assertEquals("test name", findsaga.getName());
    }
}
