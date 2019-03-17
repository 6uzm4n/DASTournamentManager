package com.example.tournamentmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DatabaseManager extends SQLiteOpenHelper {

    // Información general de la base de datos
    private static final String DATABASE_NAME = "TournamentManagerDB";
    private static final int DAATABASE_VERSION = 1;

    // Información de la tabla user
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_MAIL = "mail";

    // Información de la tabla tournament
    private static final String TABLE_TOURNAMENT = "tournament";
    private static final String COLUMN_TOURNAMENT_ID = "id";
    private static final String COLUMN_TOURNAMENT_NAME = "name";
    private static final String COLUMN_TOURNAMENT_GAME = "game";
    private static final String COLUMN_TOURNAMENT_DESCRIPTION = "description";
    private static final String COLUMN_TOURNAMENT_DATE = "date";
    private static final String COLUMN_TOURNAMENT_LOCATION = "location";
    private static final String COLUMN_TOURNAMENT_CREATOR = "creator";


    // Información de la tabla participation
    private static final String TABLE_PARTICIPATION = "participation";
    private static final String COLUMN_PARTICIPATION_USER = "p_user";
    private static final String COLUMN_PARTICIPATION_TOURNAMENT = "p_tournament";
    private static final String COLUMN_PARTICIPATION_RESULT = "result";

    // Sentencias para la creación de las tablas
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_USER_USERNAME + " TEXT PRIMARY KEY NOT NULL," +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
            COLUMN_USER_MAIL + " TEXT NOT NULL UNIQUE)";

    private static final String CREATE_TABLE_TOURNAMENT = "CREATE TABLE " + TABLE_TOURNAMENT + " (" +
            COLUMN_TOURNAMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            COLUMN_TOURNAMENT_NAME + " TEXT NOT NULL," +
            COLUMN_TOURNAMENT_GAME + " TEXT NOT NULL," +
            COLUMN_TOURNAMENT_DESCRIPTION + " TEXT," +
            COLUMN_TOURNAMENT_DATE + " DATE NOT NULL," +
            COLUMN_TOURNAMENT_LOCATION + " TEXT NOT NULL," +
            COLUMN_TOURNAMENT_CREATOR + " TEXT NOT NULL," +
            "FOREIGN KEY (" + COLUMN_TOURNAMENT_CREATOR + ") REFERENCES " + TABLE_TOURNAMENT + "(" + COLUMN_TOURNAMENT_ID + "))";

    private static final String CREATE_TABLE_PARTICIPATION = "CREATE TABLE " + TABLE_PARTICIPATION + " (" +
            COLUMN_PARTICIPATION_USER + " TEXT," +
            COLUMN_PARTICIPATION_TOURNAMENT + " INTEGER," +
            COLUMN_PARTICIPATION_RESULT + " INTEGER," +
            "FOREIGN KEY (" + COLUMN_PARTICIPATION_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_USERNAME + ")," +
            "FOREIGN KEY (" + COLUMN_PARTICIPATION_TOURNAMENT + ") REFERENCES " + TABLE_TOURNAMENT + "(" + COLUMN_TOURNAMENT_ID + "))";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DAATABASE_VERSION);
    }

    /**
     * Crea la base de datos cuando esta no existe. Se llama cuando la aplicación es instalada y abierta por primera vez.
     * También añade algunos datos de prueba para poder probar la funcionalidad de la aplicación.
     *
     * @param db base de datos a utilizar
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String DUMMY_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        // Creamos tabla de usuarios.
        db.execSQL(CREATE_TABLE_USER);
        // Creamos tabla de torneos.
        db.execSQL(CREATE_TABLE_TOURNAMENT);
        // Creamos tabla de participaciones.
        db.execSQL(CREATE_TABLE_PARTICIPATION);
        // Añadimos usuarios de prueba
        db.execSQL("INSERT INTO user VALUES " +
                "('admin', 'admin', 'admin@admin.admin')," +
                "('test1', 'test1', 'test1@gmail.com')," +
                "('test2', 'test2', 'test2@gmail.com')," +
                "('test3', 'test3', 'test3@gmail.com')");
        // Añadimos torneos de prueba
        db.execSQL("INSERT INTO tournament(game, name, description, date, location, creator) VALUES " +
                "('Smash Bros', 'Smash Bros Gran Vía', '" + DUMMY_TEXT + "', '2019-04-05 18:00', 'Bilbao', 'test3')," +
                "('Smash Bros', 'Torneo Smash', '" + DUMMY_TEXT + "' , '2019-04-02 18:00', 'Barcelona', 'test1')," +
                "('Smash Bros', 'Super Smash Sol', '" + DUMMY_TEXT + "', '2019-03-29 18:00', 'Madrid', 'test2')," +
                "('Pokemon', 'Liga Pokemon', '" + DUMMY_TEXT + "', '2019-04-03 18:00', 'Sevilla', 'test3')," +
                "('Pokemon', 'Campeonato Pokemon', '" + DUMMY_TEXT + "', '2019-03-28 18:00', 'Madrid', 'test1')," +
                "('Pokemon', 'Pokemon Masters Barcelona', '" + DUMMY_TEXT + "', '2019-04-23 18:00', 'Barcelona', 'test2')," +
                "('Mario Kart', 'Mario Kart Burgos', '" + DUMMY_TEXT + "', '2019-04-09 18:00', 'Burgos', 'test3')," +
                "('Mario Kart', 'Torneo Mario Kart Wii', '" + DUMMY_TEXT + "', '2019-04-08 18:00', 'Bilbao', 'test1')," +
                "('Mario Kart', 'Madrid Karting', '" + DUMMY_TEXT + "', '2019-05-01 19:30', 'Madrid', 'test2')");
        // Añadimos participaciones de prueba
        db.execSQL("INSERT INTO participation(p_user, p_tournament) VALUES " +
                "('test2', '1')," + "('test2', '2')," + "('test1', '1')," + "('test1', '3')," + "('admin', '2')," + "('admin', '1')," + "('test1', '5')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Añade un nuevo usuario a la base de datos.
     *
     * @param username  nombre de usuario
     * @param password  contraseña del usuario
     * @param mail      dirección de correo del usuario
     * @return          <code>true</code> si el usuario se ha añadido correctamente
     *                  <code>false</code> si ha habido un error al añadir el usuario
     */
    public boolean addUser(String username, String password, String mail) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_MAIL, mail);

        System.out.println("INSERT");
        int result = (int) db.insert(TABLE_USER, null, values);
        System.out.println(result);
        db.close();

        return result >= 1;
    }

    /**
     * Devuelve la información de un torneo dado su id.
     *
     * @param id id del torneo
     * @return arraylist que contiene las columnas del torneo seleccionado
     */
    public ArrayList<String> getTournamentById(String id) {
        ArrayList<String> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_TOURNAMENT, COLUMN_TOURNAMENT_ID, id), null);
        while (c.moveToNext()) {
            result.add(c.getString(0));
            result.add(c.getString(1));
            result.add(c.getString(2));
            result.add(c.getString(3));
            result.add(c.getString(4));
            result.add(c.getString(5));
            result.add(c.getString(6));
        }
        c.close();
        db.close();
        return result;
    }

    /**
     * Devuelve la información de todos los torneos de la base de datos.
     *
     * @return hashmap en el cual cada key es una columna de la base de datos, y cada valor un arraylist
     *         que contiene, según su índice, los datos de cada torneo
     */
    public HashMap<String, ArrayList<String>> getAllTournaments() {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put(COLUMN_TOURNAMENT_ID, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_NAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_GAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DESCRIPTION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DATE, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_LOCATION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_CREATOR, new ArrayList<String>());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_TOURNAMENT, COLUMN_TOURNAMENT_DATE), null);
        while (c.moveToNext()) {
            map.get(COLUMN_TOURNAMENT_ID).add(c.getString(0));
            map.get(COLUMN_TOURNAMENT_NAME).add(c.getString(1));
            map.get(COLUMN_TOURNAMENT_GAME).add(c.getString(2));
            map.get(COLUMN_TOURNAMENT_DESCRIPTION).add(c.getString(3));
            map.get(COLUMN_TOURNAMENT_DATE).add(c.getString(4));
            map.get(COLUMN_TOURNAMENT_LOCATION).add(c.getString(5));
            map.get(COLUMN_TOURNAMENT_CREATOR).add(c.getString(6));
        }
        c.close();
        db.close();
        return map;
    }

    /**
     * Devuelve la información de todos los torneos creados por un usuario dado.
     *
     * @param creator   creador de los torneos
     * @return          hashmap en el cual cada key es una columna de la base de datos, y cada valor un arraylist
     *                  que contiene, según su índice, los datos de cada torneo
     */
    public HashMap<String, ArrayList<String>> getTournamentsByCreator(String creator) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put(COLUMN_TOURNAMENT_ID, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_NAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_GAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DESCRIPTION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DATE, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_LOCATION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_CREATOR, new ArrayList<String>());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT * FROM %s WHERE %s='%s' ORDER BY %s DESC",
                TABLE_TOURNAMENT, COLUMN_TOURNAMENT_CREATOR, creator, COLUMN_TOURNAMENT_DATE), null);
        while (c.moveToNext()) {
            map.get(COLUMN_TOURNAMENT_ID).add(c.getString(0));
            map.get(COLUMN_TOURNAMENT_NAME).add(c.getString(1));
            map.get(COLUMN_TOURNAMENT_GAME).add(c.getString(2));
            map.get(COLUMN_TOURNAMENT_DESCRIPTION).add(c.getString(3));
            map.get(COLUMN_TOURNAMENT_DATE).add(c.getString(4));
            map.get(COLUMN_TOURNAMENT_LOCATION).add(c.getString(5));
            map.get(COLUMN_TOURNAMENT_CREATOR).add(c.getString(6));
        }
        c.close();
        db.close();
        return map;
    }

    /**
     * Devuelve la información de todos los torneos en los que participa un usuario dado.
     *
     * @param user  usuario que participa en los torneos
     * @return      hashmap en el cual cada key es una columna de la base de datos, y cada valor un arraylist
     *              que contiene, según su índice, los datos de cada torneo
     */
    public HashMap<String, ArrayList<String>> getTournamentsByParticipation(String user) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put(COLUMN_TOURNAMENT_ID, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_NAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_GAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DESCRIPTION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DATE, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_LOCATION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_CREATOR, new ArrayList<String>());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s=%s.%s WHERE %s.%s='%s' ORDER BY %s.%s DESC",
                TABLE_TOURNAMENT, TABLE_TOURNAMENT, TABLE_PARTICIPATION, TABLE_TOURNAMENT, COLUMN_TOURNAMENT_ID,
                TABLE_PARTICIPATION, COLUMN_PARTICIPATION_TOURNAMENT, TABLE_PARTICIPATION, COLUMN_PARTICIPATION_USER,
                user, TABLE_TOURNAMENT, COLUMN_TOURNAMENT_DATE), null);
        while (c.moveToNext()) {
            map.get(COLUMN_TOURNAMENT_ID).add(c.getString(0));
            map.get(COLUMN_TOURNAMENT_NAME).add(c.getString(1));
            map.get(COLUMN_TOURNAMENT_GAME).add(c.getString(2));
            map.get(COLUMN_TOURNAMENT_DESCRIPTION).add(c.getString(3));
            map.get(COLUMN_TOURNAMENT_DATE).add(c.getString(4));
            map.get(COLUMN_TOURNAMENT_LOCATION).add(c.getString(5));
            map.get(COLUMN_TOURNAMENT_CREATOR).add(c.getString(6));
        }
        c.close();
        db.close();
        return map;
    }

    /**
     * Devuelve la información de todos los torneos en los que participa un usuario dado y que tienen lugar en los próximos 7 días.
     *
     * @param user usuario que participa en los torneos
     * @return hashmap en el cual cada key es una columna de la base de datos, y cada valor un arraylist
     *         que contiene, según su índice, los datos de cada torneo
     */
    public HashMap<String, ArrayList<String>> getNextTournamentsByParticipation(String user) {

        Calendar calendar = Calendar.getInstance();
        String formatedYear = String.format("%04d", calendar.get(Calendar.YEAR));
        String formatedMonth = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
        String formatedDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String formatedDate = String.format("%s-%s-%s", formatedYear, formatedMonth, formatedDay);
        calendar.add(Calendar.DATE, 7);
        String formatedYearTomorrow = String.format("%04d", calendar.get(Calendar.YEAR));
        String formatedMonthTomorrow = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
        String formatedDayTomorrow = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String formatedDateTomorrow = String.format("%s-%s-%s", formatedYearTomorrow, formatedMonthTomorrow, formatedDayTomorrow);

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        map.put(COLUMN_TOURNAMENT_ID, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_NAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_GAME, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DESCRIPTION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_DATE, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_LOCATION, new ArrayList<String>());
        map.put(COLUMN_TOURNAMENT_CREATOR, new ArrayList<String>());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s=%s.%s WHERE %s.%s='%s'" +
                        "AND %s.%s <= '%s' AND %s.%s >= '%s' ORDER BY %s.%s",
                TABLE_TOURNAMENT, TABLE_TOURNAMENT, TABLE_PARTICIPATION, TABLE_TOURNAMENT, COLUMN_TOURNAMENT_ID,
                TABLE_PARTICIPATION, COLUMN_PARTICIPATION_TOURNAMENT, TABLE_PARTICIPATION, COLUMN_PARTICIPATION_USER, user,
                TABLE_TOURNAMENT, COLUMN_TOURNAMENT_DATE, formatedDateTomorrow, TABLE_TOURNAMENT, COLUMN_TOURNAMENT_DATE,
                formatedDate, TABLE_TOURNAMENT, COLUMN_TOURNAMENT_DATE), null);
        while (c.moveToNext()) {
            map.get(COLUMN_TOURNAMENT_ID).add(c.getString(0));
            map.get(COLUMN_TOURNAMENT_NAME).add(c.getString(1));
            map.get(COLUMN_TOURNAMENT_GAME).add(c.getString(2));
            map.get(COLUMN_TOURNAMENT_DESCRIPTION).add(c.getString(3));
            map.get(COLUMN_TOURNAMENT_DATE).add(c.getString(4));
            map.get(COLUMN_TOURNAMENT_LOCATION).add(c.getString(5));
            map.get(COLUMN_TOURNAMENT_CREATOR).add(c.getString(6));
        }
        c.close();
        db.close();
        return map;
    }

    /**
     * Dado un usuario, devuelve su dirección de correo electrónico.
     *
     * @param user  id del usuario
     * @return      mail del usuario
     */
    public String getMailByUser(String user) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_USER_MAIL};
        String selection = COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = {user};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        String result = "";
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return result;
    }

    /**
     * Añade un nuevo torneo a la base de datos.
     *
     * @param tournamentName        nombre del torneo
     * @param tournamentGame        juego del torneo
     * @param tournamentDescription descripción del torneo
     * @param tournamentDate        fecha del torneo en formato 'yyyy-mm-dd hh-mm'
     * @param tournamentLocation    ubicación del torneo
     * @param tournamentCreator     usuario creador del torneo
     * @return                      <code>true</code> si el torneo se ha añadido correctamente
     *                              <code>false</code> si ha habido un error al añadir el torneo
     */
    public boolean addTournament(String tournamentName, String tournamentGame, String tournamentDescription,
                                 String tournamentDate, String tournamentLocation, String tournamentCreator) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOURNAMENT_NAME, tournamentName);
        values.put(COLUMN_TOURNAMENT_GAME, tournamentGame);
        values.put(COLUMN_TOURNAMENT_DESCRIPTION, tournamentDescription);
        values.put(COLUMN_TOURNAMENT_DATE, tournamentDate);
        values.put(COLUMN_TOURNAMENT_LOCATION, tournamentLocation);
        values.put(COLUMN_TOURNAMENT_CREATOR, tournamentCreator);

        System.out.println("INSERT TOURNAMENT");
        int result = (int) db.insert(TABLE_TOURNAMENT, null, values);
        System.out.println(result);
        db.close();

        return result >= 1;
    }

    /**
     * Elimina un torneo de la base de datos.
     *
     * @param tournamentId  id del torneo a eliminar
     * @return              <code>true</code> si el torneo se ha eliminado correctamente
     *                      <code>false</code> si ha habido un error al eliminar el torneo
     */
    public boolean deleteTournament(String tournamentId) {
        SQLiteDatabase db = getReadableDatabase();

        // Se eliminan las participaciones en el torneo a borrar
        String whereParticipation = String.format("%s='%s'", COLUMN_PARTICIPATION_TOURNAMENT, tournamentId);
        db.delete(TABLE_PARTICIPATION, whereParticipation, null);

        // Se elimina el torneo a borrar
        String whereTournament = String.format("%s='%s'", COLUMN_TOURNAMENT_ID, tournamentId);
        int result = db.delete(TABLE_TOURNAMENT, whereTournament, null);

        return result >= 1;
    }

    /**
     * Añade una nueva participación a la base de datos.
     *
     * @param userId        usuario que participará en un torneo
     * @param tournamentId  id del torneo en el que participará un usuario
     * @return              <code>true</code> si la participación se ha añadido correctamente
     *                      <code>false</code> si ha habido un error al añadir la participación
     */
    public boolean addParticipation(String userId, String tournamentId) {
        if (checkUserExists(userId) && checkTournamentExists(tournamentId) && !checkUserParticipation(userId, tournamentId)) {
            SQLiteDatabase db = getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_PARTICIPATION_USER, userId);
            values.put(COLUMN_PARTICIPATION_TOURNAMENT, tournamentId);

            int result = (int) db.insert(TABLE_PARTICIPATION, null, values);
            System.out.println(result);
            db.close();

            return result >= 1;
        }
        return false;
    }

    /**
     * Elimina una participación de la base de datos.
     *
     * @param userId        usuario que dejará de participar en un torneo
     * @param tournamentId  id del torneo en el que dejará de participar el usuario
     * @return              <code>true</code> si la participación se ha eliminado correctamente
     *                      <code>false</code> si ha habido un error al elimiar la participación
     */
    public boolean deleteParticipation(String userId, String tournamentId) {
        if (checkUserExists(userId) && checkTournamentExists(tournamentId) && checkUserParticipation(userId, tournamentId)) {
            SQLiteDatabase db = getReadableDatabase();

            String selection = COLUMN_PARTICIPATION_USER + " = ? AND " + COLUMN_PARTICIPATION_TOURNAMENT + " = ?";
            String[] selectionArgs = {userId, tournamentId};

            int result = db.delete(TABLE_PARTICIPATION, selection, selectionArgs);
            db.close();

            return result >= 1;
        }
        return false;
    }

    /**
     * Comprueba si la contraseña pertenece al usuario.
     *
     * @param user      usuario que quiere iniciar sesión
     * @param password  contraseña del usuario
     * @return          <code>true</code> si los datos del login son correctos
     *                  <code>false</code> si los datos del login son incorrectos
     */
    public boolean checkLogin(String user, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_USER_USERNAME};
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {user, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }

    /**
     * Comprueba si un usuario existe dado un nombre.
     *
     * @param user  usuario que se quiere comprobar
     * @return      <code>true</code> si el usuario ya existe
     *              <code>false</code> si el usuario no existe
     */
    public boolean checkUserExists(String user) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_USER_USERNAME};
        String selection = COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = {user};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }

    /**
     * Dado el id de un torneo, comprueba si este ya existe.
     *
     * @param tournamentId  id del torneo a comprobar
     * @return              <code>true</code> si el torneo ya existe
     *                      <code>false</code> si ha torneo no existe
     */
    public boolean checkTournamentExists(String tournamentId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_TOURNAMENT_ID};
        String selection = COLUMN_TOURNAMENT_ID + " = ?";
        String[] selectionArgs = {tournamentId};

        Cursor cursor = db.query(TABLE_TOURNAMENT, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }

    /**
     * Dado el nombre de un torneo, comprueba si este ya existe.
     *
     * @param tournamentName    nombre del torneo a comprobar
     * @return                  <code>true</code> si el nombre del torneo está en uso
     *                          <code>false</code> si el nombre del torneo no está en uso
     */
    public boolean checkTournamentNameExists(String tournamentName) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_TOURNAMENT_ID};
        String selection = COLUMN_TOURNAMENT_NAME + " = ?";
        String[] selectionArgs = {tournamentName};

        Cursor cursor = db.query(TABLE_TOURNAMENT, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }

    /**
     * Comprueba si un correo está siendo utilizado por algun usuario.
     *
     * @param mail  email a comprobar
     * @return      <code>true</code> si el correo ya está en uso por algún usuario
     *              <code>false</code> si el correo no está en uso por algún usuario
     */
    public boolean checkMailExists(String mail) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_USER_MAIL};
        String selection = COLUMN_USER_MAIL + " = ?";
        String[] selectionArgs = {mail};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }

    /**
     * Comprueba si un usuario está apuntado a un torneo.
     *
     * @param userId        usuario para comprobar su participación
     * @param tournamentId  id del torneo a comprobar
     * @return              <code>true</code> si el usuario está participando en el torneo
     *                      <code>false</code> si el usuario no está participando en el torneo
     */
    public boolean checkUserParticipation(String userId, String tournamentId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_PARTICIPATION_USER, COLUMN_PARTICIPATION_TOURNAMENT};
        String selection = COLUMN_PARTICIPATION_USER + " = ? AND " + COLUMN_PARTICIPATION_TOURNAMENT + " = ?";
        String[] selectionArgs = {userId, tournamentId};

        Cursor cursor = db.query(TABLE_PARTICIPATION, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();

        return result > 0;
    }
}
