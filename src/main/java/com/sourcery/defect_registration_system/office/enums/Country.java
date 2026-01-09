package com.sourcery.defect_registration_system.office.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Country {

    ARGENTINA("Argentina"),
    AUSTRALIA("Australia"),
    BELGIUM("Belgium"),
    BRAZIL("Brazil"),
    CANADA("Canada"),
    CHINA("China"),
    DENMARK("Denmark"),
    FRANCE("France"),
    HUNGARY("Hungary"),
    INDIA("India"),
    IRELAND("Ireland"),
    JAPAN("Japan"),
    LATVIA("Latvia"),
    LITHUANIA("Lithuania"),
    MALAYSIA("Malaysia"),
    MEXICO("Mexico"),
    NETHERLANDS("Netherlands"),
    NEW_ZEALAND("New Zealand"),
    NORWAY("Norway"),
    PHILIPPINES("Philippines"),
    POLAND("Poland"),
    PORTUGAL("Portugal"),
    ROMANIA("Romania"),
    SPAIN("Spain"),
    SWEDEN("Sweden"),
    SWITZERLAND("Switzerland"),
    THAILAND("Thailand"),
    UNITED_ARAB_EMIRATES("United Arab Emirates"),
    UNITED_KINGDOM("United Kingdom"),
    USA("USA");

    private final String displayName;

    Country(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getValue() {
        return displayName;
    }

    public static Country fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(c -> c.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown country: " + displayName)
                );
    }
}

