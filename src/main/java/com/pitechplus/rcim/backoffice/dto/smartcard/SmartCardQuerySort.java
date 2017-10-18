package com.pitechplus.rcim.backoffice.dto.smartcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingProperty;
import com.pitechplus.rcim.backoffice.data.enums.SmartCardSortProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmartCardQuerySort {
	SortDirection direction;
	SmartCardSortProperty property;
}
