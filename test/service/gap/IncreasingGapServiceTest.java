package service.gap;

import model.PrimeGapChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncreasingGapServiceTest extends AbstractGapServiceTest {

    @BeforeAll
    public static void buildServices() {
        initializePrimeStorage();
        buildServices(new IncreasingGapService(cyclicBarrier),
                new IncreasingGapService(cyclicBarrier));
    }

    @Test
    public void testIsNewChainMemberWhenGreaterGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertTrue(super.callIsNewChainMember(chain, 5));
    }

    @Test
    public void testIsNotNewChainMemberWhenSmallerGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertFalse(super.callIsNewChainMember(chain, 3));
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
