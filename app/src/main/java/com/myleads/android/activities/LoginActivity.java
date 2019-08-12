package com.myleads.android.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.myleads.android.MyApplication;
import com.myleads.android.R;
import com.myleads.android.custom.LoaderDialog;
import com.myleads.android.custom.PrefManager;
import com.myleads.android.model.TokenModel;
import com.myleads.android.retrovit.MyLeadsAPI;
import com.myleads.android.retrovit.RetrofitClient;
import com.myleads.android.retrovit.pojo.GeneralResponseData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailWrapper,passwordWrapper;
    private Button signinBTN;
    private String email,password;
    private String TAG = LoginActivity.class.getSimpleName();
    private String API_URL;
    private PrefManager prefManager;
    private LoaderDialog loader;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        API_URL = getString(R.string.api_url);
        prefManager = new PrefManager(this);

        signinBTN = (Button) findViewById(R.id.signinBTN);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);


        signinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailWrapper.getEditText().getText().toString();
                password = passwordWrapper.getEditText().getText().toString();
                login();
            }
        });

    }



    public void login() {

        if (!validate()) {
            displayMessage("Login Fialed");
            return;
        }

        signinBTN.setEnabled(false);
        loader = new LoaderDialog();
        loader.show(getFragmentManager());
        makeRequest();
    }


    public boolean validate() {
        boolean valid = true;



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailWrapper.setError("enter a valid email address");
            valid = false;
        } else {
            emailWrapper.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            passwordWrapper.setError("Password must be atleast 6 characters");
            valid = false;
        } else {
            passwordWrapper.setError(null);
        }

        return valid;
    }

    public void displayMessage(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();

        signinBTN.setEnabled(true);
    }

    protected void makeRequest() {

        Retrofit retrofit = RetrofitClient.getClient(this);
        MyLeadsAPI api = retrofit.create(MyLeadsAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        Observable<GeneralResponseData<TokenModel>> call = api.login(params);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GeneralResponseData<TokenModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GeneralResponseData<TokenModel> value) {
                Log.e("LoginBlah",value.getMessage());
                if(value.getSuccess()){
                    TokenModel token = value.getData();
                    prefManager.setToken(token.getToken());
                    prefManager.setUserId(String.valueOf(token.getId()));
                    loader.dismiss();
                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context,value.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {

                Log.i(TAG,e.toString());
                signinBTN.setEnabled(true);
                loader.dismiss();


                HttpException error = (HttpException)e;
                ResponseBody body = error.response().errorBody();

                if(error.code() == 404){
                    displayMessage("Invalid Login credentials");
                }
                if(error.code() == 500){
                    displayMessage("Server Error");
                }

            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
