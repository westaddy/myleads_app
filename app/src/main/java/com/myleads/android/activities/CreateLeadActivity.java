package com.myleads.android.activities;

import android.content.Context;
import android.content.Intent;
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
import com.myleads.android.model.LeadModel;
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


public class CreateLeadActivity extends AppCompatActivity {
    private String title,email,password,name,middleName,lastName,phone,company,position,source,suburb,city,province,address;
    private TextInputLayout titleWrapper, nameWrapper,middleWrapper,lastWrapper, emailWrapper, phoneWrapper,companyWrapper;
    private TextInputLayout sourceWrapper,positionWrapper,addressWrapper,suburbWrapper,cityWrapper,provinceWrapper;
    private Validator validator;
    private String TAG = CreateLeadActivity.class.getSimpleName();
    private String API_URL;
    private TextView agree;
    private LoaderDialog loader;
    private PrefManager prefManager;
    private  Button submit;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead);

        prefManager = new PrefManager(this);
        context = this;

        validator = new Validator();
        titleWrapper = (TextInputLayout) findViewById(R.id.titleWrapper);
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        middleWrapper = (TextInputLayout) findViewById(R.id.middleWrapper);
        lastWrapper = (TextInputLayout) findViewById(R.id.lastWrapper);
        titleWrapper = (TextInputLayout) findViewById(R.id.titleWrapper);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);
        companyWrapper = (TextInputLayout) findViewById(R.id.companyWrapper);
        positionWrapper = (TextInputLayout) findViewById(R.id.positionWrapper);
        addressWrapper = (TextInputLayout) findViewById(R.id.addressWrapper);
        sourceWrapper = (TextInputLayout) findViewById(R.id.sourceWrapper);
        cityWrapper = (TextInputLayout) findViewById(R.id.cityWrapper);
        provinceWrapper = (TextInputLayout) findViewById(R.id.provinceWrapper);
        suburbWrapper = (TextInputLayout) findViewById(R.id.suburbWrapper);


        submit = (Button) findViewById(R.id.postLead);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = titleWrapper.getEditText().getText().toString();
                name = nameWrapper.getEditText().getText().toString();
                email = emailWrapper.getEditText().getText().toString();
                middleName = middleWrapper.getEditText().getText().toString();
                phone = phoneWrapper.getEditText().getText().toString();
                lastName = lastWrapper.getEditText().getText().toString();
                company = companyWrapper.getEditText().getText().toString();
                source = sourceWrapper.getEditText().getText().toString();
                position = positionWrapper.getEditText().getText().toString();
                address = addressWrapper.getEditText().getText().toString();
                suburb = suburbWrapper.getEditText().getText().toString();
                city = cityWrapper.getEditText().getText().toString();
                province = provinceWrapper.getEditText().getText().toString();

                create();
            }
        });




    }

    public void create() {


       if (!validate()) {
            displayMessage("Failed");
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


        if (name.isEmpty()) {
            nameWrapper.setError("enter valid name");
            valid = false;
        } else {
            nameWrapper.setError(null);
        }

        if (middleName.isEmpty()) {
            middleWrapper.setError("enter valid name");
            valid = false;
        } else {
            middleWrapper.setError(null);
        }
        if (lastName.isEmpty()) {
            lastWrapper.setError("enter valid name");
            valid = false;
        } else {
            lastWrapper.setError(null);
        }
        if (title.isEmpty()) {
            titleWrapper.setError("enter valid name");
            valid = false;
        } else {
            titleWrapper.setError(null);
        }

        if (phone.isEmpty() || !validator.validateMobile(phone)) {
            phoneWrapper.setError("enter valid mobile");
            valid = false;
        } else {
            phoneWrapper.setError(null);
        }

        if (company.isEmpty()) {
            companyWrapper.setError("enter valid company");
            valid = false;
        } else {
            companyWrapper.setError(null);
        }
        if (source.isEmpty()) {
            sourceWrapper.setError("enter valid source");
            valid = false;
        } else {
            sourceWrapper.setError(null);
        }
        if (position.isEmpty()) {
            positionWrapper.setError("enter valid name");
            valid = false;
        } else {
            positionWrapper.setError(null);
        }
        if (address.isEmpty()) {
            addressWrapper.setError("enter valid address");
            valid = false;
        } else {
            addressWrapper.setError(null);
        }
        if (suburb.isEmpty()) {
            suburbWrapper.setError("enter valid suburb");
            valid = false;
        } else {
            suburbWrapper.setError(null);
        }
        if (city.isEmpty()) {
            cityWrapper.setError("enter valid city");
            valid = false;
        } else {
            cityWrapper.setError(null);
        }
        if (province.isEmpty()) {
            provinceWrapper.setError("enter valid name");
            valid = false;
        } else {
            provinceWrapper.setError(null);
        }

        return valid;
    }

    protected void makeRequest() {

        Retrofit retrofit = RetrofitClient.getClient(this);
        MyLeadsAPI api = retrofit.create(MyLeadsAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("title", title);
        params.put("name", name);
        params.put("middle_name", middleName);
        params.put("last_name", lastName);
        params.put("phone", phone);
        params.put("company", company);
        params.put("source", source);
        params.put("position", position);
        params.put("address", address);
        params.put("city", city);
        params.put("province", province);
        params.put("suburb", suburb);

        Observable<GeneralResponseData<LeadModel>> call = api.createLead(params);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GeneralResponseData<LeadModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GeneralResponseData<LeadModel> value) {
                Log.e("LoginBlah",value.getMessage());
                if(value.getSuccess()){
                    loader.dismiss();
                    Intent intent = new Intent(CreateLeadActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context,value.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {

                Log.e("RegisterError",e.toString());
                submit.setEnabled(true);
                loader.dismiss();


                HttpException error = (HttpException)e;
                ResponseBody body = error.response().errorBody();

                if(error.code() == 422){
                    displayMessage("This contact already exist");
                }
                if(error.code() == 500){
                    displayMessage("Server Error");
                }

            }

            @Override
            public void onComplete() {
                Toast.makeText(context,"Contact has been added",Toast.LENGTH_LONG).show();
            }
        });

    }


    public void displayMessage(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();

    }



}
