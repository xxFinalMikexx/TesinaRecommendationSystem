package com.example.xxfin.tesinarecommendationsystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/*Firebase imports*/
import com.example.xxfin.tesinarecommendationsystem.Objects.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /*Firebase*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabaseReference;
    
    /*Buttons and Views*/
    private EditText mEmailField;
    private EditText mPasswordField;

    /*Tag for log*/
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        //Initialize authentication
        this.mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        
        
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
    }
    
    private void updateUI(final FirebaseUser user) {
        if (user != null) {
            //TODO retrieve information from DB
            //Ignores questions and go directly to activity
            /*Firebase reference to DB to get current user information*/
            mFirebaseDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        if(!dataSnapshot.hasChildren()) {
                            Log.e("No children", "-");
                            Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("userId", user.getUid());

                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("Children", "-");
                            boolean flag = true;
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                Users userObj = userSnap.getValue(Users.class);
                                if(userObj.getUserId().equals(user.getUid())) {
                                    Log.e("Usuario", user.getUid());
                                    /*User has already answered the questions. Go directly to*/

                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    mainIntent.putExtra("email", user.getEmail());
                                    mainIntent.putExtra("userId", user.getUid());
                                    mainIntent.putExtra("userType", userObj.getTipo());
                                    mainIntent.putExtra("genero", userObj.getGenero());
                                    mainIntent.putExtra("edad", userObj.getRangoEdad());
                                    startActivity(mainIntent);
                                    flag = false;
                                    finish();
                                    break;
                                }
                            }
                            if(flag) {
                                Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                                intent.putExtra("email", user.getEmail());
                                intent.putExtra("userId", user.getUid());

                                startActivity(intent);
                                finish();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error en UpdateUI", ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getApplicationContext(), "Se ha interrumpido la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }
    
    public void createAccount(View v) {
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            Toast.makeText(getApplicationContext(), "Creando cuenta...", Toast.LENGTH_LONG).show();
            if(!validateForm()) {
                Log.e("Error Account", email + " - " + password);
                return;   
            } else {
                Toast.makeText(getApplicationContext(), "Validate Form ok...", Toast.LENGTH_LONG).show();
                // [START create_user_with_email]
                this.mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Error de creación de Sesión",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
    }
    
    public void signIn(View v) {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.e("Iniciando sesión...", email + " -  " + password);
        if (!validateForm()) {
            return;
        } else {
            // [START sign_in_with_email]
            this.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Error de inicio de Sesión",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error :( ", e.getMessage());
                        }
                    });
        }
    }
    
    public void signOut(View v) {
        mAuth.signOut();
        updateUI(null);
    }
    
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

