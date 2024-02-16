package com.tweesky.cloudtools.codegen.util;

import java.util.Locale;

public class RequestHelper {

    public String getCurl(String url, String verb) {
        return getCurl(url, verb, null);
    }

    public String getCurl(String url, String verb, String body) {
        String curl = "curl -X " + verb.toUpperCase(Locale.ROOT) + " '" + url + "'";

        if(body != null) {
            curl = curl + " -d '" + body + "'";
        }

        return curl;
    }
}
