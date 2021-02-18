package com.example.filmenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class dataBase extends SQLiteOpenHelper {


    public static final String DB_NAME = "myDB.db";
    public static final int DB_VERSION = 1;
    //TABELEN
    public static final String DB_TABLE = "Genre";
    public static final String DB_TABLE2 = "Film";
    public static final String DB_TABLE3 = "Regisseur";
    public static final String DB_TABLE4 = "FilmUndRegisseur";

    //Genres
    public static final String GENRE_ID = "_id";
    public static final String GENRE_NAME = "name";
    //Filmen
    public static final String FILM_ID = "_id";
    public static final String FILM_NAME = "name";
    public static final String FILM_ABOUT = "about";


    public static final String FILM_GEN = "idgenres";
    public static final String FILM_FOTO = "foto";

    //Regissseurs

    public static final String REGISSEUR_ID = "_id";
    public static final String REGISSEUR_NAME = "name";
    //FILMUNDREGISSEURZUSAMMEN

    public static final String REGISSEURUNDFILM_ID = "_id";
    public static final String REGISEURUNDFILM_REGISSEUR = "idregisseur";
    public static final String REGISSEURUNDFILM_FILM = "idfilms";


    public dataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //erstellen db
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                DB_TABLE + " (" + GENRE_ID + " integer primary key autoincrement, " + GENRE_NAME + " text not null); ");
        db.execSQL("create table " +
                DB_TABLE2 + " (" + FILM_ID + " integer primary key autoincrement, " + FILM_NAME + " text not null, " +
                FILM_ABOUT + " text, " + FILM_GEN + " integer not null, " +
                FILM_FOTO + " text not null ); ");
        db.execSQL("create table " +
                DB_TABLE3 + " (" + REGISSEUR_ID + " integer primary key autoincrement, " + REGISSEUR_NAME + " text not null); ");
        db.execSQL("create table " +
                DB_TABLE4 + " (" + REGISSEURUNDFILM_ID + " integer primary key autoincrement, " + REGISEURUNDFILM_REGISSEUR + " integer not null, " +
                REGISSEURUNDFILM_FILM + " integer not null); ");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE3);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE4);
        onCreate(db);
    }

    public long addRegisseur(String name) {
        ContentValues neue = new ContentValues();
        neue.put(REGISSEUR_NAME, name);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(this.DB_TABLE3, null, neue);
        db.close();
        return id;
    }

    public void addRegUndFilm(long a, long b) {
        ContentValues neu = new ContentValues();
        neu.put(REGISEURUNDFILM_REGISSEUR, a);
        neu.put(REGISSEURUNDFILM_FILM, b);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(this.DB_TABLE4, null, neu);
        db.close();
    }

    public long addGenre(String name) {
        ContentValues neu = new ContentValues();
        neu.put(GENRE_NAME, name);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(this.DB_TABLE, null, neu);
        db.close();
        return id;
    }

    public long addFilm(Film f) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> reg = getGenreListe();

        ContentValues neu = new ContentValues();
        neu.put(FILM_NAME, f.getName());
        neu.put(FILM_ABOUT, f.getAbout());
        System.out.println("dbb about"+f.getAbout());


        neu.put(FILM_GEN, reg.indexOf(f.getGenreName()));
        //add default foto
      neu.put(FILM_FOTO,f.getImageString());
     //   System.out.println("dbb Foto "+f.getImageString());

        long id = db.insert(this.DB_TABLE2, null, neu);
        db.close();
        return id;
    }

    public ArrayList<Film> filmsGenres(long idGenre) {
        ArrayList<Film> films = getFilmsListe();
        ArrayList<Film> forGiveBack = new ArrayList<>();
        String genre = getGenreListe().get((int) idGenre);
        for (Film f : films)
            if (f.genreName.equals(genre))
                forGiveBack.add(f);
        return forGiveBack;
    }

    public Integer getIdOfFilmForRegisseur(int IDregisseur) {
        String[] columns_result = new String[]{REGISEURUNDFILM_REGISSEUR, REGISSEURUNDFILM_FILM};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> IDsOfFilms = new ArrayList<>();
        Cursor cursor = db.query(this.DB_TABLE4,
                columns_result, null,
                null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            //IN ERSTER COLUMN ID VON REGISSEUR SUCHEN WENN WIR FINDEN SCHIBEN WIR ES ZURÜCK AUS GLECHE REIHE ABER AUS ANDERE COLUMN
            if (cursor.getInt(cursor.getColumnIndexOrThrow(REGISEURUNDFILM_REGISSEUR)) == IDregisseur) {
                i = cursor.getInt(cursor.getColumnIndexOrThrow(REGISSEURUNDFILM_FILM));
                break;
            }
        }
        cursor.close();
        return i;
    }

    public ArrayList<String> getGenreListe() {
        String[] columns_result = new String[]{GENRE_ID, GENRE_NAME};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> genres = new ArrayList<>();
        Cursor cursor = db.query(this.DB_TABLE,
                columns_result, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            genres.add(cursor.getString(cursor.getColumnIndexOrThrow(GENRE_NAME)));
        }
        cursor.close();
        return genres;
    }

    public ArrayList<String> getRegisseurListe() {
        //alle regisseurs aus db und die die sich wioederholen
        String[] columns_result = new String[]{GENRE_ID, REGISSEUR_NAME};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> aut = new ArrayList<>();
        Cursor cursor = db.query(this.DB_TABLE3, columns_result, null, null, null, null, null);
        while (cursor.moveToNext()) {
            aut.add(cursor.getString(cursor.getColumnIndexOrThrow(REGISSEUR_NAME)));
        }
        cursor.close();
        return aut;
    }

    public Film filmOfId(int id) {
        Film f= getFilmsListe().get(id);
        return f;
    }


    public ArrayList<Film> filmsOfRegisseur(long idOfRegisseur) {
        int id = (int) idOfRegisseur;
        String name = getRegisseurListe().get(id);
        ArrayList<Integer> isti = new ArrayList<>();
        int i = 0;
        for (String a : getRegisseurListe()) {
            if (a.equals(name))
                isti.add(i);
            i++;
        }
        ArrayList<Integer> idFilm = new ArrayList<>();
        for (Integer idd : isti) {
            idFilm.add(getIdOfFilmForRegisseur(idd));
        }
        ArrayList<Film> filmReg = new ArrayList<>();
        for (Integer back : idFilm) {
           filmReg.add(filmOfId(back));
        }
        return filmReg;
    }

    public ArrayList<Film> getFilmsListe() {
        String[] columns_result = new String[]{FILM_ID, FILM_NAME, FILM_ABOUT,  FILM_GEN, FILM_FOTO};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Film> films = new ArrayList<>();
        Cursor cursor = db.query(this.DB_TABLE2, columns_result, null, null, null, null, null);
        int ID = cursor.getColumnIndexOrThrow(FILM_ID);
        int name = cursor.getColumnIndexOrThrow(FILM_NAME);
        int about = cursor.getColumnIndexOrThrow(FILM_ABOUT);

        int genre = cursor.getColumnIndexOrThrow(FILM_GEN);
        int foto = cursor.getColumnIndexOrThrow(FILM_FOTO);

        int nummerOfReg = 0;
        while (cursor.moveToNext()) {
            //erstmal ertellen wir regisseur
            ArrayList<Regisseur> r = new ArrayList<>();
            Regisseur reg = new Regisseur(getRegisseurListe().get(nummerOfReg), "a");
            r.add(reg);

            System.out.println("cursor foto"+cursor.getString(foto));


            Film f = new Film(cursor.getString(ID), cursor.getString(name), r, cursor.getString(about), cursor.getString(foto));
            f.setNameNachname(getRegisseurListe().get(nummerOfReg));
            f.setGenreName(getGenreListe().get(cursor.getInt(genre)));



            films.add(f);
            nummerOfReg++;
        }
        cursor.close();
        return films;

    }


}

