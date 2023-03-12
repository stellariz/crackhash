package ru.nsu.ccfit.msmanager.service.definers;

import org.springframework.stereotype.Component;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;

@Component
public class DefaultStatusResponseBuilder implements HashCrackStatusResponseBuilder{
    @Override
    public boolean isApplicable(HashCrackStatus requestStatus) {
        return requestStatus == null;
    }

    @Override
    public HashCrackStatusResponseDto buildResponse(String requestId) {
        return new HashCrackStatusResponseDto();
    }
}
