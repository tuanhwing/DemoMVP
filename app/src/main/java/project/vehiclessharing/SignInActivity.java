package project.vehiclessharing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;

import project.vehiclessharing.util.Utils;

/**
 * Created by Tuan on 19/07/2017.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private static FragmentManager fragmentManager;// Instance fragmentManager to switch fragment

    private ImageView imgClose;//Image close

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//set up notitle

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//DO NOT ROTATE the screen even if the user is shaking his phone like mad

        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        FacebookSdk.sdkInitialize(getApplicationContext());// This function initializes the Facebook SDK
        AppEventsLogger.activateApp(this);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

        setContentView(R.layout.activity_signin);
        fragmentManager = getSupportFragmentManager();

        //If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new SignInFragment(),
                            Utils.SignInFragment).commit();
        }

        addControls();
        addEvents();
    }

    private void addControls() {

    }

    private void addEvents() {
    }


    /**
     * Replace Login Fragment with animation
     */
    public void replaceSigninFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new SignInFragment(),
                        Utils.SignInFragment).commit();
    }

    /**
     * Replace forgot password fragment with animation
     */
    public void placeForgotPasswordFragment(){
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.frameContainer,
                        new ForgotFragment(),
                        Utils.ForgotFragment).commit();
    }

    /**
     * Replace signup frgament with animation
     */
    public void placeSignupFragment(){
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.frameContainer, new SignUpFragment(),
                        Utils.SignUpFragment).commit();
    }

    /**
     * Replace Confirm Code Fragment with animation
     */
    public void placeCorfirmCodeFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new SignInFragment(),
                        Utils.SignInFragment).commit();
    }
    @Override
    public void onBackPressed() {
        // Find the tag of signup and forgot password fragment
        Fragment signUpFragment = fragmentManager
                .findFragmentByTag(Utils.SignUpFragment);
        Fragment forgotFragment = fragmentManager
                .findFragmentByTag(Utils.ForgotFragment);
        Fragment confirmCodeFragment = fragmentManager
                .findFragmentByTag(Utils.ConfrimCodeFragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (signUpFragment != null)
            replaceSigninFragment();
        else if (forgotFragment != null)
            replaceSigninFragment();
        else if (confirmCodeFragment != null)
            placeSignupFragment();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
