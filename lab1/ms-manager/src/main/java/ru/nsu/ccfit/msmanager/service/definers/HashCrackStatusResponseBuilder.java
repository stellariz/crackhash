package ru.nsu.ccfit.msmanager.service.definers;

import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;

public interface HashCrackStatusResponseBuilder {
    boolean isApplicable(HashCrackStatus requestStatus);
    HashCrackStatusResponseDto buildResponse(String requestId);
}
