package com.api.onepiece.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.api.onepiece.entity.Saga;
import com.api.onepiece.repository.SagaRepository;

@SpringBootTest
public class UniqueKeyGeneratorTest {

    @Autowired
    SagaRepository sagaRepository;

    @BeforeEach
    void setUp() {
        sagaRepository.deleteAll();
    }

    @Test
    void testGenerateUniqueKey_FirstValue() {
        String prefix = "S";
        Long lastNum = sagaRepository.findMaxNumberByPrefix(prefix);
        String uniqueKey = UniqueKeyGenerator.generateUniqueKey(prefix, lastNum);
        
        assertEquals("S-001", uniqueKey);
    }
    @Test
    void testGenerateUniqueKey_Secuence() {        
        List<Saga> sagas = new ArrayList<>();
        sagas.add(Saga.builder().name("test name").uniqueKey("S-001").build());
        sagas.add(Saga.builder().name("test name2").uniqueKey("S-002").build());
        sagaRepository.saveAll(sagas);
        
        String prefix = "S";
        Long lastNum = sagaRepository.findMaxNumberByPrefix(prefix);
        String uniqueKey = UniqueKeyGenerator.generateUniqueKey(prefix, lastNum);

        assertEquals("S-003", uniqueKey);
    }
}
