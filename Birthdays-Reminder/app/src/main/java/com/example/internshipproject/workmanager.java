package com.example.internshipproject;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class workmanager extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseDatabase database;
        DatabaseReference reference;
        final ArrayList<Pojo> list;
        list=new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("birthday").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Pojo pojo = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    pojo = dataSnapshot.getValue(Pojo.class);
                    list.add(pojo);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        int i,mmonth,myear,mday;
       // Context context = getApplicationContext();

        Calendar c=Calendar.getInstance();
        myear=c.get(Calendar.YEAR);
        mmonth=c.get(Calendar.MONTH)+1;
        mday=c.get(Calendar.DATE);
        Log.d("abhi","Name");





        for(i=0; i< list.size(); i++){
            if(mday==Integer.parseInt(list.get(i).getMyday()) && mmonth==Integer.parseInt(list.get(i).getMymonth()) ){


                NotificationManagerCompat manager = NotificationManagerCompat.from(context);

                // manager.notify(23, Uri.Builder.build);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    NotificationChannel notificationChannel=new NotificationChannel("manu","manohar",NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.setLockscreenVisibility(1);
                    notificationChannel.enableVibration(true);
                    manager.createNotificationChannel(notificationChannel);
                }
                NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"manu");
                builder.setSmallIcon(R.drawable.s);
                builder.setContentTitle("Birthday reminder");
                builder.setContentText(list.get(i).getMyname()+"is celebrating his birthday today");
                manager.notify(i,builder.build());





            }



        }





      //  return Result.success();
    }
}

