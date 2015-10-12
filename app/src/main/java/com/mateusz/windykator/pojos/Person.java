package com.mateusz.windykator.pojos;

import java.io.Serializable;

/**
 * Created by Mateusz on 2015-06-02.
 */
public class Person implements Serializable {

    private long id;
    private String name;
    private String surname;
    private String phoneNo;
    private boolean sendMessage;

    public Person() {
    }

    public Person(String name, String surname, String phoneNo, boolean sendMessage) {
        this.name = name;
        this.surname = surname;
        this.phoneNo = phoneNo;
        this.sendMessage =sendMessage;
    }

    public Person(String name, String surname, String phoneNo, boolean sendMessage, long id) {
        this.name = name;
        this.surname = surname;
        this.phoneNo = phoneNo;
        this.sendMessage = sendMessage;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    @Override
    public String toString() {

        return String.format("%s %s", name, surname);
    }

    public String toDebugerString(){
        return String.format("%s %s %s %b %d",name,surname,phoneNo,sendMessage,id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {

        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Person))return false;

        Person otherPerson = (Person)other;
        if (this.name != otherPerson.name){
            if(this.surname != otherPerson.surname)
                return false;
        }

        return true;
    }

}
