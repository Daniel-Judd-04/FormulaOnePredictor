import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    // Chips
    private static final boolean tripleChip = false;
    private static final ArrayList<Driver> drivers = new ArrayList<>();
    private static final ArrayList<Constructor> constructors = new ArrayList<>();

    // Filters
    private static final boolean checkForCost = true;
    private static final boolean checkForTransfers = false;
    private static final int maxTransfers = 4;

    private static int teamNumber = 0;


    public static void main(String[] args) throws InterruptedException {
        // Display info
        for (Driver d : Driver.values()) System.out.println(d.toInfo());
        for (Constructor c : Constructor.values()) System.out.println(c.toInfo());

        // Define current team
        // Define drivers

        drivers.add(Driver.NOR);
        drivers.add(Driver.ALO);
        drivers.add(Driver.BOT);
        drivers.add(Driver.ALB);
        drivers.add(Driver.COL);
        // Define constructors

        constructors.add(Constructor.FERRARI);
        constructors.add(Constructor.MCLAREN);
        // Create Team
        Team comparableTeam = new Team("My Current Team", drivers, constructors, 200_000);
        comparableTeam.setAllStats();
        // Display
        System.out.println("\n" + comparableTeam);

        // Display Restrictions and Chips
        System.out.println("\nRestrictions:");
        System.out.println(" - Cost Cap {" + Utility.formatMoney(comparableTeam.getCurrentCost()) + "} [" + (checkForCost ? "ACTIVE" : "NOT ACTIVE") + "]");
        System.out.println(" - Max Transfers {" + maxTransfers + "} [" + (checkForTransfers ? "ACTIVE" : "NOT ACTIVE") + "]");
        System.out.println("\nAdd-ons:");
        System.out.println(" - x3 Chip [" + (tripleChip ? "ACTIVE" : "NOT ACTIVE") + "]");

        // Create all valid combinations of Teams
        LocalTime start = LocalTime.now();
        ArrayList<Team> allTeams = generateAllCombinations(Driver.values(), Constructor.values());

        System.out.println("\nGenerated " + allTeams.size() + " valid teams!");

        System.out.print("\n\nAnalysing .");
        allTeams.sort(Comparator.comparing(Team::getCostDifference).reversed()); // Prioritise more expensive teams
        System.out.print(" .");
        allTeams.sort(Comparator.comparing(Team::getTransferCount)); // Prioritise less transfers
        System.out.print(" .");
        allTeams.sort(Comparator.comparing(Team::getAvgPoints).reversed()); // Prioritise more avg points
        System.out.print(" .");
        allTeams.sort(Comparator.comparing(Team::getAvgRecPoints).reversed()); // Prioritise more avg rec points
        System.out.println(" [DONE]");

        System.out.println("\nAVG REC:");
        displayOrder(allTeams, 10, 2);

        System.out.println("\nAVG:");
        allTeams.sort(Comparator.comparing(Team::getAvgPoints).reversed()); // Prioritise more avg points
        displayOrder(allTeams, 10, 2);

        LocalTime end = LocalTime.now();
        System.out.println("Finished in (" + ((float) ((end.toNanoOfDay() - start.toNanoOfDay()) / 1000000) / 1000) + "s)");
    }

    private static void displayOrder(ArrayList<Team> allTeams, int numberOfBest, int numberOfWorst) {
        for (int i = 0; i < numberOfBest; i++) {
            System.out.println(allTeams.get(i));
        }
        System.out.println("...");
        for (int i = 0; i < numberOfWorst; i++) {
            System.out.println(allTeams.get(allTeams.size() - numberOfWorst + i));
        }
    }


    public static ArrayList<Team> generateAllCombinations(Driver[] drivers, Constructor[] constructors) {
        ArrayList<Team> allTeams = new ArrayList<>();
        List<Driver> selectedDrivers = new ArrayList<>();
        List<Constructor> selectedConstructors = new ArrayList<>();
        // Generate all combinations of drivers and constructors
        generateDriverCombinations(drivers, selectedDrivers, allTeams, constructors, selectedConstructors, 0);
        return allTeams;
    }

    private static void generateDriverCombinations(Driver[] drivers, List<Driver> currentDrivers, ArrayList<Team> allTeams, Constructor[] constructors, List<Constructor> currentConstructors, int driverStart) {
        if (currentDrivers.size() == 5) { // Now each team needs exactly 5 drivers
            generateConstructorCombinations(constructors, currentConstructors, allTeams, currentDrivers, 0);
            return;
        }

        for (int i = driverStart; i < drivers.length; i++) {
            currentDrivers.add(drivers[i]);
            generateDriverCombinations(drivers, currentDrivers, allTeams, constructors, currentConstructors, i + 1);
            currentDrivers.remove(currentDrivers.size() - 1);
        }
    }

    private static void generateConstructorCombinations(Constructor[] constructors, List<Constructor> currentConstructors, ArrayList<Team> allTeams, List<Driver> currentDrivers, int constructorStart) {
        if (currentConstructors.size() == 2) { // Now each team needs exactly 2 constructors
            Team newTeam = new Team("Team" + teamNumber++, new ArrayList<>(currentDrivers), new ArrayList<>(currentConstructors));
            if ((!checkForCost || !newTeam.isTooExpensive()) && (!checkForTransfers || newTeam.getTransferCount() <= maxTransfers)) {
                allTeams.add(newTeam);
            }
            return;
        }

        for (int i = constructorStart; i < constructors.length; i++) {
            currentConstructors.add(constructors[i]);
            generateConstructorCombinations(constructors, currentConstructors, allTeams, currentDrivers, i + 1);
            currentConstructors.remove(currentConstructors.size() - 1);
        }
    }

    public static ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public static ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    public static boolean activeTripleChip() {
        return tripleChip;
    }
}