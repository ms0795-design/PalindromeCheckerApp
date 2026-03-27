import java.io.*;
import java.util.*;

// Reservation class
class Reservation
        class Reservation implements Serializable
        {
            private static final long serialVersionUID = 1L;

            private String reservationId;
            private String guestName;
            private String roomType;
            private double bookingAmount;

            public Reservation(String reservationId,String guestName,String roomType)
            public Reservation(String reservationId,String guestName,String roomType,double bookingAmount)
            {
                this.reservationId=reservationId;
                this.guestName=guestName;
                this.roomType=roomType;
                this.bookingAmount=bookingAmount;
            }

            public String getReservationId()
            @@ -29,170 +34,176 @@ public String getRoomType()
        return roomType;
        }

        public double getBookingAmount()
        {
            return bookingAmount;
        }

        public String toString()
        {
            return "Reservation ID: "+reservationId+
                    ", Guest Name: "+guestName+
                    ", Room Type: "+roomType;
            ", Room Type: "+roomType+
                    ", Booking Amount: Rs. "+bookingAmount;
        }
}

        // Shared booking queue
        class BookingQueue
        // Booking History class
        class BookingHistory implements Serializable
        {
            private Queue<Reservation> queue=new LinkedList<>();
            private static final long serialVersionUID = 1L;

            private List<Reservation> confirmedBookings;

            public synchronized void addRequest(Reservation reservation)
            public BookingHistory()
            {
                queue.offer(reservation);
                System.out.println(Thread.currentThread().getName()+" added request: "+reservation.getReservationId());
                confirmedBookings=new ArrayList<>();
            }

            public synchronized Reservation getNextRequest()
            public void addBooking(Reservation reservation)
            {
                return queue.poll();
                confirmedBookings.add(reservation);
            }

            public synchronized boolean isEmpty()
            public List<Reservation> getBookings()
            {
                return queue.isEmpty();
                return confirmedBookings;
            }

            public void displayBookingHistory()
            {
                if(confirmedBookings.isEmpty())
                {
                    System.out.println("No booking history found.");
                    return;
                }

                System.out.println("\nBooking History:");
                for(Reservation reservation:confirmedBookings)
                {
                    System.out.println(reservation);
                }
            }
        }

        // Shared inventory service
        class InventoryService
        // Inventory Service class
        class InventoryService implements Serializable
        {
            private Map<String,Integer> inventory=new HashMap<>();
            private Map<String,Integer> roomCounter=new HashMap<>();
            private Set<String> allocatedRoomIds=new HashSet<>();
            private static final long serialVersionUID = 1L;

            private Map<String,Integer> inventory;

            public InventoryService()
            {
                inventory=new HashMap<>();
                inventory.put("Standard",2);
                inventory.put("Deluxe",2);
                inventory.put("Deluxe",1);
                inventory.put("Suite",1);

                roomCounter.put("Standard",100);
                roomCounter.put("Deluxe",200);
                roomCounter.put("Suite",300);
            }

            public synchronized String allocateRoom(String roomType) throws Exception
            public void updateInventory(String roomType,int count)
            {
                if(!inventory.containsKey(roomType))
                {
                    throw new Exception("Invalid room type: "+roomType);
                }

                int available=inventory.get(roomType);

                if(available<=0)
                {
                    throw new Exception("No rooms available for "+roomType);
                }
                inventory.put(roomType,count);
            }

            int nextNumber=roomCounter.get(roomType)+1;
        roomCounter.put(roomType,nextNumber);
            public Map<String,Integer> getInventory()
            {
                return inventory;
            }

            String prefix="";
        if(roomType.equals("Standard"))
            {
                prefix="ST";
            }
        else if(roomType.equals("Deluxe"))
            {
                prefix="D";
            }
        else if(roomType.equals("Suite"))
            public void displayInventory()
            {
                System.out.println("\nInventory Status:");
                for(String roomType:inventory.keySet())
                {
                    prefix="S";
                    System.out.println(roomType+" : "+inventory.get(roomType));
                }
            }
        }

        String roomId=prefix+nextNumber;

        while(allocatedRoomIds.contains(roomId))
        {
        nextNumber++;
        roomCounter.put(roomType,nextNumber);
        roomId=prefix+nextNumber;
        }
        // Wrapper class to persist full system state
        class SystemState implements Serializable
        {
            private static final long serialVersionUID = 1L;

        allocatedRoomIds.add(roomId);
        inventory.put(roomType,available-1);
            private BookingHistory bookingHistory;
            private InventoryService inventoryService;

        return roomId;
            public SystemState(BookingHistory bookingHistory,InventoryService inventoryService)
            {
                this.bookingHistory=bookingHistory;
                this.inventoryService=inventoryService;
            }

            public synchronized void displayInventory()
            public BookingHistory getBookingHistory()
            {
                System.out.println("\nFinal Inventory Status:");
                for(String roomType:inventory.keySet())
                {
                    System.out.println(roomType+" : "+inventory.get(roomType));
                }
                return bookingHistory;
            }

            public synchronized void displayAllocatedRooms()
            public InventoryService getInventoryService()
            {
                System.out.println("\nAllocated Room IDs:");
                for(String roomId:allocatedRoomIds)
                {
                    System.out.println(roomId);
                }
                return inventoryService;
            }
        }

        // Concurrent booking processor
        class ConcurrentBookingProcessor extends Thread
        // Persistence Service class
        class PersistenceService
        {
            private BookingQueue bookingQueue;
            private InventoryService inventoryService;
            private String fileName;

            public ConcurrentBookingProcessor(String threadName,BookingQueue bookingQueue,InventoryService inventoryService)
            public PersistenceService(String fileName)
            {
                super(threadName);
                this.bookingQueue=bookingQueue;
                this.inventoryService=inventoryService;
                this.fileName=fileName;
            }

            public void run()
            public void saveState(SystemState state)
            {
                while(true)
                    try
                    {
                        FileOutputStream fos=new FileOutputStream(fileName);
                        ObjectOutputStream oos=new ObjectOutputStream(fos);

                        oos.writeObject(state);
                        oos.close();
                        fos.close();

                        System.out.println("\nSystem state saved successfully.");
                    }
                    catch(IOException e)
                    {
                        Reservation reservation;

                        synchronized(bookingQueue)
                        {
                            if(bookingQueue.isEmpty())
                            {
                                break;
                            }
                            reservation=bookingQueue.getNextRequest();
                        }

                        if(reservation!=null)
                        {
                            processBooking(reservation);
                        }
                        System.out.println("\nError while saving system state: "+e.getMessage());
                    }
            }

            private void processBooking(Reservation reservation)
            public SystemState loadState()
            {
                try
                {
                    String roomId=inventoryService.allocateRoom(reservation.getRoomType());
                    FileInputStream fis=new FileInputStream(fileName);
                    ObjectInputStream ois=new ObjectInputStream(fis);

                    SystemState state=(SystemState)ois.readObject();

                    ois.close();
                    fis.close();

                    System.out.println(getName()+" processed "+reservation.getReservationId()+
                            " for "+reservation.getGuestName()+
                            " -> Allocated Room ID: "+roomId);
                    System.out.println("\nSystem state loaded successfully.");
                    return state;
                }
                catch(FileNotFoundException e)
                {
                    System.out.println("\nPersistence file not found. Starting with a fresh system state.");
                }
                catch(Exception e)
        catch(IOException e)
                {
                    System.out.println(getName()+" failed to process "+reservation.getReservationId()+
                            " for "+reservation.getGuestName()+
                            " -> "+e.getMessage());
                    System.out.println("\nPersistence file is corrupted or unreadable. Starting with a fresh system state.");
                }
        catch(ClassNotFoundException e)
                {
                    System.out.println("\nSaved data format is invalid. Starting with a fresh system state.");
                }

                return new SystemState(new BookingHistory(),new InventoryService());
            }
        }

        @@ -201,36 +212,34 @@ public class BookMyStayApp
{
    public static void main(String[] args)
    {
        BookingQueue bookingQueue=new BookingQueue();
        InventoryService inventoryService=new InventoryService();
        PersistenceService persistenceService=new PersistenceService("bookmystay.dat");

        bookingQueue.addRequest(new Reservation("R101","Karthik","Deluxe"));
        bookingQueue.addRequest(new Reservation("R102","Priya","Suite"));
        bookingQueue.addRequest(new Reservation("R103","Rahul","Standard"));
        bookingQueue.addRequest(new Reservation("R104","Ananya","Deluxe"));
        bookingQueue.addRequest(new Reservation("R105","Meena","Standard"));
        bookingQueue.addRequest(new Reservation("R106","Arjun","Suite"));
        // System startup recovery
        SystemState recoveredState=persistenceService.loadState();

        ConcurrentBookingProcessor t1=new ConcurrentBookingProcessor("Thread-1",bookingQueue,inventoryService);
        ConcurrentBookingProcessor t2=new ConcurrentBookingProcessor("Thread-2",bookingQueue,inventoryService);
        ConcurrentBookingProcessor t3=new ConcurrentBookingProcessor("Thread-3",bookingQueue,inventoryService);
        BookingHistory bookingHistory=recoveredState.getBookingHistory();
        InventoryService inventoryService=recoveredState.getInventoryService();

        t1.start();
        t2.start();
        t3.start();
        System.out.println("\nRecovered Data:");
        bookingHistory.displayBookingHistory();
        inventoryService.displayInventory();

        try
        {
            t1.join();
            t2.join();
            t3.join();
        }
        catch(InterruptedException e)
        {
            System.out.println("Main thread interrupted");
        }
        // Simulate new activity in the system
        Reservation r1=new Reservation("R101","Karthik","Deluxe",3500);
        Reservation r2=new Reservation("R102","Priya","Suite",5000);

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);

        inventoryService.updateInventory("Deluxe",0);
        inventoryService.updateInventory("Suite",0);

        System.out.println("\nUpdated Data Before Shutdown:");
        bookingHistory.displayBookingHistory();
        inventoryService.displayInventory();
        inventoryService.displayAllocatedRooms();

        // System shutdown save
        SystemState currentState=new SystemState(bookingHistory,inventoryService);
        persistenceService.saveState(currentState);
    }
}
}
    }
}
@@ -123,21 +162,21 @@ public class BookMyStayApp
{
    public static void main(String[] args)
    {
        BookingHistory history=new BookingHistory();
        BookingReportService reportService=new BookingReportService();

        Reservation r1=new Reservation("R101","Karthik","Deluxe",3500);
        Reservation r2=new Reservation("R102","Priya","Suite",5000);
        Reservation r3=new Reservation("R103","Rahul","Standard",2500);
        Reservation r4=new Reservation("R104","Ananya","Deluxe",3500);

        history.addConfirmedBooking(r1);
        history.addConfirmedBooking(r2);
        history.addConfirmedBooking(r3);
        history.addConfirmedBooking(r4);

        history.displayBookingHistory();

        reportService.generateSummaryReport(history.getConfirmedBookings());
        InventoryService inventoryService=new InventoryService();
        BookingService bookingService=new BookingService(inventoryService);

        Reservation r1=new Reservation("R101","Karthik","Deluxe");
        Reservation r2=new Reservation("R102","Priya","Suite");
        Reservation r3=new Reservation("R103","Rahul","Premium");
        Reservation r4=new Reservation("R104","","Standard");
        Reservation r5=new Reservation("R105","Ananya","Deluxe");

        bookingService.confirmBooking(r1);
        bookingService.confirmBooking(r2);
        bookingService.confirmBooking(r3);
        bookingService.confirmBooking(r4);
        bookingService.confirmBooking(r5);

        inventoryService.displayInventory();
    }
}
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// Reservation class
class Reservation
{
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName,String roomType,int nights)
    {
        this.guestName=guestName;
        this.roomType=roomType;
        this.nights=nights;
    }

    class Room {
        public String getGuestName()
        {
            return guestName;
        }

        String roomType;
        double price;
        public String getRoomType()
        {
            return roomType;
        }

        public Room(String roomType, double price) {
            this.roomType = roomType;
            this.price = price;
            public int getNights()
            {
                return nights;
            }

            public void displayRoomDetails() {
                System.out.println("Room Type : " + roomType);
                System.out.println("Price     : $" + price);
                public String toString()
                {
                    return "Guest:"+guestName+
                            " Room:"+roomType+
                            " Nights:"+nights;
                }
            }

            class RoomInventory {

                private HashMap<String, Integer> inventory;
                // Booking Queue class
                class BookingRequestQueue
                {
                    private Queue<Reservation> queue;

                    public RoomInventory() {
                        inventory = new HashMap<>();
    public BookingRequestQueue()
                        {
                            queue=new LinkedList<>();
                        }

                        inventory.put("Single Room", 5);
                        inventory.put("Double Room", 3);
                        inventory.put("Suite Room", 0);
                        // Add booking request
                        public void addRequest(Reservation r)
                        {
                            queue.offer(r);
                            System.out.println("Request added for "+r.getGuestName());
                        }

                        public int getAvailability(String roomType) {
                            return inventory.getOrDefault(roomType, 0);
                            // Display queue
                            public void displayRequests()
                            {
                                if(queue.isEmpty())
                                {
                                    System.out.println("No requests");
                                    return;
                                }

                                System.out.println("\nBooking Queue:");
                                for(Reservation r:queue)
                                {
                                    System.out.println(r);
                                }
                            }

                            public HashMap<String, Integer> getInventory() {
                                return inventory;
                                // Show next request
                                public void nextRequest()
                                {
                                    if(queue.isEmpty())
                                    {
                                        System.out.println("No pending request");
                                    }
                                    else
                                    {
                                        System.out.println("\nNext Request:");
                                        System.out.println(queue.peek());
                                    }
                                }
                            }

                            public class BookMyStayApp {

                                public static void main(String[] args) {

                                    System.out.println("=================================================");
                                    System.out.println("           Book My Stay Application              ");
                                    System.out.println("=================================================");
                                    System.out.println("Hotel Booking Management System");
                                    System.out.println("Application Version : 4.0");
                                    System.out.println("=================================================");
// Main class (IMPORTANT NAME)
                                    public class BookMyStayApp
                                    {
                                        public static void main(String args[])
                                        {
                                            BookingRequestQueue bq=new BookingRequestQueue();

                                            RoomInventory inventory = new RoomInventory();
                                            Reservation r1=new Reservation("Karthik","Deluxe",2);
                                            Reservation r2=new Reservation("Priya","Suite",3);
                                            Reservation r3=new Reservation("Rahul","Standard",1);

                                            Room single = new Room("Single Room", 100);
                                            Room doubleRoom = new Room("Double Room", 180);
                                            Room suite = new Room("Suite Room", 350);
                                            bq.addRequest(r1);
                                            bq.addRequest(r2);
                                            bq.addRequest(r3);

                                            System.out.println("\nSearching Available Rooms...\n");

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

                                            if (inventory.getAvailability("Suite Room") > 0) {
                                                suite.displayRoomDetails();
                                                System.out.println("Available Rooms : " + inventory.getAvailability("Suite Room"));
                                                System.out.println();
                                            }
                                            bq.displayRequests();

                                            System.out.println("Search completed. System state unchanged.");
                                            bq.nextRequest();
                                        }
                                    }
