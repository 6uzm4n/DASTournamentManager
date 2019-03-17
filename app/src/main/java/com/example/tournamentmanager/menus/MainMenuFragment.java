package com.example.tournamentmanager.menus;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tournamentmanager.R;
import com.example.tournamentmanager.tournaments.CreateTournamentActivity;
import com.example.tournamentmanager.tournaments.TournamentListFragment;


public class MainMenuFragment extends Fragment {


    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button buttonCreate = view.findViewById(R.id.button_create_tournament);
        Button buttonProfile = view.findViewById(R.id.button_profile);
        Button buttonTournaments = view.findViewById(R.id.button_tournaments);
        Button buttonExit = view.findViewById(R.id.button_exit);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateTournamentActivity.class);
                startActivity(i);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setMainFragment(new ProfileFragment());
                activity.setCheckedItem(MainActivity.PROFILE_INDEX);
            }
        });

        buttonTournaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setMainFragment(new TournamentListFragment());
                activity.setCheckedItem(MainActivity.TOURNAMENTS_INDEX);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
