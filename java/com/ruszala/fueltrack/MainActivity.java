package com.ruszala.fueltrack;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.ruszala.fueltrack.auth.login.LoginActivity;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.interfaces.OnNavFragmentInteractionListener;
import com.ruszala.fueltrack.service.TimingService;

public class MainActivity extends AppCompatActivity implements OnNavFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    LifecycleOwner owner = this;

    //set observer for global shift to control service
    private final ServiceConnection mconnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final TimingService ref = ((TimingService.LocalBinder) service).getService();
            CurrentShift.getInstance().getShiftLifeData().observe(owner, new Observer<Shift>() {
                @Override
                public void onChanged(Shift shift) {
                    if (shift != null) {
                        ref.startTimer(shift.getStartDate());
                    } else {
                        ref.stopByNotification();
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //draver
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        //init global shift
        CurrentShift.getInstance();

        //start timer service
        Intent service = new Intent(this, TimingService.class);
        startService(service);
        bindService(service, mconnect, BIND_AUTO_CREATE);

        // Passing  menu ID as a set
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.orderMapPreviewFragment, R.id.startShiftFragment, R.id.orderHomeFragment)
                .setDrawerLayout(drawer)
                .build();
        //Nav settings
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //set Draver email to current user
        View headerView = navigationView.getHeaderView(0);
        TextView email = (TextView) headerView.findViewById(R.id.emailIn);
        String email1 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.setText(email1);

    }

    //method change navigation bar title
    public void changeTitle(String title){
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //bind menu items to actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                CurrentShift.getInstance().clean();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
                
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void changeFragment(int id) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(id);
    }

    @Override
    public void changeFragment(int id, Bundle args) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(id, args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //unbind service
        if (mconnect != null) {
            unbindService(mconnect);
        }
    }




}
