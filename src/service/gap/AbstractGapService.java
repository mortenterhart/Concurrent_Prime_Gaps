package service.gap;

import logging.Logger;
import model.PrimeGapChain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static storage.PrimeStorage.primeStorage;

public abstract class AbstractGapService implements Runnable {
    private IGapService serviceProvider;
    private List<PrimeGapChain> resultChains;

    private CyclicBarrier cyclicBarrier;

    protected AbstractGapService() {
        resultChains = new ArrayList<>();
    }

    public abstract IGapService newInstance(int fromIndex, int toIndex);

    /**
     * Main algorithm for gap computation of primes in prime storage.
     * Calculates chains of prime gaps between <code>fromIndex</code>
     * (inclusive) to <code>toIndex</code> (exclusive). The prime numbers
     * are registered in the {@link storage.PrimeStorage}. A gap is considered
     * a new chain member if it satisfies the underlying condition of the
     * specific service provider.
     * <p>
     * Thus, this method stores all gap chains with the same length inside
     * a list and can be fetched by the service provider.
     *
     * @param fromIndex lower index for {@link storage.PrimeStorage}
     * @param toIndex   upper index for {@link storage.PrimeStorage}
     * @see IGapService#isNewChainMember(PrimeGapChain, long)
     * @see IGapService#fetchResults()
     */
    protected void locatePrimeGaps(int fromIndex, int toIndex) {
        // In case there are gap chains with an equal length (save them all)
        List<PrimeGapChain> bestMatches = new ArrayList<>();
        PrimeGapChain bestMatch = new PrimeGapChain();
        PrimeGapChain currentMatch = new PrimeGapChain();

        long lowerPrime = primeStorage.get(fromIndex);
        for (long upperPrime : primeStorage.subset(fromIndex + 1, toIndex)) {

            long gap = upperPrime - lowerPrime;
            if (serviceProvider.isNewChainMember(currentMatch, gap)) {
                currentMatch.add(lowerPrime, gap, upperPrime);
            } else {
                currentMatch = new PrimeGapChain();
            }

            if (currentMatch.isLongerThan(bestMatch)) {
                bestMatch = currentMatch;
                bestMatches.clear();
            } else if (currentMatch.hasEqualLengthAs(bestMatch)) {
                bestMatches.add(currentMatch);
            }

            lowerPrime = upperPrime;
        }

        resultChains = bestMatches;
    }

    protected void run(int lowerIndex, int upperIndex) {
        long runtimeStart = System.currentTimeMillis();
        locatePrimeGaps(lowerIndex, upperIndex);
        serviceProvider.setRunTime(System.currentTimeMillis() - runtimeStart);

        Logger.instance.log("  Runtime (ms) (" + serviceProvider.getServiceType().capitalizeName() +
                " Thread " + serviceProvider.getThreadId() + "): " +
                (System.currentTimeMillis() - runtimeStart));
        Logger.instance.log("  > Thread " + serviceProvider.getThreadId() + " has died!");

        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException exception) {
            exception.printStackTrace();
        }
    }

    protected List<PrimeGapChain> getResultChains() {
        return resultChains;
    }

    protected void setServiceProvider(IGapService serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    protected void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }
}
