package com.pitechplus.rcim.backoffice.utils;

import org.springframework.http.ResponseEntity;

/**
 * Created by dgliga on 27.02.2017.
 */
public class DataExtractors {

    /**
     * This method is used to extract the X-AUTH-TOKEN from a auth service response
     *
     * @param authResponse received from server
     * @return String which represents a valid X-AUTH-TOKEN
     */
    public static String extractXAuthTokenFromResponse(ResponseEntity authResponse) {
        return authResponse.getHeaders().get("X-AUTH-TOKEN").get(0);
    }
}
