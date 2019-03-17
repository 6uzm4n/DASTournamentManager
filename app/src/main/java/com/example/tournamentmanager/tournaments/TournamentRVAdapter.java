package com.example.tournamentmanager.tournaments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tournamentmanager.R;

import java.util.ArrayList;

public class TournamentRVAdapter extends RecyclerView.Adapter<TournamentRVAdapter.TournamentViewHolder> {

    private ArrayList<String> tournamentNames;
    private ArrayList<String> tournamentLocations;
    private ArrayList<String> tournamentDates;

    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public TournamentRVAdapter(ArrayList<String> names, ArrayList<String> locations, ArrayList<String> dates) {
        tournamentNames = names;
        tournamentLocations = locations;
        tournamentDates = dates;
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
        tournamentViewHolder.tournamentName.setText(tournamentNames.get(i));
        tournamentViewHolder.tournamentLocation.setText(tournamentLocations.get(i));
        tournamentViewHolder.tournamentDate.setText(tournamentDates.get(i));
    }


    @Override
    public int getItemCount() {
        return tournamentNames.size();
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
