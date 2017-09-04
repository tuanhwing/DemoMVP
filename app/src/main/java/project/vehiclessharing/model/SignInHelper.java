package project.vehiclessharing.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import project.vehiclessharing.callback.SignInResult;

import static android.content.ContentValues.TAG;

/**
 * Created by Tuan on 19/07/2017.
 */

public class SignInHelper {
    private SignInResult signInResult;

    private FirebaseAuth firebaseAuth;
    private Activity activity;

   public SignInHelper(){
   }

   public SignInHelper(Activity activity){
       this.activity = activity;
   }

   public void setOnSignInResult(SignInResult signInResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.signInResult = signInResult;
   }

    /**
     * Action sign in with account Google
     * @param account Google account
     */
    public void signInGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            signInResult.onSignInSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            signInResult.onSignInError(String.valueOf(task.getException().getMessage()));
                        }

                        // ...
                    }
                });
    }

    /**
     * Action sign in with account Facebook
     * @param accessToken
     */
    public void signInFacebook(final AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOLFB", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            signInResult.onSignInSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("LOLFB", "signInWithCredential:failure", task.getException());
                            signInResult.onSignInError(String.valueOf(task.getException()));
                        }

                        // ...
                    }
                });
    }

}
