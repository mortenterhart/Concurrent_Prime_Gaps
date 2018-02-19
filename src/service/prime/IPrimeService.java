package service.prime;

import java.util.List;

public interface IPrimeService extends Runnable {

    boolean isPrime(long number);

    List<Long> calculatePrimes(long minimum, long maximum);
}
