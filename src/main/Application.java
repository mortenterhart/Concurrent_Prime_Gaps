package main;

import prime.PrimeCalculatorService;
import service.*;
;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import static prime.PrimeStorage.storage;

public class Application {
    private final CyclicBarrier cyclicBarrier;

    public Application() {
        cyclicBarrier = new CyclicBarrier(Configuration.instance.maximumNumberOfThreads);
    }

    public void findGapChains() {
        initializePrimeStorage();

        int startIndex = 0;
        int sectionRange = storage.size() / Configuration.instance.maximumNumberOfThreads;

        System.out.println("Distinct gap matches:");
        assignThreads(new DistinctGapService(cyclicBarrier), startIndex, sectionRange);

        System.out.println("Decreasing gap matches:");
        assignThreads(new DecreasingGapService(cyclicBarrier), startIndex, sectionRange);

        System.out.println("Increasing gap matches:");
        assignThreads(new IncreasingGapService(cyclicBarrier), startIndex, sectionRange);
    }

    private void initializePrimeStorage() {
        List<Thread> serviceThreads = new ArrayList<>();

        long startPrime = 3;
        long partition = (Configuration.instance.searchMaximum - startPrime) / Configuration.instance.maximumNumberOfThreads;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread primeThread = new Thread(new PrimeCalculatorService(cyclicBarrier, startPrime,
                    startPrime + partition));
            serviceThreads.add(primeThread);
            primeThread.start();
            startPrime += partition;
        }
        storage.add(2L);

        waitFor(serviceThreads);
        serviceThreads.clear();
        storage.sort();
    }

    private void assignThreads(AbstractGapService gapService, int startIndex, int segmentRange) {
        List<Thread> serviceThreads = new ArrayList<>();

        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread gapThread = new Thread(gapService.newInstance(startIndex, startIndex += segmentRange));
            serviceThreads.add(gapThread);
            gapThread.start();
        }

        waitFor(serviceThreads);
    }

    private void waitFor(List<Thread> threads) {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.findGapChains();
    }
}
