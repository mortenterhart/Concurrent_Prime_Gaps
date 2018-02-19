package main;

public enum Configuration {
    instance;

    public int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
    public long searchMaximum = 500_000_000;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");
    public String lineSeparator = System.lineSeparator();
    public String logFilePath = userDirectory + fileSeparator + "log" + fileSeparator + "PrimeGaps.log";
}
