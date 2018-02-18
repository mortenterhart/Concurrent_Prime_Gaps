package model;

public class ConsecutivePrimes {
    private long lowerPrime;
    private long upperPrime;

    public ConsecutivePrimes(long lowerPrime, long upperPrime) {
        this.lowerPrime = lowerPrime;
        this.upperPrime = upperPrime;
    }

    public long gap() {
        return upperPrime - lowerPrime;
    }

    public long getLowerPrime() {
        return lowerPrime;
    }

    public long getUpperPrime() {
        return upperPrime;
    }
}
