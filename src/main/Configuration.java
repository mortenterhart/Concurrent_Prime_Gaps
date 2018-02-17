package main;

public enum Configuration {
    instance;

    int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
    long searchMaximum = Long.MAX_VALUE;
}
