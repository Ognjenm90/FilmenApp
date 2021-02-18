package com.example.filmenapp;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class FilmAdapter extends ArrayAdapter<Film> {
    int resource = R.layout.activity_list_element;
Context c;
    public FilmAdapter(Context con, int _resurs, List<Film> items) {
        super(con, _resurs, items);
        c=con;
        resource = _resurs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LinearLayout newView;

        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        Film oneFilm = getItem(position);
        if (oneFilm != null) {


            /*Beschreibung*/
            final TextView beschreibung= (TextView) newView.findViewById(R.id.eAbout);
            beschreibung.setTypeface(null, Typeface.ITALIC);
            if(oneFilm.getAbout()==null)
                beschreibung.setText("keine Beschreibung");
            else
               beschreibung.setText(oneFilm.getAbout());

            /*Bild*/


          final ImageView filmFoto = (ImageView) newView.findViewById(R.id.eFoto);

            filmFoto.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(oneFilm.getImageString(), Base64.DEFAULT), 0, Base64.decode(oneFilm.getImageString(), Base64.DEFAULT).length));

            /*filmName*/
            final TextView name = (TextView) newView.findViewById(R.id.eName);
            name.setTypeface(null, Typeface.BOLD);
            if(oneFilm.getFilmName()==null)
                name.setText(oneFilm.getName());
            else
                name.setText(oneFilm.getFilmName());

            /*regisseur*/
            final TextView regisseur = (TextView) newView.findViewById(R.id.eRegisseur);
            regisseur.setTypeface(null, Typeface.ITALIC);
            regisseur.setText(oneFilm.getNameNachname());

        }
        return newView;
    }




}

