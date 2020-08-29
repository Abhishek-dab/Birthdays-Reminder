package com.example.internshipproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.Viewholder> {
    ArrayList<Pojo> li;
    Context ct;
    public Myadapter(ArrayList<Pojo> list, FragmentActivity activity) {
        li=list;
        ct=activity;
    }

    @NonNull
    @Override
    public Myadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_example,parent,false);

        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myadapter.Viewholder holder, final int position) {

        Glide.with(ct).load(li.get(position).getMyuri()).into(holder.myim);

        holder.myna.setText(li.get(position).getMyname());
        holder.myph.setText(li.get(position).getMyphone());
        holder.mday.setText(li.get(position).getMyday()+"/");
        holder.month.setText(li.get(position).getMymonth());
       /* holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 DatabaseReference reference = FirebaseDatabase.getInstance().getReference("birthday");
                reference.child(li.get(position).myuri).removeValue();
            }
        });*/
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phnum = li.get(position).getMyphone();
                Uri uri = Uri.parse("tel:" + phnum);
                Intent i = new Intent(Intent.ACTION_DIAL,uri);
                ct.startActivity(i);
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = li.get(position).getMyname();
                String message = "Happy Birthday Dear "+name+". ";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,message);
                intent.setType("text/plain");
                ct.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return li.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView myph,myna,mday,month;
        ImageView myim,call,message,del;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            myph=itemView.findViewById(R.id.exnumber);
            myna=itemView.findViewById(R.id.exname);
            myim=itemView.findViewById(R.id.eximg);
            mday=itemView.findViewById(R.id.exday);
            month=itemView.findViewById(R.id.exmonth);
            call=itemView.findViewById(R.id.call);
            message=itemView.findViewById(R.id.message);
            //del =itemView.findViewById(R.id.del);

        }
    }
}

/*
    public void message(View view) {
        String name = pj.getMyname();
        String message = "Happy Birthday Dear "+name+". ";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("text/plain");
        startActivity(intent);
    }

    public void call(View view) {
        String phnum = pj.getMyphone();
        Uri uri = Uri.parse("tel:" + phnum);
        Intent i = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(i);
    }
*/

