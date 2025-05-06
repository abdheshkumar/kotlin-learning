package model;

import java.time.LocalDate;

public class User {
    private final String myFirstName;
    private final String myLastName;
    private final Company myCompany;
    private final Twitter myTwitter;
    private LocalDate dob;

    public User(String myFirstName, String myLastName, Company myCompany, Twitter myTwitter) {
        this.myFirstName = myFirstName;
        this.myLastName = myLastName;
        this.myCompany = myCompany;
        this.myTwitter = myTwitter;
    }

    public String getMyLastName() {
        return myLastName;
    }

    public Company getMyCompany() {
        return myCompany;
    }

    public Twitter getMyTwitter() {
        return myTwitter;
    }

    public LocalDate getDob() {
        return dob;
    }


    public String getMyFirstName() {
        return myFirstName;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
