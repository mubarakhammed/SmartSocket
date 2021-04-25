package com.example.smartsocket;

public class OutletList {

    public String ouletName;
    public String watt;
    public String volt;


    public OutletList(String ouletName,String watt, String volt){
        this.ouletName = ouletName;
        this.watt = watt;
        this.volt = volt;
    }


    public String getOuletName(){
        return ouletName;
    }

    public String getWatt(){
        return watt;
    }

    public String getVolt(){
        return volt;
    }



}
