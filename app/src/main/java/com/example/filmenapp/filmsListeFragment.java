package com.example.filmenapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.filmenapp.dataBase.DB_NAME;
import static com.example.filmenapp.dataBase.DB_VERSION;
import static com.example.filmenapp.genres.genres;


public class filmsListeFragment extends Fragment {

    boolean reg = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_films_liste, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reg = false;
        final EditText text = (EditText) getView().findViewById(R.id.textFind);
        final ListView genreList = (ListView) getView().findViewById(R.id.genreListe);
        final Button regisseurButton = (Button) getView().findViewById(R.id.Bregisseurs);
        final Button genreButton = (Button) getView().findViewById(R.id.Bgenres);
        final Button search = (Button) getView().findViewById(R.id.dSuchen);
        final Button addGenre = (Button) getView().findViewById(R.id.BaddGenre);
        final Button addFilm = (Button) getView().findViewById(R.id.BaddFilm);
        final ArrayList<Integer> nummerOfFilms = new ArrayList<Integer>();//damit man weiss nummer von filmen für jeden regisseur

        /*db*/
        dataBase DB = new dataBase(getActivity(), DB_NAME,null, DB_VERSION);
        final ArrayList<String> regisseurs = DB.getRegisseurListe(); //alle regiss.
        final ArrayList<String> regisseureWrite = new ArrayList<>();    //regisseure die sich nicht wiederholen
        final ArrayAdapter<String> adapter; //liste von Genres
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, genres);
        final ArrayAdapter<String> adapters;    //liste von den Regisseuren die sich nicht wiederholen
        adapters = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, regisseureWrite);

        //liste von den Regisseuren die sich nicht wiederholen
        for(String a: regisseurs){
            if(!regisseureWrite.contains(a))
                regisseureWrite.add(a);
        }

        addFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Fragment fragment = new addFilmFragment();
    getFragmentManager().beginTransaction().replace(R.id.place, fragment).addToBackStack(null).commit();

            }
        });


        genreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = new filmsFragment();
                Bundle args = new Bundle();


                args.putString("Genre", genreList.getItemAtPosition(position).toString());
                //wenn aut=true dann is genre Reg name,wenn  false dann ist es Genre-name
                args.putBoolean("Regisseur",reg);
                fragment.setArguments(args);

                    getFragmentManager().beginTransaction().replace(R.id.place, fragment).addToBackStack(null).commit();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(text.getText(), new Filter.FilterListener() {


                    @Override
                    public void onFilterComplete(int i) {
                        if (adapter.isEmpty()) {
                            addGenre.setEnabled(true);
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                addGenre.setEnabled(false);
            }
        });

        regisseurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setVisibility(View.GONE);
                addGenre.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                genreList.setAdapter(adapters);


                reg = true;
            }
        });
        genreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reg = false;
                search.setVisibility(View.VISIBLE);
                addGenre.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);

                genreList.setAdapter(adapter);
            }
        });

        addGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genres.add(0, text.getText().toString());
                //eintragen der Genre in db
                dataBase db=new dataBase(getActivity(), DB_NAME, null, DB_VERSION);

                long rez=db.addGenre(text.getText().toString());
                if (rez!=-1)Toast.makeText(getActivity(), "Genre eingetragen. ID: "+String.valueOf(rez), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Fehler beim eintragen ges Genres" , Toast.LENGTH_LONG).show();

                adapter.add(text.getText().toString());
                adapter.notifyDataSetChanged();
                adapter.getFilter().filter("");
                text.setText("");
                addGenre.setEnabled(false);
            }
        });


    }


}

