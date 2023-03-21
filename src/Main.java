import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static Cabin[] cabins = new Cabin[12];
    static ArrayList<String> firstNameWaitingList = new ArrayList<String>();
    static ArrayList<String> secondNameWaitingList = new ArrayList<String>();
    static ArrayList<Double> expensesWaitingList = new ArrayList<Double>();

    //Main method
    public static void main(String[] args) throws IOException {

        //Creating empty Cabin objects inside the cabins array at the initiation of the program
        for (int i = 0; i < cabins.length; i++) {
            cabins[i] = new Cabin();
        }
        controlBookings();

    }

    //Call the required functions by considering the options chosen by the user (returned by the displayMenu())
    public static void controlBookings() throws IOException {
        boolean cont = true;
        while (cont) {

            String selectedOption = displayMenu();

            switch (selectedOption) {
                case "A":
                    addPassengerToCabin();
                    break;
                case "V":
                    showAllCabins();
                    break;
                case "E":
                    showEmptyCabins();
                    break;
                case "D":
                    deleteCustomersFromCabinByCabinAndBedNumber();
                    break;
                case "F":
                    searchCabinByCustomerName();
                    break;
                case "S":
                    storeProgramDataInToFile();
                    break;
                case "L":
                    loadProgramDataToFile();
                    break;
                case "O":
                    viewPassengersOrderedAlphabeticallyByName();
                    break;
                case "T":
                    showPassengerExpenses();
                    break;


                case "Q":
                    cont = false;
                    break;

                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }


    private static void addPassengerToCabin() {
        try {
        if (isFreeCabinsAvailable()) {
            int cabinNo = Integer.parseInt(Util.getInputFromUser("Cabin No: ")) - 1;
            int bedNo = Integer.parseInt(Util.getInputFromUser("Bed No: ")) - 1;
            if (cabins[cabinNo].getBeds()[bedNo] == null) {
                try {
                    String firstName = Util.getInputFromUser("First Name: ");
                    String secondName = Util.getInputFromUser("Last Name: ");
                    firstName = firstName.toLowerCase();
                    secondName = secondName.toLowerCase();
                    double expenses = Double.parseDouble(Util.getInputFromUser("Expenses: "));
                    Passenger passenger = new Passenger(firstName, secondName, expenses);
                    cabins[cabinNo].setPassengerByBedNo(passenger, bedNo);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Util.printWarning("Cabin number does not exist");
                } catch (NumberFormatException e) {
                    Util.printWarning("Invalid value input");
                }
            } else {
                Util.printWarning("Selected slot is occupied!"); // handle the exception for cabin number input a greater value than 12
            }
        } else {
            System.out.println("There are no cabins and beds available! The passenger you are adding will be added in to the waiting list.");
            String firstName = Util.getInputFromUser("First Name: ");
            String secondName = Util.getInputFromUser("Last Name: ");
            double expenses = Double.parseDouble(Util.getInputFromUser("Expenses: "));
            int i = firstNameWaitingList.size();
            firstNameWaitingList.add(i, firstName);
            secondNameWaitingList.add(i, secondName);
            expensesWaitingList.add(i, expenses);
        }
    }
        catch (Exception e){
            Util.printWarning("Something Went Wrong!");
        }
    }

    //Method to check are there any free cabin beds available
    public static boolean isFreeCabinsAvailable() {
        for (Cabin cabin : cabins) {
            if (cabin == null) {
                return true;
            } else {
                for (Passenger passenger : cabin.getBeds()) {
                    if (passenger == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //Method to show all cabins
    public static void showAllCabins() {
        for (int val = 0; val < cabins.length; val++) {
            System.out.println("Cabin no: " + (val + 1));

            for (int i = 0; i < cabins[val].getBeds().length; i++) {
                if (cabins[val].getBeds()[i] != null) {
                    System.out.println("Bed no" + (i + 1) + " " + cabins[val].getBeds()[i].getFirstName());
                }
            }
        }
    }

    //Method to display empty cabin. At least one bed is free then will be considered as an empty cabin
    public static void showEmptyCabins() {
        for (int val = 0; val < cabins.length; val++) {
            if (cabins[val].isEmpty()) {
                System.out.println("Cabin no: " + (val + 1));
            }

        }
    }

    //Method to delete customers from the cabins
    public static void deleteCustomersFromCabinByCabinAndBedNumber() {
        try {
            int cabinNumber = Integer.parseInt(Util.getInputFromUser("Enter the cabin number: "));
            int bedNumber = Integer.parseInt(Util.getInputFromUser("Enter the bed number: "));
            cabinNumber -= 1;
            bedNumber -= 1;
            if (((12 > cabinNumber) && (cabinNumber >= 0)) || ((bedNumber >= 0) && (bedNumber < 3))) {
                cabins[cabinNumber].getBeds()[bedNumber] = null;
                System.out.println("Deletion Successful!");
                if (firstNameWaitingList.size() != 0) {
                    addAPassengerToACabinBedFromTheWaitingList(cabinNumber, bedNumber);
                }

            } else {
                Util.printWarning("Wrong inputs entered for the Cabin or Bed Number: ");
            }
        }
        catch (Exception e){
            Util.printWarning("Something went wrong!");
        }
    }

    //Method to add a passenger to a cabin bed from the waiting list
    public static void addAPassengerToACabinBedFromTheWaitingList(int cabinNo, int bedNo) {
        String firstName = firstNameWaitingList.get(0);
        String secondName = secondNameWaitingList.get(0);
        Double expenses = expensesWaitingList.get(0);
        Passenger passenger = new Passenger(firstName, secondName, expenses);
        cabins[cabinNo].setPassengerByBedNo(passenger, bedNo);

        System.out.println(firstNameWaitingList.get(0) + " " + secondNameWaitingList.get(0) + " from the waiting list added in to the " +
                "cabin number: " + (cabinNo + 1) + " bed number: " + (bedNo + 1) + " successfully!");

        firstNameWaitingList.remove(0);
        secondNameWaitingList.remove(0);
        expensesWaitingList.remove(0);
    }

    //Method to sort out a cabin by searching the customer name
    public static void searchCabinByCustomerName() {
        try {
            String customerName = Util.getInputFromUser("Enter first and last name of customer with a space: ");
            customerName = customerName.toLowerCase();
            for (int val = 0; val < cabins.length; val++) {
                if (cabins[val] != null) {
                    if (cabins[val].isPassengerAvailable(customerName)) {
                        System.out.println("Cabin number: " + (val + 1));
                    }
                }
            }
        }
        catch (Exception e){
            Util.printWarning("Wrong Input!");
        }
    }

    //Method to write data in to a text file

    public static void storeProgramDataInToFile() throws IOException {
        int cabinNo;
        int bedNo;
        String firstName;
        String secondName;
        double expenses;
        FileWriter fw = null;
        fw = new FileWriter("testoutV2.txt");
        for (int i = 0; i < cabins.length; i++) {
            if (cabins[i] != null) {
                for (int val = 0; val < cabins[i].getBeds().length; val++) {
                    if (cabins[i].getBeds()[val] != null) {
                        cabinNo = i + 1;
                        bedNo = val + 1;
                        firstName = cabins[i].getBeds()[val].getFirstName();
                        secondName = cabins[i].getBeds()[val].getSecondName();
                        expenses = cabins[i].getBeds()[val].getExpenses();

                        fw.write("\nCabin no: \n" + cabinNo + "\nBed no:\n" + bedNo + "\nFirst Name:\n" + firstName + "\nSecond Name:\n" + secondName + "\nExpenses:\n" + expenses);
                    }
                }
            }
        }
        fw.close();
    }

    //Method to load program data from file
    public static void loadProgramDataToFile() throws IOException {
        Path path = Paths.get("testoutV2.txt");

        if (path.toFile().isFile()) {
            File file = new File("testoutV2.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp;
            temp = br.readLine();
            while (br.readLine() != null) {
                int cabinNO = Integer.parseInt(br.readLine()) - 1;
                temp = br.readLine();
                int bedNo = Integer.parseInt(br.readLine()) - 1;
                temp = br.readLine();
                String firstName = br.readLine();
                temp = br.readLine();
                String secondName = br.readLine();
                temp = br.readLine();
                double expenses = Double.parseDouble(br.readLine());
                Passenger passenger = new Passenger(firstName, secondName, expenses);
                cabins[cabinNO].setPassengerByBedNo(passenger, bedNo);
            }
        }
    }

    //Method for View Passengers Ordered Alphabetically By Name
    public static void viewPassengersOrderedAlphabeticallyByName() {
        int temporyArrayLength = 0;
        for (Cabin s : cabins) {
            if (s != null) {
                for (Passenger r : s.getBeds()) {
                    if (r != null) {
                        temporyArrayLength++;
                    }
                }
            }
        }
        String[] tempory = new String[temporyArrayLength];
        int index = -1;
        for (Cabin s : cabins) {
            if (s != null) {
                for (Passenger r : s.getBeds()) {
                    if (r != null) {
                        String fullName = r.getFirstName() + " " + r.getSecondName();
                        index++;
                        tempory[index] = fullName;
                    }
                }
            }
        }
        String temp;
        for (int i = 0; i < temporyArrayLength; i++) {
            for (int j = i + 1; j < temporyArrayLength; j++) {

                // to compare one string with other strings
                if (tempory[i].compareTo(tempory[j]) > 0) {
                    // swapping
                    temp = tempory[i];
                    tempory[i] = tempory[j];
                    tempory[j] = temp;
                }
            }
        }
        System.out.println("Customers List in Alphabetical Order: ");
        for (String s : tempory) {
            System.out.println(s);
        }
    }

    //Method to show passenger expenses
    public static void showPassengerExpenses(){
        double total = 0;
        for(Cabin cabin:cabins){
            if(cabin != null){
                for (Passenger passenger:cabin.getBeds()){
                    if(passenger != null ){
                        total += passenger.getExpenses();
                        System.out.println("Customer Name: "+passenger.getFirstName()+" "+passenger.getSecondName()+"Expense: "+passenger.getExpenses());
                    }
                }
            }
        }
        System.out.println("\nTotal expense: "+total);
    }

    //function for the menu
    public static String displayMenu() {
        System.out.println("Main Menu");
        System.out.println("A: Add customer to cabin\nV: Views All cabins\nE: Display empty cabins \nD: Delete customer from cabin " +
                "\nF: Find cabin form customer name:\nS: Store program data into file:\nL: Load program data from file:" +
                "\nO: View passengers Ordered alphabetically by name\nT: Show Passenger Expenses\nQ: Quit(Q)");

        System.out.print("\n\nChoose option: ");

        Scanner menInp = new Scanner(System.in);
        return menInp.nextLine().toUpperCase();
    }
}
