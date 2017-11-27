package com.gajananmotors.shopfinder.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.utility.Constant;
import com.gajananmotors.shopfinder.utility.Validation;
import com.hbb20.CountryCodePicker;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

import com.gajananmotors.shopfinder.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etEmail, etContactNumber, etPassword, etConfirmPassword, etDate;
    private int mYear, mMonth, mDay;
    Button btnCalendar;
    com.hbb20.CountryCodePicker ccp;
    private int success = 0,otp=0,responseCode=0;
    private String countryCodeAndroid;
    private String pwd,confirmpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDate = findViewById(R.id.etDate);
        ccp = findViewById(R.id.ccp);
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        etDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        validation();
        ccp.setOnCountryChangeListener(new com.hbb20.CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCode();
                Log.d("Country Code", countryCodeAndroid);
            }
        });
    }

    private void validation() {
        etName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isName(etName, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(etEmail, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etDate.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etDate);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etContactNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(etContactNumber, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etPassword);
                pwd = etPassword.getText().toString();

                if (pwd.length() < 6) {
                    etPassword.setError("Password should be greater than 6 digits");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etConfirmPassword);
                confirmpwd = etConfirmPassword.getText().toString();

                if (!pwd.equals(confirmpwd)) {
                    etConfirmPassword.setError("Password mismatch");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }


    private boolean checkValidation() {

        boolean ret = true;
        if (!Validation.hasText(etName)) ret = false;
        if (!Validation.isEmailAddress(etEmail, true)) ret = false;
        if (!Validation.hasText(etDate)) ret = false;
        if (!Validation.isPhoneNumber(etContactNumber, true)) ret = false;
        if (!Validation.hasText(etPassword)) ret = false;
        if (!Validation.hasText(etConfirmPassword)) ret = false;

        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etDate:
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in EditText
                                etDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
                break;

            case R.id.btnSubmit:

                if (checkValidation()) {
                    Random rn = new Random();
                    otp=(rn.nextInt(10)*1000)+(rn.nextInt(10)*100)+(rn.nextInt(10)*10)+(rn.nextInt(10));
                    Log.d("otp",""+otp);

                   sendOTP(etContactNumber.getText().toString(), otp,RegisterActivity.this);
                    startActivity(new Intent(RegisterActivity.this, AddPostActivity.class));
                    Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @SuppressLint("StaticFieldLeak")
    public void sendOTP(final String mobile, final Integer otpCode, final Context mContext) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String url = null;
                if (!TextUtils.isEmpty(mobile) && null != otpCode) {
                    url = Constant.PROVIDER_URL +Constant. QUESTION_PARAMETER
                            + Constant.AUTHKEY_PARAMETER + Constant.EQUALS_TO_PARAMETER
                            + Constant.AUTHKEY + Constant.AMPERSAND_PARAMETER
                            + Constant.MOBILES_PARAMETER +Constant. EQUALS_TO_PARAMETER
                            + mobile + Constant.AMPERSAND_PARAMETER + Constant.MESSAGE_PARAMETER
                            +Constant. EQUALS_TO_PARAMETER + otpCode + Constant.MESSAGE
                            + Constant.AMPERSAND_PARAMETER + Constant.SENDER_PARAMETER + Constant.EQUALS_TO_PARAMETER
                            + Constant.SENDER + Constant.AMPERSAND_PARAMETER
                            + Constant.ROUTE_PARAMETER + Constant.EQUALS_TO_PARAMETER + Constant.ROUTE;
                } else {
                    Log.d("MessageSender", " sendOTP() : mobile : " + mobile + " otpCode : " + otpCode);
                }
                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    responseCode=con.getResponseCode();
                    Log.e("Response Code", responseCode+"  "+url);

                } catch (Exception e) {
                    Log.d("MessageSender" ,"sendOTP : " + e);
                }
                return null;



            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("mContext", mContext.getResources().getString(R.string.otp_sent_success));
                super.onPostExecute(aVoid);
                // Log.d("MessageSender", " sendSMS() : mobile : " + mobile + "");
                if(responseCode==200){
                    saveData();
//                    Intent intent=new Intent(mContext,Demo.class);
//                    startActivity(intent);

                }else{
                    Toast.makeText( mContext, "Sending data Fail please try again...", Toast.LENGTH_LONG).show();
                }
            }

            public void startActivity(Intent intent) {
            }
        }.execute();
    }




    public void saveData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View confirmDialog = inflater.inflate(R.layout.dialog_otp, null);
        AppCompatButton buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);
        final TextView tvResend= confirmDialog.findViewById(R.id.tvResend);
        final EditText editTextConfirmOtp = confirmDialog.findViewById(R.id.editTextOtp);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("OTP");
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Authenticating", "Verifying the entered code...", false, false);
                final String otpByUser = editTextConfirmOtp.getText().toString().trim();
                String otPassword = String.valueOf(otp);
                if (otpByUser.equals(otp + "")) {

                    //call api
                    alertDialog.dismiss();
                }else{
                    tvResend.setVisibility(View.VISIBLE);
                    tvResend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Random rn = new Random();
                            otp=(rn.nextInt(10)*1000)+(rn.nextInt(10)*100)+(rn.nextInt(10)*10)+(rn.nextInt(10));
                            sendOTP(etContactNumber.getText().toString(), otp,RegisterActivity.this);

                        }
                    });

                    Toast.makeText(RegisterActivity.this, "Wrong OTP. Please try again...",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

}
