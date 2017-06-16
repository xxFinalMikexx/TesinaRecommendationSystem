package com.example.xxfin.tesinarecommendationsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.xxfin.tesinarecommendationsystem.Objects.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mFirebaseDatabaseReference;

    private String edadesList[] = {"Entre 15 y 25", "Entre 26 y 40", "Entre 41 y 60", "Mayor de 60"};
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
            
            //TODO retrieve information from DB
            //Ignores questions and go directly to activity
            /*Firebase reference to DB to get current user information*/
            mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                            Users userObj = userSnap.getValue(Users.class);
                            if(userObj.getUserId().equals(userId)) {
                                /*User has already answered the questions. Go directly to */
                                Intent mainIntent = new Intent(QuestionActivity.this, PrincipalActivity.class);
                                startActivity(mainIntent);
                                finish();
                                break;
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context.getApplicationContext(), "Se ha interrumpido la conexión", Toast.LENGTH_SHORT).show();
                }
            });
            
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
            case "Entre 26 y 40":
                edadTipo = 2;
                break;
            case "Entre 41 y 60":
                edadTipo = 3;
                break;
            case "Mayor de 60":
                edadTipo = 4;
                break;
            default:
                edadTipo = -1;
        }

        String nombreSeleccionado = nombreUsuario.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Users user = new Users(userId, nombreSeleccionado, generoSeleccionado, edadTipo, -1, email);

        DatabaseReference products = mFirebaseDatabaseReference.child("Users");
        String key = mFirebaseDatabaseReference.child("Users").push().getKey();

        mDatabase.child("users").child(key).setValue(user);

        Intent mainIntent = new Intent(QuestionActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
