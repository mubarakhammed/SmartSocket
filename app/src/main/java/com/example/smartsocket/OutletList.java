package com.example.smartsocket;

public class OutletList {

    public String device_id;
    public String device;
    private  String status;
    public String frequency;
    public String power;
    public String current;
    public String voltage;
    public String date;



    public OutletList(String device_id,String device,  String frequency,String power, String current, String voltage, String status, String date){
        this.device_id = device_id;
        this.device = device;
        this.status = status;
        this.frequency = frequency;
        this.power = power;
        this.current = current;
        this.voltage = voltage;
        this.date = date;



    }


  public String getDevice_id(){
        return  device_id;
  }
    public String getDevice(){
        return  device;
    }
    public String getStatus(){
        return  status;
    }

    public String getFrequency(){
        return  frequency;
    }
    public String getPower(){
        return  power;
    }
    public String getCurrent(){
        return  current;
    }
    public String getVoltage(){
        return  voltage;
    }
    public String getDate(){
        return date;
    }



}
