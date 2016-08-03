package com.omenacle.bamzu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omenacle.bamzu.models.User;

/**
 * Created by omegareloaded on 7/24/16.
 */
public class CreateAccountActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private final String TAG = ".CreatAccountActivity";

    // [START declare_database]
    private DatabaseReference mDatabase;
    // [END declare_database]

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    //Views
    private EditText mEmailAddress, mPassword;
    private SignInButton mGooglePlusSignInButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        getSupportActionBar().hide();

        //Views
        mEmailAddress = (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
        tintView(mEmailAddress, R.color.colorwhite);
        tintView(mPassword, R.color.colorwhite);


        //Buttons
        findViewById(R.id.btn_email_password_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_create_account).setOnClickListener(this);
        findViewById(R.id.btn_google_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);

        //Configure Google Plus Button
        mGooglePlusSignInButton = (SignInButton) findViewById(R.id.btn_google_sign_in);
        mGooglePlusSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGooglePlusSignInButton.setColorScheme(SignInButton.COLOR_AUTO);

        //Facebook Login
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton mFacebookLoginButton = (LoginButton) findViewById(R.id.btn_facebook_sign_in);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook:onSuccess " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                hideProgressDialog();
                showSignInError();
                // [END_EXCLUDE]

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                showSignInError();
                // [END_EXCLUDE]

            }
        });


        // [START configure_google_sign_in_options]
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [END configure_google_sign_in_options]

        //[START configure_facebook_login_config]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btn_facebook_sign_in);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        //[END configure_facebook_login_config]

        //Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //[start initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        //[end initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in " + user.getUid());
                    onAuthSuccess(user);
                } else {
                    //user is logged out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
        //[END auth_state_listener]

    }


    private void showSignInError() {
        Log.d(TAG, "Sign In failed");
        Toast.makeText(CreateAccountActivity.this, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
    }

    //[START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    //[END on_start_add_listener]

    //[START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    //[END on_stop_remove_listener]


    private void signInWithEmail(String email, String password) {
        Log.d(TAG, "SignIn " + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete " + task.isSuccessful());
                hideProgressDialog();
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    showSignInError();
                } else {
                    onAuthSuccess(task.getResult().getUser());
                }

            }
            //[END signInWithEmailPassword]
        });

    }


    // [START google_api_sign_in]

    private void signInWithGoogle() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle " + account.getId());
        //[START_EXCLUDE silent]
        showProgressDialog();
        //[END_EXCLUDE silent]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            showSignInError();
                        } else {
                            onAuthSuccess(task.getResult().getUser());
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                //Google Sign In Successful
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    //[END google_api_sign_in]
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            showSignInError();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]


    private void onAuthSuccess(FirebaseUser user) {
        writeNewUser(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
        returnToMainActivity();
    }

    private void writeNewUser(String userId, String name, String email, Uri photoUrl) {
        User mCurrentUser = new User(name, email, photoUrl);
        mDatabase.child("users").child(userId).setValue(mCurrentUser);

    }

    private void returnToMainActivity() {
        Toast.makeText(CreateAccountActivity.this, getString(R.string.succesful_sign_in), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email_password_sign_in:
                signInWithEmail(mEmailAddress.getText().toString(), mPassword.getText().toString());
                break;
            case R.id.btn_google_sign_in:
                signInWithGoogle();
                break;
            case R.id.tv_forgot_password:

                break;
        }
    }

    /**
     * Check if login form is properly filled
     *
     * @return boolean formValid
     */

    private boolean validateForm() {
        Boolean valid = true;
        String mEmail = mEmailAddress.getText().toString();
        if (TextUtils.isEmpty(mEmail)) {
            mEmailAddress.setError(getString(R.string.required));
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.required));
            valid = false;
        } else {
            mPassword.setError(null);
        }
        Log.d(TAG, "validateForm: " + valid);
        return valid;
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * Set backgroundTint to {@link View} across all targeting platform level.
     *
     * @param view  the {@link View} to tint.
     * @param color color used to tint.
     */
    public static void tintView(View view, int color) {
        final Drawable d = view.getBackground();
        final Drawable nd = d.getConstantState().newDrawable();
        nd.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN));
        view.setBackground(nd);
    }
}
