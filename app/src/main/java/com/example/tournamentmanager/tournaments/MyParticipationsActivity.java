package com.example.tournamentmanager.tournaments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tournamentmanager.R;

public class MyParticipationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_participations);

        String user = getIntent().getStringExtra("user");

        Bundle args = new Bundle();
        args.putString("participation", user);

        TournamentListFragment tournamentListFragment = new TournamentListFragment();
        tournamentListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_my_participations, tournamentListFragment).commit();
    }
}
