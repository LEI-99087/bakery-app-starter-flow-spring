package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.flow.component.ItemLabelGenerator;

/**
 * Utility class providing helper methods for data providers.
 * Contains common operations for data conversion and label generation.
 */
public class DataProviderUtil {

    /**
     * Converts a source object to a target type if the source is not null.
     *
     * @param <S> the source type
     * @param <T> the target type
     * @param source the source object to convert
     * @param converter the function to convert the source to target type
     * @return the converted object, or null if source is null
     */
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
        return convertIfNotNull(source, converter, () -> null);
    }

    /**
     * Converts a source object to a target type if the source is not null,
     * otherwise returns a value from the nullValueSupplier.
     *
     * @param <S> the source type
     * @param <T> the target type
     * @param source the source object to convert
     * @param converter the function to convert the source to target type
     * @param nullValueSupplier supplier that provides a value when source is null
     * @return the converted object, or the value from nullValueSupplier if source is null
     */
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
        return source != null ? converter.apply(source) : nullValueSupplier.get();
    }

    /**
     * Creates an ItemLabelGenerator that uses the provided converter function.
     * Handles null items by returning an empty string.
     *
     * @param <T> the item type
     * @param converter the function to convert items to string labels
     * @return an ItemLabelGenerator that uses the provided converter
     */
    public static <T> ItemLabelGenerator<T> createItemLabelGenerator(Function<T, String> converter) {
        return item -> convertIfNotNull(item, converter, () -> "");
    }
}