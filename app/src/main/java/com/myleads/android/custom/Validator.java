package com.myleads.android.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 10/27/17.
 */

public class Validator {


    private Matcher matcher;
    public boolean validateEmail(String email) {
        final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        Pattern pattern = compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean validateMobile(String phone) {
        final String MOBILE_PATTERN = "^([0-9]{10})|([0-9]{3}\\-[0-9]{3}\\-[0-9]{4})|([0-9]{3}\\s+[0-9]{3}\\s+[0-9]{4})$";
        Pattern pattern = compile(MOBILE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean validateExpiryDate(String expiry) {
        final String EXPIRY_PATTERN = "^([0-9]{4})/([0-9]{2})$";
        Pattern pattern = compile(EXPIRY_PATTERN);
        matcher = pattern.matcher(expiry);
        return matcher.matches();
    }

    public boolean validateCardNumber(String cardNumber) {
        final String CARD_NUMBER_PATTERN = "^(5|4|2)[0-9]+$";
        Pattern pattern = compile(CARD_NUMBER_PATTERN);
        matcher = pattern.matcher(cardNumber);
        return matcher.matches();
    }

    public Pattern compile(String pattern){
        return Pattern.compile(pattern);
    }
}
