package storage;

import model.PrimeGapChain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public enum GapChainStorage {
    gapStorage;

    private final List<PrimeGapChain> distinctChains = new ArrayList<>();
    private final List<PrimeGapChain> decreasingChains = new ArrayList<>();
    private final List<PrimeGapChain> increasingChains = new ArrayList<>();

    public boolean addDistinctChain(PrimeGapChain distinctChain) {
        return distinctChains.add(distinctChain);
    }

    public boolean addDistinctChains(List<PrimeGapChain> chains) {
        return distinctChains.addAll(chains);
    }

    public boolean addDecreasingChain(PrimeGapChain decreasingChain) {
        return decreasingChains.add(decreasingChain);
    }

    public boolean addDecreasingChains(List<PrimeGapChain> chains) {
        return decreasingChains.addAll(chains);
    }

    public void addIncreasingChain(PrimeGapChain increasingChain) {
        increasingChains.add(increasingChain);
    }

    public void addIncreasingChains(List<PrimeGapChain> chains) {
        increasingChains.addAll(chains);
    }

    public void sortChains() {
        sort(distinctChains);
        sort(decreasingChains);
        sort(increasingChains);
    }

    private void sort(List<PrimeGapChain> chains) {
        chains.sort(Comparator.comparingInt(PrimeGapChain::length));
    }

    public List<PrimeGapChain> getDistinctChains() {
        return distinctChains;
    }

    public List<PrimeGapChain> getDecreasingChains() {
        return decreasingChains;
    }

    public List<PrimeGapChain> getIncreasingChains() {
        return increasingChains;
    }
}
