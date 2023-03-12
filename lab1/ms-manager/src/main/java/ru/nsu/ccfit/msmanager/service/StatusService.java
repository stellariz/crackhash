package ru.nsu.ccfit.msmanager.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;
import ru.nsu.ccfit.msmanager.service.definers.HashCrackStatusResponseBuilder;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final List<HashCrackStatusResponseBuilder> hashCrackStatusResponseBuilders;
    private final RequestStatusDAO requestStatusDAO;

    public void initRequestStatus(String requestId) {
        requestStatusDAO.initRequestData(requestId);
    }

    public void completeJob(String requestId, List<String> answers) {
        requestStatusDAO.updateRequestStatus(requestId, answers);
    }

    public HashCrackStatusResponseDto getRequestStatus(String requestId) {
        var requestStatus = requestStatusDAO.getCrackStatus(requestId);
        for (HashCrackStatusResponseBuilder builder : hashCrackStatusResponseBuilders) {
            if (builder.isApplicable(requestStatus)) {
                return builder.buildResponse(requestId);
            }
        }
        return null;
    }
}
