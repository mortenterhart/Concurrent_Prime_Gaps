package service.prime;

import logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static storage.PrimeStorage.primeStorage;

public class PrimeCalculatorServiceTest {
    private long minimum = 0L;
    private long maximum = 1_000L;
    private final IPrimeService primeService = new PrimeCalculatorService(new CyclicBarrier(1), minimum, maximum);

    @BeforeAll
    public static void initLogger() {
        Logger.instance.init();
        primeStorage.clear();
    }

    @Test
    public void testIsPrimeWithHighNumber() {
        assertTrue(primeService.isPrime(72006497L));
    }

    @Test
    public void testPrimeNumbersPutIntoStorage() {
        Thread thread = new Thread(primeService);
        thread.start();
        waitFor(thread);

        assertEquals(168, primeStorage.size());
    }

    @Test
    public void testCalculatePrimesNumbersArePrimes() {
        for (long prime : primeService.calculatePrimes(minimum, maximum)) {
            assertTrue(primeService.isPrime(prime));
        }
    }

    @Test
    public void testCalculatePrimesNumbersBelow1000() {
        assertEquals(168, primeService.calculatePrimes(minimum, maximum).size());
    }

    private void waitFor(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
