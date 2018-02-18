package temp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentSieveOfEratosthenes {

    public int sieve(long maximumValue) {
        int totalNumberOfPrimes = 0;

        try {
            final List<Callable<Integer>> partitions = new ArrayList<>();
            final ExecutorService executorPool = Executors.newFixedThreadPool(4);
            long sliceSize = maximumValue / 4;

            for (int i = 2; i <= maximumValue; i += sliceSize) {
                final int from = i;
                long to = i + sliceSize;
                if (to > maximumValue)
                    to = maximumValue;
                final long end = to;
                partitions.add(() -> sieveInRange(from, end));
            }

            final List<Future<Integer>> resultFromParts = executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
            executorPool.shutdown();

            for (final Future<Integer> result : resultFromParts)
                totalNumberOfPrimes += result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return totalNumberOfPrimes;
    }

    private int sieveInRange(long minimumValue, long maximumValue) {
        long size = (maximumValue - minimumValue + 1) / 2;
        boolean[] isPrime = new boolean[(int) size];

        for (long i = 0; i < size; i++)
            isPrime[(int) i] = true;

        for (long i = 3; i * i <= maximumValue; i += 2) {
            if (i >= 9 && i % 3 == 0)

                continue;
            if (i >= 25 && i % 5 == 0)
                continue;
            if (i >= 49 && i % 7 == 0)
                continue;

            long first = ((minimumValue + i - 1) / i) * i;

            if (first < i * i)
                first = i * i;

            if (first % 2 == 0)
                first += i;

            for (long j = first; j <= maximumValue; j += i * 2)
                isPrime[(int) (j - minimumValue) / 2] = false;
        }

        int numberOfPrimes = 0;

        for (int i = 0; i < size; i++)
            if (isPrime[i])
                numberOfPrimes++;

        if (minimumValue == 2)
            numberOfPrimes++;

        return numberOfPrimes;
    }
}
