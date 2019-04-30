package com.example.tournamentmanager.menus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tournamentmanager.dialogs.ExitDialog;
import com.example.tournamentmanager.R;
import com.example.tournamentmanager.tournaments.TournamentListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ExitDialog.ExitDialogListener {

    private DrawerLayout drawer;
    NavigationView navigationView;
    private String user;

    public static final int MAIN_MENU_INDEX = 0;
    public static final int PROFILE_INDEX = 1;
    public static final int TOURNAMENTS_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNavigationDrawer(savedInstanceState);

    }

    private void initializeNavigationDrawer(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();
            setCheckedItem(MAIN_MENU_INDEX);
        }


        View headerView = navigationView.getHeaderView(0);
        SharedPreferences session = getSharedPreferences("session", 0);
        user = session.getString("session", null);
        TextView textViewUsername = headerView.findViewById(R.id.textView_username);
        textViewUsername.setText(user);


        Menu menu = navigationView.getMenu();
        SwitchCompat switchTheme = MenuItemCompat.getActionView(menu.findItem(R.id.drawer_theme_switch)).findViewById(R.id.theme_switch);
        switchTheme.setChecked(getSharedPreferences("settings", MODE_PRIVATE).getString("theme", "light").equals("dark"));
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    settings.edit().putString("theme", "dark").commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    settings.edit().putString("theme", "light").commit();
                }
                recreate();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem selected = navigationView.getCheckedItem();
        if (!selected.equals(menuItem)) {
            switch (menuItem.getItemId()) {
                case R.id.drawer_home_menu:
                    setMainFragment(new MainMenuFragment());
                    break;
                case R.id.drawer_profile:
                    setMainFragment(new ProfileFragment());
                    break;
                case R.id.drawer_tournaments:
                    setMainFragment(new TournamentListFragment());
                    break;
                case R.id.drawer_settings:
                    Toast.makeText(this, "IMPLEMENTADO EN LA 2 ENTREGA", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_theme_switch:
                    // No queremos que esta opción haga nada
                    return true;
                case R.id.drawer_logout:
                    // Se elimina la sesión almacenada
                    SharedPreferences session = getSharedPreferences("session", 0);
                    SharedPreferences.Editor editor = session.edit();
                    editor.remove("session");
                    editor.commit();

                    // Se vuelve a la pantalla de login
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setMainFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment).commit();
    }

    public void setCheckedItem(int checkedItem) {
        switch (checkedItem){
            case -1:
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                break;
            case MAIN_MENU_INDEX:
                navigationView.setCheckedItem(R.id.drawer_home_menu);
                break;
            case PROFILE_INDEX:
                navigationView.setCheckedItem(R.id.drawer_profile);
                break;
            case TOURNAMENTS_INDEX:
                navigationView.setCheckedItem(R.id.drawer_tournaments);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            NavigationView navigationView = findViewById(R.id.nav_view);
            MenuItem selected = navigationView.getCheckedItem();
            if (selected == null || selected.getItemId() != R.id.drawer_home_menu) {
                backToHome();
            } else {
                ExitDialog dialog = new ExitDialog();
                dialog.show(getSupportFragmentManager(), "exit");
            }
        }
    }

    public void backToHome() {
        setMainFragment(new MainMenuFragment());
        setCheckedItem(MAIN_MENU_INDEX);
    }

    @Override
    public void pressExit() {
        finish();
    }
}
