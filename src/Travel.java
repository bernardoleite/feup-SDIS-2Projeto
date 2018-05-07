import java.util.ArrayList;
import java.util.Date;
import java.io.*;



public class Travel {

    private Integer ID;
    private Date date;
    private String startPoint;
    private String endPoint;
    private Integer numberOfSeats;
    private User creator;
    private ArrayList<User> passengers = new ArrayList<User>();

    public Travel(Integer ID, Date date, String startPoint, String endPoint,Integer numberOfSeats,User creator){
        this.ID=ID;
        this.date=date;
        this.startPoint=startPoint;
        this.endPoint=endPoint;
        this.numberOfSeats=numberOfSeats;
        this.creator=creator;
    }

    public boolean addPassenger(User passenger){
        if(passengers.size() >= numberOfSeats)
            return false;
        
        for(int i = 0; i < passengers.size(); i++) {
            for(int j = 0; j < passengers.get(i).getJoinTravels().size(); j++) {
                if(ID == passengers.get(i).getJoinTravels().get(j).getID())
                    passengers.get(i).getJoinTravels().get(j).addUser(passenger);
            }
        }
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

    public Integer getID(){
        return ID;
    }

    public void addUser(User user){
        passengers.add(user);
    }

}