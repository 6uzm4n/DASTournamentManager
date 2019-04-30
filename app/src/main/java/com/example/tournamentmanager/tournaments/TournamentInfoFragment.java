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
import com.example.tournamentmanager.ServerDB;
import com.example.tournamentmanager.dialogs.DeleteDialog;
import com.example.tournamentmanager.R;
import com.example.tournamentmanager.menus.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


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


        ServerDB serverDB = new ServerDB(getContext());
        serverDB.getTournamentById(tournamentId);

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
            return view;
        }

        JSONObject tournamentInfo = null;
        try {
            tournamentInfo = new JSONArray(result).getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView textViewName = view.findViewById(R.id.textView_tournament_name);
        TextView textViewGame = view.findViewById(R.id.textView_tournament_game);
        TextView textViewDescription = view.findViewById(R.id.textView_tournament_description);
        TextView textViewDate = view.findViewById(R.id.textView_tournament_date);
        TextView textViewLocation = view.findViewById(R.id.textView_tournament_location);
        Button buttonRegister = view.findViewById(R.id.button_tournament_participation);
        Button buttonDelete = view.findViewById(R.id.button_tournament_delete);
        Button buttonContact = view.findViewById(R.id.button_tournament_contact);

        try {
            textViewName.setText(tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_NAME));
            textViewGame.setText(tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_GAME));
            textViewDescription.setText(tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_DESCRIPTION));
            textViewDate.setText(Html.fromHtml(String.format("<b>%s:</b> %s", view.getResources().getString(R.string.tournament_date), tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_DATE))));
            textViewLocation.setText(Html.fromHtml(String.format("<b>%s:</b> %s", view.getResources().getString(R.string.tournament_location), tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_LOCATION))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Accedemos al usuario activo de la aplicación
        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        user = session.getString("session", null);

        // Si el usuario está apuntado al torneo, aparecerá un botón para desapuntarse
        // Si el usuario no está apuntado al torneo, aparecerá un botón para apuntarse
        if (user != null) {
            serverDB = new ServerDB(getContext());
            result = null;
            serverDB.checkParticipation(user, tournamentId);
            try {
                result = serverDB.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (result != null) {
                if (result.equals("true")) {
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
            } else {
                serverDB.notifyError(getView());
            }
        }

        // Si el usuario es el creador del torneo, aparecerá un botón para eliminar el torneo.
        // Si el usuario no es el creador del torneo, aparecerá un botón para contactar con este.
        try {
            final String creator = tournamentInfo.getString(DatabaseManager.COLUMN_TOURNAMENT_CREATOR);
            if (creator.equals(user)) {
                buttonDelete.setVisibility(View.VISIBLE);
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteDialog dialog = new DeleteDialog();
                        dialog.setTargetFragment(TournamentInfoFragment.this, 0);
                        dialog.show(getActivity().getSupportFragmentManager(), "exit");
                    }
                });
            } else {
                buttonContact.setVisibility(View.VISIBLE);
                buttonContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        DatabaseManager db = new DatabaseManager(getContext());
//                        String mail = db.getMailByUser(user);

                        ServerDB server = new ServerDB(getContext());
                        server.getMailByUser(creator);
                        String mail = null;
                        try {
                            mail = server.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mail !=null){
                            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
                            startActivity(i);
                        }else {
                            server.notifyError(getView());
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private boolean addParticipation() {
        ServerDB serverDB = new ServerDB(getContext());
        serverDB.addParticipation(user, tournamentId);
        String result = null;
        try {
            result = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null && result.equals("true")) {
            // Recargamos el fragment para aplicar los cambios
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            return true;
        }
        serverDB.notifyError(getView());
        return false;
    }

    private boolean deleteParticipation() {
        ServerDB serverDB = new ServerDB(getContext());
        serverDB.deleteParticipation(user, tournamentId);
        String result = null;
        try {
            result = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null && result.equals("true")) {
            // Recargamos el fragment para aplicar los cambios
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            return true;
        }
        serverDB.notifyError(getView());
        return false;
    }

    private boolean deleteTournament() {
        ServerDB serverDB = new ServerDB(getContext());
        serverDB.deleteTournament(tournamentId);
        String result = null;
        try {
            result = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null && result.equals("true")) {
            return true;
        }
        serverDB.notifyError(getView());
        return false;
    }

    @Override
    public void pressDelete() {
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
                TournamentListFragment tournamentListFragment = (TournamentListFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                activity.getSupportFragmentManager().beginTransaction().detach(tournamentListFragment).attach(tournamentListFragment).commit();
            }

        }
    }

}
