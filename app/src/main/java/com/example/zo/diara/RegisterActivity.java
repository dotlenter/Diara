package com.example.zo.diara;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zo.diara.ModelClasses.StatMessageModel;
import com.example.zo.diara.ModelClasses.User;
import com.example.zo.diara.RecyclableFunctions.DeviceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText textDateOfBirth, textEmail, textPassword, textConfirmPass;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button btnSelectDate, btnRegister;
    private int year = Calendar.getInstance().get(Calendar.YEAR), userSetYear, userSetMonth, userSetDay;
    private ConfigPref configPref;
    private android.app.AlertDialog alertDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Toast toast_green, toast_red;
    private TextView toastText, toastTextError;
    private LayoutInflater layoutInflater;
    private View layout, layout_err;
    private DeviceModel deviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register Account");

        initializeControls();

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                userSetYear = year;
                userSetMonth = month;
                userSetDay = day;
                String date = day + "/" + month + "/" + year;
                textDateOfBirth.setText(date);
            }
        };

        textEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
        textPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
        textConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;

                if (textEmail.getText()
                        .toString()
                        .isEmpty()) {
                    textEmail.setError("Invalid Email Address.");
                    flag++;
                }
                if(textPassword.getText()
                .toString()
                .isEmpty()){
                    textPassword.setError("Pass must be at least 6 characters");
                    flag++;
                }
                if (!textConfirmPass.getText()
                        .toString()
                        .equals(textPassword.getText().toString())) {
                    textConfirmPass.setError("Password does not match!");
                    flag++;
                }
                if ((Calendar.getInstance().get(Calendar.YEAR) - userSetYear) < 13) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Under 13 Years old policy")
                            .setMessage("You're too young for this app! You must be 18 Years old or above to be able to use this app.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    flag++;
                }
                if (flag == 0) {
                    alertDialog.show();
                    firebaseSignUp(textEmail.getText().toString(), textConfirmPass.getText().toString());
                }

            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void createUser(){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .createUser(
                        textEmail.getText().toString(),
                        mUser.toString(),
                        String.valueOf(userSetYear + "-" + userSetMonth + "-" + userSetDay),
                        deviceModel.getDeviceName(),
                        textConfirmPass.getText().toString(),
                        "pZdsBajiGIB+t9SzKjzhAeiUcz89PBDU6st80l8re4w="
                        );
        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getStatus().toLowerCase().equals("yes")){
                        alertDialog.dismiss();
                        Intent toSuccess = new Intent(RegisterActivity.this, SignUpSuccess.class);
                        toSuccess.putExtra("username", textEmail.getText().toString());
                        toSuccess.putExtra("password", textConfirmPass.getText().toString());
                        startActivity(toSuccess);
                        finishAffinity();
                    }else{
                        deleteFirebaseUser(textEmail.getText().toString(), textConfirmPass.getText().toString());
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                }else{
                    deleteFirebaseUser(textEmail.getText().toString(), textConfirmPass.getText().toString());
                    Log.i("onResponseUnsuccessfull", response.errorBody().toString());
                    Toast.makeText(RegisterActivity.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                deleteFirebaseUser(textEmail.getText().toString(), textConfirmPass.getText().toString());
                Log.i("onFailure-Call-Diara", t.getLocalizedMessage());
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

    }

    public void registerUser(final String email, final String password, String birthDate, String token, String firebase_id) {
        Call<User> call = ApiClient
                .getInstance()
                .getApi()
                .createUserWithEmailAndPassword(email, password, birthDate, token, firebase_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String resp = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            resp = response.body().getStatus();
                        } catch (Exception ex) {
                            resp = "Error: " + ex.toString();
                        }
                        if (resp.toLowerCase().equals("ok")) {
                            alertDialog.dismiss();
                            Intent toSuccess = new Intent(RegisterActivity.this, SignUpSuccess.class);
                            toSuccess.putExtra("username", email);
                            toSuccess.putExtra("password", password);
                            startActivity(toSuccess);
                            finishAffinity();
                        } else {
                            alertDialog.dismiss();
                            if (response.body().getMessage().toLowerCase().equals("user already exists!")) {
                                textEmail.setError("Email already registered!");
                            }
                        }
                    }else{
                        deleteFirebaseUser(email, password);
                        Log.i("errfailure&RESPBODYNUL","NULL RESPONSE");
                        alertDialog.dismiss();
                    }
                }else{
                    deleteFirebaseUser(email, password);
                    Log.i("errfailure&TASKUNSUC", response.errorBody().toString());
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                deleteFirebaseUser(email, password);
                Log.i("errfailure&FAILRESPONSE", t.getLocalizedMessage());
                alertDialog.dismiss();
            }
        });
    }

    public void deleteFirebaseUser(String email, String password){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Success[DIARA]: ", "Deleted Entry account due to failure in inserting to Main Database.");
                                        }
                                    }
                                });
                    }
                });
    }

    public void firebaseSignUp(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.i("success_firebase_signup", "Firebase signup success.");
                            mUser = mAuth.getCurrentUser();
                            createUser();
                        }  else{
                            alertDialog.dismiss();
                            Log.i("errfailure_&01", task.getException().toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertDialog.dismiss();
                        Log.i("errfailure_&01", "Firebase signup failed. Error: " + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void initializeControls() {
        textDateOfBirth = findViewById(R.id.textDateOfBirth);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        textConfirmPass = findViewById(R.id.textConfirmPass);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnRegister = findViewById(R.id.btnRegister);
        configPref = new ConfigPref(this);
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        mAuth = FirebaseAuth.getInstance();
        deviceModel = new DeviceModel();
        layoutInflater = getLayoutInflater();

        layout = layoutInflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        layout_err = layoutInflater.inflate(R.layout.toast_red,
                (ViewGroup) findViewById(R.id.layout_toast_red));


        toastText = layout.findViewById(R.id.text);
        toastTextError = layout_err.findViewById(R.id.errText);

        toast_green = new Toast(getApplicationContext());
        toast_green.setGravity(Gravity.BOTTOM, 0, 150);
        toast_green.setDuration(Toast.LENGTH_SHORT);

        toast_red = new Toast(getApplicationContext());
        toast_red.setGravity(Gravity.BOTTOM, 0, 150);
        toast_red.setDuration(Toast.LENGTH_SHORT);
    }
}
