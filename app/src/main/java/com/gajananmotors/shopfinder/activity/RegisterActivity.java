package com.gajananmotors.shopfinder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.gajananmotors.shopfinder.adapter.CropingOptionAdapter;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CropingOption;
import com.gajananmotors.shopfinder.utility.Validation;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.gajananmotors.shopfinder.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private Uri mImageCaptureUri;
    private File outPutFile;
    private EditText etName, etEmail, etContactNumber, etPassword, etConfirmPassword, etDate;
    private int mYear, mMonth, mDay;
    Button btnCalendar;
    com.hbb20.CountryCodePicker ccp;
    private int success = 0, otp = 0, responseCode = 0;
    private String countryCodeAndroid;
    private String pwd, confirmpwd;
    private String userChoosenTask;
    private CircleImageView imgProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDate = findViewById(R.id.etDate);
        ccp = findViewById(R.id.ccp);
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
       Button btnSubmit = findViewById(R.id.btnSubmit);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), ".temp.jpg");
        imgProfile.setOnClickListener(this);
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
            case R.id.imgProfile:
                selectImageOption();
                break;
        }}

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);
                    //cameraIntent();


                } else if (items[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);
                    // galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI : " + mImageCaptureUri);
            CropingIMG();

        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            System.out.println("Camera Image URI : " + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CROPING_CODE) {
            try {
                if (outPutFile.exists()) {
                    Picasso.with(RegisterActivity.this).load(outPutFile).skipMemoryCache().into(imgProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CropingIMG() {
        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        String profile_img = mImageCaptureUri.toString();
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();
                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
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


}
