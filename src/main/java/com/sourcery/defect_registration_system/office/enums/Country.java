package com.sourcery.defect_registration_system.office.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Country {
    ALL,
    ARGENTINA,
    AUSTRALIA,
    BELGIUM,
    BRAZIL,
    CANADA,
    CHINA,
    DENMARK,
    FRANCE,
    HUNGARY,
    INDIA,
    IRELAND,
    JAPAN,
    LATVIA,
    LITHUANIA,
    MALAYSIA,
    MEXICO,
    NETHERLANDS,
    NEW_ZEALAND,
    NORWAY,
    PHILIPPINES,
    POLAND,
    PORTUGAL,
    ROMANIA,
    SPAIN,
    SWEDEN,
    SWITZERLAND,
    THAILAND,
    UNITED_ARAB_EMIRATES,
    UNITED_KINGDOM,
    USA;

    @JsonValue
    public String getValue() {
        return name();
    }
}
