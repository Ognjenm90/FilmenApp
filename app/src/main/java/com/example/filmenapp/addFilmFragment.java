package com.example.filmenapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.filmenapp.dataBase.DB_NAME;
import static com.example.filmenapp.dataBase.DB_VERSION;
import static com.example.filmenapp.genres.films;
import static com.example.filmenapp.genres.genres;

public class addFilmFragment extends Fragment {
    public ImageView foto;
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_add_film,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        foto = (ImageView) getView().findViewById(R.id.foto);
        final EditText regisseur = (EditText) getView().findViewById(R.id.nameRegisseur);
        final EditText name = (EditText)  getView().findViewById(R.id.filmName);
        final EditText description = (EditText)  getView().findViewById(R.id.description);
        final Button findFoto = (Button)  getView().findViewById(R.id.findFoto);
        final Button filmEinlesen = (Button)  getView().findViewById(R.id.filmEinlesen);
        final Spinner spinner = (Spinner)  getView().findViewById(R.id.sGenres);
        final Button delete = (Button)  getView().findViewById(R.id.dErase);

        Intent intent = getActivity().getIntent();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, genres);
        spinner.setAdapter(adapter);

        filmEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*checken ob da lle Pätze ausgefühlt sind*/
                if (regisseur.getText().toString().equals("") || name.getText().toString().equals("") || description.getText().toString().equals("") )

                {
                    Toast.makeText(getActivity(), "Fullen Sie die leere Plätze aus oder ändern Sie die name des Films", Toast.LENGTH_SHORT).show();
            }else if(foto.getDrawable()==null){


                    Toast.makeText(getActivity(), "Wählen Sie das Foto aus", Toast.LENGTH_SHORT).show();
                }else {
                    //von imageView bitmapp für konstruktor machen


                    //encode image to base64 string


                 System.out.println("Fotoo "+foto);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] imageBytes = baos.toByteArray();
                    final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    //    System.out.println( "add film"+ imageString);

                    Film f=new Film(regisseur.getText().toString(),name.getText().toString(), spinner.getSelectedItem().toString(),description.getText().toString(),imageString);

                    f.setId("kein ID");

                    boolean isThere=false;

                    /*checken ob es dieses Film gibt damit man es nich 2 mal eintragen kann*/
                    for(Film o: films){
                        if (o.getName().equals(name.getText().toString()))
                            isThere=true;
                    }

                    if(!isThere){

                        films.add(f);

                        dataBase db = new dataBase(getActivity(), DB_NAME, null, DB_VERSION);
                        ArrayList<Regisseur> re = new ArrayList<>();
                        re.add(new Regisseur(regisseur.getText().toString(), f.getName()));
                        f.setRegisseure(re);
                        /*eintragen das film und regisseur*/
                        long res = db.addFilm(f);
                        long res2 = db.addRegisseur(regisseur.getText().toString());
                        db.addRegUndFilm(res, res2);

                        if (res != -1)
                            Toast.makeText(getActivity(), "eingetragen in db. id: " + String.valueOf(res), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Fehler beim eintragen", Toast.LENGTH_SHORT).show();
                    }
                    /*wenn es schon gibt*/
                    else {
                        Toast.makeText(getActivity(), "es gibt schon dieses film", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        findFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textTargetUri = new TextView(getActivity());
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
               foto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

