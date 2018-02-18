package prime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public enum PrimeStorage {
    storage;

    private final List<Long> primeList = new ArrayList<>();

    public List<Long> primes() {
        return primeList;
    }

    public boolean add(long prime) {
        return primeList.add(prime);
    }

    public boolean addAll(Collection<Long> primeSection) {
        return primeList.addAll(primeSection);
    }

    public long get(int listIndex) {
        return primeList.get(listIndex);
    }

    public List<Long> subset(int lowerIndex, int upperIndex) {
        return primeList.subList(lowerIndex, upperIndex);
    }

    public int size() {
        return primeList.size();
    }

    public void sort() {
        primeList.sort(Comparator.comparingLong(Long::longValue));
    }

    public void dumpStorage() {
        for (long prime : primeList) {
            System.out.println(prime);
        }
    }
}
