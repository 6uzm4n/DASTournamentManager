package com.example.tournamentmanager.tournaments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.dialogs.DeleteDialog;
import com.example.tournamentmanager.R;
import com.example.tournamentmanager.menus.MainActivity;

import java.util.ArrayList;


public class TournamentInfoFragment extends Fragment implements DeleteDialog.DeleteDialogListener {

    private String tournamentId;
    private String user;

    public TournamentInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_tournament_info, container, false);

        Bundle args = getArguments();
        tournamentId = args.getString("id");

        DatabaseManager db = new DatabaseManager(getContext());
        ArrayList<String> tournamentInfo = db.getTournamentById(tournamentId);

        TextView textViewName = view.findViewById(R.id.textView_tournament_name);
        TextView textViewGame = view.findViewById(R.id.textView_tournament_game);
        TextView textViewDescription = view.findViewById(R.id.textView_tournament_description);
        TextView textViewDate = view.findViewById(R.id.textView_tournament_date);
        TextView textViewLocation = view.findViewById(R.id.textView_tournament_location);
        Button buttonRegister = view.findViewById(R.id.button_tournament_participation);
        Button buttonDelete = view.findViewById(R.id.button_tournament_delete);
        Button buttonContact = view.findViewById(R.id.button_tournament_contact);

        textViewName.setText(tournamentInfo.get(1));
        textViewGame.setText(tournamentInfo.get(2));
        textViewDescription.setText(tournamentInfo.get(3));
        textViewDate.setText(Html.fromHtml(String.format("<b>%s:</b> %s", view.getResources().getString(R.string.tournament_date), tournamentInfo.get(4))));
        textViewLocation.setText(Html.fromHtml(String.format("<b>%s:</b> %s", view.getResources().getString(R.string.tournament_location), tournamentInfo.get(5))));

        // Accedemos al usuario activo de la aplicación
        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        user = session.getString("session", null);

        // Si el usuario está apuntado al torneo, aparecerá un botón para desapuntarse
        // Si el usuario no está apuntado al torneo, aparecerá un botón para apuntarse
        if (user != null) {
            if (db.checkUserParticipation(user, tournamentId)) {
                buttonRegister.setText(view.getResources().getString(R.string.tournament_leave));
                buttonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteParticipation();
                    }
                });
            } else {
                buttonRegister.setText(view.getResources().getString(R.string.tournament_join));
                buttonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addParticipation();
                    }
                });
            }
        }

        // Si el usuario es el creador del torneo, aparecerá un botón para eliminar el torneo.
        // Si el usuario no es el creador del torneo, aparecerá un botón para contactar con este.
        if (tournamentInfo.get(6).equals(user)) {
            buttonDelete.setVisibility(View.VISIBLE);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDialog dialog = new DeleteDialog();
                    dialog.setTargetFragment(TournamentInfoFragment.this, 0);
                    dialog.show(getActivity().getSupportFragmentManager(), "exit");
                }
            });
        }else{
            buttonContact.setVisibility(View.VISIBLE);
            buttonContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseManager db = new DatabaseManager(getContext());
                    String mail = db.getMailByUser(user);
                    Intent i = new  Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
                    startActivity(i);
                }
            });
        }

        db.close();
        return view;
    }

    private boolean addParticipation() {
        DatabaseManager db = new DatabaseManager(getContext());
        boolean result = db.addParticipation(user, tournamentId);
        db.close();
        if (result) {
            // Recargamos el fragment para aplicar los cambios
            getFragmentManager().beginTransaction().detach(TournamentInfoFragment.this).attach(TournamentInfoFragment.this).commit();
        }
        return result;
    }

    private boolean deleteParticipation() {
        DatabaseManager db = new DatabaseManager(getContext());
        boolean result = db.deleteParticipation(user, tournamentId);
        db.close();
        if (result) {
            // Recargamos el fragment para aplicar los cambios
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        return result;
    }

    private boolean deleteTournament() {
        DatabaseManager db = new DatabaseManager(getContext());
        boolean result = db.deleteTournament(tournamentId);
        db.close();
        return result;
    }

    @Override
    public void pressDelete() {
        System.out.println("PARENT FRAGMENT: ");
        System.out.println(getParentFragment());
        FragmentActivity activity = getActivity();
        // Se elimina el torneo de la base de datos
        if (deleteTournament()) {
            // Eliminamos esta instancia de fragment
            activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if (activity instanceof TournamentInfoActivity) {
                // En caso de estar en una actividad propia, finalizamos la actividad que mostraba la información
                activity.finish();
            } else if (activity instanceof MainActivity) {
                // En caso de estar contenido en un fragment, sólo es necesario actualizar su lista de torneos
                System.out.println(activity);
                TournamentListFragment tournamentListFragment = (TournamentListFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                activity.getSupportFragmentManager().beginTransaction().detach(tournamentListFragment).attach(tournamentListFragment).commit();
            }

        }
    }

}
