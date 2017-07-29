package com.smart.kitchen.smartkitchen.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CashierInputFilter implements InputFilter {
    private final int MAX_VALUE = Integer.MAX_VALUE;
    private final String POINTER = ".";
    private final int POINTER_LENGTH = 2;
    private final String ZERO = "0";
    Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        String charSequence2 = charSequence.toString();
        String obj = spanned.toString();
        if (TextUtils.isEmpty(charSequence2)) {
            return "";
        }
        if (obj.length() > 1 && "0".equals(obj.substring(0, 1)) && "0".equals(obj.substring(1, 2))) {
            return spanned.subSequence(i3, i4);
        }
        Matcher matcher = this.mPattern.matcher(charSequence);
        if (obj.contains(".")) {
            if (!matcher.matches()) {
                return "";
            }
            if (".".equals(charSequence.toString())) {
                return "";
            }
            if (i4 - obj.indexOf(".") > 2) {
                return spanned.subSequence(i3, i4);
            }
        } else if (!matcher.matches()) {
            return "";
        } else {
            if (".".equals(charSequence.toString()) && TextUtils.isEmpty(obj)) {
                return "";
            }
            if (!".".equals(charSequence.toString()) && "0".equals(obj)) {
                return "";
            }
        }
        if (Double.parseDouble(obj + charSequence2) > 2.147483647E9d) {
            return spanned.subSequence(i3, i4);
        }
        return spanned.subSequence(i3, i4) + charSequence2;
    }
}
