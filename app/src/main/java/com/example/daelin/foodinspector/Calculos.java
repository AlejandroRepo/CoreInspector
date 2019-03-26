package com.example.daelin.foodinspector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Calculos extends AppCompatActivity {

    SQLiteDatabase db;
    String aux;
    float azucar;
    float grasas;
    float sodio;
    int veredicto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caculos);

        Intent i = getIntent();
        String alim = i.getStringExtra("alimento");
        String cat = i.getStringExtra("categoria");

        int veredictoG=0;
        int veredictoA=0;
        int veredictoS=0;

        db = new BBDD(this).getReadableDatabase();

        try {

            Cursor prod = db.rawQuery("SELECT * FROM '"+cat+"' WHERE alimento='"+alim+"'",null);

            while (prod.moveToNext()) {
                aux = prod.getString(1);
                if (aux == null || aux.equalsIgnoreCase("0") || aux.equalsIgnoreCase("traza") || aux.equalsIgnoreCase("trazas") || aux.equalsIgnoreCase("-")) aux = "0.0";
                aux=aux.replace(',', '.');

                System.out.println(aux);
                azucar = Float.parseFloat(aux);

                aux = prod.getString(2);
                if (aux == null || aux.equalsIgnoreCase("0") || aux.equalsIgnoreCase("traza") || aux.equalsIgnoreCase("trazas") || aux.equalsIgnoreCase("-")) aux = "0.0";
                aux=aux.replace(',', '.');

                System.out.println(aux);
                grasas = Float.parseFloat(aux);

                aux = prod.getString(3);
                if (aux == null || aux.equalsIgnoreCase("0") || aux.equalsIgnoreCase("traza") || aux.equalsIgnoreCase("trazas") || aux.equalsIgnoreCase("-")) aux = "0.0";
                aux=aux.replace(',', '.');
                System.out.println(aux);
                sodio = Float.parseFloat(aux);
            }

            Bundle datis = new Bundle();
            if (cat.equalsIgnoreCase("frutas")) {veredicto=3; veredictoG=1; veredictoS=1; veredictoA=1;}
            else {
                if (grasas >= 5) veredictoG = 3;
                else if (grasas < 5 && grasas > 1.5) veredictoG = 2;
                else veredictoG = 1;
                if (azucar >= 10) veredictoA = 3;
                else if (azucar < 10 && azucar >= 5) veredictoA = 2;
                else veredictoA = 1;
                if (sodio >= 600) veredictoS = 3;
                else if (sodio < 600 & sodio >= 120) veredictoS = 2;
                else veredictoS = 1;

                veredicto = veredictoS + veredictoA + veredictoG;
            }
            //el veredicto final es la suma de los veredictos individuales, de forma que:
            // <=3 es verde, entre 4 y 6 es naranja y >=6 es rojo, lo cual debería ajustarse a los requisitos dados

            datis.putInt("veredictoA",veredictoA);
            datis.putInt("veredictoG",veredictoG);
            datis.putInt("veredictoS",veredictoS);
            datis.putInt("veredicto",veredicto);
            datis.putString("alimento", alim);
            datis.putFloat("azucar", azucar);
            datis.putFloat("grasas", grasas);
            datis.putFloat("sodio", sodio);
            i.putExtras(datis);
            setResult(RESULT_OK,i);

        }catch (Exception e){e.getMessage(); i.putExtras(new Bundle());setResult(RESULT_CANCELED,i);}
        finish();
    }
}