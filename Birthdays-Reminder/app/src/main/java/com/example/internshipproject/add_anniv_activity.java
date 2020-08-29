package com.example.internshipproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class add_anniv_activity extends AppCompatActivity {
    Button save;
    EditText date,et_name;
    int myear,mmonth,mday;
    EditText phone,bride;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference reference;
    StorageReference storagereference;
    ImageView img;
    FirebaseUser firebaseUser;
    ArrayList<pojo_new> list;
    ViewGroup layout;
    ImageView im;
    int day,mon,year;

    //Spinner day,month;
    ArrayList<String> li=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anniv_activity);
        list=new ArrayList<>();
        storage=FirebaseStorage.getInstance();
        storagereference= storage.getReference();
        date = findViewById(R.id.annivdate);
        bride = findViewById(R.id.bride);
        img = findViewById(R.id.img_a);
        et_name = findViewById(R.id.groom);
        date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        day  = Integer.parseInt(clean.substring(0,2));
                        mon  = Integer.parseInt(clean.substring(2,4));
                        year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

       /* Toolbar toolbarr = findViewById(R.id.toolbarb);
        setSupportActionBar(toolbarr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        // Inflate the layout for this fragment

        final EditText name=findViewById(R.id.groom);
        database=FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference=database.getReference("anniversaries").child(firebaseUser.getUid());
        final String nam=name.getText().toString();
       /* String days[]={"Please selectct day","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
        String months[]={"Please selectct month","1","2","3","4","5","6","7","8","9","10","11","12"};
        */
        save=findViewById(R.id.savebutton_anniv);
        phone=findViewById(R.id.phnum_anniv);
        //img=findViewById(R.id.image);



        // final Spinner day=findViewById(R.id.day);
        // final Spinner month=(Spinner)findViewById(R.id.month);










        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String mnamea=et_name.getText().toString();
                final String mdaya=Integer.toString(day);
                final String mmontha=Integer.toString(mon);
                final String pha=phone.getText().toString();
                final String brideaa= bride.getText().toString();
                if (mnamea.isEmpty()) {
                    Toast.makeText(add_anniv_activity.this, "Enter a name", Toast.LENGTH_SHORT).show();
                } else if (brideaa.isEmpty()) {
                    Toast.makeText(add_anniv_activity.this, "Enter a name", Toast.LENGTH_SHORT).show();
                } else if (pha.isEmpty()) {
                    Toast.makeText(add_anniv_activity.this, "Enter a Phone Number", Toast.LENGTH_SHORT).show();
                } else if (mdaya.equals("0")) {
                    Toast.makeText(add_anniv_activity.this, "Enter Proper Date", Toast.LENGTH_SHORT).show();
                }  else {
                    storagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String imageurii = uri.toString();
                            pojo_new pj = new pojo_new(mnamea, mdaya, mmontha, pha, imageurii, brideaa);
                            reference.push().setValue(pj);
                        }
                    });
                    Intent i = new Intent(add_anniv_activity.this, MainActivity.class);
                    startActivity(i);

                }

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                pojo_new pojo = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    pojo = dataSnapshot.getValue(pojo_new.class);
                    list.add(pojo);

                }



                date();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











    }
    public void gallery2(View view) {

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,2);

    }
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try{
            String phoneno = null;
            String phoneName = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null,null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int phname = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneno = cursor.getString(phoneIndex);
            phoneName = cursor.getString(phname);
            phone.setText(phoneno);
            bride.setText(phoneName);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void contacts_a(View view) {


        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(in, 33);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                Uri u=data.getData();
                img.setImageURI(u);
                saveimg(u);
            }
        } if(requestCode==33){
            if(resultCode==RESULT_OK){
                contactPicked(data);
            }
        }
    }

    private void saveimg(Uri u) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("File Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();
        storagereference=storagereference.child("Aniv_Images/"+ UUID.randomUUID().toString());
        storagereference.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(add_anniv_activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_anniv_activity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double d = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress((int)d);
            }
        });



    }


    private void date() {

        int i;

        Calendar c=Calendar.getInstance();
        myear=c.get(Calendar.YEAR);
        mmonth=c.get(Calendar.MONTH)+1;
        mday=c.get(Calendar.DATE);


        DatePickerDialog da=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


            }
        },myear,mmonth,mday);
        li.add("mnnd123");

        for(i=0;i<list.size();i++){
            if(mday==Integer.parseInt(list.get(i).getDay()) && mmonth==Integer.parseInt(list.get(i).getMonth()) ){

                if(li.contains(list.get(i).getName())){
                    //Toast.makeText(this, "repeated--"+list.get(i).getName(), Toast.LENGTH_SHORT).show();

                }
                else{
                    NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        NotificationChannel notificationChannel=new NotificationChannel("manu","manohar",NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setLockscreenVisibility(1);
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"manu");
                    builder.setSmallIcon(R.drawable.s);
                    Intent in = new Intent(this,MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1050,in, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setSmallIcon(R.drawable.birthday);
                    builder.setContentTitle("Anniversary reminder");
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setContentText(list.get(i).getBride()+" and "+list.get(i).getName()+" are celebrating their anniversary today");
                    notificationManager.notify(i,builder.build());
                    li.add(list.get(i).getName());




                }



            }



        }










    }


    // TODO: Rename method, update argument and hook method into UI event



}

