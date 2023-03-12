package ru.nsu.ccfit.msmanager.service.definers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;
import ru.nsu.ccfit.msmanager.service.RequestStatusDAO;

@Component
@RequiredArgsConstructor
public class ReadyStatusResponseBuilder implements HashCrackStatusResponseBuilder {
    private final RequestStatusDAO requestStatusDAO;

    @Override
    public boolean isApplicable(HashCrackStatus requestStatus) {
        return requestStatus == HashCrackStatus.READY;
    }

    @Override
    public HashCrackStatusResponseDto buildResponse(String requestId) {
        return new HashCrackStatusResponseDto(HashCrackStatus.READY, requestStatusDAO.getRequestData(requestId));
    }
}
