import java.util.Arrays;
import java.util.zip.DataFormatException;

public enum Constructor {
    // Purchase price, Current cost, Weekend points
    REDBULL(29_300_000, new int[] {89, 90, 32, 108, 126, 85, 61, 8, 25, 82, 56, 55, 47, 69, 52, 40, 26, 47}),
    FERRARI(24_700_000, new int[] {73, 58, 92, 59, 74, 79, 51, 68, -16, 44, 69, 35, 47, 47, 59, 65, 34, 49}),
    MERCEDES( 24_000_000, new int[] {42, 36, -17, 28, 62, 40, 54, 46, 74, 66, 76, 38, 58, 25, 39, 49, 65, 49}),
    ASTON_MARTIN( 15_600_000, new int[] {20, 9, 35, 32, 29, -7, 15, 13, 38, 10, 27, 31, 23, 21, 17, 11, 16, 18}),
    MCLAREN( 26_500_000, new int[] {36, 41, 54, 39, 48, 50, 64, 56, 57, 66, 55, 59, 74, 67, 81, 83, 85, 81}),
    HAAS( 11_000_000, new int[] {9, 19, 20, 20, 26, 19, 21, -65, 34, 11, 41, 30, 10, 7, 16, 11, 16, 10}),
    ALPINE( 11_000_000, new int[] {12, -13, 6, 11, 28, 29, 5, -12, 31, 24, 21, -12, -10, 18, 17, 18, 2, 11}),
    KICK( 6_800_000, new int[] {10, -4, 7, -8, 4, 29, 2, 12, 13, 8, 9, 3, 2, -14, -2, 9, 17, 8}),
    RB( 12_300_000, new int[] {7, 4, 24, -4, -14, 44, 12, 12, 19, 10, 23, 16, 25, 18, 15, -15, -14, 22}),
    WILLIAMS( 7_000_000, new int[] {4, 14, 5, -10, 20, 4, -21, 11, -26, 4, 11, 15, 9, 5, -2, 22, 31, -7});

    private static final int NUMBER_OF_RACES = 18;

    private final float cost;
    private final int[] pastPoints;

    private float AP;
    private float ARP;

    Constructor(float cost, int[] pastPoints) {
        if (pastPoints.length != NUMBER_OF_RACES) try {
            throw new DataFormatException("CONSTRUCTOR ERROR - C: " + cost + " PP: " + pastPoints.length);
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        this.cost = cost;
        this.pastPoints = pastPoints;
        setAvgPoints();
        setAvgRecPoints();
    }

    @Override
    public String toString() {
        if (!Main.getConstructors().contains(this)) return getName();
        return "\u001B[37m" + getName() + "\u001B[0m";
    }

    public String toInfo() {
        StringBuilder returnString = new StringBuilder("\n" + getName() + ":");

        returnString.append("\nCost - " + Utility.formatMoney(cost));
        returnString.append("\nAvg Points - " + getAvgPoints());
        returnString.append("\nAvg Rec Points - " + getAvgRecPoints());
        returnString.append("\nImproving - " + (isImproving() ? "Yes" : "No"));
        returnString.append("\nPast Points:");
        for (int i = 0 ; i < pastPoints.length; i++) {
            returnString.append("\n Race " + (i+1) + " - " + pastPoints[i]);
        }

        return returnString.toString();
    }

    public String getName() {
        return name();
    }

    public float getCost() {
        return cost;
    }

    private void setAvgPoints() {
        AP = (float) Arrays.stream(pastPoints).sum() / pastPoints.length;
    }

    private void setAvgRecPoints() {
        int sumOfWeightedInts = 0;
        int sumOfWeights = 0;

        for (int i = 0; i < pastPoints.length; i++) {
            int weight = i + 1; // Weight increases with each number
            sumOfWeightedInts += pastPoints[i] * weight;
            sumOfWeights += weight;
        }

        // Ensure we don't divide by zero
        if (sumOfWeights == 0) {
            ARP = 0;
        } else {
            ARP = (float) sumOfWeightedInts / sumOfWeights;
        }
    }

    public float getAvgPoints() {
        return AP;
    }

    public float getAvgRecPoints() {
        return ARP;
    }

    public float getPointsPerMillion() {
        return getAvgPoints() / (cost/1_000_000);
    }

    public boolean isImproving() {
        return getAvgPoints() <= getAvgRecPoints();
    }
}