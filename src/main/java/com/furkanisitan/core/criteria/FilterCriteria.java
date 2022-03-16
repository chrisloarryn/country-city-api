package com.furkanisitan.core.criteria;

import com.furkanisitan.core.exceptions.InvalidFieldException;
import com.furkanisitan.core.exceptions.InvalidFilterException;
import com.furkanisitan.core.utils.GenericUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@Getter
public final class FilterCriteria {

    private static final String REGEX = "(^\\w+)(:|!:|:%|%:|%%|>|<|>:|<:)(.+$)";

    private final String field;
    private final FilterOperator operator;
    private final String value;

    private FilterCriteria(String field, FilterOperator operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public static <T> FilterCriteria of(Class<T> clazz, String filter) {

        if (StringUtils.isAllBlank(filter))
            return null;

        var matcher = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS).matcher(filter);
        if (!matcher.find())
            throw new InvalidFilterException(filter);

        var fieldName = matcher.group(1);
        if (!GenericUtils.hasField(clazz, fieldName))
            throw new InvalidFieldException(fieldName);

        return new FilterCriteria(fieldName, FilterOperator.of(matcher.group(2)), matcher.group(3));
    }
}
