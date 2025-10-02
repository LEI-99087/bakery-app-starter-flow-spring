package com.vaadin.starter.bakery.ui.views.admin.products;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

/**
 * Converter for handling price values between presentation (String) and model (Integer) formats.
 * Converts between formatted currency strings (e.g., "10.50") and integer cents (e.g., 1050).
 */
class PriceConverter implements Converter<String, Integer> {

    private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

    /**
     * Converts a presentation value (formatted string) to a model value (integer cents).
     *
     * @param presentationValue the formatted price string (e.g., "10.50")
     * @param valueContext the value context for conversion
     * @return a Result containing the integer cents value or an error message
     */
    @Override
    public Result<Integer> convertToModel(String presentationValue, ValueContext valueContext) {
        try {
            return Result.ok((int) Math.round(df.parse(presentationValue).doubleValue() * 100));
        } catch (ParseException e) {
            return Result.error("Invalid value");
        }
    }

    /**
     * Converts a model value (integer cents) to a presentation value (formatted string).
     *
     * @param modelValue the integer cents value (e.g., 1050)
     * @param valueContext the value context for conversion
     * @return the formatted price string (e.g., "10.50"), or empty string if modelValue is null
     */
    @Override
    public String convertToPresentation(Integer modelValue, ValueContext valueContext) {
        return convertIfNotNull(modelValue, i -> df.format(BigDecimal.valueOf(i, 2)), () -> "");
    }
}