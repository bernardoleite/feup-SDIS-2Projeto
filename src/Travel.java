import java.util.ArrayList;
import java.util.Date;
import java.io.*;



public class Travel implements Serializable{

    private Integer ID;
    private Date date;
    private String startPoint;
    private String endPoint;
    private Integer numberOfSeats;
    private User creator;
    private ArrayList<User> passengers = new ArrayList<User>();
    private ArrayList<User> passengersRequest = new ArrayList<User>();

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
            if(passengers.get(i).getEmail().equals(passenger.getEmail()))
                return false;
        }
        boolean b = false;
        for(int i = 0; i < passengersRequest.size(); i++) {
            if(passengersRequest.get(i).getEmail().equals(passenger.getEmail())) {
                passengers.add(passenger);
                passengersRequest.remove(passenger);
                passenger.addJoinTravel(this);
                passenger.deleteRequestTravel(this);
                return true;
            }
        }
        
        return false;
    }

    public ArrayList<User> getPassengers(){
        return passengers;
    }

    public ArrayList<User> getPassengersRequest(){
        return passengersRequest;
    }

    public String getStartPoint(){
        return startPoint;
    }

    public String getEndPoint(){
        return endPoint;
    }

    public Date getDate(){
        return date;
    }

    public boolean removePassenger(User passenger){
        if(passengers.size() == 0)
            return false;
        boolean b = passengers.remove(passenger);
        if(b) {
            passenger.deleteJoinTravel(this);
            passengersRequest.add(passenger);
            passenger.addRequestTravel(this);
        }
        return  b;
    }

    public boolean removePassengersRequest(User passenger){
        
        for(int i = 0; i < passengersRequest.size(); i++) {
            if(passengersRequest.get(i).getEmail().equals(passenger.getEmail())) {
                passengersRequest.remove(passenger);
                passenger.deleteRequestTravel(this);
                return true;
            }
        }
        for(int i = 0; i < passengers.size(); i++) {
            if(passengers.get(i).getEmail().equals(passenger.getEmail())) {
                passengers.remove(passenger);
                passenger.deleteJoinTravel(this);
                return true;
            }
        }
        return false;
    }

    public User getCreator(){
        return creator;
    }

    public Integer getID(){
        return ID;
    }

    public boolean addPassengerRequest(User user){

        for(int i = 0; i < passengersRequest.size(); i++) {
            if(passengersRequest.get(i).getEmail().equals(user.getEmail()))
                return false;
        }
        for(int i = 0; i < passengers.size(); i++) {
            if(passengers.get(i).getEmail().equals(user.getEmail()))
                return false;
        }
        passengersRequest.add(user);
        user.addRequestTravel(this);

        return true;
    }

}