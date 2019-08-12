package com.myleads.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.myleads.android.R;
import com.myleads.android.custom.ExcelCreator;
import com.myleads.android.custom.LoaderDialog;
import com.myleads.android.dummy.DummyContent.DummyItem;
import com.myleads.android.model.LeadModel;
import com.myleads.android.retrovit.MyLeadsAPI;
import com.myleads.android.retrovit.RetrofitClient;
import com.myleads.android.retrovit.pojo.GeneralResponseData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;


public class ReportFragment extends Fragment {

    private Button generate;
    private Context context;
    private DatePicker datePicker;
    private TextView selectDate,selectedDateValue;
    private LoaderDialog loader;




    public ReportFragment() {
    }


    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        generate = (Button) view.findViewById(R.id.generate);
        selectDate = (TextView) view.findViewById(R.id.selectDate);
        selectedDateValue = (TextView) view.findViewById(R.id.selectDateValue);

        initDatePicker();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDateValue.getText().toString().equals("")){
                    displayMessage("Please select a date");
                }
                else{
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        return;
                    }
                    view.setEnabled(false);
                    loader = new LoaderDialog();
                    loader.show(getActivity().getFragmentManager());
                    generate();
                }
            }
        });
        return view;
    }

    protected  void initDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selectedDateValue.setText(sdf.format(calendar.getTime()));
            }

        };
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatePickerDialog picker = new DatePickerDialog(context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                picker.setButton(DialogInterface.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    DatePicker datePicker = picker.getDatePicker();
                                    date.onDateSet(datePicker,
                                            datePicker.getYear(),
                                            datePicker.getMonth(),
                                            datePicker.getDayOfMonth());
                                }
                            }
                        });


                picker.setCancelable(false);
                picker.show();
                picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);

            }
        });
    }

    protected void generate() {

        Retrofit retrofit = RetrofitClient.getClient(context);
        MyLeadsAPI api = retrofit.create(MyLeadsAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("created_at",selectedDateValue.getText().toString());

        Observable<GeneralResponseData<ArrayList<LeadModel>>> call = api.getLeads(params);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GeneralResponseData<ArrayList<LeadModel>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GeneralResponseData<ArrayList<LeadModel>> value) {
                if(value.getSuccess()){
                    loader.dismiss();

                    ArrayList<LeadModel> list = value.getData();

                    if(list.size() >=1){
                        ExcelCreator creator = new ExcelCreator();
                        creator.save("MyLeadsReport.xls",list);
                        displayAlert("Report successfully created, Click View to view","MyLeadsReport.xls");
                    }
                    else{
                        displayMessage("There is no report to generate");
                    }

                }
                else{
                    displayMessage(value.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {

                generate.setEnabled(true);
                loader.dismiss();


                HttpException error = (HttpException)e;
                ResponseBody body = error.response().errorBody();

                if(error.code() == 500){
                    displayMessage("Server Error");
                }

            }

            @Override
            public void onComplete() {
                generate.setEnabled(true);

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //This method is responsible for loading more errands


    public void displayMessage(String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    public void displayAlert(String message, final String filename){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        builder.setMessage(message)
                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        File externalFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/EXCEL/", filename);
                        Uri path = Uri.fromFile(externalFile);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Uri apkURI = FileProvider.getUriForFile(
                                context,
                                context.getApplicationContext()
                                        .getPackageName() + ".provider", externalFile);
                        intent.setDataAndType(apkURI, "application/vnd.ms-excel");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            startActivity(intent);
                        }
                        catch (ActivityNotFoundException e) {
                            displayMessage("No Application Available to View Excel");
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
