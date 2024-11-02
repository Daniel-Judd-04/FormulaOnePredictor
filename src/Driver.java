import java.util.Arrays;
import java.util.zip.DataFormatException;

public enum Driver {
  // Current cost, Weekend points
  VER(31_800_000,
      new int[]{45, 36, -10, 47, 58, 40, 35, 13, 37, 38, 25, 29, 17, 35, 28, 15, 18, 28, 38, 21}),
  PER(23_300_000,
      new int[]{31, 31, 22, 33, 40, 30, 11, -20, -17, 19, 18, 6, 20, 19, 14, 15, -2, 9, 19, 10}),
  LEC(25_700_000,
      new int[]{22, 37, 38, 31, 34, 33, 24, 45, -16, 18, 16, 2, 20, 22, 26, 46, 28, 21, 55, 35}),
  SAI(24_300_000,
      new int[]{36, 0, 46, 28, 30, 26, 17, 23, -16, 16, 28, 28, 17, 15, 20, 19, -4, 18, 42, 47}),
  HAM(24_700_000,
      new int[]{12, 6, -19, 5, 30, 20, 16, 20, 32, 29, 23, 47, 26, 46, 21, 18, 26, 17, -10, 24}),
  RUS(22_300_000,
      new int[]{20, 15, -3, 13, 27, 10, 23, 16, 27, 27, 43, -9, 27, -21, 13, 21, 26, 19, 38, 20}),
  NOR(27_200_000,
      new int[]{16, 8, 23, 19, 36, 25, 37, 19, 40, 51, 13, 26, 28, 17, 56, 36, 38, 35, 30, 29}),
  PIA(25_500_000,
      new int[]{10, 23, 21, 10, 12, 22, 24, 27, 17, 15, 42, 23, 46, 30, 20, 29, 47, 26, 30, 26}),
  ALO(15_400_000, new int[]{7, 16, 9, 14, 15, 9, 2, 5, 14, 1, 10, 9, 4, 9, 6, 6, 14, 9, -4, -19}),
  STR(14_800_000,
      new int[]{8, -17, 16, 13, 9, -19, 12, 2, 14, 6, 16, 12, 9, 7, 1, 4, -3, 4, 4, 11}),
  OCO(13_300_000, new int[]{7, 8, 0, 5, 12, 11, 1, -19, 16, 4, 4, 6, 7, 10, 3, 4, 7, 6, 10, 10}),
  GAS(10_000_000,
      new int[]{6, -20, 5, 5, 13, 15, 1, 2, 14, 7, 12, -20, -19, 3, 9, 1, -4, 4, -1, 6}),
  //    MAG(13_400_000, new int[]{7, 7, 8, 9, 17, 1, 13, -33, 14, 4, 16, 11, 7, 6, 7, 9, 0, -1, 9, 15}),
  HUL(11_700_000, new int[]{-3, 9, 11, 10, 4, 13, 3, -32, 19, 6, 20, 14, 0, 2, 6, -3, 6, 6, 11, 9}),
  BEA(13_300_000, new int[]{26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0}),
  ALB(9_100_000,
      new int[]{0, 6, 4, -20, 11, 10, -20, 5, -12, 4, 12, 6, 5, 2, -2, 6, 12, -16, 11, -18}),
  COL(7_300_000, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 9, 6, 9, 8}),
  //    SAR( 4_800_000, new int[] {3, 7, 0, 9, 8, -7, -2, 1, -19, 1, -1, 4, 1, 2, 0}),
  ZHO(8_200_000, new int[]{11, -2, 4, -17, 13, 20, 5, 2, 6, 4, 2, -2, 0, -18, -2, 4, 7, 6, 6, 7}),
  BOT(6_900_000, new int[]{0, -1, 2, 8, -14, 10, -2, 8, 8, 1, 8, 4, 1, 3, 1, 6, 1, 3, 1, 2}),
  //    RIC(12_000_000, new int[]{5, 0, 8, -19, -9, 16, -2, 0, 9, 7, 10, 6, 1, 8, 5, 1, 2, 23}),
  LAW(10_600_000, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 4}),
  TSU(9_900_000,
      new int[]{-1, -1, 11, 10, -6, 23, 4, 7, 0, 4, 10, 7, 4, 6, -3, -17, -19, 4, 2, -20});

  private static final int NUMBER_OF_RACES = 20;

  private final float cost;
  private final int[] pastPoints;

  private float AP;
  private float ARP;
  private float C;

  Driver(float cost, int[] pastPoints) {
    if (pastPoints.length != NUMBER_OF_RACES) {
      try {
        throw new DataFormatException(
            "DRIVER ERROR - C: " + cost + " PP: " + pastPoints.length);
      } catch (DataFormatException e) {
        throw new RuntimeException(e);
      }
    }
    this.cost = cost;
    this.pastPoints = pastPoints;
    setAvgPoints();
    setAvgRecPoints();
    setConsistency();
  }

  @Override
  public String toString() {
    if (values().length != 20) {
      throw new RuntimeException("Number of drivers not equal to 20");
    }
    if (!Main.getDrivers().contains(this)) {
      return getName();
    }
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
  