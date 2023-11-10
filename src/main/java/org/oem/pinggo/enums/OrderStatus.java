package org.oem.pinggo.enums;

public enum OrderStatus {

    NEW('N'),
    ACCEPTED('A'),

    CANCELLED('C'),

    REJECTED('R'),

    DELIVERED('D');

    private final char code;

    OrderStatus(char code) {
        this.code = code;
    }

    public static OrderStatus fromCode(char code) {
        if (code == 'N' || code == 'n') {
            return NEW;
        }
        if (code == 'A' || code == 'a') {
            return ACCEPTED;
        }

        if (code == 'C' || code == 'c') {
            return CANCELLED;
        }
        if (code == 'R' || code == 'r') {
            return REJECTED;
        }

        if (code == 'D' || code == 'd') {
            return DELIVERED;
        }

        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }

    public char getCode() {
        return code;
    }


}
