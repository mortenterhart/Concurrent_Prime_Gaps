package service;

import model.PrimeGapChain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static prime.PrimeStorage.storage;

public class DistinctGapService implements IGapService {
    private static int idCounter = 0;

    private int threadId = 0;
    private GapServiceType type = GapServiceType.distinct;
    private int lowerIndex = 0;
    private int upperIndex = 0;

    private CyclicBarrier cyclicBarrier;

    public DistinctGapService(CyclicBarrier cyclicBarrier, int fromIndex, int toIndex) {
        this.threadId = idCounter;
        this.cyclicBarrier = cyclicBarrier;
        this.lowerIndex = fromIndex;
        this.upperIndex = toIndex;
        idCounter++;
    }

    public void locatePrimeGaps(int lowerIndex, int upperIndex) {
        // In case there are gap chains with an equal length (save them all)
        List<PrimeGapChain> bestMatches = new ArrayList<>();
        PrimeGapChain bestMatch = new PrimeGapChain();
        PrimeGapChain currentMatch = new PrimeGapChain();

        long lowerPrime = storage.get(lowerIndex);
        for (long upperPrime : storage.subset(lowerIndex + 1, upperIndex)) {

            long gap = upperPrime - lowerPrime;
            if (!currentMatch.contains(gap)) {
                currentMatch.add(lowerPrime, gap, upperPrime);
            } else {
                currentMatch = new PrimeGapChain();
            }

            if (currentMatch.isLongerThan(bestMatch)) {
                bestMatch = currentMatch;
                bestMatches.clear();
            } else if (currentMatch.hasEqualLengthAs(bestMatch)) {
                bestMatches.add(bestMatch);
            }

            lowerPrime = upperPrime;
        }

        System.out.println("Gaps Thread " + threadId + ": " + bestMatch.elements());
        for (PrimeGapChain bestMatchI : bestMatches) {
            System.out.println("ConP Thread " + threadId + ": " + bestMatchI.getConsecutivePrimes());
        }
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
        } catch (BrokenBarrierException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + " (id " + threadId + ")";
    }
}
