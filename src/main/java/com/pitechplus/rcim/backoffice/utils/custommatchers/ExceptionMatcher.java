package com.pitechplus.rcim.backoffice.utils.custommatchers;

import com.pitechplus.qautils.randomgenerators.DateGenerator;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dgliga on 21.02.2017.
 */
public class ExceptionMatcher {

    /**
     * This Custom Matcher checks that exception received from server is the one expected
     *
     * @param httpStatus       which the server should have in response
     * @param message          expected message in the response body
     * @param validationErrors expected validation errors in the response body
     * @return Matcher which checks all the above
     */
    public static Matcher<BackOfficeException> isExpectedBackOfficeException(HttpStatus httpStatus, String message,
                                                                             String developerMessage, Set<String> validationErrors) {
        return new TypeSafeMatcher<BackOfficeException>() {
            @Override
            protected boolean matchesSafely(BackOfficeException exception) {
                boolean areValidationErrorsCorrect = true;
                boolean isDeveloperMessageCorrect = true;
                if (validationErrors != null) {
                              areValidationErrorsCorrect = exception.getValidationErrors().equals(validationErrors);
                }
                if (developerMessage != null) {
                    isDeveloperMessageCorrect = exception.getDeveloperMessage().equals(developerMessage);
                }
                return exception.getStatus().equals(httpStatus.toString())
                        && exception.getMessage().equals(message)
                        && exception.getDate().contains(DateGenerator.getCurrentDate().toString("yyyy-MM-dd"))
                        && areValidationErrorsCorrect
                        && isDeveloperMessageCorrect;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Exception with status: " + httpStatus.toString() + ", message: " + message + " and " +
                        "date today.");
                if (validationErrors != null) {
                    description.appendText("\nValidation errors: ");
                    for (String error : validationErrors) {
                        description.appendText("\n" + error);
                    }
                }
                if (developerMessage != null) {
                    description.appendText("\nDeveloper message: " + developerMessage);
                }
            }

            @Override
            public void describeMismatchSafely(final BackOfficeException exception, final Description mismatchDescription) {
                mismatchDescription.appendText("was Exception with status: " + exception.getStatus() + ", message: " +
                        exception.getMessage() + " and date: " + exception.getDate());
                if (exception.getValidationErrors() != null) {
                    mismatchDescription.appendText("\nValidation errors: ");
                    for (String error : exception.getValidationErrors()) {
                        mismatchDescription.appendText("\n" + error);
                    }
                }
                if (exception.getDeveloperMessage() != null) {
                    mismatchDescription.appendText("\nDeveloper message: " + exception.getDeveloperMessage());
                }
            }
        };
    }
}
