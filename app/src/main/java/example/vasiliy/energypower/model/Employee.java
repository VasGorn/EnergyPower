package example.vasiliy.energypower.model;

import java.io.Serializable;

public class Employee implements Serializable{
    private String firstName;
    private String lastName;
    private String middleName;

    private int ID;

    public Employee(String lastName, String firstName, String middleName, int id){
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.ID = id;
    }

    public String getName(){
        return (lastName + " " + firstName + " " + middleName);
    }

    public int getID(){
        return this.ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String toString(){ return (lastName + " " + firstName + " " + middleName); }

    @Override
    public boolean equals(Object o){
        if( o == null){
            return false;
        }else if (o instanceof Employee){
            return this.ID == ((Employee) o).getID();
        }

        return false;
    }
}
