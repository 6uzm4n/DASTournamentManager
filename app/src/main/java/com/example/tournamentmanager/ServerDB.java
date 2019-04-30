package com.example.tournamentmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ServerDB extends AsyncTask<String, Void, String> {

    private static final String server = "https://134.209.235.115/glopez041/WEB/";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ImageDownloader.ImageDownloadedListener listener;

    public ServerDB(Context c) {
        context = c;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpsURLConnection urlConnection;
        try {
            urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(context, server + strings[0]);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);


            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(strings[1]);
            out.close();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                inputStream.close();
                return result.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (listener != null) {
            String image64 = null;
            try {
                image64 = this.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.downloadCompressed(listener, image64);
            listener = null;
        }
    }

    public void addUser(String username, String mail, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 0);
            params.put("user", username);
            params.put("mail", mail);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void checkUserExists(String username) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 1);
            params.put("user", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void checkMailExists(String mail) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 2);
            params.put("mail", mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void checkLogin(String username, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 3);
            params.put("user", username);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void getMailByUser(String username) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 4);
            params.put("user", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void updateUserToken(String username, String token) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 5);
            params.put("user", username);
            params.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void getUserLocation(String username) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 6);
            params.put("user", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void updateUserLocation(String username, String location) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 7);
            params.put("user", username);
            params.put("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void getUserImage(String username, ImageDownloader.ImageDownloadedListener pListener) {
        listener = pListener;
        JSONObject params = new JSONObject();
        try {
            params.put("function", 8);
            params.put("user", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void updateUserImage(String username, String image) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 9);
            params.put("user", username);
            params.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("user.php", params.toString());
    }

    public void addTournament(String name, String game, String description, String date, String location, String creator) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 0);
            params.put("name", name);
            params.put("game", game);
            params.put("description", description);
            params.put("date", date);
            params.put("location", location);
            params.put("creator", creator);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void deleteTournament(String id) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 1);
            params.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void getAllTournaments() {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void getTournamentById(String id) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 3);
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void getTournamentsByCreator(String creator) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 4);
            params.put("creator", creator);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void checkTournamentNameExists(String name) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 5);
            params.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    public void addParticipation(String user, String tournament) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 0);
            params.put("p_user", user);
            params.put("p_tournament", tournament);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("participation.php", params.toString());
    }

    public void deleteParticipation(String user, String tournament) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 1);
            params.put("p_user", user);
            params.put("p_tournament", tournament);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("participation.php", params.toString());
    }

    public void checkParticipation(String user, String tournament) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 2);
            params.put("p_user", user);
            params.put("p_tournament", tournament);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("participation.php", params.toString());
    }

    public void getTournamentsByParticipation(String user) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 3);
            params.put("p_user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("participation.php", params.toString());
    }

    public void notifyError(View view) {
        Snackbar.make(view, "No se ha podido acceder al servidor", Snackbar.LENGTH_LONG).show();

    }

}
