package com.team.hairdresser.utils.pageablesearch.model;


public enum SearchOperationEnum {
    EQUALITY, NEGATION, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, CONTAINS, BETWEEN, NOT_CONTAINS;

    private static final String[] OPERATION_SET = {":", "!", ">", ">=", "<", "<=", "~", "b", "!~"};

    public static SearchOperationEnum getOperation(String input) {
        switch (input) {
            case ":":
                return EQUALITY;
            case "!":
                return NEGATION;
            case ">":
                return GREATER_THAN;
            case ">=":
                return GREATER_THAN_OR_EQUAL;
            case "<":
                return LESS_THAN;
            case "<=":
                return LESS_THAN_OR_EQUAL;
            case "~":
                return CONTAINS;
            case "b":
                return BETWEEN;
            case "!~":
                return NOT_CONTAINS;
            default:
                return null;
        }
    }

    public String getOperationKey() {
        return OPERATION_SET[this.ordinal()];
    }
}

