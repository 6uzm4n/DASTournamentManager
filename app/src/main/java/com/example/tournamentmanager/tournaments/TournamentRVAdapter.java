package com.example.tournamentmanager.tournaments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TournamentRVAdapter extends RecyclerView.Adapter<TournamentRVAdapter.TournamentViewHolder> {

    private JSONArray tournaments;

    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public TournamentRVAdapter(JSONArray pTournaments) {
        tournaments = pTournaments;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutInflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tournament_cardview, viewGroup, false);
        TournamentViewHolder viewHolder = new TournamentViewHolder(layoutInflater, myListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder tournamentViewHolder, int i) {
        try {
            tournamentViewHolder.tournamentName.setText(tournaments.getJSONObject(i).getString(DatabaseManager.COLUMN_TOURNAMENT_NAME));
            tournamentViewHolder.tournamentLocation.setText(tournaments.getJSONObject(i).getString(DatabaseManager.COLUMN_TOURNAMENT_LOCATION));
            tournamentViewHolder.tournamentDate.setText(tournaments.getJSONObject(i).getString(DatabaseManager.COLUMN_TOURNAMENT_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return tournaments.length();
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {
        public TextView tournamentName;
        public TextView tournamentLocation;
        public TextView tournamentDate;

        public TournamentViewHolder(View view, final OnItemClickListener listener) {
            super(view);

            tournamentName = view.findViewById(R.id.textview_tournament_name);
            tournamentLocation = view.findViewById(R.id.textview_tournament_location);
            tournamentDate = view.findViewById(R.id.textView_tournament_date);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
