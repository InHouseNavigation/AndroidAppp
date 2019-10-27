package com.syntaxerror.naviapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class ZielManuellActivity extends AppCompatActivity {

    EditText editText;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity zu Fullscreen machen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ziel_manuell);

        //Desing Elemente initalisieren
        editText = findViewById(R.id.eTZielManuell);
        sharedPreferences = getSharedPreferences("com.syntaxerror.naviapp", MODE_PRIVATE);
    }

    //methode für Bestätigungs Button
    public void manuellBestaetigen(View view) {
        //nummer aus editText holen und Speichern
        String raumnummer = editText.getText().toString();
        sharedPreferences.edit().putString("ziel", raumnummer).commit();

        //Aktivity beenden
        finish();
    }
}
