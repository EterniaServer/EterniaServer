package br.com.eterniaserver.eterniaserver.objects;

public class ChannelObject {

    private final String format;
    private final String name;
    private final String perm;
    private final boolean hasRange;
    private final Integer range;

    public ChannelObject(String format, String name, String perm, boolean hasRange, Integer range) {
        this.format = format;
        this.name = name;
        this.perm = perm;
        this.hasRange = hasRange;
        this.range = range;
    }

    public String getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public String getPerm() {
        return perm;
    }

    public boolean isHasRange() {
        return hasRange;
    }

    public Integer getRange() {
        return range;
    }

}
