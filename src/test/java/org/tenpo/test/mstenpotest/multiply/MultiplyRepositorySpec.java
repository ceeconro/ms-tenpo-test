package org.tenpo.test.mstenpotest.multiply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
class MultiplyRepositorySpec {

    @Autowired
    private MultiplyRepository multiplyRepository;

    @BeforeEach
    void setUp() {
        multiplyRepository.deleteAll();
    }

    @Test
    @DisplayName("Given an operation of multiply, then will be persisted in bd")
    public void persistMultiplyOperation() {

        MultiplyEntity multiplyEntity = new MultiplyEntity(new BigDecimal(2), new BigDecimal(3), new BigDecimal(6));
        MultiplyEntity multiplySaved = multiplyRepository.save(multiplyEntity);
        System.out.println("ID: " + multiplySaved.getId());
        assertEquals(multiplyEntity, multiplySaved);

    }

    @Test
    @DisplayName("Given a multiply entity with incomplete data, then will be rejected without save")
    public void rejectMutiṕlyPersist() {
        MultiplyEntity allNullEntity = new MultiplyEntity();
        assertThrows(TransactionSystemException.class, () -> multiplyRepository.save(allNullEntity));
        MultiplyEntity numberANullEntity = new MultiplyEntity();
        numberANullEntity.setNumberB(new BigDecimal(5));
        numberANullEntity.setResult(new BigDecimal(0));
        assertThrows(TransactionSystemException.class, () -> multiplyRepository.save(numberANullEntity));

    }

    @Test
    @DisplayName("Given a pageable, then will response a list page of multiply")
    void getMultiplyHistory() {
        prepareMultiplyHistory();
        Page<MultiplyEntity> multiplyHistory =  multiplyRepository.findAll(PageRequest.of(0, 2));
        assertEquals(2, multiplyHistory.getSize());

    }

    private void prepareMultiplyHistory() {
        multiplyRepository.save(new MultiplyEntity(new BigDecimal(2), new BigDecimal(2), new BigDecimal(8) ));
        multiplyRepository.save(new MultiplyEntity(new BigDecimal(2), new BigDecimal(2), new BigDecimal(8) ));
        multiplyRepository.save(new MultiplyEntity(new BigDecimal(2), new BigDecimal(2), new BigDecimal(8) ));
        multiplyRepository.save(new MultiplyEntity(new BigDecimal(2), new BigDecimal(2), new BigDecimal(8) ));
    }


}