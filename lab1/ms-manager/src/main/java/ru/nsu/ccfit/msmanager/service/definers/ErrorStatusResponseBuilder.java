package ru.nsu.ccfit.msmanager.service.definers;

import java.util.Collections;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;

@Component
public class ErrorStatusResponseBuilder implements HashCrackStatusResponseBuilder {
    @Override
    public boolean isApplicable(HashCrackStatus requestStatus) {
        return requestStatus == HashCrackStatus.ERROR;
    }

    @Override
    public HashCrackStatusResponseDto buildResponse(String requestId) {
        return new HashCrackStatusResponseDto(HashCrackStatus.ERROR, Collections.emptyList());
    }
}
