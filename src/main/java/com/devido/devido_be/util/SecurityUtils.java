package com.devido.devido_be.util;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;


public final class SecurityUtils {
    private static final String PRINCIPAL = "principal";
    private static final String ID = "uid";
    private static final String NAME = "name";

    public static String getCurrentUserId() {
        return getKey(ID);
    }

    public static String getCurrentUserName() {
        return getKey(NAME);
    }

    public static String getKey(String key) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof Jwt jwt) {
                var value = jwt.getClaim(key);
                if (value != null) {
                    return (String) value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
