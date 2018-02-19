package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimeGapChainTest {

    @Test
    public void testFirstTwoConsecutivePrimesInserted() {
        List<Long> expectedChain = Arrays.asList(2L, 3L);
        PrimeGapChain chain = new PrimeGapChain();

        chain.add(2, 3);

        assertEquals(expectedChain, chain.getConsecutivePrimes());
    }

    @Test
    public void testSecondGapOnlyUpperPrimeInserted() {
        List<Long> expectedChain = Arrays.asList(2L, 3L, 5L);
        PrimeGapChain chain = new PrimeGapChain();

        chain.add(2, 3);
        chain.add(3, 5);

        assertEquals(expectedChain, chain.getConsecutivePrimes());
    }

    @Test
    public void testChainIsLongerThanOtherChain() {
        PrimeGapChain chain1 = new PrimeGapChain();
        chain1.add(2, 3);
        chain1.add(3, 5);
        chain1.add(5, 7);

        PrimeGapChain chain2 = new PrimeGapChain();
        chain2.add(7, 11);
        chain2.add(11, 13);

        assertTrue(chain1.isLongerThan(chain2));
    }

    @Test
    public void testChainHasEqualLengthAsOtherChain() {
        PrimeGapChain chain1 = new PrimeGapChain();
        chain1.add(2, 3);
        chain1.add(3, 5);

        PrimeGapChain chain2 = new PrimeGapChain();
        chain2.add(5, 7);
        chain2.add(7, 11);

        assertTrue(chain1.hasEqualLengthAs(chain2));
    }

    @Test
    public void testLastGapSmallerThanOtherGap() {
        PrimeGapChain chain1 = new PrimeGapChain();
        chain1.add(2, 3);
        chain1.add(3, 5);
        chain1.add(5, 7);

        assertTrue(chain1.lastGapSmallerThan(4));
    }

    @Test
    public void testLastGapGreaterThanOtherGap() {
        PrimeGapChain chain1 = new PrimeGapChain();
        chain1.add(2, 3);
        chain1.add(3, 5);
        chain1.add(5, 7);

        assertTrue(chain1.lastGapGreaterThan(1));
    }
}
