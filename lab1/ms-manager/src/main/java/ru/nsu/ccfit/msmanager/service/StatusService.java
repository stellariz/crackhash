package ru.nsu.ccfit.msmanager.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;
import ru.nsu.ccfit.msmanager.model.status.RequestStatus;
import ru.nsu.ccfit.msmanager.service.definers.HashCrackStatusResponseBuilder;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final List<HashCrackStatusResponseBuilder> hashCrackStatusResponseBuilders;
    private final RequestStatusDAO requestStatusDAO;

    public RequestStatus initRequestStatus(String requestId) {
        return requestStatusDAO.initRequestData(requestId);
    }

    @RabbitListener(queues = "task_status_queue")
    public void completeJob(CrackHashWorkerResponse response) {
        requestStatusDAO.updateRequestStatus(response.getRequestId(), response.getAnswers().getWords());
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
