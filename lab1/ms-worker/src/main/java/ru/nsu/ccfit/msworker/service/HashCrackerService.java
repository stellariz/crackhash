package ru.nsu.ccfit.msworker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HashCrackerService {
    public List<String> crackHash(List<String> alphabet, int partNumber, int partCount, int maxLength, String hash) {
        List<String> answers = new ArrayList<>();
        List<String> firstLetters = getComputePart(alphabet, partNumber, partCount);
        for (String letter : firstLetters) {
            IntStream
                    .range(0, maxLength)
                    .mapToObj(i -> Generator.permutation(alphabet).withRepetitions(i).stream())
                    .flatMap(Function.identity())
                    // add first letter
                    .map(word -> String.format("%s%s", letter, String.join("", word)))
                    .filter(word -> hash.equals(DigestUtils.md5Hex(word)))
                    .forEach(answers::add);
        }
        return answers;
    }

    private List<String> getComputePart(List<String> alphabet, int partNumber, int partCount) {
        int partSize = alphabet.size() / partCount;
        int offset = partSize * partNumber;
        return partNumber == partCount - 1 ? alphabet.subList(offset, alphabet.size()) :
                alphabet.subList(offset, offset + partSize);
    }
}
