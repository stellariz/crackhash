package ru.nsu.ccfit.msmanager.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.status.RequestStatus;

@Slf4j
@Service
public class RequestStatusDAO {
    @Value("${workers.number:1}")
    private int WORKERS_NUMBER;
    private final Map<String, RequestStatus> statusHashMap = new ConcurrentHashMap<>();

    public RequestStatus updateRequestStatus(String requestId, List<String> answers) {
        return statusHashMap.computeIfPresent(requestId, (k, v) -> {
            if (v.getCompletedWorkers() + 1 == WORKERS_NUMBER) {
                log.info("Request with id {} completed", requestId);
                return new RequestStatus(HashCrackStatus.READY, v.getCompletedWorkers() + 1,
                        ListUtils.union(v.getData(), answers));
            }
            log.info("Request with id {} updated", requestId);
            return new RequestStatus(v.getHashCrackStatus(), v.getCompletedWorkers() + 1,
                    ListUtils.union(v.getData(), answers));
        });
    }

    public RequestStatus initRequestData(String requestId) {
        return statusHashMap.put(requestId, new RequestStatus(HashCrackStatus.IN_PROGRESS, 0,
                new ArrayList<>()));
    }

    public List<String> getRequestData(String requestId) {
        RequestStatus requestStatus = statusHashMap.get(requestId);
        if (requestStatus == null) {
            return Collections.emptyList();
        }
        return ListUtils.emptyIfNull(requestStatus.getData());
    }

    public HashCrackStatus getCrackStatus(String requestId) {
        RequestStatus requestStatus = statusHashMap.get(requestId);
        return requestStatus == null ? null : requestStatus.getHashCrackStatus();
    }
}
