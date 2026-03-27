class Reservation
{
    String guestName;
    String roomType;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String guestName,String roomType)
    public Reservation(String reservationId,String guestName,String roomType)
    {
        this.reservationId=reservationId;
        this.guestName=guestName;
        this.roomType=roomType;
    }

    public String getReservationId()
    {
        return reservationId;
    }

    public String getGuestName()
    {
        return guestName;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public String toString()
    {
        return guestName+" requested "+roomType;
        return "Reservation ID: "+reservationId+
                ", Guest Name: "+guestName+
                ", Room Type: "+roomType;
    }
}

// Inventory Service
class InventoryService
// Add-On Service class
class AddOnService
{
    HashMap<String,Integer> inventory=new HashMap<>();
    private String serviceName;
    private double serviceCost;

    public InventoryService()
    public AddOnService(String serviceName,double serviceCost)
    {
        inventory.put("Deluxe",3);
        inventory.put("Suite",2);
        inventory.put("Standard",4);
        this.serviceName=serviceName;
        this.serviceCost=serviceCost;
    }

    public boolean checkAvailability(String roomType)
    public String getServiceName()
    {
        return inventory.getOrDefault(roomType,0)>0;
        return serviceName;
    }

    public void reduceInventory(String roomType)
    public double getServiceCost()
    {
        inventory.put(roomType,inventory.get(roomType)-1);
        return serviceCost;
    }

    public void showInventory()
    public String toString()
    {
        System.out.println("\nCurrent Inventory:");
        for(String type:inventory.keySet())
        {
            System.out.println(type+" Rooms left: "+inventory.get(type));
        }
        return serviceName+" (Rs. "+serviceCost+")";
    }
}

// Booking Service
class BookingService
// Add-On Service Manager class
class AddOnServiceManager
{
    Queue<Reservation> requestQueue=new LinkedList<>();

    HashSet<String> allocatedRooms=new HashSet<>();

    HashMap<String,Set<String>> roomMap=new HashMap<>();

    InventoryService inventory;

    int roomCounter=101;
    private Map<String,List<AddOnService>> reservationServices;

    public BookingService(InventoryService inventory)
    public AddOnServiceManager()
    {
        this.inventory=inventory;
        reservationServices=new HashMap<>();
    }

    // Add request
    public void addRequest(Reservation r)
    public void addService(String reservationId,AddOnService service)
    {
        requestQueue.offer(r);
        System.out.println("Request added: "+r);
        reservationServices.putIfAbsent(reservationId,new ArrayList<>());
        reservationServices.get(reservationId).add(service);
        System.out.println(service.getServiceName()+" added to Reservation ID "+reservationId);
    }

    // Process booking
    public void processRequest()
    public void displayServices(String reservationId)
    {
        if(requestQueue.isEmpty())
            List<AddOnService> services=reservationServices.get(reservationId);

        if(services==null || services.isEmpty())
        {
            System.out.println("No requests");
            System.out.println("No add-on services selected for Reservation ID "+reservationId);
            return;
        }

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
            System.out.println("\nSelected Add-On Services for Reservation ID "+reservationId+":");
        for(AddOnService service:services)
        {
            System.out.println("Booking Failed - No rooms available");
            System.out.println(service);
        }
    }

    public void showAllocations()
    public double calculateTotalAddOnCost(String reservationId)
    {
        System.out.println("\nRoom Allocations:");
        List<AddOnService> services=reservationServices.get(reservationId);
        double total=0;

        for(String type:roomMap.keySet())
            if(services!=null)
            {
                System.out.println(type+" -> "+roomMap.get(type));
                for(AddOnService service:services)
                {
                    total=total+service.getServiceCost();
                }
            }

        return total;
    }
}

// MAIN CLASS (DO NOT CHANGE)
// Main class
public class BookMyStayApp
{
    public static void main(String args[])
    public static void main(String[] args)
    {
        InventoryService inventory=new InventoryService();
        Reservation r1=new Reservation("R101","Karthik","Deluxe");
        Reservation r2=new Reservation("R102","Priya","Suite");

        AddOnServiceManager manager=new AddOnServiceManager();

        BookingService booking=new BookingService(inventory);
        AddOnService s1=new AddOnService("Breakfast",500);
        AddOnService s2=new AddOnService("Airport Pickup",1200);
        AddOnService s3=new AddOnService("Extra Bed",800);
        AddOnService s4=new AddOnService("Spa Access",1500);

        booking.addRequest(new Reservation("Karthik","Deluxe"));
        booking.addRequest(new Reservation("Priya","Suite"));
        booking.addRequest(new Reservation("Rahul","Deluxe"));
        booking.addRequest(new Reservation("Ananya","Standard"));
        manager.addService(r1.getReservationId(),s1);
        manager.addService(r1.getReservationId(),s2);
        manager.addService(r1.getReservationId(),s4);

        booking.processRequest();
        booking.processRequest();
        booking.processRequest();
        booking.processRequest();
        manager.addService(r2.getReservationId(),s3);

        booking.showAllocations();
        System.out.println("\n"+r1);
        manager.displayServices(r1.getReservationId());
        System.out.println("Total Add-On Cost: Rs. "+manager.calculateTotalAddOnCost(r1.getReservationId()));

        inventory.showInventory();
        System.out.println("\n"+r2);
        manager.displayServices(r2.getReservationId());
        System.out.println("Total Add-On Cost: Rs. "+manager.calculateTotalAddOnCost(r2.getReservationId()));
    }
}