package com.example.internshipproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class second_adapter extends RecyclerView.Adapter<second_adapter.ViewHolder> {
    ArrayList<pojo_new> lis;
    Context ct;

    public second_adapter(ArrayList<pojo_new> list, FragmentActivity context) {
        lis = list;
        ct = context;
    }

    @NonNull
    @Override
    public second_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View va=  LayoutInflater.from(ct).inflate(R.layout.activity_anniv_card,parent,false);

        return new ViewHolder(va);
    }

    @Override
    public void onBindViewHolder(@NonNull second_adapter.ViewHolder holder, final int position) {
        Glide.with(ct).load(lis.get(position).getImage()).into(holder.myimage);

        holder.anniv_bride.setText(lis.get(position).getName());
        holder.anniv_groom.setText(lis.get(position).getBride());
        holder.anniv_ph.setText(lis.get(position).getPhone());
        holder.anniv_day.setText(lis.get(position).getDay()+"/");
        holder.anniv_month.setText(lis.get(position).getMonth());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phnum = lis.get(position).getPhone();
                Uri uri = Uri.parse("tel:" + phnum);
                Intent i = new Intent(Intent.ACTION_DIAL,uri);
                ct.startActivity(i);
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = lis.get(position).getName();
                String naame = lis.get(position).getBride();
                String message = "Happy Anniversary Dear "+name+" and "+naame+"." ;
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
        return lis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView anniv_ph,anniv_bride,anniv_day,anniv_month,anniv_groom;
        ImageView myimage,call,message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anniv_ph=itemView.findViewById(R.id.re_num);
            anniv_bride=itemView.findViewById(R.id.re_bride);
            anniv_groom = itemView.findViewById(R.id.re_groom);
            myimage=itemView.findViewById(R.id.anniv_img);
            anniv_day=itemView.findViewById(R.id.re_day);
            anniv_month=itemView.findViewById(R.id.re_month);
            call=itemView.findViewById(R.id.call_a);
            message=itemView.findViewById(R.id.message_a);
        }
    }
}
