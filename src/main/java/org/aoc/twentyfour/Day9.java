package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dragos
 */
@Slf4j
public class Day9 {

    private static final Logger logger = LoggerFactory.getLogger(Day9.class);

    private static final int EMPTY_BLOCK = -1;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day9Input.txt");

        String input = Files.readString(path);

        logger.debug("Day 9 part 1 result: {}", getFirstPartAnswer(input));
        logger.debug("Day 9 part 2 result: {}", getSecondPartAnswer(input));
    }

    private static long getFirstPartAnswer(String seq) {
        List<Integer> expanded = getExpanded(seq);
        rearrange(expanded);

        long sum = 0L;
        for (int i = 0; i < expanded.size(); i++) {
            int no = expanded.get(i);
            if (no != EMPTY_BLOCK) {
                sum += (long) no * i;
            }
        }

        return sum;
    }

    private static long getSecondPartAnswer(String seq) {
        List<Integer> expanded = getExpanded(seq);
        rearrangeWholeFiles(expanded);

        long sum = 0L;
        for (int i = 0; i < expanded.size(); i++) {
            int no = expanded.get(i);
            if (no != EMPTY_BLOCK) {
                sum += (long) no * i;
            }
        }

        return sum;
    }

    private static List<Integer> getExpanded(String seq) {
        List<Integer> list = new ArrayList<>();
        boolean file = true;
        for (int i = 0; i < seq.length(); i++) {
            int no = seq.charAt(i) - '0';
            for (int j = 0; j < no; j++) {
                list.add(file ? i / 2 : EMPTY_BLOCK);
            }
            file = !file;
        }

        return list;
    }

    private static void rearrange(List<Integer> expanded) {
        for (int i = expanded.size() - 1; i > -1; i--) {
            int no = expanded.get(i);
            boolean foundSpace = false;
            if (no != EMPTY_BLOCK) {
                for (int j = 0; j < i; j++) {
                    int temp = expanded.get(j);
                    if (temp == EMPTY_BLOCK) {
                        expanded.set(j, no);
                        expanded.set(i, EMPTY_BLOCK);
                        foundSpace = true;
                        break;
                    }
                }

                if (!foundSpace) {
                    return;
                }
            }
        }
    }

    private static void rearrangeWholeFiles(List<Integer> expanded) {
        for (int i = expanded.size() - 1; i > -1; i--) {
            int no = expanded.get(i);
            if (no != EMPTY_BLOCK) {
                int fileSize = getFileSize(expanded, i);
                int posToMove = getPositionForFile(expanded, fileSize, i);
                if (posToMove >= 0) {
                    for (int j = 0; j < fileSize; j++) {
                        expanded.set(posToMove + j, no);
                        expanded.set(i - j, EMPTY_BLOCK);
                    }
                }
                if (fileSize > 0) {
                    i = i - fileSize + 1;
                }
            }
        }
    }

    private static int getFileSize(List<Integer> expanded, int i) {
        int no = expanded.get(i);
        int size = 1;
        i--;
        while (i > -1 && expanded.get(i) == no) {
            size++;
            i--;
        }
        return size;
    }

    private static int getPositionForFile(List<Integer> expanded, int fileSize, int finalPos) {
        for (int i = 0; i < finalPos; i++) {
            int currNo = expanded.get(i);
            if (currNo == EMPTY_BLOCK) {
                int currPos = i + 1;
                while (currPos < finalPos && expanded.get(currPos) == EMPTY_BLOCK) {
                    currPos++;
                }
                if(currPos - i >= fileSize) {
                    return i;
                }
            }
        }

        return -1;
    }

}