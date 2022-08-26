package com.example.demo.utils;

public class CalculatePrice {

    public static int calculatePrice(String timeArrive,String timeDepart,int rest){
        String[] timeStart=timeArrive.split(":");
        String[] timeEnd=timeDepart.split(":");
        int price=0;

        if (timeStart.length!=0 && timeEnd.length!=0){
            int hourStart=Integer.parseInt(timeStart[0]);
            int hourEnd=Integer.parseInt(timeEnd[0]);

            int minStart=Integer.parseInt(timeStart[1]);
            int minEnd=Integer.parseInt(timeEnd[1]);

            int period=(hourEnd*60+minEnd)-(hourStart*60+minStart)-rest;
            int hours=period/60;
            if (period%60!=0){
                hours++;
            }
            price=hours*100;
        }
        return price;
    }
}
