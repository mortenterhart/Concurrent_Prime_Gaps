package service;

import model.PrimeGapChain;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class IncreasingGapService extends AbstractGapService implements IGapService {
    private static int idCounter = 0;

    private int threadId = 0;
    private GapServiceType serviceType = GapServiceType.increasing;
    private int lowerIndex = 0;
    private int upperIndex = 0;

    private CyclicBarrier cyclicBarrier;

    public IncreasingGapService(CyclicBarrier cyclicBarrier, int fromIndex, int toIndex) {
        this.threadId = idCounter;
        this.cyclicBarrier = cyclicBarrier;
        this.lowerIndex = fromIndex;
        this.upperIndex = toIndex;
        idCounter++;

        super.setServiceProvider(this);
    }

    public void locatePrimeGaps(int lowerIndex, int upperIndex) {
        super.locatePrimeGaps(lowerIndex, upperIndex);
    }

    public boolean isNewChainMember(PrimeGapChain chain, long gap) {
        return chain.lastGapSmallerThan(gap);
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
        locatePrimeGaps(lowerIndex, upperIndex);

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException exception) {
            exception.printStackTrace();
        }

    }

    public int getLowerIndex() {
        return lowerIndex;
    }

    public int getUpperIndex() {
        return upperIndex;
    }

    public GapServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public String toString() {
        return getClass().getName() + " (id " + threadId + ")";
    }
}
