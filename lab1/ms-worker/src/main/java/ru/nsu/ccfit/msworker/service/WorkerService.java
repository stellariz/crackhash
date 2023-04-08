package ru.nsu.ccfit.msworker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.msworker.service.HashCrackerService;
import ru.nsu.ccfit.schema.CrackHashManagerRequest;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {
    private final HashCrackerService hashCrackerService;

    @RabbitListener(queues = "task_queue")
    @SendTo("task_status_queue")
    public CrackHashWorkerResponse startJob(CrackHashManagerRequest request) {
        log.info("Received request with id {}", request.getRequestId());
        CrackHashWorkerResponse response = new CrackHashWorkerResponse();
        response.setRequestId(request.getRequestId());
        response.setPartNumber(request.getPartNumber());

        CrackHashWorkerResponse.Answers responseAnswers = new CrackHashWorkerResponse.Answers();
        List<String> answers = hashCrackerService.crackHash(request.getAlphabet().getSymbols(),
                request.getPartNumber(), request.getPartCount(), request.getMaxLength(), request.getHash());
        responseAnswers.getWords().addAll(answers);
        response.setAnswers(responseAnswers);

        return response;
    }
}
