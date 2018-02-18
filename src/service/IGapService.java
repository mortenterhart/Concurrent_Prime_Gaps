package service;

public interface IGapService extends Runnable {
    void locatePrimeGaps(int fromIndex, int toIndex);
}
