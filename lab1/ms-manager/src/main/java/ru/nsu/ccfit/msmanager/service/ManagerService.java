package ru.nsu.ccfit.msmanager.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.ccfit.msmanager.model.request.HashCrackRequestDto;
import ru.nsu.ccfit.msmanager.model.response.HashCrackResponseDto;
import ru.nsu.ccfit.schema.CrackHashManagerRequest;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    @Value("${workers.number:1}")
    private int workersNumber;
    @Value("${workers.service.url}")
    private String workersUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final StatusService statusService;
    private final List<String> alphabet = List.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o",
            "p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9");

    public HashCrackResponseDto startCrack(HashCrackRequestDto request) {
        String requestId = UUID.randomUUID().toString();
        String hash = request.getHash();
        int maxLength = request.getMaxLength();

        statusService.initRequestStatus(requestId);
        IntStream.range(0, workersNumber)
                .forEach(workerNumber ->
                    // it doesn't work in docker but without it all's ok....
                    CompletableFuture.runAsync(() -> delegateJob(requestId, workerNumber, workersNumber, hash, maxLength)
                ));
        log.info("Created request with id {}", requestId);
        return new HashCrackResponseDto(requestId);
    }

    private void delegateJob(String requestId, int partNumber, int partCount, String hash, int maxLength) {
        CrackHashManagerRequest request = new CrackHashManagerRequest();
        request.setRequestId(requestId);
        request.setHash(hash);
        request.setMaxLength(maxLength);
        request.setPartNumber(partNumber);
        request.setPartCount(partCount);

        var requestAlphabet = new CrackHashManagerRequest.Alphabet();
        requestAlphabet.getSymbols().addAll(alphabet);
        request.setAlphabet(requestAlphabet);
        restTemplate.postForObject(
                workersUrl + "/internal/api/worker/hash/crack/task", request, CrackHashWorkerResponse.class);
    }
}
