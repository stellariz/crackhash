package ru.nsu.ccfit.msworker.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.msworker.service.HashCrackerService;
import ru.nsu.ccfit.msworker.service.UpdateRequestStatusService;
import ru.nsu.ccfit.schema.CrackHashManagerRequest;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Slf4j
@RestController
@RequestMapping("/internal/api/worker/hash/crack/task")
@RequiredArgsConstructor
public class WorkerController {
    private final HashCrackerService hashCrackerService;
    private final UpdateRequestStatusService updateRequestStatusService;

    @PostMapping
    public CrackHashWorkerResponse startJob(@RequestBody CrackHashManagerRequest request) {
        log.debug("Received request with id {}", request.getRequestId());
        CrackHashWorkerResponse response = new CrackHashWorkerResponse();
        response.setRequestId(request.getRequestId());
        response.setPartNumber(request.getPartNumber());

        CrackHashWorkerResponse.Answers responseAnswers = new CrackHashWorkerResponse.Answers();
        List<String> answers = hashCrackerService.crackHash(request.getAlphabet().getSymbols(),
                request.getPartNumber(), request.getPartCount(), request.getMaxLength(), request.getHash());
        responseAnswers.getWords().addAll(answers);
        response.setAnswers(responseAnswers);

        updateRequestStatusService.updateStatus(response);
        return response;
    }
}
