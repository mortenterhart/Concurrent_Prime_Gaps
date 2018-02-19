package service.gap;

public enum GapServiceType {
    distinct, increasing, decreasing;

    public String capitalizeName() {
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1);
    }
}
