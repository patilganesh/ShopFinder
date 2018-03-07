package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CropingOptionAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CropingOptionModel;
import com.gajananmotors.shopfinder.model.UserRegisterModel;
import com.gajananmotors.shopfinder.utility.Validation;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private Uri mImageCaptureUri;
    private File outPutFile;
    private EditText etName, etEmail, etContactNumber, etPassword, etConfirmPassword, etDate;
    private int mYear, mMonth, mDay;
    private com.hbb20.CountryCodePicker ccp;
    private int success = 0, otp = 0, responseCode = 0;
    private String countryCodeAndroid = "";
    private String pwd, confirmpwd;
    private CircleImageView imgProfile;
    private Bitmap bitmap;
    private Button btnSubmit;
    private String device_token = "";
    private SharedPreferences sharedpreferences;
    private Call<UserRegisterModel> user;
    private boolean flag = false;
    private String name = "", email = "", profile = "", logingoogle = "";
    private Toolbar toolbar;
    private ProgressBar register_progressbar;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDate = findViewById(R.id.etDate);
        ccp = findViewById(R.id.ccp);
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSubmit = findViewById(R.id.btnSubmit);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        imgProfile.setOnClickListener(this);
        register_progressbar = findViewById(R.id.register_progressbar);
        // outPutFile = null;
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);

        device_token = sharedpreferences.getString(Constant.DEVICE_TOKEN, "00000");
        //   Log.e(TAG, "savetoken" + sharedpreferences.getString(Constant.DEVICE_TOKEN,""));

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), ".temp.jpg");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("owner_name");
            email = extras.getString("owner_email");
            logingoogle = extras.getString("usertype");
            profile = extras.getString("owner_profile");
            Picasso.with(RegisterActivity.this)
                    .load(profile)
                    .fit()
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(imgProfile);

            outPutFile = new File(profile);
            etName.setText(name);
            etEmail.setText(email);

        }
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

        validationForAll();
    }

    private void validationForAll() {

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LinearLayout linear_layout = findViewById(R.id.linear_layout);
                    String name = etName.getText().toString();
                    if (name.matches("")) {
                        showSnackBar("Please Enter Username", linear_layout);
                        etName.requestFocus();

                    }
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LinearLayout linear_layout = findViewById(R.id.linear_layout);
                    String name = etName.getText().toString();
                    if (name.matches("")) {
                        showSnackBar("Please Enter Username", linear_layout);
                    }
                }
            }
        });

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LinearLayout linear_layout = findViewById(R.id.linear_layout);
                    String email = etEmail.getText().toString();
                    if (email.matches("")) {
                        showSnackBar("Please Enter Email", linear_layout);
                    } else {

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!email.matches(emailPattern)) {
                            showSnackBar("Invalid Email", linear_layout);
                        }
                    }

                }
            }
        });

        etContactNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LinearLayout linear_layout = findViewById(R.id.linear_layout);
                    String email = etEmail.getText().toString();
                    if (email.matches("")) {
                        showSnackBar("Please Enter Email", linear_layout);
                    } else {

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!email.matches(emailPattern)) {
                            showSnackBar("Invalid Email", linear_layout);
                        }
                    }
                    String date = etDate.getText().toString();
                    if (date.matches("")) {

                        showSnackBar("Please Enter Date", linear_layout);
                    }
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LinearLayout linear_layout = findViewById(R.id.linear_layout);
                    String mob = etContactNumber.getText().toString();

                    if (mob.matches("")) {
                        showSnackBar("Please Enter Mobile Number", linear_layout);
                    } else {
                        if (mob.length() < 9) {
                            showSnackBar("Please Enter Valid Mobile Number", linear_layout);
                        }
                    }
                }
            }


        });
    }


    void showSnackBar(String msg, View view) {

        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.show();

    }


    /*Calling Api and register shop owner's Data*/
    private void registerUser() {
        File shop_cover_photo = null;
        byte[] imgbyte = null;
        Retrofit retrofit;
        UserRegisterModel user_data;
        MultipartBody.Part fileToUpload = null;
        user_data = new UserRegisterModel();
        user_data.setOwner_name(etName.getText().toString());
        user_data.setMob_no(etContactNumber.getText().toString());
        user_data.setOwner_email(etEmail.getText().toString());
        user_data.setPassword(etPassword.getText().toString());
        user_data.setDate_of_birth(etDate.getText().toString());
        user_data.setDevice_token(device_token);
        user_data.setImage1(profile);
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        if (!flag) {
            Toast.makeText(this, "Flag:" + flag, Toast.LENGTH_SHORT).show();
            outPutFile = null;
        }
        if (outPutFile != null) {
            try {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), outPutFile);
                fileToUpload = MultipartBody.Part.createFormData("image", outPutFile.getName(), mFile);
                user = restInterface.userRegister(user_data.getOwner_name(), user_data.getOwner_email(), user_data.getMob_no(), user_data.getDate_of_birth(), fileToUpload, user_data.getPassword(), user_data.getDevice_token());
                //   RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), shop_cover_photo.getName());
            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (logingoogle.equals("google")) {
            if (!profile.equals(""))
                user = restInterface.userRegisterforGoogleImage(user_data.getOwner_name(), user_data.getOwner_email(), user_data.getMob_no(), user_data.getDate_of_birth(), user_data.getImage1(), user_data.getPassword(), user_data.getDevice_token());
        }else if (profile.equals("")&& outPutFile != null) {
            try {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), outPutFile);
                fileToUpload = MultipartBody.Part.createFormData("image", outPutFile.getName(), mFile);
                user = restInterface.userRegister(user_data.getOwner_name(), user_data.getOwner_email(), user_data.getMob_no(), user_data.getDate_of_birth(), fileToUpload, user_data.getPassword(), user_data.getDevice_token());
                //   RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), shop_cover_photo.getName());
            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (outPutFile == null && profile.isEmpty()) {
            user = restInterface.userRegisterforEmptyImage(user_data.getOwner_name(), user_data.getOwner_email(), user_data.getMob_no(), user_data.getDate_of_birth(), user_data.getPassword(), user_data.getDevice_token());
        }

        btnSubmit.setVisibility(View.INVISIBLE);
        register_progressbar.setVisibility(View.VISIBLE);
        register_progressbar.setIndeterminate(true);
        register_progressbar.setProgress(500);
        user.enqueue(new Callback<UserRegisterModel>() {
            @Override
            public void onResponse(Call<UserRegisterModel> call, Response<UserRegisterModel> response) {
                if (response.isSuccessful()) {
                    register_progressbar.setVisibility(View.INVISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    UserRegisterModel user = response.body();
                    String msg = user.getMsg();
                    String name = user.getOwner_name();
                    String email = user.getOwner_email();
                    String mobile = user.getMob_no();
                    String dob = user.getDate_of_birth();
                    String image = user.getImage1();
                    int owner_id = user.getOwner_id();
                    int result = user.getResult();
                    int google=user.getGoogle();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    if (result == 1) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setMessage("Successfully Registered\nOwner Id:" + owner_id + "\nMesg:" + msg + "\nImage:" + image + "\nName:" + name + "\nEmail:" + email + "\nResult:" + result); //display response in Alert dialog.
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                        //  Toast.makeText(RegisterActivity.this, "msg:" + msg + "\nImage:" + image + "\nMobile No:" + mobile, Toast.LENGTH_LONG).show();
                        //Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                             /*  setting values to sharedpreferences keys.*/
                        editor.putInt(Constant.OWNER_ID, owner_id);
                        editor.putString(Constant.OWNER_NAME, name);
                        editor.putString(Constant.OWNWER_EMAIL, email);
                        editor.putString(Constant.DATE_OF_BIRTH, dob);
                        editor.putString(Constant.MOBILE, mobile);
                        editor.putString(Constant.OWNER_PROFILE, "http://www.findashop.in/images/owner_profile/" + image);
                        editor.apply();
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(RegisterActivity.this, AddPostActivity.class));
                        startActivity(intent);
                        finish();
                    } else if (result == 1&& google == 1) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setMessage("Successfully Registered\nOwner Id:" + owner_id + "\nMesg:" + msg + "\nImage:" + image + "\nName:" + name + "\nEmail:" + email + "\nResult:" + result); //display response in Alert dialog.
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                        //  Toast.makeText(RegisterActivity.this, "msg:" + msg + "\nImage:" + image + "\nMobile No:" + mobile, Toast.LENGTH_LONG).show();
                        //Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                             /*  setting values to sharedpreferences keys.*/
                        editor.putInt(Constant.OWNER_ID, owner_id);
                        editor.putString(Constant.OWNER_NAME, name);
                        editor.putString(Constant.OWNWER_EMAIL, email);
                        editor.putString(Constant.DATE_OF_BIRTH, dob);
                        editor.putString(Constant.MOBILE, mobile);
                        editor.putString(Constant.OWNER_PROFILE,image);
                        editor.apply();
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(RegisterActivity.this, AddPostActivity.class));
                        startActivity(intent);
                        finish();
                    }
                        Toast.makeText(RegisterActivity.this, "User Already Registered With This Mobile Number!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserRegisterModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error" + t, Toast.LENGTH_LONG).show();
                Log.e("failure", "onFailure: " + t.toString());
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
                                etDate.setText(year + "/"
                                        + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
                break;
            case R.id.btnSubmit:
              /*  if (checkValidation()) {*/
                //Toast.makeText(this, "Registration.....", Toast.LENGTH_SHORT).show();
                if (checkValidation())
                    registerUser();//calling register method for web services
                // }
                break;
            case R.id.imgProfile:
                selectImageOption();
                break;
        }
    }

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(outPutFile);
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
                if (outPutFile.exists() && resultCode == -1) {

                    flag = true;
                    Picasso.with(RegisterActivity.this).load(outPutFile).skipMemoryCache().into(imgProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                        }
                    });
                } else {
                    if (outPutFile == null) {
                        Picasso.with(RegisterActivity.this).load(sharedpreferences.getString(Constant.OWNER_PROFILE, "")).skipMemoryCache().into(imgProfile, new com.squareup.picasso.Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                            }
                        });
                    } else {
                        Picasso.with(RegisterActivity.this).load(R.drawable.ic_account_circle_black_24dp).skipMemoryCache().into(imgProfile, new com.squareup.picasso.Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                            }
                        });
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CropingIMG() {
        final ArrayList<CropingOptionModel> cropOptions = new ArrayList<CropingOptionModel>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
       // String profile_img = mImageCaptureUri.toString();
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
                    final CropingOptionModel co = new CropingOptionModel();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void validation() {
        etPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etPassword, "");
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
                Validation.hasText(etConfirmPassword, "");
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

    /*
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
    */
    private boolean checkValidation() {
        boolean ret = true;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        LinearLayout linear_layout = findViewById(R.id.linear_layout);
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String date = etDate.getText().toString();
        String mob = etContactNumber.getText().toString();
        String password = etPassword.getText().toString();
        String cpassword = etConfirmPassword.getText().toString();
        if (name.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Name", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (email.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Email", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (!email.matches(emailPattern)) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Invalid Email", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (date.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Date of Birth", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (mob.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Mobile Number", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (mob.length() <= 9) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Invalid Mobile Number", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (password.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Password", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if (cpassword.matches("")) {

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Confirm Password", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        return ret;
    }
}
