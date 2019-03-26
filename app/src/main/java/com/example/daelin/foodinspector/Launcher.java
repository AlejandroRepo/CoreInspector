package com.example.daelin.foodinspector;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

public class Launcher extends AppCompatActivity {

    private pl.droidsonroids.gif.GifImageView startMsg;
    private pl.droidsonroids.gif.GifImageView soundcfg;
    private boolean soundon=true;
    private RadioButton es;
    private RadioButton en;
    private RadioButton gal;
    private Resources res;
    private DisplayMetrics dm;
    private TextView idioma;
    private android.content.res.Configuration conf;
    private MediaPlayer mp;
    private com.example.daelin.foodinspector.CustomTextView link1;
    private com.example.daelin.foodinspector.CustomTextView link2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        es = (RadioButton) findViewById(R.id.lngES);
        gal = (RadioButton) findViewById(R.id.lngGAL);
        en = (RadioButton) findViewById(R.id.lngEN);
        idioma = (TextView) findViewById(R.id.txtIdioma);

        link1 = (com.example.daelin.foodinspector.CustomTextView) findViewById(R.id.txtChap);
        link2 = (com.example.daelin.foodinspector.CustomTextView) findViewById(R.id.txtteis1);

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.edu.xunta.gal/centros/ieschapela/"));
                startActivity(navegador);
            }
        });

        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.edu.xunta.gal/centros/iesteis//"));
                startActivity(navegador);
            }
        });

        es.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));
        en.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));
        gal.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));

        mp= MediaPlayer.create(Launcher.this,R.raw.komikuskateost);
        mp.setLooping(true);
        mp.start();

        res = getResources();
        dm = res.getDisplayMetrics();
        conf = res.getConfiguration();
        pl.droidsonroids.gif.GifImageView start = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.GifImageView);
        startMsg = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.GifImageView);

        //ajusta las opciones del launcher en base al idioma de sistema

        if (Locale.getDefault().toString().equalsIgnoreCase("gl")){
            gal.setChecked(true); startMsg.setImageResource(R.drawable.startgl);
        } else if (Locale.getDefault().toString().equalsIgnoreCase("en")){
            en.setChecked(true); startMsg.setImageResource(R.drawable.starten);
        } else {es.setChecked(true); startMsg.setImageResource(R.drawable.start);}

        //cambio dinámico de locale

        gal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    conf.setLocale(new Locale("gl"));
                    res.updateConfiguration(conf, dm);
                    startMsg.setImageResource(R.drawable.startgl);
                    idioma.setText("Linguaxe");
                }
            }
        });

        en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    conf.setLocale(new Locale("en"));
                    res.updateConfiguration(conf, dm);
                    startMsg.setImageResource(R.drawable.starten);
                    idioma.setText("Language");
                }
            }
        });

        es.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    conf.setLocale(new Locale("es"));
                    res.updateConfiguration(conf, dm);
                    startMsg.setImageResource(R.drawable.start);
                    idioma.setText("Idioma");
                }
            }
        });


        soundcfg = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.soundConf);
        soundcfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (soundon) {
                soundon=false;
                soundcfg.setImageResource(R.drawable.soundoff);
                mp.pause();
                }
                else{
                    soundon=true;
                    soundcfg.setImageResource(R.drawable.soundon);
                    mp= MediaPlayer.create(Launcher.this,R.raw.komikuskateost);
                    mp.start();
            }
            }
        });

        // Arranque actividad main

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) mp.stop();
                Intent i = new Intent(Launcher.this, Main.class);
                i.putExtra("soundcfg", soundon);
                startActivity(i);
            }
        });
    }

    // comportamiento de la musica de fondo

    @Override
    protected void onResume() {
        super.onResume();
        if (!mp.isPlaying() && soundon==true) {
            mp= MediaPlayer.create(Launcher.this,R.raw.komikuskateost);
            mp.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }
}