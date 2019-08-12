package com.myleads.android.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.myleads.android.R;
import com.myleads.android.custom.LoaderDialog;
import com.myleads.android.custom.PrefManager;
import com.myleads.android.custom.Validator;
import com.myleads.android.model.RegisterModel;
import com.myleads.android.model.TokenModel;
import com.myleads.android.retrovit.MyLeadsAPI;
import com.myleads.android.retrovit.RetrofitClient;
import com.myleads.android.retrovit.pojo.GeneralResponseData;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {
    private String email,password,fullname,mobile,company;
    private TextInputLayout fullnameWrapper, passwordWrapper, emailWrapper, mobileWrapper,companyWrapper;
    private Validator validator;
    private String TAG = RegisterActivity.class.getSimpleName();
    private String API_URL;
    private TextView agree;
    private LoaderDialog loader;
    private PrefManager prefManager;
    private  Button submit;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        API_URL = getString(R.string.api_url);
        prefManager = new PrefManager(this);
        context = this;

        validator = new Validator();
        fullnameWrapper = (TextInputLayout) findViewById(R.id.fullnameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        mobileWrapper = (TextInputLayout) findViewById(R.id.mobileWrapper);
        companyWrapper = (TextInputLayout) findViewById(R.id.companyWrapper);


        submit = (Button) findViewById(R.id.nextBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fullname = fullnameWrapper.getEditText().getText().toString();
                email = emailWrapper.getEditText().getText().toString();
                password = passwordWrapper.getEditText().getText().toString();
                mobile = mobileWrapper.getEditText().getText().toString();
                company = companyWrapper.getEditText().getText().toString();

                register();
            }
        });




    }

    public void register() {


       if (!validate()) {
            displayMessage("Registration Failed");
            return;
        }

        submit.setEnabled(false);
        loader = new LoaderDialog();
        loader.show(getFragmentManager());

        makeRequest();
    }


    public boolean validate() {
        boolean valid = true;
        Log.i(TAG, String.valueOf(validator.validateEmail(email)));
        if (email.isEmpty() || !validator.validateEmail(email)) {
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

        if (fullname.isEmpty()) {
            fullnameWrapper.setError("enter valid name");
            valid = false;
        } else {
            fullnameWrapper.setError(null);
        }

        if (mobile.isEmpty() || !validator.validateMobile(mobile)) {
            mobileWrapper.setError("enter valid mobile");
            valid = false;
        } else {
            mobileWrapper.setError(null);
        }

        return valid;
    }

    protected void makeRequest() {

        Retrofit retrofit = RetrofitClient.getClient(this);
        MyLeadsAPI api = retrofit.create(MyLeadsAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("full_name", fullname);
        params.put("phone", mobile);
        params.put("company", company);

        Observable<GeneralResponseData<TokenModel>> call = api.register(params);
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
                    Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context,value.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {

                Log.i("RegisterError",e.toString());
                submit.setEnabled(true);
                loader.dismiss();


                HttpException error = (HttpException)e;
                ResponseBody body = error.response().errorBody();

                if(error.code() == 422){
                    displayMessage("Email already exist");
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


    public void displayMessage(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();

    }



}
