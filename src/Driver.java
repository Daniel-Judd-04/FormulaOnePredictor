import java.util.Arrays;
import java.util.zip.DataFormatException;

public enum Driver {
    // Current cost, Weekend points
    VER(31_500_000, new int[]{45, 36, -10, 47, 58, 40, 35, 13, 37, 38, 25, 29, 17, 35, 28, 15, 18}),
    PER(23_000_000, new int[]{31, 31, 22, 33, 40, 30, 11, -20, -17, 19, 18, 6, 20, 19, 14, 15, -2}),
    LEC(25_200_000, new int[]{22, 37, 38, 31, 34, 33, 24, 45, -16, 18, 16, 2, 20, 22, 26, 46, 28}),
    SAI(22_700_000, new int[]{36, 0, 46, 28, 30, 26, 17, 23, -16, 16, 28, 28, 17, 15, 20, 19, -4}),
    HAM(24_800_000, new int[]{12, 6, -19, 5, 30, 20, 16, 20, 32, 29, 23, 47, 26, 46, 21, 18, 26}),
    RUS(21_400_000, new int[]{20, 15, -3, 13, 27, 10, 23, 16, 27, 27, 43, -9, 27, -21, 13, 21, 26}),
    NOR(26_800_000, new int[]{16, 8, 23, 19, 36, 25, 37, 19, 40, 51, 13, 26, 28, 17, 56, 36, 38}),
    PIA(25_200_000, new int[]{10, 23, 21, 10, 12, 22, 24, 27, 17, 15, 42, 23, 46, 30, 20, 29, 47}),
    ALO(16_300_000, new int[]{7, 16, 9, 14, 15, 9, 2, 5, 14, 1, 10, 9, 4, 9, 6, 6, 14}),
    STR(14_500_000, new int[]{8, -17, 16, 13, 9, -19, 12, 2, 14, 6, 16, 12, 9, 7, 1, 4, -3}),
    OCO(11_800_000, new int[]{7, 8, 0, 5, 12, 11, 1, -19, 16, 4, 4, 6, 7, 10, 3, 4, 7}),
    GAS(9_800_000, new int[]{6, -20, 5, 5, 13, 15, 1, 2, 14, 7, 12, -20, -19, 3, 9, 1, -4}),
    MAG(12_600_000, new int[]{7, 7, 8, 9, 17, 1, 13, -33, 14, 4, 16, 11, 7, 6, 7, 9, 0}), // -25
    HUL(10_200_000, new int[]{-3, 9, 11, 10, 4, 13, 3, -32, 19, 6, 20, 14, 0, 2, 6, -3, 6}),
    ALB(9_000_000, new int[]{0, 6, 4, -20, 11, 10, -20, 5, -12, 4, 12, 6, 5, 2, -2, 6, 12}),
    COL(5_800_000, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 9}),
//    SAR( 4_800_000, new int[] {3, 7, 0, 9, 8, -7, -2, 1, -19, 1, -1, 4, 1, 2, 0}),
    ZHO(7_300_000, new int[]{11, -2, 4, -17, 13, 20, 5, 2, 6, 4, 2, -2, 0, -18, -2, 4, 7}),
    BOT(7_200_000, new int[]{0, -1, 2, 8, -14, 10, -2, 8, 8, 1, 8, 4, 1, 3, 1, 6, 1}),
    RIC(11_000_000, new int[]{5, 0, 8, -19, -9, 16, -2, 0, 9, 7, 10, 6, 1, 8, 5, 1, 2}),
    TSU(10_300_000, new int[]{-1, -1, 11, 10, -6, 23, 4, 7, 0, 4, 10, 7, 4, 6, -3, -17, -19});

    private static final int NUMBER_OF_RACES = 17;

    private final float cost;
    private final int[] pastPoints;

    private float AP;
    private float ARP;
    private float C;

    Driver(float cost, int[] pastPoints) {
        if (pastPoints.length != NUMBER_OF_RACES) try {
            throw new DataFormatException("DRIVER ERROR - C: " + cost + " PP: " + pastPoints.length);
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        this.cost = cost;
        this.pastPoints = pastPoints;
        setAvgPoints();
        setAvgRecPoints();
        setConsistency();
    }

    @Override
    public String toString() {
        if (!Main.getDrivers().contains(this)) return getName();
        return "\u001B[37m" + getName() + "\u001B[0m";
    }

    public String toInfo() {
        StringBuilder returnString = new StringBuilder("\n" + getName() + ":");

        returnString.append("\nCost - " + Utility.formatMoney(cost));
        returnString.append("\nAvg Points - " + getAvgPoints());
        returnString.append("\nAvg Rec Points - " + getAvgRecPoints());
        returnString.append("\nImproving - " + (isImproving() ? "Yes" : "No"));
        returnString.append("\nPast Points:");
        for (int i = 0; i < pastPoints.length; i++) {
            returnString.append("\n Race " + (i + 1) + " - " + pastPoints[i]);
        }

        return returnString.toString();
    }

    public String getName() {
        return name();
    }

    public float getCost() {
        return cost;
    }

    public int[] getPastPoints() {
        return pastPoints;
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

    public void setConsistency() {
        float consistency = 0;
        float avg = getAvgPoints();
        for (int point : pastPoints) {
            consistency += Math.abs(point - avg);
        }
        System.out.println(consistency / pastPoints.length);
        C = consistency / pastPoints.length;
    }

    public float getAvgPoints() {
        return AP;
    }

    public float getAvgRecPoints() {
        return ARP;
    }

    public float getPointsPerMillion() {
        return getAvgPoints() / (cost / 1_000_000);
    }

    public boolean isImproving() {
        return getAvgRecPoints() > getAvgPoints();
    }
}
  