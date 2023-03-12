package ru.nsu.ccfit.msworker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Service
@RequiredArgsConstructor
public class UpdateRequestStatusService {
    @Value("${manager.service.url}")
    private String managerUrl;
    private final RestTemplate restTemplate;

    public ResponseEntity<?> updateStatus(CrackHashWorkerResponse patchResponse) {
        return restTemplate.patchForObject(managerUrl + "/internal/api/manager/hash/crack/request",
                patchResponse, ResponseEntity.class);
    }
}
