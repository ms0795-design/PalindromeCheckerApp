private String reservationId;
private String guestName;
private String roomType;
private String roomId;
private boolean cancelled;

public Reservation(String reservationId,String guestName,String roomType,String roomId)
public Reservation(String reservationId,String guestName,String roomType)
{
    this.reservationId=reservationId;
    this.guestName=guestName;
    this.roomType=roomType;
    this.roomId=roomId;
    this.cancelled=false;
}

public String getReservationId()
@@ -33,151 +29,169 @@ public String getRoomType()
        return roomType;
    }

public String getRoomId()
public String toString()
{
    return roomId;
    return "Reservation ID: "+reservationId+
            ", Guest Name: "+guestName+
            ", Room Type: "+roomType;
}
}

// Shared booking queue
class BookingQueue
{
    private Queue<Reservation> queue=new LinkedList<>();

    public boolean isCancelled()
    public synchronized void addRequest(Reservation reservation)
    {
        return cancelled;
        queue.offer(reservation);
        System.out.println(Thread.currentThread().getName()+" added request: "+reservation.getReservationId());
    }

    public void cancel()
    public synchronized Reservation getNextRequest()
    {
        cancelled=true;
        return queue.poll();
    }

    public String toString()
    public synchronized boolean isEmpty()
    {
        return "Reservation ID: "+reservationId+
                ", Guest Name: "+guestName+
                ", Room Type: "+roomType+
                ", Room ID: "+roomId+
                ", Status: "+(cancelled ? "Cancelled" : "Confirmed");
        return queue.isEmpty();
    }
}

// Inventory Service
// Shared inventory service
class InventoryService
{
    private Map<String,Integer> inventory;
    private Map<String,Integer> inventory=new HashMap<>();
    private Map<String,Integer> roomCounter=new HashMap<>();
    private Set<String> allocatedRoomIds=new HashSet<>();

    public InventoryService()
    {
        inventory=new HashMap<>();
        inventory.put("Standard",2);
        inventory.put("Deluxe",1);
        inventory.put("Deluxe",2);
        inventory.put("Suite",1);
    }

    public void incrementInventory(String roomType)
    {
        inventory.put(roomType,inventory.getOrDefault(roomType,0)+1);
        roomCounter.put("Standard",100);
        roomCounter.put("Deluxe",200);
        roomCounter.put("Suite",300);
    }

    public void displayInventory()
    public synchronized String allocateRoom(String roomType) throws Exception
    {
        System.out.println("\nCurrent Inventory Status:");
        for(String roomType:inventory.keySet())
            if(!inventory.containsKey(roomType))
            {
                System.out.println(roomType+" : "+inventory.get(roomType));
                throw new Exception("Invalid room type: "+roomType);
            }
    }
}

// Booking History
class BookingHistory
{
    private List<Reservation> confirmedBookings;
    int available=inventory.get(roomType);

    public BookingHistory()
    {
        confirmedBookings=new ArrayList<>();
    }
        if(available<=0)
    {
        throw new Exception("No rooms available for "+roomType);
    }

    public void addBooking(Reservation reservation)
    {
        confirmedBookings.add(reservation);
        int nextNumber=roomCounter.get(roomType)+1;
        roomCounter.put(roomType,nextNumber);

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
        {
            prefix="S";
        }

        String roomId=prefix+nextNumber;

        while(allocatedRoomIds.contains(roomId))
        {
            nextNumber++;
            roomCounter.put(roomType,nextNumber);
            roomId=prefix+nextNumber;
        }

        allocatedRoomIds.add(roomId);
        inventory.put(roomType,available-1);

        return roomId;
    }

    public Reservation findReservation(String reservationId)
    public synchronized void displayInventory()
    {
        for(Reservation reservation:confirmedBookings)
            System.out.println("\nFinal Inventory Status:");
        for(String roomType:inventory.keySet())
        {
            if(reservation.getReservationId().equals(reservationId))
            {
                return reservation;
            }
            System.out.println(roomType+" : "+inventory.get(roomType));
        }
        return null;
    }

    public void displayBookingHistory()
    public synchronized void displayAllocatedRooms()
    {
        System.out.println("\nBooking History:");
        for(Reservation reservation:confirmedBookings)
            System.out.println("\nAllocated Room IDs:");
        for(String roomId:allocatedRoomIds)
        {
            System.out.println(reservation);
            System.out.println(roomId);
        }
    }
}

// Cancellation Service
class CancellationService
// Concurrent booking processor
class ConcurrentBookingProcessor extends Thread
{
    private BookingQueue bookingQueue;
    private InventoryService inventoryService;
    private BookingHistory bookingHistory;
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventoryService,BookingHistory bookingHistory)
    public ConcurrentBookingProcessor(String threadName,BookingQueue bookingQueue,InventoryService inventoryService)
    {
        super(threadName);
        this.bookingQueue=bookingQueue;
        this.inventoryService=inventoryService;
        this.bookingHistory=bookingHistory;
        rollbackStack=new Stack<>();
    }

    public void cancelBooking(String reservationId)
    public void run()
    {
        Reservation reservation=bookingHistory.findReservation(reservationId);

        if(reservation==null)
            while(true)
            {
                System.out.println("\nCancellation Failed: Reservation not found for ID "+reservationId);
                return;
            }

        if(reservation.isCancelled())
        {
            System.out.println("\nCancellation Failed: Reservation already cancelled for ID "+reservationId);
            return;
        }
        Reservation reservation;

        rollbackStack.push(reservation.getRoomId());

        inventoryService.incrementInventory(reservation.getRoomType());

        reservation.cancel();
        synchronized(bookingQueue)
        {
            if(bookingQueue.isEmpty())
            {
                break;
            }
            reservation=bookingQueue.getNextRequest();
        }

        System.out.println("\nCancellation Successful");
        System.out.println("Reservation ID: "+reservation.getReservationId());
        System.out.println("Released Room ID: "+rollbackStack.peek());
        System.out.println("Inventory restored for room type: "+reservation.getRoomType());
        if(reservation!=null)
        {
            processBooking(reservation);
        }
    }
}

public void displayRollbackStack()
private void processBooking(Reservation reservation)
{
    System.out.println("\nRollback Stack:");
    if(rollbackStack.isEmpty())
        try
        {
            System.out.println("No released room IDs.");
            String roomId=inventoryService.allocateRoom(reservation.getRoomType());

            System.out.println(getName()+" processed "+reservation.getReservationId()+
                    " for "+reservation.getGuestName()+
                    " -> Allocated Room ID: "+roomId);
        }
    else
        catch(Exception e)
    {
        for(String roomId:rollbackStack)
        {
            System.out.println(roomId);
        }
        System.out.println(getName()+" failed to process "+reservation.getReservationId()+
                " for "+reservation.getGuestName()+
                " -> "+e.getMessage());
    }
}
}
@@ -187,27 +201,36 @@ public class BookMyStayApp
{
    public static void main(String[] args)
    {
        BookingQueue bookingQueue=new BookingQueue();
        InventoryService inventoryService=new InventoryService();
        BookingHistory bookingHistory=new BookingHistory();

        Reservation r1=new Reservation("R101","Karthik","Deluxe","D201");
        Reservation r2=new Reservation("R102","Priya","Suite","S301");
        Reservation r3=new Reservation("R103","Rahul","Standard","ST101");
        bookingQueue.addRequest(new Reservation("R101","Karthik","Deluxe"));
        bookingQueue.addRequest(new Reservation("R102","Priya","Suite"));
        bookingQueue.addRequest(new Reservation("R103","Rahul","Standard"));
        bookingQueue.addRequest(new Reservation("R104","Ananya","Deluxe"));
        bookingQueue.addRequest(new Reservation("R105","Meena","Standard"));
        bookingQueue.addRequest(new Reservation("R106","Arjun","Suite"));

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);
        bookingHistory.addBooking(r3);
        ConcurrentBookingProcessor t1=new ConcurrentBookingProcessor("Thread-1",bookingQueue,inventoryService);
        ConcurrentBookingProcessor t2=new ConcurrentBookingProcessor("Thread-2",bookingQueue,inventoryService);
        ConcurrentBookingProcessor t3=new ConcurrentBookingProcessor("Thread-3",bookingQueue,inventoryService);

        CancellationService cancellationService=new CancellationService(inventoryService,bookingHistory);
        t1.start();
        t2.start();
        t3.start();

        bookingHistory.displayBookingHistory();

        cancellationService.cancelBooking("R102");
        cancellationService.cancelBooking("R105");
        cancellationService.cancelBooking("R102");
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

        bookingHistory.displayBookingHistory();
        inventoryService.displayInventory();
        cancellationService.displayRollbackStack();
        inventoryService.displayAllocatedRooms();
    }
}