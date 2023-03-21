public class Passenger {
    private  String firstName;
    private String secondName;
    private double expenses;

    public Passenger(String firstName, String secondName, double expenses) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.expenses = expenses;
    }



    public  String getFirstName() {

        return firstName;
    }


    public String getSecondName() {

        return secondName;
    }


    public double getExpenses() {

        return expenses;
    }

}
