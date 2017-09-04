package project.vehiclessharing.view;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tuan on 19/07/2017.
 */

public interface SignInView {

    void loadFormSelectAccountGoogle();

    void showSignInProgress();

    void dismissSignInProgress();

    void signInSuccess(FirebaseUser user);

    void signInError(String error);

    void startMainActivity(FirebaseUser user);

}
