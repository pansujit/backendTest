package com.pitechplus.rcim.backoffice.utils.exceptions;

import lombok.Getter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Set;

/**
 * Created by dgliga on 21.02.2017.
 */

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackOfficeException extends Exception {
    private String message;
    private String status;
    private String date;
    private String developerMessage;
    private Set<String> validationErrors;
    private String errorCode;
}
