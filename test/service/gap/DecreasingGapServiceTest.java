package service.gap;

import model.PrimeGapChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecreasingGapServiceTest extends AbstractGapServiceTest {

    @BeforeAll
    public static void buildServices() {
        initializePrimeStorage();
        buildServices(new DecreasingGapService(cyclicBarrier),
                new DecreasingGapService(cyclicBarrier));
    }

    @Test
    public void testIsNewChainMemberWhenSmallerGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertTrue(super.callIsNewChainMember(chain, 3));
    }

    @Test
    public void testIsNotNewChainMemberWhenGreaterGap() {
        PrimeGapChain chain = new PrimeGapChain();
        chain.add(13, 17);

        assertFalse(super.callIsNewChainMember(chain, 5));
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
