package prime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class PrimeCalculatorService implements Runnable {
    private static int idCounter = 0;

    private int threadId = 0;
    private long minimum = 0;
    private long maximum = 0;
    private final CyclicBarrier cyclicBarrier;

    public PrimeCalculatorService(CyclicBarrier barrier,
                                  long minimum, long maximum) {
        this.threadId = idCounter;
        this.cyclicBarrier = barrier;
        this.minimum = minimum;
        this.maximum = maximum;
        idCounter++;
    }

    private boolean isPrime(long number) {
        if (number % 2 == 0) {
            return false;
        }

        for (long i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    private List<Long> calculatePrimes(long minimum, long maximum) {
        List<Long> primes = new ArrayList<>();

        // Iterate only over odd numbers (even numbers are no primes)
        if (isEven(minimum)) {
            minimum++;
        }

        long start = System.currentTimeMillis();
        for (long number = minimum; number < maximum; number += 2) {
            if (isPrime(number)) {
                primes.add(number);
            }
        }

        return primes;
    }

    private boolean isEven(long number) {
        return number % 2 == 0;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        long start = System.currentTimeMillis();
        PrimeStorage.storage.addAll(calculatePrimes(minimum, maximum));
        System.out.println("Runtime (ms) (Thread " + threadId + "): " + (System.currentTimeMillis() - start));

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException exception) {
            System.err.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public long getMinimum() {
        return minimum;
    }

    public long getMaximum() {
        return maximum;
    }
}
