package service.gap;

import logging.Logger;
import model.PrimeGapChain;
import service.prime.IPrimeService;
import service.prime.PrimeCalculatorService;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

import static storage.PrimeStorage.primeStorage;

public abstract class AbstractGapServiceTest {
    private static AbstractGapService abstractService;
    private static IGapService interfaceService;

    private static int minimum = 0;
    private static int maximum = 1_000;

    protected static final CyclicBarrier cyclicBarrier = new CyclicBarrier(1);

    protected static void buildServices(AbstractGapService abstractGapService,
                                        IGapService interfaceGapService) {
        abstractService = abstractGapService;
        interfaceService = interfaceGapService;
    }

    protected static void initializePrimeStorage() {
        Logger.instance.init();
        primeStorage.clear();

        IPrimeService primeService = new PrimeCalculatorService(cyclicBarrier, minimum, maximum);
        Thread thread = new Thread(primeService);
        thread.start();

        waitFor(thread);
    }

    protected boolean callIsNewChainMember(PrimeGapChain chain, long gap) {
        return interfaceService.isNewChainMember(chain, gap);
    }

    protected IGapService callNewInstance(int fromIndex, int toIndex) {
        return abstractService.newInstance(fromIndex, toIndex);
    }

    protected List<PrimeGapChain> runAndFetchResults() {
        Thread thread = new Thread(abstractService.newInstance(0, primeStorage.size()));
        thread.start();
        waitFor(thread);

        return interfaceService.fetchResults();
    }

    protected static void waitFor(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
