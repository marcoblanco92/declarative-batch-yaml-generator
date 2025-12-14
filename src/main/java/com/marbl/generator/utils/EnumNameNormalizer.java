package com.marbl.generator.utils;

public final class EnumNameNormalizer {

    private EnumNameNormalizer() {}

    public static String normalize(String input) {
        return input
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase();
    }
}
