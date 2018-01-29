package com.gajananmotors.shopfinder.utility;

/**
 * Created by asus on 22-Nov-17.
 */

import android.widget.EditText;

import java.util.regex.Pattern;

public class Validation {


    // Regular Expression
    private static final String EMAIL_REGEX = "^[_a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[a-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9]{10}";
    private static final String NAME_REGEX = "[a-zA-Z]+\\.?";

    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid Email";
    private static final String PHONE_MSG = "Invalid Mobile Number";
    private static final String NAME_MSG = "Invalid Name";

    // to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required,String field) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required,field);
    }

    //to check name validation
    public static boolean isName(EditText editText, boolean required,String field) {
        return isValid(editText, NAME_REGEX, NAME_MSG, required,field);
    }

    // to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required,String field) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required,field);
    }



    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required,String field) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText,field)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }
        ;

        return true;
    }

    public static boolean hastext(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText,String field) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(field +REQUIRED_MSG);
            return false;
        }

        return true;
    }
}
