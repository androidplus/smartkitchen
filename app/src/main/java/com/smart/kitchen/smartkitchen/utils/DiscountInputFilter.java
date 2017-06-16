package com.smart.kitchen.smartkitchen.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscountInputFilter implements InputFilter {
    private final Double MAX_VALUE = Double.valueOf(10.0d);
    private final String POINTER = ".";
    private final int POINTER_LENGTH = 1;
    private final String ZERO = "0";
    Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        String charSequence2 = charSequence.toString();
        String obj = spanned.toString();
        if (TextUtils.isEmpty(charSequence2)) {
            return "";
        }
        if (obj.length() == 0 && ".".equals(charSequence2)) {
            return "";
        }
        if (i == 0 && ".".equals(charSequence2)) {
            return "";
        }
        Matcher matcher = this.mPattern.matcher(charSequence);
        if (obj.contains(".")) {
            if (!matcher.matches()) {
                return "";
            }
            if (".".equals(charSequence)) {
                return "";
            }
            if (obj.length() > 1 && "0".equals(obj.substring(0, 1)) && Integer.valueOf(obj.substring(0, obj.indexOf("."))).intValue() == 0) {
                return "";
            }
            if (i4 - obj.indexOf(".") > 1) {
                return spanned.subSequence(i3, i4);
            }
        } else if (!matcher.matches()) {
            return "";
        } else {
            if ("0".equals(charSequence) && obj.length() == 1 && "0".equals(charSequence)) {
                return "";
            }
        }
        if (Double.parseDouble(obj + charSequence2) > this.MAX_VALUE.doubleValue()) {
            return spanned.subSequence(i3, i4);
        }
        return spanned.subSequence(i3, i4) + charSequence2;
    }
}
