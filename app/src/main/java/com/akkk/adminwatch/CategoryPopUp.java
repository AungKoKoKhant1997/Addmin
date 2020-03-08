package com.akkk.adminwatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CategoryPopUp extends DialogFragment {
    CategoryModel model;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.categorypopup, container, false);
        Button save=myView.findViewById(R.id.save);
        final EditText name=myView.findViewById(R.id.categories_name);
        if (model!=null){
            name.setText(model.categoryName);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                }
                else {
                    CategoryModel tempmodel=new CategoryModel();
                    tempmodel.categoryName=name.getText().toString().trim();
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    CollectionReference ref=db.collection("categories");
                    if (model!=null) {
                        ref.document(id).set(tempmodel);
                        Toast.makeText(getContext(),"Edited",Toast.LENGTH_LONG).show();
                    }
                    else {
                        ref.add(tempmodel);
                        Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                    name.setText("");
                    dismiss();
                }


            }
        });
        Button close=myView.findViewById(R.id.cancelcategorypopup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return  myView;
    }
}
