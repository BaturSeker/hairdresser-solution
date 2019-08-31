package com.team.hairdresser.utils.pageablesearch.exception;

/**
 * Undefined search operation exception
 */
public class SearchOperationNotFoundException extends RuntimeException {

    public SearchOperationNotFoundException(String operation) {
        super("Search operation not found: " + operation);
    }

}

