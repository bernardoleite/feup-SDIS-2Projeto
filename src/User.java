import java.io.*;
import java.util.*;
import java.net.*;

public class User implements Serializable{

    private String email="";
    private String password="";
    private boolean isAdmin;
    private Integer id;
    private ArrayList<Travel> myTravels = new ArrayList<Travel>();
    private ArrayList<Travel> joinTravels= new ArrayList<Travel>();
    private ArrayList<Travel> requestTravels= new ArrayList<Travel>();
    private ArrayList<Travel> notificationsWaitForTravels= new ArrayList<Travel>();

    public User(String email, String password, Boolean isAdmin,Integer id){
        this.email=email;
        this.password=password;
        this.isAdmin=isAdmin;
        this.id=id;
    }

    public ArrayList<Travel> getMyTravels(){
        return myTravels;
    }

    public Travel getMyTravel(int travelID){
        for(int i = 0; i < myTravels.size(); i++) {
            if(myTravels.get(i).getID() == travelID)
                return myTravels.get(i);
        }
        return null;
    }

    public boolean checkMyTravel(int travelID){
        for(int i = 0; i < myTravels.size(); i++) {
            if(myTravels.get(i).getID() == travelID)
                return true;
        }
        return false;
    }

    public ArrayList<Travel> getJoinTravels(){
        return joinTravels;
    }

    public ArrayList<Travel> getRequestTravels(){
        return requestTravels;
    }

    public ArrayList<Travel> getNotificationsWaitForTravels(){
        return notificationsWaitForTravels;
    }

    public boolean checkNotificationsWaitForTravel(Date date, String startPoint, String endPoint, Integer time){
        long hour = 3600*1000;
        for(int i=0; i < notificationsWaitForTravels.size();i++){
            Date date2 = new Date(notificationsWaitForTravels.get(i).getDate().getTime()+ notificationsWaitForTravels.get(i).getTime()*hour);

            if(date.after(notificationsWaitForTravels.get(i).getDate()) && date.before(date2) && notificationsWaitForTravels.get(i).getStartPoint().equals(startPoint) && notificationsWaitForTravels.get(i).getEndPoint().equals(endPoint)){
                return true;
            }
        }
        return false;
    }

    public boolean addMyTravel(Travel travel){
        for(int i=0; i < myTravels.size();i++){
            if(myTravels.get(i).checkTravel(travel.getDate(), null, null, 0))
                return false;
        }
        myTravels.add(travel);
        Server.serialize_Object();

        return true;
    }

    public void addNotificationsWaitForTravel(Travel travel){
        notificationsWaitForTravels.add(travel);
        Server.serialize_Object();
    }

    public void addJoinTravel(Travel travel){
        joinTravels.add(travel);
        Server.serialize_Object();
    }

    public void addRequestTravel(Travel travel){
        requestTravels.add(travel);
        Server.serialize_Object();
    }

    public Integer getId(){
        return id;
    }

    public boolean deleteMyTravel(int travelID){

         for(int i = 0 ; i < myTravels.size(); i++){
            if(myTravels.get(i).getID()==travelID){
                if(myTravels.remove(myTravels.get(i))){
                     Server.serialize_Object();
                     return true;
                }
            }
        }

        return false;
    }

    public boolean deleteJoinTravel(Travel travel){

        if(joinTravels.remove(travel)){
             Server.serialize_Object();
             return true;
        }

        return false;
    }

    public boolean deleteJoinTravel(int travelID){

         for(int i = 0 ; i < joinTravels.size(); i++){
            if(joinTravels.get(i).getID()==travelID){
                if(joinTravels.remove(joinTravels.get(i))){
                     Server.serialize_Object();
                     return true;
                }
            }
        }
        return false;
    }


    public boolean deleteRequestTravel(Travel travel){

        if(requestTravels.remove(travel)){
            Server.serialize_Object();
            return true;
        }

        return false;

    }

    public boolean deleteRequestTravel(int travelID){

         for(int i = 0 ; i < requestTravels.size(); i++){
            if(requestTravels.get(i).getID()==travelID){
                if(requestTravels.remove(requestTravels.get(i))){
                     Server.serialize_Object();
                     return true;
                }
            }
        }
        return false;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public int checkMyTravels(Date date, String startPoint, String endPoint, Integer time) {

        for(int i=0; i < myTravels.size();i++){
            if(myTravels.get(i).checkTravel(date, startPoint, endPoint, time))
                return myTravels.get(i).getID();
        }

        return -1;
    }

    public ArrayList<Travel> getNotifications(){
        return notificationsWaitForTravels;
    }
}
