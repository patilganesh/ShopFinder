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
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.adapter.CropingOptionAdapter;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.CropingOptionModel;
import com.gajananmotors.shopfinder.model.DeleteUserModel;
import com.gajananmotors.shopfinder.model.UpdateUserModel;
import com.gajananmotors.shopfinder.model.UserRegisterModel;
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

    private EditText etName, etEmail, etMobile, etDate;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private File outPutFile = null;
    private CircleImageView imgProfile;
    private Uri mImageCaptureUri;
    private int mYear, mMonth, mDay;
    private Button btnCalendar;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private SharedPreferences sharedpreferences;
    private Button btnEdit, btn_delete;
    private ImageView edtProfile;
    private boolean flag = false;
    private CoordinatorLayout coordinatorLayout_setting;
    private Call<UpdateUserModel> user;
    private String name,email,dob,mobile,image;
    private int owner_id;

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
        coordinatorLayout_setting=findViewById(R.id.coordinatorLayout_setting);
        etDate = findViewById(R.id.etDate);
        edtProfile = findViewById(R.id.fab_iv_edit);
        btnEdit = findViewById(R.id.btnEdit);
        btn_delete = findViewById(R.id.btn_delete);
        btnEdit.setOnClickListener(this);
        etDate.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        Button btnEdit = findViewById(R.id.btnEdit);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), ".temp.jpg");
        imgProfile = findViewById(R.id.imgProfile);
        if(!sharedpreferences.getString(Constant.OWNER_NAME,"").isEmpty()) {
            etName.setText(sharedpreferences.getString(Constant.OWNER_NAME, ""));
            etEmail.setText(sharedpreferences.getString(Constant.OWNWER_EMAIL, ""));
            etMobile.setText(sharedpreferences.getString(Constant.MOBILE, ""));
            etDate.setText(sharedpreferences.getString(Constant.DATE_OF_BIRTH, ""));
            Picasso.with(ProfileActivity.this)
                    .load("http://www.findashop.in/images/owner_profile/" +sharedpreferences.getString(Constant.OWNER_PROFILE, ""))
                    .fit()
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(imgProfile);
        }
        btnEdit.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
    }

    public void checkConnection(final String service) {
        final ConnectionDetector detector = new ConnectionDetector(ProfileActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ProfileActivity.this);
        if (!detector.isConnectingToInternet()) {
            alertDialog.setMessage("Network not available!");
            alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (service.equalsIgnoreCase("update")) {
                        if (detector.isConnectingToInternet()) {
                            dialog.dismiss();
                            updateUser();
                        } else {
                            checkConnection("update");
                        }
                    } else {
                        if (service.equalsIgnoreCase("delete")) {
                            if (detector.isConnectingToInternet()) {
                                dialog.dismiss();
                                deleteOwnerService();
                            } else {
                                checkConnection("delete");
                            }
                        }
                    }
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            if (service.equalsIgnoreCase("update"))
                updateUser();
            else
                deleteOwnerService();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:

                if (btnEdit.getText().toString().equals("Edit")) {
                    RelativeLayout deleteLayout = findViewById(R.id.btn_deleteLayout);
                    deleteLayout.setVisibility(View.GONE);
                    etName.setFocusable(true);
                    etName.setEnabled(true);
                    etName.setFocusableInTouchMode(true);
                    etName.setClickable(true);
                    etMobile.setEnabled(true);
                    etMobile.setFocusable(true);
                    etMobile.setFocusableInTouchMode(true);
                    etMobile.setClickable(true);
                    etEmail.setEnabled(true);
                    etEmail.setFocusable(true);
                    etEmail.setFocusableInTouchMode(true);
                    etEmail.setClickable(true);
                    com.hbb20.CountryCodePicker ccp_setting = findViewById(R.id.ccp_setting);
                    ccp_setting.setVisibility(View.VISIBLE);
                    edtProfile.setVisibility(View.VISIBLE);
                    btnEdit.setText("Update");
                } else if (btnEdit.getText().toString().equals("Update")) {
                    etName.setFocusable(false);
                    etName.setEnabled(false);
                    etName.setFocusableInTouchMode(false);
                    etName.setClickable(false);
                    etMobile.setEnabled(false);
                    etMobile.setFocusable(false);
                    etMobile.setFocusableInTouchMode(false);
                    etMobile.setClickable(false);
                    etEmail.setEnabled(false);
                    etEmail.setFocusable(false);
                    etEmail.setFocusableInTouchMode(false);
                    etEmail.setClickable(false);
                    checkConnection("update");
                    // updateUser();
                }
                break;
            case R.id.imgProfile:
                if (btnEdit.getText().toString().equals("Update")) {
                    selectImageOption();
                }
                break;
            case R.id.etDate:
                // Process to get Current Date
                if (btnEdit.getText().toString().equals("Update")) {
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
                }
                break;
            case R.id.btn_delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        ProfileActivity.this);
                alertDialog.setMessage("Are you sure you want to delete your account? ");
                alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkConnection("delete");
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
        }
    }

    private void deleteOwnerService() {
        Retrofit retrofit;
        DeleteUserModel deleteOwner;
        deleteOwner=new DeleteUserModel();
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<DeleteUserModel> deleteUser= restInterface.deleteOwnerList( sharedpreferences.getInt(Constant.OWNER_ID, 0));
        deleteUser.enqueue(new Callback<DeleteUserModel>() {
            @Override
            public void onResponse(Call<DeleteUserModel> call, Response<DeleteUserModel> response) {
                if (response.isSuccessful()){
                 String msg=  response.body().getMsg();
                    Snackbar.make(coordinatorLayout_setting, "" + msg, Snackbar.LENGTH_LONG).show();
                    sharedpreferences = getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                }
            }
            @Override
            public void onFailure(Call<DeleteUserModel> call, Throwable t) {

            }
        });
    }
    private void updateUser() {
        File shop_cover_photo = null;
        byte[] imgbyte = null;
        Retrofit retrofit;
        //   UserRegisterModel updateRegister=new UserRegisterModel();;
        UpdateUserModel updateUserModel = new UpdateUserModel();
        MultipartBody.Part fileToUpload = null;
        updateUserModel.setOwner_name(etName.getText().toString());
        updateUserModel.setMob_no(etMobile.getText().toString());
        updateUserModel.setOwner_email(etEmail.getText().toString());
        updateUserModel.setDate_of_birth(etDate.getText().toString());
        updateUserModel.setOwner_id(sharedpreferences.getInt(Constant.OWNER_ID, 0));
        retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        if (!flag) {
            outPutFile = null;
        }
        if (outPutFile != null) {
            try {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), outPutFile);
                fileToUpload = MultipartBody.Part.createFormData("image", outPutFile.getName(), mFile);
                user = restInterface.updateRegister(updateUserModel.getOwner_id(), updateUserModel.getOwner_name(), updateUserModel.getOwner_email(), updateUserModel.getMob_no(), updateUserModel.getDate_of_birth(), fileToUpload);
                //   RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), shop_cover_photo.getName());
            } catch (Exception e) {
                Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
              user = restInterface.updateRegisterforEmptyImage(updateUserModel.getOwner_name(), updateUserModel.getOwner_email(), updateUserModel.getMob_no(), updateUserModel.getDate_of_birth(), updateUserModel.getOwner_id());
        }

        user.enqueue(new Callback<UpdateUserModel>() {
            @Override
            public void onResponse(Call<UpdateUserModel> call, Response<UpdateUserModel> response) {
                if (response.isSuccessful()) {
                    UpdateUserModel user = response.body();
                    String msg = user.getMsg();
                    String name = user.getOwner_name();
                    String email = user.getOwner_email();
                    String mobile = user.getMob_no();
                    String dob = user.getDate_of_birth();
                    String image = user.getImage1();
                    int owner_id = user.getOwner_id();
                    int result = user.getResult();
                    if (result == 1 && name != null) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt(Constant.OWNER_ID, owner_id);
                        editor.putString(Constant.OWNER_NAME, name);
                        editor.putString(Constant.OWNWER_EMAIL, email);
                        editor.putString(Constant.DATE_OF_BIRTH, dob);
                        editor.putString(Constant.MOBILE, mobile);
                        editor.putString(Constant.OWNER_PROFILE, image);
                        editor.apply();
                        RelativeLayout deleteLayout = findViewById(R.id.btn_deleteLayout);
                        deleteLayout.setVisibility(View.VISIBLE);
                        com.hbb20.CountryCodePicker ccp_setting = findViewById(R.id.ccp_setting);
                        ccp_setting.setVisibility(View.GONE);
                        edtProfile.setVisibility(View.GONE);
                        btnEdit.setText("Edit");
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        // updateserProfile();
                        Toast.makeText(ProfileActivity.this, "" + msg, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UpdateUserModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error" + t, Toast.LENGTH_LONG).show();
                Log.e("failure", "onFailure: " + t.toString());
            }
        });
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
                    flag = true;
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
        final ArrayList<CropingOptionModel> cropOptions = new ArrayList<CropingOptionModel>();
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
        finish();

    }
}
