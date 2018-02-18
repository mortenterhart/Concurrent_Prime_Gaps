package service;

import model.PrimeGapChain;

public interface IGapService extends Runnable {
    void locatePrimeGaps(int fromIndex, int toIndex);
    int getLowerIndex();
    int getUpperIndex();
    GapServiceType getServiceType();
    boolean isNewChainMember(PrimeGapChain chain, long gap);
}
