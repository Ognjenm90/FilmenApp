package com.example.filmenapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.filmenapp.dataBase.DB_NAME;
import static com.example.filmenapp.dataBase.DB_VERSION;
import static com.example.filmenapp.genres.genres;


public class filmsFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_films, container, false);

        if ( getArguments() != null && getArguments().containsKey("Genre")) {
            final Button back = (Button) view.findViewById(R.id.dBack);


            final ListView filmList = (ListView) view.findViewById(R.id.filmsListe);
            final ArrayList<Film> write = new ArrayList<Film>();
            final FilmAdapter adapter;
            adapter = new FilmAdapter(getActivity(), R.layout.activity_list_element, write);
            boolean regisseures = getArguments().getBoolean("Regisseur");
            final String conditionGenre = getArguments().getString("Genre");
            int nummerOf;



            final dataBase db = new dataBase(getActivity(), DB_NAME, null, DB_VERSION);


            if (!regisseures) {
                genres = db.getGenreListe();
                nummerOf = genres.indexOf(conditionGenre);
                /*write sind genres die sich anzeigen*/
                ArrayList<Film> fff = db.filmsGenres((long) nummerOf);
                for (Film f : fff)
                    write.add(f);
            } else {
                /*regisseure die sich nicht widerholen*/
                ArrayList<String> regiseurListe= db.getRegisseurListe();
                int nummerOfRegisseur = regiseurListe.indexOf(conditionGenre);

                ArrayList<Film> filmsOfRegisseurs = db.filmsOfRegisseur((long) nummerOfRegisseur);


                for (Film r : filmsOfRegisseurs)
                    write.add(r);

            }

            filmList.setAdapter(adapter);
            filmList.setClickable(true);

            filmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    adapter.notifyDataSetChanged();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });

        }
        return view;
    }
}





