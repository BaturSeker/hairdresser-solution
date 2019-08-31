package com.team.hairdresser.dto;

public class SuccessResponseDto {
    private String title;
    private String message;

    public SuccessResponseDto(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
