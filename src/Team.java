import java.util.ArrayList;
import java.util.Comparator;

public class Team {

    private final String name;
    private final ArrayList<Driver> drivers;
    private final ArrayList<Constructor> constructors;

    private static Team comparableTeam;
    private static float spareMoney;



    private float CC;
    private float CD;
    private float AP;
    private float ARP;
    private byte TC;

    public Team(String name, ArrayList<Driver> drivers, ArrayList<Constructor> constructors) {
        this.name = name;
        this.drivers = drivers;
        this.constructors = constructors;
        // Set stats
        if (comparableTeam != null) setAllStats();
    }

    public Team(String name, ArrayList<Driver> drivers, ArrayList<Constructor> constructors, float spareMoney) {
        this(name, drivers, constructors);
        setComparableTeam(this);
        setSpareMoney(spareMoney);
    }

    @Override
    public String toString() {
        drivers.sort(Comparator.comparing(Driver::getAvgPoints).reversed());
        constructors.sort(Comparator.comparing(Constructor::getAvgPoints).reversed());

        String suffix =
                "[AP:" + round(getAvgPoints(), 2) + "] [" +
                "ARP:" + round(getAvgRecPoints(), 2) + "] " +
                getDrivers() + " " +
                getConstructors();

        if (!comparableTeam.equals(this)) {
            if (getTransferCount() != 0) {
                return name + " [" +
                        Utility.formatMoney(getCurrentCost()) + "] (" +
                        "T:" + getTransferCount() + ") " +
                        suffix;
            }
            else {
                return "*" + name + "* [" +
                        Utility.formatMoney(getCurrentCost()) + "] " +
                        suffix;
            }
        }
        // Comparable Team
        return name + " " +
                suffix;
    }

    private float round(float num, int dp) {
        return (float) (Math.round(num * Math.pow(10, dp)) / Math.pow(10, dp));
    }

    public String getName() {
        return name;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    public static void setComparableTeam(Team team) {
        Team.comparableTeam = team;
    }

    public static void setSpareMoney(float spareMoney) {
        Team.spareMoney = spareMoney;
    }


    public void setAllStats() {
        setCurrentCost();
        setCostDifference();
        setAvgPoints();
        setAvgRecPoints();
        setTransferCount();
    }

    private void setCurrentCost() {
        float sum = 0;
        for (Driver d : drivers) {
            sum += d.getCost();
        }
        for (Constructor c : constructors) {
            sum += c.getCost();
        }
        CC = sum + (comparableTeam.equals(this) ? spareMoney : 0);
    }

    private void setCostDifference() {
        CD = getCurrentCost() - comparableTeam.getCurrentCost();
    }

    private void setAvgPoints() {
        drivers.sort(Comparator.comparing(Driver::getAvgPoints).reversed());
        float sum = 0;
        for (Driver d : drivers) {
            sum += d.getAvgPoints();
        }

        if (Main.activeTripleChip()) {
            sum += 2 * drivers.get(0).getAvgPoints();
            sum += drivers.get(1).getAvgPoints(); // Double highest driver (not accurate but will help simulate 2x chip)
        } else {
            sum += drivers.get(0).getAvgPoints(); // Double highest driver (not accurate but will help simulate 2x chip)
        }

        for (Constructor c : constructors) {
            sum += c.getAvgPoints();
        }
        AP = sum;
    }

    private void setAvgRecPoints() {
        drivers.sort(Comparator.comparing(Driver::getAvgRecPoints).reversed());

        float sum = 0;
        for (Driver d : drivers) {
            sum += d.getAvgRecPoints();
        }

        if (Main.activeTripleChip()) {
            sum += 2 * drivers.get(0).getAvgRecPoints(); // Triple highest driver (not accurate but will help simulate 3x chip)
            sum += drivers.get(1).getAvgRecPoints(); // Double second driver (not accurate but will help simulate 2x chip)
        } else {
            sum += drivers.get(0).getAvgRecPoints(); // Double highest driver (not accurate but will help simulate 2x chip)
        }

        for (Constructor c : constructors) {
            sum += c.getAvgRecPoints();
        }
        ARP = sum;
    }

    private void setTransferCount() {
        byte transferCount = 0;

        // Count drivers in this team not in the comparableTeam
        for (Driver d : drivers) {
            if (!comparableTeam.drivers.contains(d)) transferCount++;
        }
        // Count constructors in this team not in the comparableTeam
        for (Constructor c : constructors) {
            if (!comparableTeam.constructors.contains(c)) transferCount++;
        }

        TC = transferCount;
    }

    // Not needed but nice to keep
    public float getPointsPerMillion() {
        float highestDriver = Float.NEGATIVE_INFINITY;
        float sum = 0;
        for (Driver d : drivers) {
            sum += d.getPointsPerMillion();
            if (d.getPointsPerMillion() > highestDriver) {
                highestDriver = d.getPointsPerMillion();
            }
        }
        sum += highestDriver; // Double highest driver (not accurate but will help simulate 2x chip)
        for (Constructor c : constructors) {
            sum += c.getPointsPerMillion();
        }
        return sum/7;
    }

    public float getCurrentCost() {
        return CC;
    }

    public float getAvgPoints() {
        return AP;
    }

    public float getAvgRecPoints() {
        return ARP;
    }

    public float getCostDifference() {
        return CD;
    }

    public int getTransferCount() {
        return TC;
    }

    public boolean isTooExpensive() {
        return comparableTeam.getCurrentCost() < getCurrentCost();
    }

    public boolean contains(Driver driver) {
        for (Driver d : drivers) {
            if (d.equals(driver)) return true;
        }
        return false;
    }

}
