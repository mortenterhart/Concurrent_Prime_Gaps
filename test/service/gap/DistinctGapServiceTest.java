package service.gap;

import model.PrimeGapChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DistinctGapServiceTest extends AbstractGapServiceTest {

    @BeforeAll
    public static void buildServices() {
        initializePrimeStorage();
        buildServices(new DistinctGapService(cyclicBarrier),
                new DistinctGapService(cyclicBarrier));
    }

    @Test
    public void testIsNewChainMemberWhenDistinctGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertTrue(super.callIsNewChainMember(chain, 5));
    }

    @Test
    public void testIsNotNewChainMemberWhenNotDistinctGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertFalse(super.callIsNewChainMember(chain, 4));
    }

    @Test
    public void testNewInstanceNotNull() {
        assertNotNull(super.callNewInstance(0, 1000));
    }

    @Test
    public void testRunAndFetchResultsNotNull() {
        assertNotNull(super.runAndFetchResults());
    }
}
