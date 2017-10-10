package com.pitechplus.rcim.backoffice.utils.custommatchers;

import com.google.common.collect.Ordering;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole.*;

/**
 * Created by dgliga on 16.03.2017.
 */
public class SortingMatchers {

    private static Logger LOGGER = LogManager.getLogger(SortingMatchers.class);


    /**
     * This Custom Matcher is used to check if a List of objects is correctly sorted by a field
     *
     * @param fieldSortedBy field by which you want to check if the List is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return Matcher which analyzes if your list is sorted by one field in the sort direction expected
     */
    public static Matcher<List> isListSortedByField(String fieldSortedBy, SortDirection sortDirection) {
        return new TypeSafeMatcher<List>() {
            @Override
            protected boolean matchesSafely(List listToBeChecked) {
                return checkSortingByString(listToBeChecked, fieldSortedBy, sortDirection);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("List sorted by field: " + fieldSortedBy + " direction: " + sortDirection.toString());
            }

            @Override
            public void describeMismatchSafely(final List listToBeChecked, final Description mismatchDescription) {
                mismatchDescription.appendText("was list which is not sorted by: " + fieldSortedBy + " direction: " +
                        sortDirection.toString());
            }
        };
    }

    /**
     * This Custom Matcher is used to check if a List of objects is correctly sorted by a boolean field
     *
     * @param fieldSortedBy field by which you want to check if the List is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return Matcher which analyzes if your list is sorted by boolean field in the sort direction expected
     */
    public static Matcher<List> isListSortedByBoolean(String fieldSortedBy, SortDirection sortDirection) {
        return new TypeSafeMatcher<List>() {
            @Override
            protected boolean matchesSafely(List listToBeChecked) {
                return checkSortingByBoolean(listToBeChecked, fieldSortedBy, sortDirection);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("List sorted by field: " + fieldSortedBy + " direction: " + sortDirection.toString());
            }

            @Override
            public void describeMismatchSafely(final List listToBeChecked, final Description mismatchDescription) {
                mismatchDescription.appendText("was list which is not sorted by: " + fieldSortedBy + " direction: " +
                        sortDirection.toString());
            }
        };
    }

    /**
     * This Custom Matcher is used to check if a List of objects is correctly sorted by back office role
     *
     * @param fieldSortedBy field by which you want to check if the List is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return Matcher which analyzes if your list is sorted by back office role in the sort direction expected
     */
    public static Matcher<List> isListSortedByRole(String fieldSortedBy, SortDirection sortDirection) {
        return new TypeSafeMatcher<List>() {
            @Override
            protected boolean matchesSafely(List listToBeChecked) {
                return checkSortingByBackOfficeRole(listToBeChecked, fieldSortedBy, sortDirection);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("List sorted by field: " + fieldSortedBy + " direction: " + sortDirection.toString());
            }

            @Override
            public void describeMismatchSafely(final List listToBeChecked, final Description mismatchDescription) {
                mismatchDescription.appendText("was list which is not sorted by: " + fieldSortedBy + " direction: " +
                        sortDirection.toString());
            }
        };
    }

    /**
     * This method checks if a List of objects is sorted by one String field in the expected sort direction ( ASC / DESC )
     *
     * @param objects       the list which you want to check if it is sorted
     * @param fieldSortedBy field by which you want to check if the list is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return true or false depending if the list is sorted or not by the field in the sort direction expected
     */
    private static boolean checkSortingByString(List objects, String fieldSortedBy, SortDirection sortDirection) {
        boolean result = false;
        try {
       System.out.println("this is my ttest"+ objects.get(0).getClass().getDeclaredField(fieldSortedBy)); 	
            for (int i = 0; i < objects.size() - 1; i++) {
                Field fieldOne = objects.get(i).getClass().getDeclaredField(fieldSortedBy);
                Field fieldTwo = objects.get(i + 1).getClass().getDeclaredField(fieldSortedBy);
                fieldOne.setAccessible(true);
                fieldTwo.setAccessible(true);
                String valueOne = (String) fieldOne.get(objects.get(i));
                String valueTwo = (String) fieldTwo.get(objects.get(i + 1));
                if (sortDirection.equals(SortDirection.ASC) & valueOne.compareToIgnoreCase(valueTwo) <= 0 ||
                        sortDirection.equals(SortDirection.DESC) & valueOne.compareToIgnoreCase(valueTwo) >= 0) {
                    result = true;
                } else {
                    return false;
                }

            }
        } catch (Exception exception) {
            LOGGER.info("Could not check if list is sorted because of exception: " + exception.toString());
        }
        return result;
    }

    /**
     * This method checks if a List of objects is sorted by one boolean field in the expected sort direction ( ASC / DESC )
     *
     * @param objects       the list which you want to check if it is sorted
     * @param fieldSortedBy field by which you want to check if the list is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return true or false depending if the list is sorted or not by the boolean field in the sort direction expected
     */
    private static boolean checkSortingByBoolean(List objects, String fieldSortedBy, SortDirection sortDirection) {
        boolean result = false;
        List<Boolean> entitiesBooleanOrder = new ArrayList<>();
        try {
            for (Object object : objects) {
                Field field = object.getClass().getDeclaredField(fieldSortedBy);
                field.setAccessible(true);
                entitiesBooleanOrder.add((Boolean) field.get(object));
            }
        } catch (Exception exception) {
            LOGGER.info("Could not load elements into boolean list to check because of exception: " + exception.toString());
        }
        if (sortDirection.equals(SortDirection.ASC)) {
            result = Ordering.explicit(false, true).isOrdered(entitiesBooleanOrder);
        } else if (sortDirection.equals(SortDirection.DESC)) {
            result = Ordering.explicit(true, false).isOrdered(entitiesBooleanOrder);
        }
        return result;
    }

    /**
     * This method checks if a List of objects is sorted by back office role in the expected sort direction ( ASC / DESC )
     *
     * @param objects       the list which you want to check if it is sorted
     * @param fieldSortedBy field by which you want to check if the list is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return true or false depending if the list is sorted or not by back office role in the sort direction expected
     */
    private static boolean checkSortingByBackOfficeRole(List objects, String fieldSortedBy, SortDirection sortDirection) {
        boolean result = false;
        List<BackOfficeRole> userRoles = new ArrayList<>();
        try {
            for (Object object : objects) {
                Field field = object.getClass().getDeclaredField(fieldSortedBy);
                field.setAccessible(true);
                userRoles.add(BackOfficeRole.valueOf((String) field.get(object)));
            }
        } catch (Exception exception) {
            LOGGER.info("Could not load elements into boolean list to check because of exception: " + exception.toString());
        }
        if (sortDirection.equals(SortDirection.ASC)) {
            result = Ordering.explicit(ROLE_ADMIN, ROLE_CALL_CENTER_OPERATOR, ROLE_FLEET_MANAGER, ROLE_SUPER_ADMIN).isOrdered(userRoles);
        } else if (sortDirection.equals(SortDirection.DESC)) {
            result = Ordering.explicit(ROLE_SUPER_ADMIN, ROLE_FLEET_MANAGER, ROLE_CALL_CENTER_OPERATOR, ROLE_ADMIN).isOrdered(userRoles);
        }
        return result;
    }
}
