package project.vehiclessharing.util;

/**
 * Created by Tuan on 19/07/2017.
 */

public class Utils {

    //Fragment Tags
    public static final String SignInFragment = "SignInFragment";
    public static final String SignUpFragment = "SignUpFragment";
    public static final String ForgotFragment = "ForgotFragment";
    public static final String ConfrimCodeFragment = "ConfirmCodeFragment";


    //Value
    public static final int GoogleSignIn = 1;
    public static final int FacebookSignIn = 2;
    public static final String StringGoogleSignIn = "google.com";
    public static final String StringFacebookSignIn = "facebook.com";


    //Value request permission
    public static final int REQ_PERMISSION_LOCATION = 20;// Value request permission locaiton
    public static final int REQ_RESULT_SOURCE_PLACE = 30;//// Value request place google API
    public static final int REQ_RESULT_DESTINATION_PLACE = 31;//// Value request place google API
    public static final int REQ_PERMISSION_READ_SMS = 41;//// Value request permission locaito

    //String
    public static final String STRING_SIGN_IN = "Sign In";
    public static final String STRING_PLEASE_WAIT = "Please wait...";

    //Unique for hashmap
    public static final String STRING_KEY_CURRENTLOCATION_CURRNET_USER = "CURRENTLOCATION_USER_KEY_QWE";
    public static final String STRING_KEY_START_CURRENT_USER = "LOCATION_USER_KEY_SOURCE_ASD";
    public static final String STRING_KEY_DESTINATION_CURRENT_USER = "LOCATION_USER_DESTINATION_ZXC";


    //URL API
    public static final String STRING_URL_API = "https://us-central1-vehicle-sharing-ef0ad.cloudfunctions.net/";


    //TAG ERROR handle API
    public static final String TAG_ERROR_API_ADD_USER = "API_ADD_USER";



}
