package BookMyStayApp;

abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq.ft");
        System.out.println("Price     : $" + price);
    }
}


class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}


class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 350);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("        Book My Stay Application     ");
        System.out.println("=====================================");
        System.out.println("Hotel Booking Management System");
        System.out.println("Version : 2.1");
        System.out.println("-------------------------------------");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("\nSingle Room Details");
        single.displayRoomDetails();
        System.out.println("Available Rooms : " + singleAvailable);

        System.out.println("\nDouble Room Details");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms : " + doubleAvailable);

        System.out.println("\nSuite Room Details");
        suite.displayRoomDetails();
        System.out.println("Available Rooms : " + suiteAvailable);

        System.out.println("\nApplication Terminated.");
    }
}