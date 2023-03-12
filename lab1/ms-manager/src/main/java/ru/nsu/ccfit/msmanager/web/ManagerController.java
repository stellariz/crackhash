package ru.nsu.ccfit.msmanager.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.msmanager.model.request.HashCrackRequestDto;
import ru.nsu.ccfit.msmanager.model.response.HashCrackResponseDto;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatusResponseDto;
import ru.nsu.ccfit.msmanager.service.ManagerService;
import ru.nsu.ccfit.msmanager.service.StatusService;

@RestController
@RequestMapping("/api/hash")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final StatusService statusService;

    @PostMapping("/crack")
    public HashCrackResponseDto crack(@RequestBody HashCrackRequestDto request) {
        return managerService.startCrack(request);
    }

    @GetMapping("/status")
    public HashCrackStatusResponseDto getStatus(@RequestParam String requestId) {
        return statusService.getRequestStatus(requestId);
    }
}
