package main;

public enum Configuration {
    instance;

    int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
    long searchMaximum = 10_000_000L;
}
