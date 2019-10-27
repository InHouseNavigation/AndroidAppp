package com.syntaxerror.naviapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {


    //Intent Variablen für die jeweiligen Aktivitys
    Intent intentScan;
    Intent intentZiel;
    Intent intentManuell;

    //variablen der Desing Elemente
    TextView textView;
    PhotoView photoView;

    //sharedPreferences für permanente Datenspeicherung
    SharedPreferences sharedPreferences;

    //url variable des Servers
    String url;

    //Downloader für die Bilder
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity zu Fullscreen machen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Intents initalisieren
        intentScan = new Intent(getApplicationContext(), ScanActivity.class);
        intentZiel = new Intent(getApplicationContext(), ZielActivity.class);
        intentManuell = new Intent(getApplicationContext(), ZielManuellActivity.class);

        //Desing Elemente finden
        textView = findViewById(R.id.textView);
        photoView = findViewById(R.id.imageView);

        //sharedPreferenzes initalisieren
        sharedPreferences = getSharedPreferences("com.syntaxerror.naviapp", MODE_PRIVATE);

        //Zugriff auf Kamera Überprüfen
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }


    }


    //methode um die Scan Activity
    public void startScan(View view) {
        startActivity(intentScan);
    }


    //methode um die jeweilige Zieleingabe zu öffnen
    public void startZiel(View view) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Assistent?").setMessage("Willst du den Assistenten nutzen oder die Raumnummer manuel eingeben").setPositiveButton("Assistent", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ziel eingabe mit unterstützung starten
                startActivity(intentZiel);
            }
        }).setNegativeButton("Manuell", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ziel eingabe mit ohne unterstützung starten
                startActivity(intentManuell);
            }
        }).show();
    }

    //methode für den Anleitungs Button
    public void anleitung(View view) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Anleitung:").setMessage("1.Ziel eingeben \n2.QR-Code scannen \n3.Navigation folgen \n4.Optional weitere Qr codes scannen um Position zu aktuallisieren").setPositiveButton("Ok", null).show();
    }


    //beim zurückkehren in die Methode das Bild aktuallisieren
    @Override
    protected void onResume() {

        //Ziel text setzen
        textView.setText("Ziel: " + sharedPreferences.getString("ziel", "Kein Ziel"));

        //Bild downloaden und setzen
        url = "http://"+getString(R.string.url)+"/query/image.php?room=" + sharedPreferences.getString("QR", "") + "&target=" + sharedPreferences.getString("ziel", "");
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;
        try {
            myImage = task.execute(url).get();
            photoView.setImageBitmap(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}
