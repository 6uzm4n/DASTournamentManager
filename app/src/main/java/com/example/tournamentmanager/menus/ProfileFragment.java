package com.example.tournamentmanager.menus;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tournamentmanager.R;
import com.example.tournamentmanager.tournaments.MyParticipationsActivity;
import com.example.tournamentmanager.tournaments.MyTournamentsActivity;


public class ProfileFragment extends Fragment {

    String user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        user = session.getString("session", null);

        TextView textViewUsername = view.findViewById(R.id.textView_username);
        Button buttonMyTournaments = view.findViewById(R.id.button_my_tournaments);
        Button buttonMyParticipations = view.findViewById(R.id.button_my_participations);

        textViewUsername.setText(user);

        buttonMyTournaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyTournamentsActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        buttonMyParticipations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyParticipationsActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        return view;
    }

}
