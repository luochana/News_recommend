package com.luochen.web.register.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerKits {
    public static boolean testUserAccount(String username,String password,String email)
    {
        Pattern usernamePattern = Pattern.compile("^[a-z]{1}[0-9_a-z]{5,16}$");
        Pattern passwordPattern=Pattern.compile("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,20}$");
        Pattern emailPattern=Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        Matcher passwordMatcher=passwordPattern.matcher(password);
        Matcher emailMatcher=emailPattern.matcher(email);
        boolean flag=true;
        if(!usernameMatcher.matches())
            flag=false;
        if(!passwordMatcher.matches())
            flag=false;
        if(!emailMatcher.matches())
            flag=false;
        return flag;
    }
}
