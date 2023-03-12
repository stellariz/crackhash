package ru.nsu.ccfit.msmanager.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashCrackStatusResponseDto {
    private HashCrackStatus hashCrackStatus;
    private List<String> data;
}
