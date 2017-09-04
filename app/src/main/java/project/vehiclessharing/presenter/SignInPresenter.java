package project.vehiclessharing.presenter;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import project.vehiclessharing.callback.SignInResult;
import project.vehiclessharing.model.SignInHelper;
import project.vehiclessharing.util.Utils;
import project.vehiclessharing.view.SignInView;

/**
 * Created by Tuan on 19/07/2017.
 */

public class SignInPresenter implements SignInResult {
    private SignInView view;
    private SignInHelper model;

    public SignInPresenter(SignInView view, SignInHelper model){
        this.view = view;
        this.model = model;
        this.model.setOnSignInResult(this);
    }

    public void startMainActivity(FirebaseUser user){
        view.dismissSignInProgress();
        view.startMainActivity(user);
    }

    /**
     * Sign In
     * @param value - sign in with google/facebook
     */
    public void signIn(int value){
        if(value == Utils.GoogleSignIn){//Google
            view.loadFormSelectAccountGoogle();
        }
    }

    public void signInGoogle(GoogleSignInAccount account){
        view.showSignInProgress();
        model.signInGoogle(account);
    }

    public void singInFacebook(AccessToken accessToken){
        model.signInFacebook(accessToken);
    }

    @Override
    public void onSignInSuccess(FirebaseUser user) {
        view.signInSuccess(user);
    }

    @Override
    public void onSignInError(String error) {
        view.signInError(error);
    }

}
