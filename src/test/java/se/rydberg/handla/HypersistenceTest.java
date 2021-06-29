package se.rydberg.handla;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class HypersistenceTest {

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        assertTrue(hypersistenceOptimizer.getEvents().isEmpty());
    }

}
