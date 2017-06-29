package com.example.xxfin.tesinarecommendationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xxfin.tesinarecommendationsystem.Objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuestionActivity extends AppCompatActivity {
    /*Firebase DB reference*/
    private DatabaseReference mFirebaseDatabaseReference;

    private String edadesList[] = {"Entre 15 y 25", "Entre 26 y 35", "Entre 36 y 45", "Mayor de 45"};
    private String generoList[] = {"Masculino", "Femenino"};
    
    private String email = "";
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            email = (String) extras.get("email");
            userId = (String) extras.get("userId");
        } else {
            //TODO there are no information to retrieve
            //Back to the login
            Intent mainIntent = new Intent(QuestionActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
            
        }

        Spinner generoSpin = (Spinner) findViewById(R.id.genero);
        Spinner edadesSpin = (Spinner) findViewById(R.id.rangoEdad);

        ArrayAdapter<String> generoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.generoList);
        ArrayAdapter<String> edadedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.edadesList);
        generoSpin.setAdapter(generoAdapter);
        edadesSpin.setAdapter(edadedAdapter);
      
    }

    public void registrarInfo(View v) {
        EditText nombreUsuario = (EditText) findViewById(R.id.nombre);
        Spinner generoSpin = (Spinner) findViewById(R.id.genero);
        Spinner edadesSpin = (Spinner) findViewById(R.id.rangoEdad);

        String generoSeleccionado = generoSpin.getSelectedItem().toString();
        String edadSeleccionada = edadesSpin.getSelectedItem().toString();

        int edadTipo;
        switch(edadSeleccionada) {
            case "Entre 15 y 25":
                edadTipo = 1;
                break;
            case "Entre 26 y 35":
                edadTipo = 2;
                break;
            case "Entre 36 y 45":
                edadTipo = 3;
                break;
            case "Mayor de 45":
                edadTipo = 4;
                break;
            default:
                edadTipo = -1;
        }
        
        int tipoUsuario = getUserType(generoSeleccionado, edadTipo);

        String nombreSeleccionado = nombreUsuario.getText().toString();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Users user = new Users(userId, nombreSeleccionado, generoSeleccionado, edadTipo, tipoUsuario, email);

        String key = mFirebaseDatabaseReference.child("Users").push().getKey();
        mFirebaseDatabaseReference.child("Users").child(key).setValue(user);

        Intent mainIntent = new Intent(QuestionActivity.this, MainActivity.class);
        mainIntent.putExtra("email", user.getEmail());
        mainIntent.putExtra("userId", user.getUserId());
        mainIntent.putExtra("userType", user.getTipo());
        mainIntent.putExtra("genero", user.getGenero());
        mainIntent.putExtra("edad", user.getRangoEdad());
        startActivity(mainIntent);
        this.finish();
    }
    
    public int getUserType(String genero, int edad) {
        int generoType = -1;
        switch(genero) {
            case "Masculino":
                generoType = 1;
                break;
            case "Femenino":
                generoType = 2;
                break;
        }
        
        int userType = -1;
        if(generoType == 1) {
            if(edad == 1) {
                userType = 1;
            } else if(edad == 2) {
                userType = 2;
            } else if(edad == 3) {
                userType = 3;
            } else if(edad == 4) {
                userType = 4;
            } else {
                //TODO not defined   
                userType = -1;
            }
        } else if(generoType == 2) {
            if(edad == 1) {
                userType = 5;
            } else if(edad == 2) {
                userType = 6;
            } else if(edad == 3) {
                userType = 7;
            } else if(edad == 4) {
                userType = 8;
            } else {
                //TODO not defined   
                userType = -1;
            }
        } else {
            //TODO not defined   
            userType = -1;
        }
        return userType;
    }
    

}
