package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.practica.ismael.examen_ismaelraqi_pmdmo.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private MainViewModel mainViewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupConf();
    }

    private void setupConf() {
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mainViewModel.setUndoDelete(preferences.getBoolean(getString(R.string.switchKey), getResources().getBoolean(R.bool.switchDef)));
        mainViewModel.setShowConfirm(preferences.getBoolean(getString(R.string.checkboxKey), getResources().getBoolean(R.bool.checkDef)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
