package main;

import prime.PrimeCalculatorService;
import service.DecreasingGapService;
import service.DistinctGapService;
import service.IncreasingGapService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import static prime.PrimeStorage.storage;

public class Application {
    private final CyclicBarrier cyclicBarrier;

    public Application() {
        cyclicBarrier = new CyclicBarrier(Configuration.instance.maximumNumberOfThreads);
    }

    public void execute() {
        List<Thread> serviceThreads = new ArrayList<>();

        long partition = (Configuration.instance.searchMaximum - 2) / Configuration.instance.maximumNumberOfThreads;
        long startPrime = 2;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread primeThread = new Thread(new PrimeCalculatorService(cyclicBarrier, startPrime,
                    startPrime + partition));
            serviceThreads.add(primeThread);
            primeThread.start();
            startPrime += partition;
        }

        waitFor(serviceThreads);
        serviceThreads.clear();
        storage.sort();

        int sectionRange = storage.size() / Configuration.instance.maximumNumberOfThreads;

        System.out.println("Distinct gap matches:");
        int startIndex = 0;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread distinctGapThread = new Thread(new DistinctGapService(cyclicBarrier, startIndex,
                    startIndex + sectionRange));
            serviceThreads.add(distinctGapThread);
            distinctGapThread.start();
            startIndex += sectionRange;
        }

        waitFor(serviceThreads);
        serviceThreads.clear();

        System.out.println("Decreasing gap matches:");
        startIndex = 0;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread decreasingGapThread = new Thread(new DecreasingGapService(cyclicBarrier, startIndex,
                    startIndex + sectionRange));
            serviceThreads.add(decreasingGapThread);
            decreasingGapThread.start();
            startIndex += sectionRange;
        }

        waitFor(serviceThreads);
        serviceThreads.clear();

        System.out.println("Increasing gap matches:");
        startIndex = 0;
        for (int i = 0; i < Configuration.instance.maximumNumberOfThreads; i++) {
            Thread decreasingGapThread = new Thread(new IncreasingGapService(cyclicBarrier, startIndex,
                    startIndex + sectionRange));
            serviceThreads.add(decreasingGapThread);
            decreasingGapThread.start();
            startIndex += sectionRange;
        }

        waitFor(serviceThreads);
        serviceThreads.clear();
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
        application.execute();
    }
}
