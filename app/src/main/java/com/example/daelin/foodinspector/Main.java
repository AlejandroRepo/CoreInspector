package com.example.daelin.foodinspector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private Spinner categorias;
    private ListView alimentos2;

    private LinearLayout bloque;
    private LinearLayout segundo;
    private LinearLayout tercero;
    private LinearLayout cuarto;
    private LinearLayout result;

    private boolean animando;
    private boolean soundon;

    private ArrayList<String> alim = new ArrayList();
    private SQLiteDatabase db;

    private String cat;
    private ImageButton iconoCat;

    private pl.droidsonroids.gif.GifImageView target;
    private ImageView resultIco;
    private Button operacion;
    private Button back;
    private Button btnRetry;

    private TextView dialogo;
    private TextView masInfo;
    private TextView txtVeredicto;

    private TextView txtNom;
    private TextView txtAzucar;
    private TextView txtGrasas;
    private TextView txtSodio;

    private MediaPlayer mp;
    private String guardaTexto;
    private Drawable guardaEstado;
    private String alimSel;

    private boolean stillTalking;

    private final int CALCULA = 2;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        soundon = i.getBooleanExtra("soundcfg", false);
        setContentView(R.layout.activity_main);

        cargaComponentes();

        animando=true;
        Runnable runnable = new Runnable(){
            public void run() {
                target.setImageResource(R.drawable.pixhabla1);
                dialogo.setText(R.string.intro2);
            }
        };
        handler.postDelayed(runnable, 4700);

        Runnable runnable2 = new Runnable(){
            public void run() {
                target.setImageResource(R.drawable.pixgirlnormal);
                segundo.setVisibility(View.VISIBLE);
                animando=false;
                stillTalking=false;
            }
        };

        handler.postDelayed(runnable2, 8000);


        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = categorias.getSelectedItem().toString();

                if (!cat.equalsIgnoreCase("----")){
                    switch (cat){
                        case "Frutas":  iconoCat.setImageResource(R.drawable.catfrutas); break;
                        case "Lácteos": iconoCat.setImageResource(R.drawable.catlacteos);break;
                        case "Cereales": iconoCat.setImageResource(R.drawable.catcereal); break;
                        case "Verduras y hortalizas": iconoCat.setImageResource(R.drawable.catveggies); break;
                        case "Huevos": iconoCat.setImageResource(R.drawable.cathuevos); break;
                        case "Bebidas": iconoCat.setImageResource(R.drawable.catbebidas); break;
                        case "Legumbres": iconoCat.setImageResource(R.drawable.catlegus); break;
                        case "Frutos secos y semillas": iconoCat.setImageResource(R.drawable.catsemillas); break;
                        default: iconoCat.setImageResource(R.drawable.icononada);
                            break;
                    }
                    masInfo.setVisibility(View.VISIBLE);
                    Cursor datos = db.rawQuery("SELECT alimento from '"+cat+"'",null);
                    alim.clear();
                    while(datos.moveToNext()){
                    alim.add(datos.getString(0));
                    }
                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Main.this, android.R.layout.simple_list_item_checked, alim);
                    alimentos2.setAdapter(adaptador);
                    tercero.setVisibility(View.VISIBLE);
                    datos.close();

                } else { masInfo.setVisibility(View.GONE); tercero.setVisibility(View.GONE);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alim.clear();
            }
        }
        );

        alimentos2.setItemsCanFocus(true);
        alimentos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alimSel = alimentos2.getItemAtPosition(position).toString();
                operacion.setEnabled(true);
                operacion.setTextSize(22);
                operacion.setText(R.string.letsgo);
            }
        });
    }

    public void cargaComponentes(){
        BBDD bbdd = new BBDD(getApplicationContext());
        db = bbdd.getReadableDatabase();
        stillTalking = true;
        categorias = (Spinner) findViewById(R.id.SPcategorias);
        alimentos2 = (ListView) findViewById(R.id.SPalimentos);

        bloque = (LinearLayout) findViewById(R.id.bloque);
        segundo = (LinearLayout) findViewById(R.id.segundo);
        tercero = (LinearLayout) findViewById(R.id.tercero);
        cuarto = (LinearLayout) findViewById(R.id.cuarto);
        result = (LinearLayout) findViewById(R.id.result);
        resultIco = (ImageView) findViewById(R.id.resultIco);

        target = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.imageView2);
        dialogo = (TextView) findViewById(R.id.dialogo);
        iconoCat = (ImageButton) findViewById(R.id.icono);
        masInfo = (TextView) findViewById(R.id.masinfo);
        dialogo.setText(R.string.intro1);
        operacion = (Button) findViewById(R.id.botonGo);
        operacion.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));
        back = (Button) findViewById(R.id.back);
        btnRetry = (Button) findViewById(R.id.btnRetry);
        back.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));
        btnRetry.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/yoster.ttf"));

        txtNom = (TextView) findViewById(R.id.txtAlim);
        txtAzucar = (TextView) findViewById(R.id.txtazucar);
        txtGrasas = (TextView) findViewById(R.id.txtgrasas);
        txtSodio = (TextView) findViewById(R.id.txtsodio);
        txtVeredicto = (TextView) findViewById(R.id.veredictTxt);
    }

    public void opera (View v){

        if (!stillTalking) {

            animando = true;
            bloque.setVisibility(View.VISIBLE);
            target.setImageResource(R.drawable.thinking2);
            dialogo.setText(R.string.letssee);
            tercero.setVisibility(View.GONE);
            cuarto.setVisibility(View.VISIBLE);
            masInfo.setVisibility(View.GONE);
            segundo.setVisibility(View.GONE);

            Runnable runnable = new Runnable() {
                public void run() {
                    Intent i = new Intent(getApplicationContext(), Calculos.class);
                    i.putExtra("categoria", cat);
                    i.putExtra("alimento", alimSel);
                    startActivityForResult(i, CALCULA);
                    animando = false;
                }
            };
            handler.postDelayed(runnable, 4000);
        }
    }

    public void masInfor(View v){

        if (animando==false){
        animando=true;
        guardaEstado = target.getDrawable();
        guardaTexto = dialogo.getText().toString();

        if (!cat.equalsIgnoreCase("----")){
            target.setImageResource(R.drawable.pixhabla3);

            switch (cat){
                case "Frutas":  dialogo.setText(R.string.fruits);
                    break;
                case "Lácteos": dialogo.setText(R.string.milk);
                    break;
                case "Legumbres": dialogo.setText(R.string.legum);
                    break;
                case "Huevos": dialogo.setText(R.string.eggs);
                    break;
                case "Cereales": {target.setImageResource(R.drawable.pixhablaserius); dialogo.setText(R.string.bread);}
                    break;
                case "Bebidas": {target.setImageResource(R.drawable.pixhablaserius); dialogo.setText(R.string.drinks);}
                    break;
                case "Verduras y hortalizas": dialogo.setText(R.string.veggies);
                    break;
                case "Frutos secos y semillas": dialogo.setText(R.string.seeds);
                    break;

                default: iconoCat.setImageResource(R.drawable.icononada);
                    break;
            }

        timea(4000);
    }
    }
    }

    public void bother(View v){

        if (animando==false){
        animando=true;
        target.setImageResource(R.drawable.pixgirlwhat2);
        guardaEstado = target.getDrawable();
        guardaTexto = dialogo.getText().toString();
        int pose = (int) (Math.random()*3)+1;

        switch (pose){
            case 1:        target.setImageResource(R.drawable.pixgirlshock);
                dialogo.setText("¿¡E-eh!?...");
                break;
            case 2:        target.setImageResource(R.drawable.pixgirlcry);
                dialogo.setText("...");
                break;
            case 3:        target.setImageResource(R.drawable.pixgirlmad);
                dialogo.setText("Uhmmm...");
                break;
        }

        timea(1200);
        }
    }

    public void timea (int timer){

        Runnable runnable = new Runnable(){
            public void run() {
                target.setImageDrawable(guardaEstado);
                dialogo.setText(guardaTexto);
                animando=false;
            }
        };

        handler.postDelayed(runnable, timer);
    }


    public void back (View v){
        finish();
    }

    public void reTry (View v){
        result.setVisibility(View.GONE);
        bloque.setVisibility(View.GONE);
        segundo.setVisibility(View.VISIBLE);

        target.setImageResource(R.drawable.pixgirlnormal);
        guardaEstado = target.getDrawable();
        guardaTexto = getResources().getString(R.string.intro2);
        target.setImageResource(R.drawable.pixhabla2);
        dialogo.setText(R.string.shootCowbow);
        timea(4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        cuarto.setVisibility(View.GONE);
        result.setVisibility(View.VISIBLE);

        String nom = data.getStringExtra("alimento");
        float azucar= data.getFloatExtra("azucar",0);
        float grasas = data.getFloatExtra("grasas",0);
        float sodio= data.getFloatExtra("sodio",0);
        int veredicto = data.getIntExtra("veredicto",0);
        int veredictoA = data.getIntExtra("veredictoA",0);
        int veredictoG = data.getIntExtra("veredictoG",0);
        int veredictoS = data.getIntExtra("veredictoS",0);

        if (requestCode==CALCULA){
            if (resultCode==RESULT_OK){

                txtNom.setText(nom);
                txtAzucar.setText(String.valueOf(azucar));
                switch(veredictoA){
                    case 1: txtAzucar.setBackgroundColor(getResources().getColor(R.color.verde)); break;
                    case 2: txtAzucar.setBackgroundColor(getResources().getColor(R.color.naranja)); break;
                    case 3: txtAzucar.setBackgroundColor(getResources().getColor(R.color.rojo)); break;
                    default: txtGrasas.setBackgroundColor(getResources().getColor(R.color.negro));}

                txtGrasas.setText(String.valueOf(grasas));
                switch(veredictoG){
                    case 1: txtGrasas.setBackgroundColor(getResources().getColor(R.color.verde)); break;
                    case 2: txtGrasas.setBackgroundColor(getResources().getColor(R.color.naranja)); break;
                    case 3: txtGrasas.setBackgroundColor(getResources().getColor(R.color.rojo)); break;
                    default: txtGrasas.setBackgroundColor(getResources().getColor(R.color.negro));}

                txtSodio.setText(String.valueOf(sodio));
                switch(veredictoS){
                    case 1: txtSodio.setBackgroundColor(getResources().getColor(R.color.verde)); break;
                    case 2: txtSodio.setBackgroundColor(getResources().getColor(R.color.naranja)); break;
                    case 3: txtSodio.setBackgroundColor(getResources().getColor(R.color.rojo)); break;
                    default: txtSodio.setBackgroundColor(getResources().getColor(R.color.negro));}

                if (veredicto >= 6) {
                    target.setImageResource(R.drawable.pixgirlcryanim);
                    dialogo.setText(R.string.bad);
                    txtVeredicto.setText(R.string.unhealthy);
                    txtVeredicto.setTextColor(getResources().getColor(R.color.rojo));
                    resultIco.setImageDrawable(getResources().getDrawable(R.drawable.verbad));
                    }

                else if (veredicto <6 && veredicto >3){
                    target.setImageResource(R.drawable.pixhabla3);
                    dialogo.setText(R.string.ok);
                    txtVeredicto.setText(R.string.semihealthy);
                    txtVeredicto.setTextColor(getResources().getColor(R.color.naranja));
                    resultIco.setImageDrawable(getResources().getDrawable(R.drawable.vernormal));
                    }

                else if (veredicto<=3) {{
                    dialogo.setText(R.string.great);
                    target.setImageResource(R.drawable.pixhcheer);}
                    txtVeredicto.setText(R.string.healthy);
                    txtVeredicto.setTextColor(getResources().getColor(R.color.verde));
                    resultIco.setImageDrawable(getResources().getDrawable(R.drawable.vergood));
                    }

                else {
                    result.setBackgroundColor(getResources().getColor(R.color.negro));
                    target.setImageResource(R.drawable.pixgirlcryanim);
                    dialogo.setText(R.string.error);}

            } else if (resultCode==RESULT_CANCELED){
                result.setBackgroundColor(getResources().getColor(R.color.negro));
                target.setImageResource(R.drawable.pixgirlcryanim);
                dialogo.setText(R.string.error);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundon){if (mp.isPlaying()) mp.stop();}
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundon){if (mp.isPlaying()) mp.pause();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        soundon = i.getBooleanExtra("soundcfg", false);
        if (soundon){
            mp= MediaPlayer.create(Main.this,R.raw.mathgrantuneasytownost);
            if (!mp.isPlaying()) mp.start();}
    }
}
