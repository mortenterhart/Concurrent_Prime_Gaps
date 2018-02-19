package main;

import logging.Logger;

import java.util.concurrent.CyclicBarrier;

public class Application {
    private final CyclicBarrier cyclicBarrier;
    private final ServiceExecutor serviceExecutor;

    public Application() {
        cyclicBarrier = new CyclicBarrier(Configuration.instance.maximumNumberOfThreads);
        serviceExecutor = new ServiceExecutor(this);
    }

    private void init() {
        Logger.instance.init();
    }

    private void execute() {
        serviceExecutor.execute();
    }

    private void prepareShutdown() {
        Logger.instance.close();
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.init();
        application.execute();
        application.prepareShutdown();
    }
}
