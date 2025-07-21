package com.web.utils;

public class NumberUtils {
    public static boolean isLong(String value) {
        if (value == null)
            return false;
        try {
            Long numBer = Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static String shortenCurrency(String value) {
        if (value == null || value.isEmpty()) {
            return "0";
        }
        try {
            double number = Double.parseDouble(value);
            if (number >= 1_000_000) {
                return String.format("%.2fM", number / 1_000_000);
            } else if (number >= 1_000) {
                return String.format("%.2fK", number / 1_000);
            } else {
                return String.format("%.2f", number);
            }
        } catch (NumberFormatException ex) {
            return "0";
        }
    }
}
