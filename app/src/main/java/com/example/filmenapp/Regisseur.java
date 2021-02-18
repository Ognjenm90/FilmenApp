package com.example.filmenapp;

import java.util.ArrayList;

public class Regisseur {
    String nameNachname;


    public Regisseur(String nameNachname, String film) {
        this.nameNachname = nameNachname;
        ArrayList<String> f= new ArrayList<String>();
       f.add(film);
    }



}
