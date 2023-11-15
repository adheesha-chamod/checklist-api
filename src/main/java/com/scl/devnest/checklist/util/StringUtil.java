package com.scl.devnest.checklist.util;

public class StringUtil {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static  String uppercaseAnyMatch(String value) {
        return "%" + value.toUpperCase() + "%";
    }
}
