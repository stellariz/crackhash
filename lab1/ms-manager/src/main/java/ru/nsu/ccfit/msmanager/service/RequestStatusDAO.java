package ru.nsu.ccfit.msmanager.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.status.RequestStatus;
import ru.nsu.ccfit.msmanager.repository.RequestStatusRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestStatusDAO {
    @Value("${workers.number:1}")
    private int WORKERS_NUMBER;

    @Value("${workers.timeout}")
    private int timeout;
    private final RequestStatusRepository requestStatusRepository;

    @Transactional
    public void updateRequestStatus(String requestId, List<String> answers) {
        Optional<RequestStatus> requestStatus = requestStatusRepository.findById(requestId);

        requestStatus.ifPresent(status -> {
            if (status.getHashCrackStatus() == HashCrackStatus.ERROR) {
                return;
            }
            log.info("Request with id {} updating", requestId);
            if (status.getCompletedWorkers() + 1 == WORKERS_NUMBER) {
                log.info("Request with id {} completed", requestId);
                status.setHashCrackStatus(HashCrackStatus.READY);
            }
            status.setCompletedWorkers(status.getCompletedWorkers() + 1);
            status.setData(ListUtils.union(status.getData(), answers));
            requestStatusRepository.save(status);
        });
    }

    @Transactional
    public RequestStatus initRequestData(String requestId) {
        return requestStatusRepository.save(new RequestStatus(requestId, HashCrackStatus.IN_PROGRESS,
                0, new ArrayList<>(), LocalDateTime.now()));
    }

    public List<String> getRequestData(String requestId) {
        Optional<RequestStatus> requestStatus = requestStatusRepository.findById(requestId);
        return requestStatus.map(RequestStatus::getData).orElse(Collections.emptyList());
    }

    public HashCrackStatus getCrackStatus(String requestId) {
        Optional<RequestStatus> requestStatus = requestStatusRepository.findById(requestId);
        return requestStatus.map(RequestStatus::getHashCrackStatus).orElse(null);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    private void setErrorRequestStatusDueToTimeout() {
        log.info("Checking uncompleted requests");
        requestStatusRepository.findRequestStatusByHashCrackStatus(HashCrackStatus.IN_PROGRESS).stream()
                .filter(request ->
                        ChronoUnit.SECONDS.between(request.getCreatedDate(), LocalDateTime.now()) > timeout)
                .forEach(requestStatus -> {
                    log.info("Request with id [{}] wasn't completed", requestStatus.getRequestId());
                    requestStatus.setHashCrackStatus(HashCrackStatus.ERROR);
                    requestStatusRepository.save(requestStatus);
                });
    }
}
