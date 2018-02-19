package service.gap;

import model.PrimeGapChain;

import java.util.List;

public interface IGapService extends Runnable {

    void locatePrimeGaps(int fromIndex, int toIndex);

    GapServiceType getServiceType();

    void setRunTime(long runTime);

    long getRunTime();

    int getThreadId();

    boolean isNewChainMember(PrimeGapChain chain, long gap);

    List<PrimeGapChain> fetchResults();
}
