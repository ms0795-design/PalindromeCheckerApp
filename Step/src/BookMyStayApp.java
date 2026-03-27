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