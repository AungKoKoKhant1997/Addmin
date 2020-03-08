package com.akkk.adminwatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoviePopUp extends DialogFragment {
    MovieModel model;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.moviepopup,container,false);
        final EditText edtname=myView.findViewById(R.id.movie_name);
        final EditText edtvideo=myView.findViewById(R.id.movie_video);
        final EditText edtimg=myView.findViewById(R.id.movie_img);
        final Spinner spcategory=myView.findViewById(R.id.movie_spinner1);
        final ArrayList<String> categoryNames = new ArrayList<String>();
        if (model!=null) {
            edtname.setText(model.name);
            edtimg.setText(model.imglink);
            edtvideo.setText(model.videolink);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("categories");
        CollectionReference serieref=db.collection("series");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot s : queryDocumentSnapshots) {
                    CategoryModel c = s.toObject(CategoryModel.class);
                    categoryNames.add(c.categoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,categoryNames);
                spcategory.setAdapter(adapter);
                if (model!=null){
                    for (int i=0;i<categoryNames.size();i++){
                        if (categoryNames.get(i).equals(model.category))
                        {
                            spcategory.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });
        /*final ArrayList<String> serieName=new ArrayList<String>();
        final Spinner spseries=myView.findViewById(R.id.movie_spinner);
        serieref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                    serieName.add(snapshot.toObject(SeriesModel.class).name);

                }ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,serieName);
                spseries.setAdapter(adapter);
                if (model!=null){
                    for (int i=0;i<serieName.size();i++){
                        if (serieName.get(i).equals(model.series)){
                            spseries.setSelection(i);
                        }
                    }
                }



            }
        });*/
        final CollectionReference seriesRef=db.collection("movie");
        Button save=myView.findViewById(R.id.mv_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieModel newModel=new MovieModel();
                newModel.name=edtname.getText().toString().trim();
                newModel.imglink=edtimg.getText().toString().trim();
                newModel.videolink=edtvideo.getText().toString().trim();
                newModel.series="";
                newModel.category=categoryNames.get(spcategory.getSelectedItemPosition());

                if (model!=null){
                    seriesRef.document(id).set(newModel);
                    Toast.makeText(getContext(),"Edited",Toast.LENGTH_LONG).show();
                }
                else {
                    seriesRef.add(newModel);
                    Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
                }
                edtname.setText("");
                edtimg.setText("");
                edtvideo.setText("");
            }
        });
        Button cancel=myView.findViewById(R.id.mv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtimg.setText("");
                edtname.setText("");
                dismiss();
            }
        });
        Button btnclose=myView.findViewById(R.id.cancelmovie);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return  myView;
    }
}
