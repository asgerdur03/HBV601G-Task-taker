package com.example.hbv601G;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.view.View;

import com.example.hbv601G.data.DummyGognVinnsla;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hbv601G.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DummyGognVinnsla.loadData(this); // TEMP skjal pre api, losa umbreyta þegar all good?


        DummyGognVinnsla.loadData(getApplicationContext()); // hlaða inn dummy

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setja á navbar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_category, R.id.navigation_settings, R.id.navigation_archived)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_new_task) {
                binding.navView.setVisibility(View.GONE); // ef new task ting, fela nav
            } else {
                binding.navView.setVisibility(View.VISIBLE); // almenna syna nav
            }
        });


        // nytt task popup
        FloatingActionButton fab = findViewById(R.id.new_task);
        fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
            popupMenu.getMenu().add("Create a new task");

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Create a new task")) {
                    navController.navigate(R.id.navigation_new_task);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });


               /* {
            navController.navigate(R.id.navigation_new_task, null, new NavOptions.Builder()
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .build());

        }*/








    }

    // back arrow virkni ?
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}



