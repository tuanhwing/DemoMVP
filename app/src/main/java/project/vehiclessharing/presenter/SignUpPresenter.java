package project.vehiclessharing.presenter;

import project.vehiclessharing.callback.SignUpResult;
import project.vehiclessharing.model.SignUpHelper;
import project.vehiclessharing.view.SignUpView;

/**
 * Created by Tuan on 31/07/2017.
 */

public class SignUpPresenter implements SignUpResult {
    private SignUpHelper model;
    private SignUpView view;

    public SignUpPresenter(){

    }

    public SignUpPresenter(SignUpView view, SignUpHelper model){
        this.view = view;
        this.model = model;
        this.model.setOnSignInResult(this);
    }

    /**
     * sign up
     * @param phonenumber
     */
    public void signUp(String phonenumber){
        model.signUp(phonenumber);
    }

    @Override
    public void codeSent() {
        view.codeSent();
    }

    @Override
    public void onSignUpSuccess() {

    }

    @Override
    public void onSignUpError(String error) {

    }
}
