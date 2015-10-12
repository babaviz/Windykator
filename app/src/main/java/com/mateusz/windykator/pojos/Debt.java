package com.mateusz.windykator.pojos;

import java.io.Serializable;

/**
 * Created by Mateusz on 2015-06-02.
 */
public class Debt implements Serializable {

    private double value;
    private String description;
    private long personID;
    private int ID;

    public Debt() {}

    public Debt(double value, String description) {
        this.value = value;
        this.description = description;
    }

    public Debt(double value, String description, long personID, int ID) {
        this.value = value;
        this.description = description;
        this.personID = personID;
        this.ID = ID;
    }



    public double getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return description + "\n" + value;
    }

}
