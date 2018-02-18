package model;

import java.util.ArrayList;
import java.util.List;

public class PrimeGapChain {
    /**
     * chainElements:   2   2   5    2
     * primeNumbers:  3   5   7   11   13   (primeNumbers always has one element more than chainElements)
     */
    private List<Long> chainElements;
    private List<Long> consecutivePrimes;

    public PrimeGapChain() {
        chainElements = new ArrayList<>();
        consecutivePrimes = new ArrayList<>();
    }

    public void add(long lowerPrime, long upperPrime) {
        add(lowerPrime, upperPrime - lowerPrime, upperPrime);
    }

    public void add(long lowerPrime, long gap, long upperPrime) {
        if (chainElements.isEmpty()) {
            consecutivePrimes.add(lowerPrime);
            consecutivePrimes.add(upperPrime);
        } else {
            consecutivePrimes.add(upperPrime);
        }

        chainElements.add(gap);
    }

    public List<Long> elements() {
        return chainElements;
    }
    
    public List<Long> getConsecutivePrimes() {
        return consecutivePrimes;
    }

    public boolean contains(long gap) {
        return chainElements.contains(gap);
    }

    public boolean hasEqualLengthAs(PrimeGapChain bestMatch) {
        return this.length() == bestMatch.length();
    }

    public boolean isLongerThan(PrimeGapChain otherChain) {
        return this.length() > otherChain.length();
    }

    public boolean lastGapSmallerThan(long gap) {
        return chainElements.isEmpty() || chainElements.get(this.length() - 1) <= gap;
    }

    public boolean lastGapGreaterThan(long gap) {
        return chainElements.isEmpty() || chainElements.get(this.length() - 1) >= gap;
    }

    public int length() {
        return chainElements.size();
    }

    public void clear() {
        chainElements.clear();
        consecutivePrimes.clear();
    }
}
