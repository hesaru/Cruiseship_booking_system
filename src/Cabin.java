import java.util.Objects;

public class Cabin {
    private final Passenger[] beds = new Passenger[3];


    public  Passenger[] getBeds() {

        return this.beds;
    }

    public void setPassengerByBedNo(Passenger passenger, int bedNo) {

        this.beds[bedNo] = passenger;
    }


    public boolean isPassengerAvailable(String name) {
        String[] splitted = name.split("\\s+");
        for (Passenger passenger : beds) {
            if(passenger != null) {
                if (Objects.equals(passenger.getFirstName(), splitted[0]) && (Objects.equals(passenger.getSecondName(), splitted[1]))) {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean isEmpty() {
        for (Passenger passenger : beds) {
            if (passenger == null) {
                return true;
            }
        }
        return false;
    }

}


