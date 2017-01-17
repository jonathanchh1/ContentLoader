package com.example.jonat.historyclasscontentprovider;

import java.util.ArrayList;

/**
 * Created by jonat on 1/16/2017.
 */

public class ProfileInfo {
    public String id;
    public String name;
    public ArrayList<Emails> emails;
    public ArrayList<PhoneNumbers> numbers;

    public ProfileInfo(String id, String name){
        this.id = id;
        this.name = name;
        this.emails = new ArrayList<Emails>();
        this.numbers = new ArrayList<PhoneNumbers>();
    }

    @Override
    public String toString() {
        String results = name;
        if(numbers.size() > 0){
            PhoneNumbers number = numbers.get(0);
            results += " (" + number.number + " - " + number.type + ")";
        }

        if(emails.size() > 0){
            Emails email = emails.get(0);
            results += " [" + email.address + " - " + email.type + "]";
        }

        return results;
    }

    public void addEmail(String address, String type){
        emails.add(new Emails(address, type));
    }

    public void addNumber(String number, String type){
        numbers.add(new PhoneNumbers(number, type));
    }
}
