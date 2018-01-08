package com.gajananmotors.shopfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
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
    String str;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();


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

        etUserName.addTextChangedListener(new TextWatcher() {
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
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                        }
                    });

                    Toast.makeText(LoginActivity.this, "Wrong OTP. Please try again...", Toast.LENGTH_SHORT).show();
                }
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




