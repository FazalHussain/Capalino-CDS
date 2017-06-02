package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 8/1/2016.
 */
public class UpdateData {
    String fname,lastname,email,buisnessaddress,phno,city,state,buisnessname,buisnessweb,zipcode;

    public UpdateData(String fname, String lastname, String email, String buisnessaddress, String phno, String city,
                      String state, String buisnessname, String buisnessweb) {
        this.fname = fname;
        this.lastname = lastname;
        this.email = email;
        this.buisnessaddress = buisnessaddress;
        this.phno = phno;
        this.city = city;
        this.state = state;
        this.buisnessname = buisnessname;
        this.buisnessweb = buisnessweb;
    }

    public UpdateData(String fname, String lastname, String email, String buisnessaddress, String phno, String city, String state,
                      String buisnessname, String buisnessweb, String zipcode) {
        this.fname = fname;
        this.lastname = lastname;
        this.email = email;
        this.buisnessaddress = buisnessaddress;
        this.phno = phno;
        this.city = city;
        this.state = state;
        this.buisnessname = buisnessname;
        this.buisnessweb = buisnessweb;
        this.zipcode = zipcode;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBuisnessaddress() {
        return buisnessaddress;
    }

    public void setBuisnessaddress(String buisnessaddress) {
        this.buisnessaddress = buisnessaddress;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBuisnessname() {
        return buisnessname;
    }

    public void setBuisnessname(String buisnessname) {
        this.buisnessname = buisnessname;
    }

    public String getBuisnessweb() {
        return buisnessweb;
    }

    public void setBuisnessweb(String buisnessweb) {
        this.buisnessweb = buisnessweb;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
