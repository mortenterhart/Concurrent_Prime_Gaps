package main;

public enum Configuration {
    instance;

    public int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
    public long searchMaximum = 100_000_000;
}
