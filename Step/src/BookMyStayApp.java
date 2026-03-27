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
@@ -29,90 +31,90 @@ public String getRoomType()
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

// Add-On Service class
class AddOnService
// Booking History class
class BookingHistory
{
    private String serviceName;
    private double serviceCost;
    private List<Reservation> confirmedBookings;

    public AddOnService(String serviceName,double serviceCost)
    public BookingHistory()
    {
        this.serviceName=serviceName;
        this.serviceCost=serviceCost;
        confirmedBookings=new ArrayList<>();
    }

    public String getServiceName()
    public void addConfirmedBooking(Reservation reservation)
    {
        return serviceName;
        confirmedBookings.add(reservation);
        System.out.println("Booking added to history: "+reservation.getReservationId());
    }

    public double getServiceCost()
    public List<Reservation> getConfirmedBookings()
    {
        return serviceCost;
        return confirmedBookings;
    }

    public String toString()
    public void displayBookingHistory()
    {
        return serviceName+" (Rs. "+serviceCost+")";
        if(confirmedBookings.isEmpty())
        {
            System.out.println("No confirmed bookings found.");
            return;
        }

        System.out.println("\nBooking History:");
        for(Reservation reservation:confirmedBookings)
        {
            System.out.println(reservation);
        }
    }
}

// Add-On Service Manager class
class AddOnServiceManager
// Booking Report Service class
class BookingReportService
{
    private Map<String,List<AddOnService>> reservationServices;

    public AddOnServiceManager()
    {
        reservationServices=new HashMap<>();
    }

    public void addService(String reservationId,AddOnService service)
    {
        reservationServices.putIfAbsent(reservationId,new ArrayList<>());
        reservationServices.get(reservationId).add(service);
        System.out.println(service.getServiceName()+" added to Reservation ID "+reservationId);
    }

    public void displayServices(String reservationId)
    public void generateSummaryReport(List<Reservation> bookings)
    {
        List<AddOnService> services=reservationServices.get(reservationId);

        if(services==null || services.isEmpty())
            if(bookings.isEmpty())
            {
                System.out.println("No add-on services selected for Reservation ID "+reservationId);
                System.out.println("No data available for report.");
                return;
            }

        System.out.println("\nSelected Add-On Services for Reservation ID "+reservationId+":");
        for(AddOnService service:services)
            int totalBookings=bookings.size();
        double totalRevenue=0;

        Map<String,Integer> roomTypeCount=new HashMap<>();

        for(Reservation reservation:bookings)
        {
            System.out.println(service);
            totalRevenue=totalRevenue+reservation.getBookingAmount();

            String roomType=reservation.getRoomType();
            roomTypeCount.put(roomType,roomTypeCount.getOrDefault(roomType,0)+1);
        }
    }

    public double calculateTotalAddOnCost(String reservationId)
    {
        List<AddOnService> services=reservationServices.get(reservationId);
        double total=0;
        System.out.println("\nBooking Summary Report");
        System.out.println("Total Confirmed Bookings: "+totalBookings);
        System.out.println("Total Revenue: Rs. "+totalRevenue);

        if(services!=null)
            System.out.println("\nBookings by Room Type:");
        for(String roomType:roomTypeCount.keySet())
        {
            for(AddOnService service:services)
            {
                total=total+service.getServiceCost();
            }
            System.out.println(roomType+" : "+roomTypeCount.get(roomType));
        }

        return total;
    }
}

@@ -121,28 +123,21 @@ public class BookMyStayApp
{
    public static void main(String[] args)
    {
        Reservation r1=new Reservation("R101","Karthik","Deluxe");
        Reservation r2=new Reservation("R102","Priya","Suite");

        AddOnServiceManager manager=new AddOnServiceManager();

        AddOnService s1=new AddOnService("Breakfast",500);
        AddOnService s2=new AddOnService("Airport Pickup",1200);
        AddOnService s3=new AddOnService("Extra Bed",800);
        AddOnService s4=new AddOnService("Spa Access",1500);
        BookingHistory history=new BookingHistory();
        BookingReportService reportService=new BookingReportService();

        manager.addService(r1.getReservationId(),s1);
        manager.addService(r1.getReservationId(),s2);
        manager.addService(r1.getReservationId(),s4);
        Reservation r1=new Reservation("R101","Karthik","Deluxe",3500);
        Reservation r2=new Reservation("R102","Priya","Suite",5000);
        Reservation r3=new Reservation("R103","Rahul","Standard",2500);
        Reservation r4=new Reservation("R104","Ananya","Deluxe",3500);

        manager.addService(r2.getReservationId(),s3);
        history.addConfirmedBooking(r1);
        history.addConfirmedBooking(r2);
        history.addConfirmedBooking(r3);
        history.addConfirmedBooking(r4);

        System.out.println("\n"+r1);
        manager.displayServices(r1.getReservationId());
        System.out.println("Total Add-On Cost: Rs. "+manager.calculateTotalAddOnCost(r1.getReservationId()));
        history.displayBookingHistory();

        System.out.println("\n"+r2);
        manager.displayServices(r2.getReservationId());
        System.out.println("Total Add-On Cost: Rs. "+manager.calculateTotalAddOnCost(r2.getReservationId()));
        reportService.generateSummaryReport(history.getConfirmedBookings());
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
