package ru.nsu.ccfit.msmanager.model.request;

import lombok.Data;

@Data
public class HashCrackRequestDto {
    private String hash;
    private Integer maxLength;
}
