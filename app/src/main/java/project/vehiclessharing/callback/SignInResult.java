package project.vehiclessharing.callback;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tuan on 19/07/2017.
 */

public interface SignInResult {
    public void onSignInSuccess(FirebaseUser user);

    public void onSignInError(String error);


}
