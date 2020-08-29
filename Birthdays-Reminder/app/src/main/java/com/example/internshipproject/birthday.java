package com.example.internshipproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;


public class birthday extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog pd;

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;



    private OnFragmentInteractionListener mListener;

    public birthday() {
        // Required empty public constructor
    }


    public static birthday newInstance(String param1, String param2) {
        birthday fragment = new birthday();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         pd = new ProgressDialog(getContext());
        pd.setMessage("Getting Info...");
        pd.setProgress(0);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setMax(100);
        pd.setCancelable(false);
        pd.show();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        final View v=inflater.inflate(R.layout.fragment_birthday, container, false);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=database.getReference("birthday").child(firebaseUser.getUid());
      //  Query query = reference.child("birthdays").orderByChild("myname").
        final ArrayList<Pojo> list=new ArrayList<>();
        final RecyclerView recyclerView;
        recyclerView=v.findViewById(R.id.recycler);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list.clear();

                    Pojo pojo = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        pojo = dataSnapshot.getValue(Pojo.class);
                        list.add(pojo);

                        Myadapter adapter = new Myadapter(list, (FragmentActivity) getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        pd.dismiss();
                    }
                }else{
                    pd.dismiss();
                    //Toast.makeText(getContext(), "No entries", Toast.LENGTH_SHORT).show();
                }











            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (pd != null) {
                    pd.dismiss();
                }
            }
        });
        pd.show();




        FloatingActionButton floatingActionButton;

        floatingActionButton=v.findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),birthdayaddingitems.class);
                startActivity(intent);
            }
        });










        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
