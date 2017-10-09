package com.pitechplus.rcim.backoffice.utils.builders;

import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.data.enums.ValidationError;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dgliga on 28.02.2017.
 */
public class ValidationErrorsBuilder {


    /**
     * This method builds a Set of validation errors
     *
     * @param serviceCalled       for which you want the validation errors
     * @param validationErrors to be included in the Set
     * @return Set containing validation errors which you gave as arguments
     */
    public static Set<String> buildValidationErrors(ServiceCalled serviceCalled, ValidationError... validationErrors) {
        Set<String> validationErrorsSet = new HashSet<>();
        for (ValidationError validationError : validationErrors) {
            validationErrorsSet.add(validationError.getField() + serviceCalled.getValue() + validationError.getErrorMessage());
        }
        return validationErrorsSet;
    }
}
