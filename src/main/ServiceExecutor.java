package main;

import logging.Logger;
import model.PrimeGapChain;
import service.gap.*;
import service.prime.PrimeCalculatorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import static storage.GapChainStorage.gapStorage;
import static storage.PrimeStorage.primeStorage;

public class ServiceExecutor {
    private static final Logger logger = Logger.instance;
    private final CyclicBarrier cyclicBarrier;

    private final Map<Integer, Long> distinctThreadRunTimes;
    private final Map<Integer, Long> decreasingThreadRunTimes;
    private final Map<Integer, Long> increasingThreadRunTimes;

    public ServiceExecutor(Application application) {
        this.cyclicBarrier = application.getCyclicBarrier();

        distinctThreadRunTimes = new HashMap<>();
        decreasingThreadRunTimes = new HashMap<>();
        increasingThreadRunTimes = new HashMap<>();
    }

    public void execute() {
        logger.log("--- Number of available threads: " + Configuration.instance.maximumNumberOfThreads);
        logger.newLine();
        initializePrimeStorage();
        findGapChains();
        dumpResults();
    }

    private void initializePrimeStorage() {
        logger.log("## Initializing prime storage");
        List<Thread> serviceThreads = new ArrayList<>();

        long startPrime = 3;
        long partition = (Configuration.instance.searchMaximum - startPrime) / Configuration.instance.maximumNumberOfThreads;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            long upperPrimeLimit = startPrime + partition;
            logger.log("> Starting thread " + i + " with a range from " + startPrime + " to " + upperPrimeLimit);
            Thread primeThread = new Thread(new PrimeCalculatorService(cyclicBarrier, startPrime, upperPrimeLimit));
            serviceThreads.add(primeThread);
            primeThread.start();
            startPrime = upperPrimeLimit;
        }

        // Important note: Counting starts from 3 in prime calculator service to
        // allow skipping even numbers
        //   -> Therefore add 2 as a prime number manually
        primeStorage.add(2);
        logger.log("Note: Adding 2 to the prime storage");

        waitFor(serviceThreads);
        primeStorage.sort();
        logger.newLine();
    }

    private void findGapChains() {
        logger.log("## Assigning threads to gap service tasks");
        int sectionRange = primeStorage.size() / Configuration.instance.maximumNumberOfThreads;

        assignThreads(new DistinctGapService(cyclicBarrier), sectionRange);
        assignThreads(new DecreasingGapService(cyclicBarrier), sectionRange);
        assignThreads(new IncreasingGapService(cyclicBarrier), sectionRange);
    }

    private void assignThreads(AbstractGapService gapService, int segmentRange) {
        List<IGapService> services = new ArrayList<>();
        List<Thread> serviceThreads = new ArrayList<>();

        int startIndex = 0;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            int upperIndex = startIndex + segmentRange;
            IGapService serviceInstance = gapService.newInstance(startIndex, upperIndex);
            Thread gapThread = new Thread(serviceInstance);
            services.add(serviceInstance);
            serviceThreads.add(gapThread);

            logger.log("> Starting thread " + i + " for task '" + serviceInstance.getServiceType().capitalizeName() +
                    " Prime Gaps' for range (" + startIndex + ", " + upperIndex + ") in prime storage");
            logger.log("  Total number of primes: " + primeStorage.size());
            gapThread.start();
            startIndex = upperIndex;
        }

        waitFor(serviceThreads);
        mergeThreadResults(services);
        logger.newLine();
    }

    private void mergeThreadResults(List<IGapService> services) {
        GapServiceType serviceType = GapServiceType.distinct;
        if (!services.isEmpty() && services.get(0) != null) {
            serviceType = services.get(0).getServiceType();
        }

        List<PrimeGapChain> foundGapChains = new ArrayList<>();
        for (IGapService gapService : services) {
            foundGapChains.addAll(gapService.fetchResults());
        }

        storeChains(foundGapChains, serviceType);

        for (IGapService thread : services) {
            storeRunTimes(thread);
        }
    }

    private void storeChains(List<PrimeGapChain> chains, GapServiceType chainType) {
        if (chainType == null || chains == null || chains.isEmpty()) {
            return;
        }

        switch (chainType) {
            case distinct:
                gapStorage.addDistinctChains(chains);
                break;
            case decreasing:
                gapStorage.addDecreasingChains(chains);
                break;
            case increasing:
                gapStorage.addIncreasingChains(chains);
                break;
        }

        gapStorage.sortChains();
    }

    private void storeRunTimes(IGapService service) {
        if (service == null || service.getServiceType() == null) {
            return;
        }

        switch (service.getServiceType()) {
            case distinct:
                distinctThreadRunTimes.put(service.getThreadId(), service.getRuntime());
                break;
            case decreasing:
                decreasingThreadRunTimes.put(service.getThreadId(), service.getRuntime());
                break;
            case increasing:
                increasingThreadRunTimes.put(service.getThreadId(), service.getRuntime());
                break;
        }
    }

    private void waitFor(List<Thread> threads) {
        logger.newLine();
        logger.log("... waiting for threads to die");
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void dumpResults() {
        logger.log("## Gap Chain Result Dump");
        logger.log(String.valueOf(primeStorage.size()) + " prime numbers were collected " +
                "by the PrimeCalculatorService");
        logger.log("Found " + gapStorage.numberOfDistinctChains() + " gap chains for distinct type (sorted ascending by length):");
        dumpChainType(gapStorage.getDistinctChains(), GapServiceType.distinct);

        logger.log("Found " + gapStorage.numberOfDecreasingChains() + " gap chains for decreasing type (sorted ascending by length):");
        dumpChainType(gapStorage.getDecreasingChains(), GapServiceType.decreasing);

        logger.log("Found " + gapStorage.numberOfIncreasingChains() + " gap chains for increasing type (sorted ascending by length):");
        dumpChainType(gapStorage.getIncreasingChains(), GapServiceType.increasing);

        dumpRunTimes();
    }

    private void dumpChainType(List<PrimeGapChain> chains, GapServiceType type) {
        int number = 1;
        for (PrimeGapChain chain : chains) {
            logger.log(String.format("  %s Chains (Number %d):", type.capitalizeName(), number));
            logger.log("    Chain Length:       " + chain.length());
            logger.log("    Gap Elements:       " + chain.elements().toString());
            logger.log("    Consecutive Primes: " + chain.getConsecutivePrimes().toString());
            logger.newLine();
            number++;
        }
    }

    private void dumpRunTimes() {
        logger.log("## Thread Runtime Dump");
        dumpRunTimeServiceType(distinctThreadRunTimes, GapServiceType.distinct);
        dumpRunTimeServiceType(decreasingThreadRunTimes, GapServiceType.decreasing);
        dumpRunTimeServiceType(increasingThreadRunTimes, GapServiceType.increasing);
    }

    private void dumpRunTimeServiceType(Map<Integer, Long> runTimes, GapServiceType type) {
        logger.log(" > " + type.capitalizeName() + " Prime Gaps:");
        for (int threadId : runTimes.keySet()) {
            logger.log("    Thread:       " + threadId);
            logger.log("    Runtime (ms): " + runTimes.get(threadId));
            logger.newLine();
        }
    }
}
