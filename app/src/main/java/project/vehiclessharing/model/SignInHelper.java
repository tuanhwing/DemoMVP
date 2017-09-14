package project.vehiclessharing.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import project.vehiclessharing.callback.RequestCallback;
import project.vehiclessharing.callback.SignInResult;
import project.vehiclessharing.request.MakeRequest;
import project.vehiclessharing.util.Utils;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

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
                            handleAPIStoreInfoUser(user);
//                            signInResult.onSignInSuccess();
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
                            handleAPIStoreInfoUser(user);
//                            signInResult.onSignInSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("LOLFB", "signInWithCredential:failure", task.getException());
                            signInResult.onSignInError(String.valueOf(task.getException()));
                        }

                        // ...
                    }
                });
    }

    public void handleAPIStoreInfoUser(final FirebaseUser user){
        HashMap<String,String> params = new HashMap<>();
        Log.d("Token_signin1",String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)));
        Log.d("Token_signin2",String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getIdToken(false)));
        params.put("userId",user.getUid());
        params.put("fullName",user.getDisplayName());
        params.put("urlImage", String.valueOf(user.getPhotoUrl()));
        MakeRequest.makingRequest(Utils.STRING_URL_API + "addUser", Request.Method.POST, params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        signInResult.onSignInSuccess();
                    } else {
                        Toast.makeText(getApplicationContext(), gson.fromJson(result.getString("message"), String.class), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(Utils.TAG_ERROR_API_ADD_USER,String.valueOf(e.getMessage()));
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
