package se.rydberg.handla.hyper;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("se.rydberg.handla")
public class HyperTest {

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Test
    public void test() {
        assertTrue(hypersistenceOptimizer.getEvents().isEmpty());
    }
}
