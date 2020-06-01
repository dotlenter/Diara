package com.example.zo.diara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zo.diara.ModelClasses.LoginModel;
import com.example.zo.diara.ModelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpSuccess extends AppCompatActivity {

    private android.app.AlertDialog alertDialog;
    private ConfigPref configPref;
    private LoginActivity loginActivity;
    private Button btnToLogin;
    private FirebaseAuth mAuth;
    private String userImage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        configPref = new ConfigPref(this);
        btnToLogin = findViewById(R.id.btnToLogin);
        loginActivity = new LoginActivity();
        mAuth = FirebaseAuth.getInstance();

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                alertDialog.show();
                loginUserWithEmailAndPassword(extras.getString("username"), extras.getString("password"));
            }
        });

    }
    public void performLogin(final String uid, final String pwd) {
        Call<User> call = ApiClient
                .getInstance()
                .getApi()
                .performLogin(uid, pwd);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String resp = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            resp = response.body().getStatus();

                        } catch (Exception ex) {
                            resp = "error";
                        }
                        if (resp.toLowerCase().equals("ok")) {
                            firebaseSignIn(uid, pwd, response.body().getAuth_user_id(), response.body().getAuth_token());

                        } else {
                            alertDialog.dismiss();
                        }
                    }else{
                        alertDialog.dismiss();
                    }
                }else{
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
    }

    public void loginUserWithEmailAndPassword(final String email, final String pass){
        Call<LoginModel> loginModelCall = ApiClient
                .getInstance()
                .getApi()
                .loginUserWithEmailAndPassword(email, pass);
        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.isSuccessful() && response.body() !=null){
                    Log.i("diaraLoginSuccess","Found 1 User with email and password.");
                    Log.i("diaraLog", response.body().getMessage());
                    userImage = response.body().getAuthUserImgUrl();
                    firebaseSignIn(
                            email,
                            pass,
                            response.body().getAuthUserId(),
                            response.body().getAuthToken());
                }else{
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
    }
    public void firebaseSignIn(final String email, String password, final long userID, final String token){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alertDialog.dismiss();
                            configPref.writeLoginStatus(true, userID, token, email, userImage);
                            Intent toMain = new Intent(SignUpSuccess.this, MainActivity.class);
                            startActivity(toMain);
                            finishAffinity();
                        }else{
                            alertDialog.dismiss();
                        }
                    }
                });
    }
}
