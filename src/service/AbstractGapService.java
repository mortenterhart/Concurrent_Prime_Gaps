package service;

import model.PrimeGapChain;

import java.util.ArrayList;
import java.util.List;

import static prime.PrimeStorage.storage;

public abstract class AbstractGapService {
    private IGapService serviceProvider;

    protected AbstractGapService() { }

    void locatePrimeGaps(long minimum, long maximum) {
        // In case there are gap chains with an equal length (save them all)
        List<PrimeGapChain> bestMatches = new ArrayList<>();
        PrimeGapChain bestMatch = new PrimeGapChain();
        PrimeGapChain currentMatch = new PrimeGapChain();

        long lowerPrime = storage.get(serviceProvider.getLowerIndex());
        for (long upperPrime : storage.subset(serviceProvider.getLowerIndex() + 1,
                serviceProvider.getUpperIndex())) {

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

        System.out.println(serviceProvider.getServiceType().name() + " service:");
        for (PrimeGapChain chain : bestMatches) {
            System.out.println("Gaps: " + chain.elements());
            System.out.println("Consecutive primes: " + chain.getConsecutivePrimes());
        }
    }

    protected void setServiceProvider(IGapService serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public abstract IGapService newInstance(int fromIndex, int toIndex);
}
