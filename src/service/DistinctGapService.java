package service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class DistinctGapService implements IGapService {
    private int threadId = 0;
    private GapServiceType type = GapServiceType.distinct;

    private CyclicBarrier cyclicBarrier;
    private List<BigInteger> primes = new ArrayList<>();

    public DistinctGapService(int threadId) {
        cyclicBarrier = new CyclicBarrier(3);
        this.threadId = threadId;
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
    @Override
    public void run() {

    }
}
