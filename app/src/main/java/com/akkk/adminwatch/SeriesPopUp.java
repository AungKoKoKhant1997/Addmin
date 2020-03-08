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

public class SeriesPopUp extends DialogFragment {
    SeriesModel model;
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.seriespopup, container, false);
        final EditText editname = myView.findViewById(R.id.serie_name);
        final EditText edtimg = myView.findViewById(R.id.serie_img);
        final EditText edtvideo = myView.findViewById(R.id.serie_video);
        final Spinner spcategory = myView.findViewById(R.id.serie_spinner);
        if (model!=null){
            editname.setText(model.name);
            edtvideo.setText(model.videolink);
            edtimg.setText(model.imglink);

        }
        final ArrayList<String> categoryNames = new ArrayList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("categories");
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
        final CollectionReference seriesRef=db.collection("series");
        Button save=myView.findViewById(R.id.se_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SeriesModel newModel=new SeriesModel();
              newModel.name=editname.getText().toString().trim();
              newModel.imglink=edtimg.getText().toString().trim();
              newModel.videolink=edtvideo.getText().toString().trim();
              newModel.category=categoryNames.get(spcategory.getSelectedItemPosition());
                if (model!=null){
                    seriesRef.document(id).set(newModel);
                    Toast.makeText(getContext(),"Edited",Toast.LENGTH_LONG).show();
                }
                else {
                    seriesRef.add(newModel);
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();

                    edtimg.setText("");
                    editname.setText("");
                    edtvideo.setText("");
                }}
        });
        Button cancel=myView.findViewById(R.id.se_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtimg.setText("");
                editname.setText("");
                edtvideo.setText("");
                dismiss();
            }
        });
        Button btnclose=myView.findViewById(R.id.cancelserie);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return myView;
    }
}
