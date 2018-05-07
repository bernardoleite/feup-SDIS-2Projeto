import java.util.ArrayList;
import java.io.*;


public class User {

    private String email="";
    private String password="";
    private boolean isAdmin;
    private Integer id;
    private ArrayList<Travel> myTravels = new ArrayList<Travel>();
    private ArrayList<Travel> joinTravels= new ArrayList<Travel>();

    public User(String email, String password, Boolean isAdmin,Integer id){
        this.email=email;
        this.password=password;
        this.isAdmin=isAdmin;
        this.id=id;
    }

    public ArrayList<Travel> getMyTravels(){
        return myTravels;
    }


    public ArrayList<Travel> getJoinTravels(){
        return joinTravels;
    }

    public void addMyTravel(Travel travel){
        myTravels.add(travel);
    }

    public void addJoinTravel(Travel travel){
        joinTravels.add(travel);
    }

    public Integer getId(){
        return id;
    }
    
    public boolean deleteMyTravel(Travel travel){

        if(travel.getCreator().getId()!=id)
            return false;

        return myTravels.remove(travel);      
    }

    public boolean deleteJoinTravel(Travel travel){

        return joinTravels.remove(travel);
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
