package project.vehiclessharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import project.vehiclessharing.model.SignInHelper;
import project.vehiclessharing.presenter.SignInPresenter;
import project.vehiclessharing.util.Utils;
import project.vehiclessharing.view.SignInView;

/**
 * Created by Tuan on 14/07/2017.
 */

public class SignInFragment extends Fragment implements SignInView,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{


    private static View view;

    //MVP
    private SignInPresenter signInPresenter;
    private SignInHelper signInHelper;

    private TextView tvGoogleSignIn;
    private TextView tvFacebookSignIn;
    private TextView tvSignInPhone;
    private LoginButton btnFBSignIn;
    private static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 69;//Value request sign in Google
    private ProgressDialog signInProgress;//Progress to wait sign in

    //temp
    private EditText edPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;


    private CallbackManager callbackManager;

    public SignInFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }
        if(signInPresenter == null){
            if(signInHelper == null){
                signInHelper = new SignInHelper(getActivity());
            }
            signInPresenter = new SignInPresenter(this,signInHelper);
        }
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        if(mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("LOLPHONE", "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.d("LOLPHONE", "onCodeAutoRetrievalTimeOut" + s);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("LOLPHONE", "onVerificationFailed", e);
                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    edPhone.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(getActivity(),"Quota exceeded",Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("LOLPHONE", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
//                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]

    }

    // [START sign_in_with_phone] temp
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOLPHONE", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                           startMainActivity();
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("LOLPHONE", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls();
        addEvents();
        intizileFB();
    }

    /**
     * Initialize Facebook Login button
     */
    private void intizileFB() {

        btnFBSignIn.setReadPermissions("email", "public_profile");
        // If using in a fragment
        btnFBSignIn.setFragment(this);
        // Other app specific specialization
        // Callback registration
        btnFBSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LOLFB", "faceboo k:onSuccess:" + loginResult);
                signInPresenter.singInFacebook(loginResult.getAccessToken());//Sign in Facebook account
//                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("LOLFB", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LOLFB", "facebook:onError", error);
            }
        });
    }


    private void addControls() {
        tvGoogleSignIn = (TextView) view.findViewById(R.id.tv_signin_google);
        tvFacebookSignIn = (TextView) view.findViewById(R.id.tv_signin_facebook);
        btnFBSignIn = (LoginButton) view.findViewById(R.id.btn_fb_signin);
        callbackManager = CallbackManager.Factory.create();

        //[Start] Setup for progress
        signInProgress =new ProgressDialog(getActivity());
        signInProgress.setTitle(Utils.STRING_SIGN_IN);
        signInProgress.setMessage(Utils.STRING_PLEASE_WAIT);
        signInProgress.setCancelable(false);
        signInProgress.setCanceledOnTouchOutside(false);
        //[End] Setup for progress

        //temp
        tvSignInPhone = (TextView) view.findViewById(R.id.tv_signin_phone);
        edPhone = (EditText) view.findViewById(R.id.ed_phone);
        mAuth = FirebaseAuth.getInstance();


    }

    private void addEvents() {
        tvGoogleSignIn.setOnClickListener(this);
        tvFacebookSignIn.setOnClickListener(this);
        btnFBSignIn.setOnClickListener(this);
        tvSignInPhone.setOnClickListener(this);//temp
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
//        String temp = "+84"+phoneNumber;
        Log.d("LOLPHONE",phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    @Override
    public void loadFormSelectAccountGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showSignInProgress() {
        if(!signInProgress.isShowing()) signInProgress.show();
    }

    @Override
    public void dismissSignInProgress() {
        if(signInProgress != null) signInProgress.dismiss();
    }

    @Override
    public void signInSuccess() {
        //switch activty
        signInPresenter.startMainActivity();

    }


    @Override
    public void signInError(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(getActivity(),MainActivity.class));
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_signin_facebook: {
                btnFBSignIn.performClick();
                break;
            }
            case R.id.tv_signin_google: {
                signInPresenter.signIn(Utils.GoogleSignIn);
                break;
            }
            case R.id.tv_signin_phone: {
                new SignInActivity().placeForgotPasswordFragment();
                break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("conect_gg_failed",String.valueOf(connectionResult.toString()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {//Google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                signInPresenter.signInGoogle(account);
//                pDialog.show();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(getActivity(),"Error slect account",Toast.LENGTH_SHORT).show();
            }
        }
        else {//Facebook
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
