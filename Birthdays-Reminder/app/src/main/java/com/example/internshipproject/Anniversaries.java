package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Anniversaries extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
ProgressDialog pd;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Anniversaries() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Anniversaries newInstance(String param1, String param2) {
        Anniversaries fragment = new Anniversaries();
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
        final View v=inflater.inflate(R.layout.fragment_anniversaries, container, false);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
        DatabaseReference reference=database.getReference("anniversaries").child(firebaseUser.getUid());
        final ArrayList<pojo_new> list=new ArrayList<>();
        final RecyclerView recyclerView;
        recyclerView=v.findViewById(R.id.recycler_anniv);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list.clear();
                    pojo_new pojo = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        pojo = dataSnapshot.getValue(pojo_new.class);
                        list.add(pojo);
                        second_adapter adapter = new second_adapter(list, (FragmentActivity) getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        pd.dismiss();
                    }
                }else{
                    pd.dismiss();
                    //Toast.makeText(getContext(), "No entries", Toast.LENGTH_SHORT).show();
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

        floatingActionButton=v.findViewById(R.id.fab3);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),add_anniv_activity.class);
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
