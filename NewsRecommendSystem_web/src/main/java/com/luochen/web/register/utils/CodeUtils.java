package com.luochen.web.register.utils;

import java.util.UUID;

public class CodeUtils {
    public static String generateUniqueCode(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
