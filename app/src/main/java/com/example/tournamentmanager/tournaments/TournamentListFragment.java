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
import com.example.tournamentmanager.ServerDB;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;


public class TournamentListFragment extends Fragment {

    private boolean masterFlow = false;
    RecyclerView recyclerView;

    public TournamentListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_tournament_list, container, false);

        if (view.findViewById(R.id.fragment_container_tournament) != null) {
            masterFlow = true;
        }

        recyclerView = view.findViewById(R.id.recyclerview_tournaments);

        //updateList();

        return view;
    }

    private void updateList() {
//        DatabaseManager db = new DatabaseManager(getContext());

        Bundle args = getArguments();
        ServerDB serverDB = new ServerDB(getContext());
        if (args != null && args.getString("creator") != null) {
            serverDB.getTournamentsByCreator(args.getString("creator"));
        } else if (args != null && args.getString("participation") != null) {
            serverDB.getTournamentsByParticipation(args.getString("participation"));
        } else {
            serverDB.getAllTournaments();
        }

        String result = null;
        try {
            result = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result == null) {
            serverDB.notifyError(getActivity().getWindow().getDecorView().getRootView());
            return;
        }


        JSONArray tournaments = null;
        try {
            tournaments = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TournamentRVAdapter adapter = new TournamentRVAdapter(tournaments);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); //TODO: context?
        recyclerView.setLayoutManager(linearLayoutManager);

        final JSONArray tournamentsFinal = tournaments;

        adapter.setOnItemClickListener(new TournamentRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Bundle args = new Bundle();
                try {
                    args.putString("id", tournamentsFinal.getJSONObject(pos).getString(DatabaseManager.COLUMN_TOURNAMENT_ID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (masterFlow) {
                    TournamentInfoFragment tournamentInfoFragment = new TournamentInfoFragment();
                    tournamentInfoFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tournament, tournamentInfoFragment).commit();
                } else {
                    Intent i = new Intent(getActivity(), TournamentInfoActivity.class);
                    i.putExtras(args);
                    startActivity(i);
                }
            }
        });


//        Bundle args = getArguments();
//        if (args != null && args.getString("creator") != null) {
//            tournaments = db.getTournamentsByCreator(args.getString("creator"));
//        } else if (args != null && args.getString("participation") != null) {
//            tournaments = db.getTournamentsByParticipation(args.getString("participation"));
//        } else {
//            tournaments = db.getAllTournaments();
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateList();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Aquí se accede a la clase original
    }

}
