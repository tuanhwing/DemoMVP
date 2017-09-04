package project.vehiclessharing.callback;

/**
 * Created by Tuan on 31/07/2017.
 */

public interface SignUpResult {
    public void codeSent();

    public void onSignUpSuccess();

    public void onSignUpError(String error);
}
