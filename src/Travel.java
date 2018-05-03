import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;


public class Travel {

    private Date date;
    private String startPoint;
    private String endPoint;
    private Integer numberOfSeats;
    private User creator;
    private ArrayList<User> passengers = new ArrayList<User>();

    public Travel(Date date, String startPoint, String endPoint,Integer numberOfSeats,User creator){
        this.date=date;
        this.startPoint=startPoint;
        this.endPoint=endPoint;
        this.numberOfSeats=numberOfSeats;
        this.creator=creator;
    }

    public boolean addPassenger(User passenger){
        if(passengers.size() >= numberOfSeats)
            return false;
        
        passengers.add(passenger);
        return true;
    }

    public ArrayList<User> getPassengers(){
        return passengers;
    }

    public boolean removePassenger(User passenger){
        if(passengers.size() == 0)
            return false;

        return  passengers.remove(passenger);
    }

    public User getCreator(){
        return creator;
    }

}