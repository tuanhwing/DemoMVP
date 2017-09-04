package project.vehiclessharing;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import project.vehiclessharing.model.SignUpHelper;
import project.vehiclessharing.presenter.SignUpPresenter;
import project.vehiclessharing.view.SignUpView;

import static android.content.ContentValues.TAG;

/**
 * Created by Tuan on 24/07/2017.
 */

public class SignUpFragment extends Fragment implements SignUpView, View.OnClickListener{
    private static View view;

    //MVP
    private SignUpPresenter signUpPresenter;
    private SignUpHelper signUpHelper;

    private EditText edFullName;
    private EditText edPhone;
    private EditText edPassword;
    private EditText edConfirmPassword;
    private RadioButton rdMale, rdFemale;
    private TextView tvAleadyUser;
    private Button btnSignup;
    private CheckBox termsConditions;

    private FirebaseAuth mAuth;//temp

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }
        if(signUpPresenter == null){
            if(signUpHelper == null){
                signUpHelper = new SignUpHelper(getActivity());
            }
            signUpPresenter = new SignUpPresenter(this,signUpHelper);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls();
        addEvents();
        setTextSelector();

    }

    private void setTextSelector() {
        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            tvAleadyUser.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void addControls() {
        edFullName = (EditText) view.findViewById(R.id.ed_fullname);
        edPhone = (EditText) view.findViewById(R.id.ed_phone);
        edPassword = (EditText) view.findViewById(R.id.ed_password);
        edConfirmPassword = (EditText) view.findViewById(R.id.ed_confirm_assword);
        rdMale = (RadioButton) view.findViewById(R.id.rd_male);
        rdFemale = (RadioButton) view.findViewById(R.id.rd_female);
        tvAleadyUser = (TextView) view.findViewById(R.id.tv_already_user);
        btnSignup = (Button) view.findViewById(R.id.btn_signup);
        termsConditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        mAuth = FirebaseAuth.getInstance();//temp
    }

    private void addEvents() {
        tvAleadyUser.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                Toast.makeText(getActivity(),"Invalid code",Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_already_user: {
                // Replace login fragment
                new SignInActivity().replaceSigninFragment();
                break;
            }
            case R.id.btn_signup: {
                signUpPresenter.signUp(String.valueOf(edPhone.getText().toString()));
                break;
            }
        }

    }

    @Override
    public void codeSent() {
        new SignInActivity().placeCorfirmCodeFragment();
    }
}
