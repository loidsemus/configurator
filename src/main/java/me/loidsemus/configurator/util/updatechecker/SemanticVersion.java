package me.loidsemus.configurator.util.updatechecker;

public class SemanticVersion implements Comparable<SemanticVersion> {

    private final String versionString;
    private final int[] numbers;

    public SemanticVersion(String version) {
        this.versionString = version;

        final String[] split = version.split("-")[0].split("\\.");
        numbers = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            numbers[i] = Integer.parseInt(split[i]);
        }
    }

    @Override
    public int compareTo(SemanticVersion another) {
        final int maxLength = Math.max(numbers.length, another.numbers.length);
        for (int i = 0; i < maxLength; i++) {
            final int left = i < numbers.length ? numbers[i] : 0;
            final int right = i < another.numbers.length ? another.numbers[i] : 0;
            if (left != right) {
                return left < right ? -1 : 1;
            }
        }
        return 0;
    }

    public String getVersionString() {
        return versionString;
    }

    @Override
    public String toString() {
        return getVersionString();
    }
}
