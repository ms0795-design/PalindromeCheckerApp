import java.util.*;

// Custom Exception for invalid booking scenarios
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
            private String roomId;
            private boolean cancelled;

            public Reservation(String reservationId,String guestName,String roomType)
            public Reservation(String reservationId,String guestName,String roomType,String roomId)
            {
                this.reservationId=reservationId;
                this.guestName=guestName;
                this.roomType=roomType;
                this.roomId=roomId;
                this.cancelled=false;
            }

            public String getReservationId()
            @@ -38,11 +33,28 @@ public String getRoomType()
        return roomType;
        }

        public String getRoomId()
        {
            return roomId;
        }

        public boolean isCancelled()
        {
            return cancelled;
        }

        public void cancel()
        {
            cancelled=true;
        }

        public String toString()
        {
            return "Reservation ID: "+reservationId+
                    ", Guest Name: "+guestName+
                    ", Room Type: "+roomType;
            ", Room Type: "+roomType+
                    ", Room ID: "+roomId+
                    ", Status: "+(cancelled ? "Cancelled" : "Confirmed");
        }
}

        @@ -59,36 +71,9 @@ public InventoryService()
        inventory.put("Suite",1);
    }

        public boolean isValidRoomType(String roomType)
        public void incrementInventory(String roomType)
        {
            return inventory.containsKey(roomType);
        }

        public boolean isAvailable(String roomType)
        {
            return inventory.getOrDefault(roomType,0)>0;
        }

        public void allocateRoom(String roomType) throws InvalidBookingException
        {
            if(!isValidRoomType(roomType))
            {
                throw new InvalidBookingException("Invalid room type: "+roomType);
            }

            if(!isAvailable(roomType))
            {
                throw new InvalidBookingException("No rooms available for room type: "+roomType);
            }

            int currentCount=inventory.get(roomType);

            if(currentCount<=0)
            {
                throw new InvalidBookingException("Inventory cannot become negative for room type: "+roomType);
            }

            inventory.put(roomType,currentCount-1);
            inventory.put(roomType,inventory.getOrDefault(roomType,0)+1);
        }

        public void displayInventory()
        @@ -101,58 +86,98 @@ public void displayInventory()
    }
            }

        // Validator class
        class InvalidBookingValidator
        // Booking History
        class BookingHistory
        {
            public void validateReservation(Reservation reservation,InventoryService inventoryService) throws InvalidBookingException
            private List<Reservation> confirmedBookings;

            public BookingHistory()
            {
                if(reservation.getGuestName()==null || reservation.getGuestName().trim().isEmpty())
                {
                    throw new InvalidBookingException("Guest name cannot be empty.");
                }
                confirmedBookings=new ArrayList<>();
            }

        if(reservation.getReservationId()==null || reservation.getReservationId().trim().isEmpty())
            {
                throw new InvalidBookingException("Reservation ID cannot be empty.");
            }
            public void addBooking(Reservation reservation)
            {
                confirmedBookings.add(reservation);
            }

        if(reservation.getRoomType()==null || reservation.getRoomType().trim().isEmpty())
            public Reservation findReservation(String reservationId)
            {
                for(Reservation reservation:confirmedBookings)
                {
                    throw new InvalidBookingException("Room type cannot be empty.");
                    if(reservation.getReservationId().equals(reservationId))
                    {
                        return reservation;
                    }
                }
                return null;
            }

        if(!inventoryService.isValidRoomType(reservation.getRoomType()))
            public void displayBookingHistory()
            {
                System.out.println("\nBooking History:");
                for(Reservation reservation:confirmedBookings)
                {
                    throw new InvalidBookingException("Invalid room type entered: "+reservation.getRoomType());
                    System.out.println(reservation);
                }
            }
        }

        // Booking Service
        class BookingService
        // Cancellation Service
        class CancellationService
        {
            private InventoryService inventoryService;
            private InvalidBookingValidator validator;
            private BookingHistory bookingHistory;
            private Stack<String> rollbackStack;

            public BookingService(InventoryService inventoryService)
            public CancellationService(InventoryService inventoryService,BookingHistory bookingHistory)
            {
                this.inventoryService=inventoryService;
                this.validator=new InvalidBookingValidator();
                this.bookingHistory=bookingHistory;
                rollbackStack=new Stack<>();
            }

            public void confirmBooking(Reservation reservation)
            public void cancelBooking(String reservationId)
            {
                try
                Reservation reservation=bookingHistory.findReservation(reservationId);

                if(reservation==null)
                {
                    validator.validateReservation(reservation,inventoryService);
                    inventoryService.allocateRoom(reservation.getRoomType());
                    System.out.println("\nCancellation Failed: Reservation not found for ID "+reservationId);
                    return;
                }

                System.out.println("\nBooking Confirmed Successfully");
                System.out.println(reservation);
                if(reservation.isCancelled())
                {
                    System.out.println("\nCancellation Failed: Reservation already cancelled for ID "+reservationId);
                    return;
                }

                rollbackStack.push(reservation.getRoomId());

                inventoryService.incrementInventory(reservation.getRoomType());

                reservation.cancel();

                System.out.println("\nCancellation Successful");
                System.out.println("Reservation ID: "+reservation.getReservationId());
                System.out.println("Released Room ID: "+rollbackStack.peek());
                System.out.println("Inventory restored for room type: "+reservation.getRoomType());
            }

            public void displayRollbackStack()
            {
                System.out.println("\nRollback Stack:");
                if(rollbackStack.isEmpty())
                {
                    System.out.println("No released room IDs.");
                }
        catch(InvalidBookingException e)
        else
                {
                    System.out.println("\nBooking Failed: "+e.getMessage());
                    for(String roomId:rollbackStack)
                    {
                        System.out.println(roomId);
                    }
                }
            }
        }
        @@ -163,20 +188,26 @@ public class BookMyStayApp
        public static void main(String[] args)
        {
            InventoryService inventoryService=new InventoryService();
            BookingService bookingService=new BookingService(inventoryService);
            BookingHistory bookingHistory=new BookingHistory();

            Reservation r1=new Reservation("R101","Karthik","Deluxe","D201");
            Reservation r2=new Reservation("R102","Priya","Suite","S301");
            Reservation r3=new Reservation("R103","Rahul","Standard","ST101");

            bookingHistory.addBooking(r1);
            bookingHistory.addBooking(r2);
            bookingHistory.addBooking(r3);

            CancellationService cancellationService=new CancellationService(inventoryService,bookingHistory);

            Reservation r1=new Reservation("R101","Karthik","Deluxe");
            Reservation r2=new Reservation("R102","Priya","Suite");
            Reservation r3=new Reservation("R103","Rahul","Premium");
            Reservation r4=new Reservation("R104","","Standard");
            Reservation r5=new Reservation("R105","Ananya","Deluxe");
            bookingHistory.displayBookingHistory();

            bookingService.confirmBooking(r1);
            bookingService.confirmBooking(r2);
            bookingService.confirmBooking(r3);
            bookingService.confirmBooking(r4);
            bookingService.confirmBooking(r5);
            cancellationService.cancelBooking("R102");
            cancellationService.cancelBooking("R105");
            cancellationService.cancelBooking("R102");

            bookingHistory.displayBookingHistory();
            inventoryService.displayInventory();
            cancellationService.displayRollbackStack();
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
