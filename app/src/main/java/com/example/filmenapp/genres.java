package com.example.filmenapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.filmenapp.dataBase.DB_NAME;
import static com.example.filmenapp.dataBase.DB_VERSION;

public class genres extends AppCompatActivity {

    public static ArrayList<Film> films = new ArrayList<Film>();
    public static ArrayList<String> genres = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dataBase db = new dataBase(getApplicationContext(), DB_NAME, null, DB_VERSION);

        setContentView(R.layout.activity_genres_akt);

        //aus db einlesen
        genres = db.getGenreListe();

// wenn es leer ist hinzufügen  in db  und dann lesen  aus db
        if (genres.isEmpty()) {
            db.addGenre("Drama");
            db.addGenre("Action");
             db.addGenre("Comedie");
             db.addGenre("Horor");
            db.addGenre("Triller");


            genres = db.getGenreListe();

        }

        films = db.getFilmsListe();
        /* hinzufügen 1 film und 1 regisseur*/
        if (films.isEmpty()) {

            ArrayList<Regisseur> r = new ArrayList<>();
            r.add(new Regisseur("JK Rowling", "Harry Potter"));

            Film f = new Film("JK Rowling", "Harry Potter", "Drama", "Harry Potter is ein Film", "");

            f.setId("kein ID");

            films.add(f);

            r.add(new Regisseur("JK Rowling", "Harry Potter"));

            f.setRegisseure(r);
            /*eintragen das film und regisseur*/
            long add1 = db.addFilm(f);
            long add2 = db.addRegisseur("JK Rowling");

            db.addRegUndFilm(add1, add2);
        }
        FragmentManager fm = getSupportFragmentManager();

        //ez
        filmsListeFragment f = (filmsListeFragment) fm.findFragmentByTag("Liste");
        if (f == null) {
            f= new filmsListeFragment();
            fm.beginTransaction().replace(R.id.place, f, "Liste").commit();
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }
}