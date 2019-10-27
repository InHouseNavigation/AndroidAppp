package com.syntaxerror.naviapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ZielActivity extends AppCompatActivity {

    ListView listView;
    Intent intentAbteilungen;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("com.syntaxerror.naviapp", MODE_PRIVATE);

        //Activity zu Fullscreen machen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziel_aktivity);

        //Listview initalisieren
        listView = findViewById(R.id.listView);

        //Intent initalisieren für die spätere Personen auswahl
        intentAbteilungen = new Intent(getApplicationContext(), AbteilungActivity.class);

        //arraylist für die Liste
        ArrayList<ListenKategories> listenKategoriesArrayList = new ArrayList<>();

        //Objekte für die Liste erzeugen
        ListenKategories autoUndVerkehr = new ListenKategories("Auto und Verkehr", R.drawable.auto);
        ListenKategories schuleVolkshochschule = new ListenKategories("Schule, Volkshochschule", R.drawable.schule);
        ListenKategories arbeitUndSoziales = new ListenKategories("Arbeit und Soziales", R.drawable.arbeit);
        ListenKategories jugendFamilieSportEhrenamt = new ListenKategories("Jugend, Familie, Sport, Ehrenamt", R.drawable.jugend);
        ListenKategories bauenUndWohnen = new ListenKategories("Bauen und Wohnen", R.drawable.bauen);
        ListenKategories gesundheit = new ListenKategories("Gesundheit", R.drawable.gesundheit);
        ListenKategories landwirtschaft = new ListenKategories("Landwirtschaft", R.drawable.landwirtschaft);
        ListenKategories naturUndUmwelt = new ListenKategories("Natur und Umwelt", R.drawable.natur);
        ListenKategories wirtschaftEnergieUndRegionalentwicklung = new ListenKategories("Wirtschaft, Energie und Regionalentwicklung", R.drawable.wirtschaft);
        ListenKategories oeffentlicheSicherheit = new ListenKategories("Öffentliche Sicherheit", R.drawable.sicherheit);
        ListenKategories wahlen = new ListenKategories("Wahlen", R.drawable.wahlen);
        ListenKategories auslaendischeMitbuerger = new ListenKategories("Ausländische Mitbürger", R.drawable.ausland);
        ListenKategories freizeitUndTourismus = new ListenKategories("Freizeit und Tourismus", R.drawable.freizeit);
        ListenKategories kultur = new ListenKategories("Kultur", R.drawable.kultur);

        //Objekte in die Arraylist hinzufügen
        listenKategoriesArrayList.add(autoUndVerkehr);
        listenKategoriesArrayList.add(schuleVolkshochschule);
        listenKategoriesArrayList.add(arbeitUndSoziales);
        listenKategoriesArrayList.add(jugendFamilieSportEhrenamt);
        listenKategoriesArrayList.add(bauenUndWohnen);
        listenKategoriesArrayList.add(gesundheit);
        listenKategoriesArrayList.add(landwirtschaft);
        listenKategoriesArrayList.add(naturUndUmwelt);
        listenKategoriesArrayList.add(wirtschaftEnergieUndRegionalentwicklung);
        listenKategoriesArrayList.add(oeffentlicheSicherheit);
        listenKategoriesArrayList.add(wahlen);
        listenKategoriesArrayList.add(auslaendischeMitbuerger);
        listenKategoriesArrayList.add(freizeitUndTourismus);
        listenKategoriesArrayList.add(kultur);

        //adapter initalisieren und verknüpfen
        ListenAdapter listenAdapter = new ListenAdapter(this, R.layout.row, listenKategoriesArrayList);
        listView.setAdapter(listenAdapter);

        //listener für die Liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position übergeben für zuordnung des Bereichs
                intentAbteilungen.putExtra("position", position);
                startActivity(intentAbteilungen);
            }
        });

    }

    @Override
    protected void onResume() {
        //Beenden falls man kurz vorher eine Person ausgewählt hat
        if (sharedPreferences.getBoolean("zielgewaelt", false)) {
            sharedPreferences.edit().putBoolean("zielgewaelt", false).commit();
            finish();
        }
        super.onResume();
    }
}
