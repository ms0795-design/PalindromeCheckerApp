import java.util.HashMap;

class Room {

    String roomType;
    double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Price     : $" + price);
    }
}

class RoomInventory {

    private HashMap<String, Integer> roomInventory;
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        roomInventory = new HashMap<>();
        inventory = new HashMap<>();

        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0);
    }

    public int getAvailability(String roomType) {
        return roomInventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        roomInventory.put(roomType, count);
        return inventory.getOrDefault(roomType, 0);
    }


    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (String room : roomInventory.keySet()) {
            System.out.println(room + " Available : " + roomInventory.get(room));
        }
        public HashMap<String, Integer> getInventory() {
            return inventory;
        }
    }


    public class BookMyStayApp {

        public static void main(String[] args) {
            @@ -40,22 +47,35 @@ public static void main(String[] args) {
                System.out.println("           Book My Stay Application              ");
                System.out.println("=================================================");
                System.out.println("Hotel Booking Management System");
                System.out.println("Application Version : 3.1");
                System.out.println("Application Version : 4.0");
                System.out.println("=================================================");

                RoomInventory inventory = new RoomInventory();

                inventory.displayInventory();
                Room single = new Room("Single Room", 100);
                Room doubleRoom = new Room("Double Room", 180);
                Room suite = new Room("Suite Room", 350);

                System.out.println("\nChecking availability of Single Room...");
                System.out.println("Available : " + inventory.getAvailability("Single Room"));
                System.out.println("\nSearching Available Rooms...\n");

                System.out.println("\nUpdating Suite Room availability...");
                inventory.updateAvailability("Suite Room", 4);
                if (inventory.getAvailability("Single Room") > 0) {
                    single.displayRoomDetails();
                    System.out.println("Available Rooms : " + inventory.getAvailability("Single Room"));
                    System.out.println();
                }

                if (inventory.getAvailability("Double Room") > 0) {
                    doubleRoom.displayRoomDetails();
                    System.out.println("Available Rooms : " + inventory.getAvailability("Double Room"));
                    System.out.println();
                }

                System.out.println("\nUpdated Inventory:");
                inventory.displayInventory();
                if (inventory.getAvailability("Suite Room") > 0) {
                    suite.displayRoomDetails();
                    System.out.println("Available Rooms : " + inventory.getAvailability("Suite Room"));
                    System.out.println();
                }

                System.out.println("\nApplication terminated.");
                System.out.println("Search completed. System state unchanged.");
            }
        }