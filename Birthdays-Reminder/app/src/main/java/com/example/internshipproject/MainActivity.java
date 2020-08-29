package com.example.internshipproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager pager;
    TabLayout tab;
    Toolbar bar;
    DrawerLayout layout;
    NavigationView navig;
    FragmentManager manager;
    FragmentTransaction transaction;
    Pojo pj = new Pojo();
PeriodicWorkRequest periodicWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager=findViewById(R.id.viewpager);
        tab=findViewById(R.id.tablayout);
        navig=findViewById(R.id.navigation);
        layout=findViewById(R.id.drawer);
setwork();


        bar=findViewById(R.id.toolbar);
        pager.setAdapter(new Adapter (getSupportFragmentManager(),Adapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tab.getTabCount()));
        tab.setupWithViewPager(pager);
        setSupportActionBar(bar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,layout,bar,0,0);
        toggle.syncState();
        layout.addDrawerListener(toggle);
        navig.setNavigationItemSelectedListener(this);




    }

    private void setwork() {
        //periodicWorkRequest = new PeriodicWorkRequest.Builder(workmanager.class, 12, TimeUnit.HOURS).build();
        //WorkManager.getInstance().enqueue(periodicWorkRequest);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,01);

        Intent i = new Intent(MainActivity.this,WorkManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,i,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent );

       // startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.appmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    class Adapter extends FragmentPagerAdapter {
        public Adapter(@NonNull FragmentManager fm, int behaviorResumeOnlyCurrentFragment, int tabCount) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new birthday() ;
                case 1:
                    return new Anniversaries() ;
                case 2:
                    return new others() ;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "Birthdays" ;
                case 1:
                    return " Anniversaries" ;
                case 2:
                    return  "others" ;

            }
            return super.getPageTitle(position);


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        switch(item.getItemId()){
            case R.id.settings:
                Intent inten =new Intent(this,settings.class);
                startActivity(inten);
                break;



        }
        return false;
    }


}
