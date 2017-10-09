package com.pitechplus.rcim.nissan.be.nissanutils.nissancustommatchers;

import com.google.common.collect.Ordering;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto.*;

/**
 * Created by dgliga  on 20.09.2017.
 */
public class NissanSortingMatchers {
    private static Logger LOGGER = LogManager.getLogger(NissanSortingMatchers.class);

    /**
     * This Custom Matcher is used to check if a List of objects is correctly sorted by group status or state
     *
     * @param fieldSortedBy field by which you want to check if the List is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return Matcher which analyzes if your list is sorted by group satus in the sort direction expected
     */
    public static Matcher<List> isListSortedByGroupStatus(String fieldSortedBy, SortDirection sortDirection) {
        return new TypeSafeMatcher<List>() {
            @Override
            protected boolean matchesSafely(List listToBeChecked) {
                return checkSortingByGroupState(listToBeChecked, fieldSortedBy, sortDirection);
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
     * This method checks if a List of objects is sorted by group status in the expected sort direction ( ASC / DESC )
     *
     * @param objects       the list which you want to check if it is sorted
     * @param fieldSortedBy field by which you want to check if the list is sorted
     * @param sortDirection sort direction ASC or DESC which you expect your list to have
     * @return true or false depending if the list is sorted or not by group status in the sort direction expected
     */
    private static boolean checkSortingByGroupState(List objects, String fieldSortedBy, SortDirection sortDirection) {
        boolean result = false;
        List<GroupStateDto> groupState = new ArrayList<>();
        try {
            for (Object object : objects) {
                Field field = object.getClass().getDeclaredField(fieldSortedBy);
                field.setAccessible(true);
                groupState.add(GroupStateDto.valueOf((String) field.get(object)));
            }
        } catch (Exception exception) {
            LOGGER.info("Could not load elements into enum list to check because of exception: " + exception.toString());
        }
        if (sortDirection.equals(SortDirection.ASC)) {
            result = Ordering.explicit(ACTIVE, CLOSED, ENDED, INACTIVE, PENDING, READY).isOrdered(groupState);
        } else if (sortDirection.equals(SortDirection.DESC)) {
            result = Ordering.explicit(INACTIVE, READY, ENDED, CLOSED, ACTIVE, PENDING).isOrdered(groupState);
        }
        return result;
    }
}


