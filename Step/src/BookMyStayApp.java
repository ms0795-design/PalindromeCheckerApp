import java.util.LinkedList;
import java.util.Queue;
import java.util.*;

// Reservation class
class Reservation
{
    private String guestName;
    private String roomType;
    private int nights;
    String guestName;
    String roomType;

    public Reservation(String guestName,String roomType,int nights)
    public Reservation(String guestName,String roomType)
    {
        this.guestName=guestName;
        this.roomType=roomType;
        this.nights=nights;
    }

    public String getGuestName()
    public String toString()
    {
        return guestName;
        return guestName+" requested "+roomType;
    }
}

// Inventory Service
class InventoryService
{
    HashMap<String,Integer> inventory=new HashMap<>();

    public String getRoomType()
    public InventoryService()
    {
        return roomType;
        inventory.put("Deluxe",3);
        inventory.put("Suite",2);
        inventory.put("Standard",4);
    }

    public int getNights()
    public boolean checkAvailability(String roomType)
    {
        return nights;
        return inventory.getOrDefault(roomType,0)>0;
    }

    public String toString()
    public void reduceInventory(String roomType)
    {
        return "Guest:"+guestName+
                " Room:"+roomType+
                " Nights:"+nights;
        inventory.put(roomType,inventory.get(roomType)-1);
    }

    public void showInventory()
    {
        System.out.println("\nCurrent Inventory:");
        for(String type:inventory.keySet())
        {
            System.out.println(type+" Rooms left: "+inventory.get(type));
        }
    }
}

// Booking Queue class
class BookingRequestQueue
// Booking Service
class BookingService
{
    private Queue<Reservation> queue;
    Queue<Reservation> requestQueue=new LinkedList<>();

    HashSet<String> allocatedRooms=new HashSet<>();

    HashMap<String,Set<String>> roomMap=new HashMap<>();

    public BookingRequestQueue()
    InventoryService inventory;

    int roomCounter=101;

    public BookingService(InventoryService inventory)
    {
        queue=new LinkedList<>();
        this.inventory=inventory;
    }

    // Add booking request
    // Add request
    public void addRequest(Reservation r)
    {
        queue.offer(r);
        System.out.println("Request added for "+r.getGuestName());
        requestQueue.offer(r);
        System.out.println("Request added: "+r);
    }

    // Display queue
    public void displayRequests()
    // Process booking
    public void processRequest()
    {
        if(queue.isEmpty())
            if(requestQueue.isEmpty())
            {
                System.out.println("No requests");
                return;
            }

        System.out.println("\nBooking Queue:");
        for(Reservation r:queue)
            Reservation r=requestQueue.poll();

        System.out.println("\nProcessing: "+r);

        if(inventory.checkAvailability(r.roomType))
        {
            String roomID="R"+roomCounter++;

            while(allocatedRooms.contains(roomID))
            {
                roomID="R"+roomCounter++;
            }

            allocatedRooms.add(roomID);

            roomMap.putIfAbsent(r.roomType,new HashSet<>());
            roomMap.get(r.roomType).add(roomID);

            inventory.reduceInventory(r.roomType);

            System.out.println("Booking Confirmed");
            System.out.println("Guest: "+r.guestName);
            System.out.println("Room Type: "+r.roomType);
            System.out.println("Allocated Room ID: "+roomID);
        }
        else
        {
            System.out.println(r);
            System.out.println("Booking Failed - No rooms available");
        }
    }

    // Show next request
    public void nextRequest()
    public void showAllocations()
    {
        if(queue.isEmpty())
        {
            System.out.println("No pending request");
        }
        else
            System.out.println("\nRoom Allocations:");

        for(String type:roomMap.keySet())
        {
            System.out.println("\nNext Request:");
            System.out.println(queue.peek());
            System.out.println(type+" -> "+roomMap.get(type));
        }
    }
}

// Main class (IMPORTANT NAME)
// MAIN CLASS (DO NOT CHANGE)
public class BookMyStayApp
{
    public static void main(String args[])
    {
        BookingRequestQueue bq=new BookingRequestQueue();
        InventoryService inventory=new InventoryService();

        BookingService booking=new BookingService(inventory);

        Reservation r1=new Reservation("Karthik","Deluxe",2);
        Reservation r2=new Reservation("Priya","Suite",3);
        Reservation r3=new Reservation("Rahul","Standard",1);
        booking.addRequest(new Reservation("Karthik","Deluxe"));
        booking.addRequest(new Reservation("Priya","Suite"));
        booking.addRequest(new Reservation("Rahul","Deluxe"));
        booking.addRequest(new Reservation("Ananya","Standard"));

        bq.addRequest(r1);
        bq.addRequest(r2);
        bq.addRequest(r3);
        booking.processRequest();
        booking.processRequest();
        booking.processRequest();
        booking.processRequest();

        bq.displayRequests();
        booking.showAllocations();

        bq.nextRequest();
        inventory.showInventory();
    }
}