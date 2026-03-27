class InvalidBookingException extends Exception
{
    public InvalidBookingException(String message)
    {
        super(message);
    }
}

// Reservation class
class Reservation
{
    private String reservationId;
    private String guestName;
    private String roomType;
    private double bookingAmount;

    public Reservation(String reservationId,String guestName,String roomType,double bookingAmount)
    public Reservation(String reservationId,String guestName,String roomType)
    {
        this.reservationId=reservationId;
        this.guestName=guestName;
        this.roomType=roomType;
        this.bookingAmount=bookingAmount;
    }

    public String getReservationId()
    @@ -31,89 +38,121 @@ public String getRoomType()
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
            ", Room Type: "+roomType+
            ", Booking Amount: Rs. "+bookingAmount;
    ", Room Type: "+roomType;
}
}

// Booking History class
class BookingHistory
// Inventory Service
class InventoryService
{
    private List<Reservation> confirmedBookings;
    private Map<String,Integer> inventory;

    public BookingHistory()
    public InventoryService()
    {
        confirmedBookings=new ArrayList<>();
        inventory=new HashMap<>();
        inventory.put("Standard",2);
        inventory.put("Deluxe",1);
        inventory.put("Suite",1);
    }

    public void addConfirmedBooking(Reservation reservation)
    public boolean isValidRoomType(String roomType)
    {
        confirmedBookings.add(reservation);
        System.out.println("Booking added to history: "+reservation.getReservationId());
        return inventory.containsKey(roomType);
    }

    public List<Reservation> getConfirmedBookings()
    public boolean isAvailable(String roomType)
    {
        return confirmedBookings;
        return inventory.getOrDefault(roomType,0)>0;
    }

    public void displayBookingHistory()
    public void allocateRoom(String roomType) throws InvalidBookingException
    {
        if(confirmedBookings.isEmpty())
            if(!isValidRoomType(roomType))
            {
                System.out.println("No confirmed bookings found.");
                return;
                throw new InvalidBookingException("Invalid room type: "+roomType);
            }

        System.out.println("\nBooking History:");
        for(Reservation reservation:confirmedBookings)
            if(!isAvailable(roomType))
            {
                System.out.println(reservation);
                throw new InvalidBookingException("No rooms available for room type: "+roomType);
            }

        int currentCount=inventory.get(roomType);

        if(currentCount<=0)
        {
            throw new InvalidBookingException("Inventory cannot become negative for room type: "+roomType);
        }

        inventory.put(roomType,currentCount-1);
    }

    public void displayInventory()
    {
        System.out.println("\nCurrent Inventory Status:");
        for(String roomType:inventory.keySet())
        {
            System.out.println(roomType+" : "+inventory.get(roomType));
        }
    }
}

// Booking Report Service class
class BookingReportService
// Validator class
class InvalidBookingValidator
{
    public void generateSummaryReport(List<Reservation> bookings)
    public void validateReservation(Reservation reservation,InventoryService inventoryService) throws InvalidBookingException
    {
        if(bookings.isEmpty())
            if(reservation.getGuestName()==null || reservation.getGuestName().trim().isEmpty())
            {
                System.out.println("No data available for report.");
                return;
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

        int totalBookings=bookings.size();
        double totalRevenue=0;

        Map<String,Integer> roomTypeCount=new HashMap<>();
        if(reservation.getReservationId()==null || reservation.getReservationId().trim().isEmpty())
        {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        for(Reservation reservation:bookings)
            if(reservation.getRoomType()==null || reservation.getRoomType().trim().isEmpty())
            {
                totalRevenue=totalRevenue+reservation.getBookingAmount();
                throw new InvalidBookingException("Room type cannot be empty.");
            }

        String roomType=reservation.getRoomType();
        roomTypeCount.put(roomType,roomTypeCount.getOrDefault(roomType,0)+1);
        if(!inventoryService.isValidRoomType(reservation.getRoomType()))
        {
            throw new InvalidBookingException("Invalid room type entered: "+reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService
{
    private InventoryService inventoryService;
    private InvalidBookingValidator validator;

        System.out.println("\nBooking Summary Report");
        System.out.println("Total Confirmed Bookings: "+totalBookings);
        System.out.println("Total Revenue: Rs. "+totalRevenue);
    public BookingService(InventoryService inventoryService)
    {
        this.inventoryService=inventoryService;
        this.validator=new InvalidBookingValidator();
    }

        System.out.println("\nBookings by Room Type:");
        for(String roomType:roomTypeCount.keySet())
    public void confirmBooking(Reservation reservation)
    {
        try
        {
            validator.validateReservation(reservation,inventoryService);
            inventoryService.allocateRoom(reservation.getRoomType());

            System.out.println("\nBooking Confirmed Successfully");
            System.out.println(reservation);
        }
        catch(InvalidBookingException e)
        {
            System.out.println(roomType+" : "+roomTypeCount.get(roomType));
            System.out.println("\nBooking Failed: "+e.getMessage());
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
