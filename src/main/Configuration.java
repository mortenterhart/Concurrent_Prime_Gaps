package main;

public enum Configuration {
    instance;

    int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
    long maximum = 10;
}
