package com.gajananmotors.shopfinder.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.LoginUser;
import com.gajananmotors.shopfinder.model.LoginUsersList;
import com.gajananmotors.shopfinder.model.UserRegister;
import com.gajananmotors.shopfinder.utility.Validation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    CallbackManager callbackManager;
    LoginButton login;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    private EditText etUserName, etPassword;
    com.hbb20.CountryCodePicker ccp;
    private String countryCodeAndroid;
    private static final int RC_SIGN_IN = 1;
    private int success = 0, otp = 0, responseCode = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    String tvDetails;
    private SignInButton btnSignIn;
    String DeviceToken;
    private View view;
    private static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_bottom);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        DeviceToken=sharedpreferences.getString(Constant.DEVICETOKEN,"");
        Log.d("token",DeviceToken);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setOnClickListener(this);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        login = findViewById(R.id.login_button);
        login.setReadPermissions("public_profile email");

        /*etUserName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(etUserName, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etPassword);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
     /*   ccp.setOnCountryChangeListener(new com.hbb20.CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCode();
                Log.d("Country Code", countryCodeAndroid);
            }
        });
     */   login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        facebook_id = profile.getId();
                        f_name = profile.getFirstName();
                        m_name = profile.getMiddleName();
                        l_name = profile.getLastName();
                        full_name = profile.getName();
                        profile_image = profile.getProfilePictureUri(400, 400).toString();
                    }
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed : " + connectionResult);
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.isPhoneNumber(etUserName, true)) ret = false;
        if (!Validation.hasText(etPassword)) ret = false;

        return ret;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (checkValidation()) {
                 
                    loginService();//calling Api for Authentication
                    startActivity(new Intent(LoginActivity.this, AddPostActivity.class));
                }
                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.btnSignIn:
                signIn();
                break;
        }
    }

    public void loginService() {
        Retrofit retrofit= APIClient.getClient();
        RestInterface restInterface=retrofit.create(RestInterface.class);
        Call<LoginUser> userList=restInterface.loginUsersList(etUserName.getText().toString(),etPassword.getText().toString(),"device_token");
        userList.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                LoginUser loginUser=response.body();

            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });
    }
    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data:" + json);
                try {
                    if (json != null) {
                        String text = "<b>Name:</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...

                    }
                });
    }

    private void revokeAccess() {

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64206)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                tvDetails = "Profile Name :" + acct.getDisplayName() +
                        "\nEmail : " + acct.getEmail() +
                        "\nFamily Name :" + acct.getFamilyName() +
                        "\n Given Name :" + acct.getGivenName() +
                        "\n ID :" + acct.getId();
                Log.e("google result", tvDetails);
                Picasso.with(LoginActivity.this)
                        .load(acct.getPhotoUrl());
                // .into(ivProfileImage);
            }
        }
    }
}




