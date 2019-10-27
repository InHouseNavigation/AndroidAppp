package com.syntaxerror.naviapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AbteilungActivity extends AppCompatActivity {

    //Variablen für späteren Gebrauch
    ListView listViewAbteilung;
    ArrayList<ListenKategories> listenKategoriesArrayList;
    ListenAdapter listenAdapter;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity zu Fullscreen machen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_abteilung);

        //Listview initalisieren
        listViewAbteilung = findViewById(R.id.listViewAbteilung);

        //Array List für die Listenitems
        listenKategoriesArrayList = new ArrayList<>();

        //Adapter für den Listview erstellen und verknüpfen
        listenAdapter = new ListenAdapter(this, R.layout.row, listenKategoriesArrayList);
        listViewAbteilung.setAdapter(listenAdapter);

        //sharedpreferences initalisieren für permanente Datenspeicherung
        sharedPreferences = getSharedPreferences("com.syntaxerror.naviapp", MODE_PRIVATE);

        //abrufen welche Position in der Liste in ZielActivity gedrückt wurde
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position");

        //Posizion dem Bereich zuordnen
        String department = "auto";
        switch (position) {
            case 0:
                department = "auto";
                break;
            case 1:
                department = "schule";
                break;
            case 2:
                department = "arbeit";
                break;
            case 3:
                department = "jugend";
                break;
            case 4:
                department = "bauen";
                break;
            case 5:
                department = "gesundheit";
                break;
            case 6:
                department = "landwirtschaft";
                break;
            case 7:
                department = "natur";
                break;
            case 8:
                department = "wirtschaft";
                break;
            case 9:
                department = "sicherheit";
                break;
            case 10:
                department = "wahlen";
                break;
            case 11:
                department = "ausland";
                break;
            case 12:
                department = "freizeit";
                break;
            case 13:
                department = "kultur";
                break;
        }


        //passenden JSON String vom Server downloaden
        DownloadTask task = new DownloadTask();
        task.execute("http://"+getString(R.string.url)+"/query/sql.php?department=" + department);

        //onClick Listener für erkennung welche Person ausgewählt wurde
        listViewAbteilung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Büronummer speichern für spätere Routenplanung
                    sharedPreferences.edit().putString("ziel", jsonObject.getString("office")).commit();

                    //Boolean um die vorherige Activity auch zu schließen
                    sharedPreferences.edit().putBoolean("zielgewaelt", true).commit();

                    //Aktivity beenden
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    //Downloader für den Download der JSON Datei
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                return inpuStreamReader(urlConnection);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        private String inpuStreamReader(HttpURLConnection connection) throws Exception {

            StringBuffer response = new StringBuffer();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
            in.close();
            connection.disconnect();
            return response.toString();
        }


        //nach dem Empfang des JSON Strings diesen verarbeiten
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //String in ein JSON Array packen
                JSONArray jsonArray = new JSONArray(s);

                //den Array durchgehen um die Liste zu erstellen
                for (int zaeler = jsonArray.length() - 1; zaeler >= 0; zaeler--) {

                    //an der Position das JSON Objekt abrufen
                    jsonObject = jsonArray.getJSONObject(zaeler);

                    //Namen in einen neuen Array packen
                    JSONArray jsonArrayNamen = new JSONArray(jsonObject.getString("name"));

                    //Status auslesen und danach das Symbol setzen
                    int drawableSymbol = R.drawable.ic_launcher;
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        //fals true auf das verfügbar Symbol setzen
                        drawableSymbol = R.drawable.ic_verfuegbar;
                    } else if (status.equals("false")) {
                        //fals false auf das nicht verfügbar Symbol setzen
                        drawableSymbol = R.drawable.ic_nicht_verfeugbar;
                    }

                    //neues Objekt für die Liste erstellen
                    ListenKategories listenKategories = new ListenKategories(jsonArrayNamen.getString(1) + " " + jsonArrayNamen.getString(0), drawableSymbol);

                    //Das objekt der Liste hinzufügen
                    listenKategoriesArrayList.add(listenKategories);

                    //Liste aktuallisieren
                    listenAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
