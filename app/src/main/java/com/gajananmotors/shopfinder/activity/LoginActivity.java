package com.gajananmotors.shopfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.gajananmotors.shopfinder.helper.ConnectionDetector;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.LoginUserModel;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.gajananmotors.shopfinder.helper.Constant.MyPREFERENCES;
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private CallbackManager callbackManager;
    private LoginButton login;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    private EditText etUserName, etPassword;
    private com.hbb20.CountryCodePicker ccp;
    private String countryCodeAndroid = "";
    private static final int RC_SIGN_IN = 1;
    private int success = 0, otp = 0, responseCode = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String tvDetails = "", owner_name = "", owner_email = "", ownner_mobile = "", owner_dob = "", owner_image = "";
    private SignInButton btnSignIn;
    private Button btnLogin, btnRegister;
    private ProgressBar login_progressbar;
    private SharedPreferences sharedpreferences;
    private String device_token="";
    private int owner_id, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        login_progressbar = findViewById(R.id.login_progressbar);
        //getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        device_token = sharedpreferences.getString(Constant.DEVICE_TOKEN, "000000");
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
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        login = findViewById(R.id.login_button);
        login.setReadPermissions("public_profile email");
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
                        profile_image = profile.getProfilePictureUri(50, 50).toString();
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
        boolean ret=true;
        LinearLayout linear_layout=findViewById(R.id.linear_layout);
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if(username.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Username", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        if(password.matches("")){

            Snackbar snackbar = Snackbar
                    .make(linear_layout, "Please Enter Password", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        return ret;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
            /*    if (etUserName.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(this, "Username or Password cant be blank! ", Toast.LENGTH_SHORT).show();
                } else {*/
            if(checkValidation()){
                ConnectionDetector detector = new ConnectionDetector(this);
                if (detector.isConnectingToInternet())
                    loginService();//calling Api for Authentication
                else {
                    Toast.makeText(this, "Please check your data Connection.", Toast.LENGTH_LONG).show();
                }
            }
                break;
            case R.id.btnRegister:
                //  Toast.makeText(this, "Clicked....", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.btnSignIn:
                signIn();
                break;
        }
    }
    public void loginService() {
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<LoginUserModel> loginUser = restInterface.loginUsersList(etUserName.getText().toString(), etPassword.getText().toString(), device_token);
        login_progressbar.setVisibility(View.VISIBLE);
        //progressBar.setLeft(20);
        // btnLogin.setVisibility(View.GONE);
        login_progressbar.setIndeterminate(true);
        login_progressbar.setProgress(500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            login_progressbar.setMin(0);
        }
        login_progressbar.setMax(100);
        loginUser.enqueue(new Callback<LoginUserModel>() {
            @Override
            public void onResponse(Call<LoginUserModel> call, Response<LoginUserModel> response) {
                if (response.isSuccessful()) {
                    LoginUserModel user = response.body();
                    String msg = user.getMsg();
                    owner_name = user.getOwner_name();
                    owner_email = user.getOwner_email();
                    ownner_mobile = user.getMob_no();
                    owner_dob = user.getDate_of_birth();
                    owner_image = user.getImage();
                    owner_id = user.getOwner_id();
                    status = user.getStatus();
                    if (user.getResult() == 1 && status == 1) {
                        Toast.makeText(LoginActivity.this, "Owner Id:" + user.getOwner_id() + "Name:" + owner_name
                                        + "\nEmail:" + owner_email + "\nMobile:" + ownner_mobile + "\nImage:" + "http://www.findashop.in/images/owner_profile/" + owner_image
                                , Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        // setting values to sharedpreferences keys.
                        editor.putInt(Constant.OWNER_ID, owner_id);
                        editor.putString(Constant.OWNER_NAME, owner_name);
                        editor.putString(Constant.OWNWER_EMAIL, owner_email);
                        editor.putString(Constant.DATE_OF_BIRTH, owner_dob);
                        editor.putString(Constant.MOBILE, ownner_mobile);
                        editor.putString(Constant.OWNER_PROFILE,owner_image);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, AddPostActivity.class));
                        finish();
                        login_progressbar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(LoginActivity.this, "Fail to Login:", Toast.LENGTH_LONG).show();
                        etUserName.setText("");
                        etPassword.setText("");
                        login_progressbar.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginUserModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "On Failure ", Toast.LENGTH_LONG).show();
            }
        });
    }
   /* public void saveData() {
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
    }*/
    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data:" + json);
                try {
                    if (json != null) {
                        String text = "<b>Name:</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        owner_name=json.getString("name");
                        owner_email=json.getString("email");
                        owner_image=json.getString("link");
                        FacegleloginService();

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
                owner_name= acct.getDisplayName();
                owner_email= acct.getEmail();
                owner_image= acct.getPhotoUrl().toString();
                FacegleloginService();

            }
        }
    }
    private void FacegleloginService() {
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<LoginUserModel> loginUser = restInterface.loginUsersFacegleList(owner_email,device_token);
        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setLeft(20);
        // btnLogin.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        progressBar.setMax(100);
        loginUser.enqueue(new Callback<LoginUserModel>() {
            @Override
            public void onResponse(Call<LoginUserModel> call, Response<LoginUserModel> response) {
                if (response.isSuccessful()) {
                    LoginUserModel user = response.body();
                    String msg = user.getMsg();
                    status = user.getStatus();
                    if (user.getResult() == 1 && status == 1) {
Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, AddPostActivity.class));
                        finish();
                        progressBar.setVisibility(View.GONE);
                    } else if(user.getResult() == 0) {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        Bundle b = new Bundle();
                        b.putString("owner_name",owner_name);
                        b.putString("owner_email", owner_email);
                        b.putString("owner_profile", owner_image);
                        Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                        in.putExtras(b);
                        startActivity(in);

                    }else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginUserModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "On Failure ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();

    }
}




