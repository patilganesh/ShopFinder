package com.gajananmotors.shopfinder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.utility.PermissionUtility;
import com.gajananmotors.shopfinder.utility.Validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

import com.gajananmotors.shopfinder.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 1;
    private EditText etName, etEmail, etContactNumber, etPassword, etConfirmPassword, etDate;
    private int mYear, mMonth, mDay;
    Button btnCalendar;
    com.hbb20.CountryCodePicker ccp;
    private int success = 0, otp = 0, responseCode = 0;
    private String countryCodeAndroid;
    private String pwd, confirmpwd;
    String userChoosenTask;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imgProfile =findViewById(R.id.imgProfile);
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
                    otp = (rn.nextInt(10) * 1000) + (rn.nextInt(10) * 100) + (rn.nextInt(10) * 10) + (rn.nextInt(10));
                    Log.d("otp", "" + otp);

                   // sendOTP(etContactNumber.getText().toString(), otp, RegisterActivity.this);
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
                    url = Constant.PROVIDER_URL + Constant.QUESTION_PARAMETER
                            + Constant.AUTHKEY_PARAMETER + Constant.EQUALS_TO_PARAMETER
                            + Constant.AUTHKEY + Constant.AMPERSAND_PARAMETER
                            + Constant.MOBILES_PARAMETER + Constant.EQUALS_TO_PARAMETER
                            + mobile + Constant.AMPERSAND_PARAMETER + Constant.MESSAGE_PARAMETER
                            + Constant.EQUALS_TO_PARAMETER + otpCode + Constant.MESSAGE
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
                    responseCode = con.getResponseCode();
                    Log.e("Response Code", responseCode + "  " + url);

                } catch (Exception e) {
                    Log.d("MessageSender", "sendOTP : " + e);
                }
                return null;


            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("mContext", mContext.getResources().getString(R.string.otp_sent_success));
                super.onPostExecute(aVoid);
                if (responseCode == 200) {
                    saveData();
                } else {
                    Toast.makeText(mContext, "Sending data Fail please try again...", Toast.LENGTH_LONG).show();
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
        final TextView tvResend = confirmDialog.findViewById(R.id.tvResend);
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
                final String otpByUser = editTextConfirmOtp.getText().toString().trim();
                String otPassword = String.valueOf(otp);
                if (otpByUser.equals(otp + "")) {

                    //call api
                    alertDialog.dismiss();
                } else {
                    tvResend.setVisibility(View.VISIBLE);
                    tvResend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Random rn = new Random();
                            otp = (rn.nextInt(10) * 1000) + (rn.nextInt(10) * 100) + (rn.nextInt(10) * 10) + (rn.nextInt(10));
                           // sendOTP(etContactNumber.getText().toString(), otp, RegisterActivity.this);

                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Wrong OTP. Please try again...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void getProfilepicture(View view) {

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= PermissionUtility.checkPermission(RegisterActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(thumbnail);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgProfile.setImageBitmap(bm);
        }

    }
}
