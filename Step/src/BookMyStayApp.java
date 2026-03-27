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
import java.util.HashMap;

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq.ft");
        System.out.println("Price     : $" + price);
    }
}
class RoomInventory {

    private HashMap<String, Integer> roomInventory;

    class SingleRoom extends Room {
        public RoomInventory() {
            roomInventory = new HashMap<>();

    public SingleRoom() {
                super("Single Room", 1, 200, 100);
                roomInventory.put("Single Room", 5);
                roomInventory.put("Double Room", 3);
                roomInventory.put("Suite Room", 2);
            }
        }

        class DoubleRoom extends Room {

            public DoubleRoom() {
                super("Double Room", 2, 350, 180);
                public int getAvailability(String roomType) {
                    return roomInventory.getOrDefault(roomType, 0);
                }
            }

            public void updateAvailability(String roomType, int count) {
                roomInventory.put(roomType, count);
            }

            class SuiteRoom extends Room {

                public SuiteRoom() {
                    super("Suite Room", 3, 600, 350);
                    public void displayInventory() {
                        System.out.println("\nCurrent Room Inventory:");
                        for (String room : roomInventory.keySet()) {
                            System.out.println(room + " Available : " + roomInventory.get(room));
                        }
                    }
                }


                public class BookMyStayApp {

                    public static void main(String[] args) {

                        System.out.println("=====================================");
                        System.out.println("        Book My Stay Application     ");
                        System.out.println("=====================================");
                        System.out.println("=================================================");
                        System.out.println("           Book My Stay Application              ");
                        System.out.println("=================================================");
                        System.out.println("Hotel Booking Management System");
                        System.out.println("Version : 2.1");
                        System.out.println("-------------------------------------");
                        System.out.println("Application Version : 3.1");
                        System.out.println("=================================================");

                        Room single = new SingleRoom();
                        Room doubleRoom = new DoubleRoom();
                        Room suite = new SuiteRoom();
                        RoomInventory inventory = new RoomInventory();

                        int singleAvailable = 5;
                        int doubleAvailable = 3;
                        int suiteAvailable = 2;
                        inventory.displayInventory();

                        System.out.println("\nSingle Room Details");
                        single.displayRoomDetails();
                        System.out.println("Available Rooms : " + singleAvailable);
                        System.out.println("\nChecking availability of Single Room...");
                        System.out.println("Available : " + inventory.getAvailability("Single Room"));

                        System.out.println("\nDouble Room Details");
                        doubleRoom.displayRoomDetails();
                        System.out.println("Available Rooms : " + doubleAvailable);
                        System.out.println("\nUpdating Suite Room availability...");
                        inventory.updateAvailability("Suite Room", 4);

                        System.out.println("\nSuite Room Details");
                        suite.displayRoomDetails();
                        System.out.println("Available Rooms : " + suiteAvailable);
                        System.out.println("\nUpdated Inventory:");
                        inventory.displayInventory();

                        System.out.println("\nApplication Terminated.");
                        System.out.println("\nApplication terminated.");
                    }
                }