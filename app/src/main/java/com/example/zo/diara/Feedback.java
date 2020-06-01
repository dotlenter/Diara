package com.example.zo.diara;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zo.diara.ModelClasses.StatMessageModel;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback extends AppCompatActivity {

    private Button btnSubmitFeedback;
    private EditText textFeedBody;
    private ConfigPref configPref;
    private RatingBar ratingBar;
    private AlertDialog alertDialogLoad;
    private String feedBody = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        textFeedBody = findViewById(R.id.textFeedback);
        configPref = new ConfigPref(this);
        ratingBar = findViewById(R.id.ratingFeedback);
        alertDialogLoad = new SpotsDialog.Builder().setContext(this).build();

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textFeedBody.getText().toString().isEmpty()){

                    final AlertDialog alertDialog = new AlertDialog.Builder(Feedback.this)
                            .setIcon(R.drawable.icon_sad)
                            .setTitle("Awwww!")
                            .setMessage("Are you sure you don't have anything in mind? We'd love to hear something from you.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    feedBody = "N/A";
                                    alertDialogLoad.show();
                                    createFeedback();
                                    textFeedBody.setText("");
                                    ratingBar.setRating(4);
                                }
                            })
                            .setNegativeButton("No, I'll put something", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }else{
                    feedBody = textFeedBody.getText().toString();
                    alertDialogLoad.show();
                    createFeedback();
                    textFeedBody.setText("");
                    ratingBar.setRating(4);
                }
            }
        });

    }

    public void createFeedback(){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .createFeedback(
                        configPref.getUserID(),
                        feedBody,
                        ratingBar.getRating());
        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    alertDialogLoad.dismiss();
                    Log.i("onSuccessCall",response.body().getMessage());
                    Toast.makeText(Feedback.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    alertDialogLoad.dismiss();
                    Log.i("onErrorResponse",response.errorBody().toString());
                    Toast.makeText(Feedback.this, "Something went wrong! More detail: " + response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                alertDialogLoad.dismiss();
                Log.i("onFailureCall",t.getLocalizedMessage());
                Toast.makeText(Feedback.this, "Something went wrong! More detail: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
