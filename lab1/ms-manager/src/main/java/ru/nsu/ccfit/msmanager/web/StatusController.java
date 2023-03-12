package ru.nsu.ccfit.msmanager.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.msmanager.service.StatusService;
import ru.nsu.ccfit.schema.CrackHashWorkerResponse;

@Slf4j
@RestController
@RequestMapping("/internal/api/manager")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @PatchMapping("/hash/crack/request")
    public ResponseEntity<?> updateStatus(@RequestBody CrackHashWorkerResponse response) {
        statusService.completeJob(response.getRequestId(), response.getAnswers().getWords());
        return ResponseEntity.ok().build();
    }
}
