package com.hl.indpark.utils;

public class IDCard {
    public static boolean isIdCardNum(String idCard) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!idCard.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }
}
