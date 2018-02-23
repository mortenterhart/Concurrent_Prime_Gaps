package service.gap;

import model.PrimeGapChain;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class DistinctGapService extends AbstractGapService implements IGapService {
    private static int idCounter = 0;

    private int threadId = 0;
    private long runtime = 0;

    private final CyclicBarrier cyclicBarrier;

    private GapServiceType serviceType = GapServiceType.distinct;
    private int lowerIndex = 0;
    private int upperIndex = 0;


    public DistinctGapService(CyclicBarrier cyclicBarrier) {
        super();
        this.cyclicBarrier = cyclicBarrier;
    }

    private DistinctGapService(CyclicBarrier cyclicBarrier, int fromIndex, int toIndex) {
        super();
        this.threadId = idCounter;
        this.cyclicBarrier = cyclicBarrier;
        this.lowerIndex = fromIndex;
        this.upperIndex = toIndex;
        idCounter++;

        super.setServiceProvider(this);
        super.setCyclicBarrier(cyclicBarrier);
    }

    public void locatePrimeGaps(int lowerIndex, int upperIndex) {
        super.locatePrimeGaps(lowerIndex, upperIndex);
    }

    public boolean isNewChainMember(PrimeGapChain chain, long gap) {
        return !chain.contains(gap);
    }

    public List<PrimeGapChain> fetchResults() {
        return super.getResultChains();
    }

    @Override
    public IGapService newInstance(int fromIndex, int toIndex) {
        return new DistinctGapService(cyclicBarrier, fromIndex, toIndex);
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
        super.run(lowerIndex, upperIndex);
    }

    public GapServiceType getServiceType() {
        return serviceType;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runTime) {
        this.runtime = runTime;
    }

    public int getThreadId() {
        return threadId;
    }

    @Override
    public String toString() {
        return getClass().getName() + " (id " + threadId + ")";
    }
}
