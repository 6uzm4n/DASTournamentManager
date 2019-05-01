package com.example.tournamentmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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

        // El listener sólo se utiliza para la recepción de imágenes. En caso de haberse pasado como parámetro, se utilizará.
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

    /**
     * Añade un nuevo usuario a la base de datos.
     *
     * @param username  nombre de usuario
     * @param password  contraseña del usuario
     * @param mail      dirección de correo del usuario
     */
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

    /**
     * Comprueba si un usuario existe dado un nombre.
     *
     * @param username  usuario que se quiere comprobar
     */
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

    /**
     * Comprueba si un correo está siendo utilizado por algun usuario.
     *
     * @param mail  email a comprobar
     */
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

    /**
     * Comprueba si la contraseña pertenece al usuario.
     *
     * @param username      usuario que quiere iniciar sesión
     * @param password  contraseña del usuario
     */
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

    /**
     * Dado un usuario, devuelve su dirección de correo electrónico.
     *
     * @param username  id del usuario
     */
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

    /**
     * Dado un usuario y un token, actualiza el token de dicho usuario en la base de datos.
     *
     * @param username  id del usuario
     * @param token     token del usuario
     */
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

    /**
     * Dado un usuario, devuelve su localización.
     *
     * @param username  id del usuario
     */
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

    /**
     * Dado un usuario y una localización, actualiza la localización de dicho usuario en la base de datos.
     *
     * @param username  id del usuario
     * @param location  token del usuario
     */
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

    /**
     * Dado un usuario, devuelve su imagen de perfil.
     *
     * @param username  id del usuario
     * @param pListener listener que recibirá la notificación al descargarse la imágen
     */
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

    /**
     * Dado un usuario y una imagen, actualiza la imagen de dicho usuario en la base de datos.
     *  @param username  id del usuario
     * @param image  token del usuario
     */
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

    /**
     * Añade un nuevo torneo a la base de datos.
     *
     * @param name        nombre del torneo
     * @param game        juego del torneo
     * @param description descripción del torneo
     * @param date        fecha del torneo en formato 'yyyy-mm-dd hh-mm'
     * @param location    ubicación del torneo
     * @param creator     usuario creador del torneo
     */
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

    /**
     * Elimina un torneo de la base de datos.
     *
     * @param id  id del torneo a eliminar
     */
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

    /**
     * Devuelve la información de todos los torneos de la base de datos.
     *
     */
    public void getAllTournaments() {
        JSONObject params = new JSONObject();
        try {
            params.put("function", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.execute("tournament.php", params.toString());
    }

    /**
     * Devuelve la información de un torneo dado su id.
     *
     * @param id id del torneo
     */
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

    /**
     * Devuelve la información de todos los torneos creados por un usuario dado.
     *
     * @param creator   creador de los torneos
     */
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

    /**
     * Dado el nombre de un torneo, comprueba si este ya existe.
     *
     * @param name    nombre del torneo a comprobar
     */
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

    /**
     * Añade una nueva participación a la base de datos.
     *
     * @param user        usuario que participará en un torneo
     * @param tournament  id del torneo en el que participará un usuario
     */
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

    /**
     * Elimina una participación de la base de datos.
     *
     * @param user        usuario que dejará de participar en un torneo
     * @param tournament  id del torneo en el que dejará de participar el usuario
     */
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

    /**
     * Comprueba si un usuario está apuntado a un torneo.
     *
     * @param user        usuario para comprobar su participación
     * @param tournament  id del torneo a comprobar
     */
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

    /**
     * Devuelve la información de todos los torneos en los que participa un usuario dado.
     *
     * @param user  usuario que participa en los torneos
     */
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

    /**
     * Crea un snackbar notificando un error de conexión al servidor
     *
     * @param view  Vista donde se creará el snackbar.
     */
    public void notifyError(View view) {
        Snackbar.make(view, view.getContext().getString(R.string.connection_error), Snackbar.LENGTH_LONG).show();

    }

}
