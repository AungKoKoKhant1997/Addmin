package com.akkk.adminwatch;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {
    ArrayList<String> ids=new ArrayList<String>();
    ListView list;





    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_series, container, false);
        FloatingActionButton add=myView.findViewById(R.id.buadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesPopUp Popup = new SeriesPopUp();
                Popup.show(getFragmentManager(), "show series");
            }
        });
         list=myView.findViewById(R.id.serieslist);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("series");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               ids.clear();
                ArrayList<SeriesModel> myModels=new ArrayList<>();
                for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                    myModels.add(snapshot.toObject(SeriesModel.class));
                    ids.add(snapshot.getId());
                }
                SeriesAdapter adapter=new SeriesAdapter(myModels);
                list.setAdapter(adapter);
            }
        });





        return myView;
    }
    private class SeriesAdapter extends BaseAdapter{
        ArrayList<SeriesModel> seriesModels=new ArrayList<>();

        public SeriesAdapter(ArrayList<SeriesModel> seriesModels) {
            this.seriesModels = seriesModels;
        }

        @Override
        public int getCount() {
            return seriesModels.size();
        }

        @Override
        public Object getItem(int position) {
            return seriesModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View myView=getLayoutInflater().inflate(R.layout.serieslayout,null);
            final SeriesModel temp=seriesModels.get(position);
            TextView sr=myView.findViewById(R.id.sr);
            final TextView name=myView.findViewById(R.id.name);
            TextView category=myView.findViewById(R.id.category);
            TextView series=myView.findViewById(R.id.series);
            final ImageView myimage=myView.findViewById(R.id.se_image);
            sr.setText(position+1+"");
            name.setText(temp.name);
            category.setText(temp.category);
            series.setVisibility(View.GONE);
            Glide.with(getContext())
                    .load(temp.imglink)
                    .into(myimage);
            myimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pmenu = new PopupMenu(getContext(), myimage);
                    MenuInflater inf = pmenu.getMenuInflater();
                    inf.inflate(R.menu.popmenu, pmenu.getMenu());
                    pmenu.show();
                    pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.edit_menu) {
                                SeriesPopUp popUp = new SeriesPopUp();
                                popUp.model = temp;
                                popUp.id = ids.get(position);
                                popUp.show(getFragmentManager(), "Edit");


                            }
                            if (item.getItemId() == R.id.delete_menu) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference ref = db.collection("series");
                                ref.document(ids.get(position)).delete();
                                ref.get().addOnSuccessListener(
                                        new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                ids.clear();
                                                ArrayList<SeriesModel> cm = new ArrayList<SeriesModel>();
                                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                    cm.add(snapshot.toObject(SeriesModel.class));
                                                    ids.add(snapshot.getId());

                                                }
                                                SeriesAdapter adapter = new SeriesAdapter(cm);
                                                list.setAdapter(adapter);
                                            }

                            });

                            }
                            return true;
                        }
                    });
            }
        });

                    return myView;
                }
            }
        }


