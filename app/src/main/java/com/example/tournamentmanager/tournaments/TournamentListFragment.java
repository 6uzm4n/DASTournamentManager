package com.example.tournamentmanager.tournaments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.R;

import java.util.ArrayList;
import java.util.HashMap;


public class TournamentListFragment extends Fragment {

    private boolean masterFlow = false;
    RecyclerView recyclerView;


    private ArrayList<String> tournamentIds = new ArrayList<>();

    private ArrayList<String> tournamentNames = new ArrayList<>();
    private ArrayList<String> tournamentLocations = new ArrayList<>();
    private ArrayList<String> tournamentDates = new ArrayList<>();

    public TournamentListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("FRAGMENT CREATE VIEW");
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_tournament_list, container, false);

        if (view.findViewById(R.id.fragment_container_tournament) != null) {
            masterFlow = true;
        }

        recyclerView = view.findViewById(R.id.recyclerview_tournaments);

        updateList();

        return view;
    }

    private void updateList(){
        DatabaseManager db = new DatabaseManager(getContext());
        HashMap<String, ArrayList<String>> map;

        Bundle args = getArguments();
        if (args != null && args.getString("creator") != null) {
            map = db.getTournamentsByCreator(args.getString("creator"));
        } else if (args != null && args.getString("participation") != null) {
            map = db.getTournamentsByParticipation(args.getString("participation"));
        } else {
            map = db.getAllTournaments();
        }

        tournamentIds = map.get("id");
        tournamentNames = map.get("name");
        tournamentLocations = map.get("location");
        tournamentDates = map.get("date");


        TournamentRVAdapter adapter = new TournamentRVAdapter(tournamentNames, tournamentLocations, tournamentDates);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //TODO: context?
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickListener(new TournamentRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Bundle args = new Bundle();
                args.putString("id", tournamentIds.get(pos));
                if (masterFlow) {
                    System.out.println("Tablet");
                    TournamentInfoFragment tournamentInfoFragment = new TournamentInfoFragment();
                    tournamentInfoFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tournament, tournamentInfoFragment).commit();
                } else {
                    System.out.println("Movil");
                    Intent i = new Intent(getActivity(), TournamentInfoActivity.class);
                    i.putExtras(args);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ON START");
        System.out.println(getArguments());
        updateList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Aquí se accede a la clase original
        System.out.println("FRAGMENT ACTIVITY CREATED");
    }

    public void newItem(String st) {
        tournamentNames.add(st);
        tournamentLocations.add(st);
    }
}
