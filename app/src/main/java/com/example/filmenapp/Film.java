package com.example.filmenapp;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Film implements Parcelable{
    String nameNachname;
    String filmName;
    String genreName;
String imageString;
    Bitmap bild;
   byte[] image;

    String id="";
    String name="";
    ArrayList<Regisseur> regisseure;
    String about="";

    URL bild1;






    protected Film(Parcel in) throws MalformedURLException{
        id=in.readString();

        name = in.readString();
        regisseure = in.readArrayList(Regisseur.class.getClassLoader());
        about = in.readString();

        bild1 = new URL(in.readString());

    }
    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            try{
                return new Film(in);
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(id);
        dest.writeString(name);
        dest.writeList(regisseure);
        dest.writeString(about);

        dest.writeString(bild.toString());


    }

    public Film(String id, String name, ArrayList<Regisseur> regisseure, String about, String imageString) {
        this.id = id;
        this.name = name;
        this.regisseure = regisseure;
        this.about = about;
        this.imageString=imageString;
        this.bild= bild;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageString() {
        return imageString;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public void setRegisseure(ArrayList<Regisseur> regisseure) {
        this.regisseure = regisseure;
    }


    public Film(String nameNachname, String filmName, String genreName, String about, String imageString) {
        this.name = filmName;
        this.nameNachname = nameNachname;
        this.filmName= filmName;
        this.genreName = genreName;

        this.about=about;
        this.imageString=imageString;
    }

    public String getNameNachname() {
        return nameNachname;
    }

    public void setNameNachname(String nameNachname) {
        this.nameNachname = nameNachname;
    }







    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }



    public String getFilmName() {
        return filmName;
    }




}
