package com.gajananmotors.shopfinder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CropingOptionAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CropingOption;
import com.gajananmotors.shopfinder.model.UserRegister;

import com.squareup.picasso.Picasso;

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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etEmail, etMobile, etDate;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private File outPutFile = null;
    private CircleImageView imgProfile;
    private Uri mImageCaptureUri;
    private int mYear, mMonth, mDay;
    Button btnCalendar;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;
    private Button btnEdit,btn_delete;
    ImageView edtProfile;
   /* private String name,email,dob,mobile,image;
    private int owner_id;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        //    permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etDate = findViewById(R.id.etDate);
        edtProfile=findViewById(R.id.fab_iv_edit);
        btnEdit = findViewById(R.id.btnEdit);
        btn_delete = findViewById(R.id.btn_delete);
        btnEdit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        Button btnEdit = findViewById(R.id.btnEdit);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), ".temp.jpg");
        imgProfile = findViewById(R.id.imgProfile);
        if (!sharedpreferences.getString(Constant.OWNER_NAME, "").isEmpty()) {
            etName.setText(sharedpreferences.getString(Constant.OWNER_NAME, ""));
            etEmail.setText(sharedpreferences.getString(Constant.OWNWER_EMAIL, ""));
            etMobile.setText(sharedpreferences.getString(Constant.MOBILE, ""));
            etDate.setText(sharedpreferences.getString(Constant.DATE_OF_BIRTH, ""));
            Picasso.with(ProfileActivity.this)
                    .load(sharedpreferences.getString(Constant.OWNER_PROFILE, ""))
                    .fit()
                    .into(imgProfile);
        }
        btnEdit.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                RelativeLayout deleteLayout = findViewById(R.id.btn_deleteLayout);
                deleteLayout.setVisibility(View.GONE);
                com.hbb20.CountryCodePicker ccp_setting=findViewById(R.id.ccp_setting);
                ccp_setting.setVisibility(View.VISIBLE);
                edtProfile.setVisibility(View.VISIBLE);
                btnEdit.setText("UPDATE");

                //updateUser();
                break;

            case R.id.imgProfile:
                selectImageOption();
                break;

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
        }
    }

    private void updateUser() {


        File shop_cover_photo = null;
        byte[] imgbyte = null;
        Retrofit retrofit;
        UserRegister updateRegister;
        MultipartBody.Part fileToUpload = null;
        updateRegister = new UserRegister();
        if (outPutFile != null) {
            try {
              
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), outPutFile);
                fileToUpload = MultipartBody.Part.createFormData("image", outPutFile.getName(), mFile);
                //   RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), shop_cover_photo.getName());
            } catch (Exception e) {
                Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        updateRegister.setOwner_name(etName.getText().toString());
        updateRegister.setMob_no(etMobile.getText().toString());
        updateRegister.setOwner_email(etEmail.getText().toString());
        updateRegister.setDate_of_birth(etDate.getText().toString());
        updateRegister.setOwner_id(sharedpreferences.getInt(Constant.OWNER_ID, 0));
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<UserRegister> user = restInterface.updateRegister(updateRegister.getOwner_name(), updateRegister.getOwner_email(), updateRegister.getMob_no(), updateRegister.getDate_of_birth(), fileToUpload, updateRegister.getOwner_id());
        try {
            user.enqueue(new Callback<UserRegister>() {
                @Override
                public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                    if (response.isSuccessful()) {
                        UserRegister user = response.body();
                        String msg = user.getMsg();
                        String name = user.getOwner_name();
                        String email = user.getOwner_email();
                        String mobile = user.getMob_no();
                        String dob = user.getDate_of_birth();
                        String image = user.getImage1();
                        int owner_id = user.getOwner_id();
                        int result = user.getResult();

                        if (result == 1&&  name!= null) {

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt(Constant.OWNER_ID, owner_id);
                            editor.putString(Constant.OWNER_NAME, name);
                            editor.putString(Constant.OWNWER_EMAIL, email);
                            editor.putString(Constant.DATE_OF_BIRTH, dob);
                            editor.putString(Constant.MOBILE, mobile);
                            editor.putString(Constant.OWNER_PROFILE, "http://www.findashop.in/images/owner_profile/" + image);
                            editor.apply();
                            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                           // updateserProfile();
                            Toast.makeText(ProfileActivity.this, "" + msg + result, Toast.LENGTH_LONG).show();

                        }else{                            Toast.makeText(ProfileActivity.this, "Error" , Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Error" + t, Toast.LENGTH_LONG).show();
                    Log.e("failure", "onFailure: " + t.toString());
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error Message:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void updateserProfile() {


    }

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
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
                    Picasso.with(ProfileActivity.this).load(outPutFile).skipMemoryCache().into(imgProfile, new com.squareup.picasso.Callback() {
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
}
