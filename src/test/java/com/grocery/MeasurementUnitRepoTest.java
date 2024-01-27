package com.grocery;

import com.grocery.repos.MeasurementUnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeasurementUnitRepoTest {

    @Autowired
    MeasurementUnitRepository unitRepository;
    @Test
    public void findByIdTest() {
        try {
            unitRepository.findById(null);
        } catch (DataAccessException e) {
            System.out.println("error: " + e.getMessage() );
        }
    }

}
