package com.example.tournamentmanager.tournaments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tournamentmanager.R;

public class TournamentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info);

        Bundle args = getIntent().getExtras();

        TournamentInfoFragment tournamentInfoFragment = new TournamentInfoFragment();
        tournamentInfoFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tournament_info, tournamentInfoFragment).commit();
    }
}
