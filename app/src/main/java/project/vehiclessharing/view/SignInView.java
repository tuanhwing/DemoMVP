package project.vehiclessharing.view;

/**
 * Created by Tuan on 19/07/2017.
 */

public interface SignInView {

    void loadFormSelectAccountGoogle();

    void showSignInProgress();

    void dismissSignInProgress();

    void signInSuccess();

    void signInError(String error);

    void startMainActivity();


}
