package com.example.zo.diara;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private EditText dEmail;
    private EditText dPassword;
    private Button dProceed;
    private ConfigPref configPref;
    private android.app.AlertDialog alertDialog;
    private LayoutInflater layoutInflater;
    private View layout, layout_err;
    private Toast toast_green, toast_red;
    private TextView toastText, toastTextError;
    private FirebaseAuth mAuth;
    private String userImage="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dEmail = findViewById(R.id.txtEmail);
        dPassword = findViewById(R.id.txtPassword);
        dProceed = findViewById(R.id.btnProceedLogin);
        configPref = new ConfigPref(getApplicationContext());
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        mAuth = FirebaseAuth.getInstance();

        layoutInflater = getLayoutInflater();

        layout = layoutInflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        layout_err = layoutInflater.inflate(R.layout.toast_red,
                (ViewGroup) findViewById(R.id.layout_toast_red));


        toastText = layout.findViewById(R.id.text);
        toastTextError = layout_err.findViewById(R.id.errText);

        toast_green = new Toast(this);
        toast_green.setGravity(Gravity.BOTTOM, 0, 150);
        toast_green.setDuration(Toast.LENGTH_SHORT);

        toast_red = new Toast(this);
        toast_red.setGravity(Gravity.BOTTOM, 0, 150);
        toast_red.setDuration(Toast.LENGTH_SHORT);

        dEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

        dPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        dProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if (dEmail.length() == 0) {
                    dEmail.setError("Please enter a valid Email address.");
                    flag++;
                }
                if (dPassword.length() < 4 || dPassword.length() > 50) {
                    dPassword.setError("Your password must contain between 4 and 50 characters.");
                    flag++;
                }

                if (flag == 0) {
                    alertDialog.show();
                    loginUserWithEmailAndPassword();
                }

            }
        });

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loginUserWithEmailAndPassword(){
        Call<LoginModel> loginModelCall = ApiClient
                .getInstance()
                .getApi()
                .loginUserWithEmailAndPassword(dEmail.getText().toString(), dPassword.getText().toString());
        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.isSuccessful() && response.body() !=null){
                    Log.i("diaraLoginSuccess","Found 1 User with email and password.");
                    Log.i("diaraLog", response.body().getMessage());
                    userImage = response.body().getAuthUserImgUrl();
                    firebaseSignIn(
                            dEmail.getText().toString(),
                            dPassword.getText().toString(),
                            response.body().getAuthUserId(),
                            response.body().getAuthToken());
                }else{
                    alertDialog.dismiss();
                    toastTextError.setText(getString(R.string.notestablished));
                    toast_red.setView(layout_err);
                    toast_red.show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                alertDialog.dismiss();
                toastTextError.setText("There's an error! More details: " + t.getMessage());
                toast_red.setView(layout_err);
                toast_red.show();
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
                            toastTextError.setText(response.body().getMessage());
                            toast_red.setView(layout_err);
                            toast_red.show();
                        }
                    }else{
                        alertDialog.dismiss();
                        toastTextError.setText(getString(R.string.unavailableacct));
                        toast_red.setView(layout_err);
                        toast_red.show();
                    }
                }else{
                    alertDialog.dismiss();
                    toastTextError.setText(getString(R.string.internetConnPrompt));
                    toast_red.setView(layout_err);
                    toast_red.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                alertDialog.dismiss();
                toastTextError.setText(t.getMessage());
                toast_red.setView(layout_err);
                toast_red.show();
            }
        });
    }

    public void dismissAndPrompt(int type, String promptMessage, View view){
        alertDialog.dismiss();
        toastTextError.setText(promptMessage);
        toast_red.setView(view);
        toast_red.show();

    }
    public void firebaseSignIn(final String email, String password, final long userID, final String token){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alertDialog.dismiss();
                            configPref.writeLoginStatus(true, userID, token, email, userImage);
                            toMain();
                        }else{
                            alertDialog.dismiss();
                            toastTextError.setText(task.getException().toString());
                            toast_red.setView(layout_err);
                            toast_red.show();
                        }
                    }
                });
    }

    public void toMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
