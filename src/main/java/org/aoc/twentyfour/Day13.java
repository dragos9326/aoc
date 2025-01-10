package org.aoc.twentyfour;

import lombok.extern.slf4j.Slf4j;
import org.aoc.common.AxisPoint;
import org.aoc.common.Prize;
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
public class Day13 {

    private static final Logger logger = LoggerFactory.getLogger(Day13.class);

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentyfour/day13Input.txt");

        List<String> list = Files.readAllLines(path);
        List<Prize> prizes = getPrizes(list);

        logger.debug("Day 13 part 1 result: {}", getFirstPartAnswer(prizes));
        logger.debug("Day 13 part 2 result: {}", getSecondPartAnswer(prizes));
    }

    private static long getFirstPartAnswer(List<Prize> prizes) {
        long sum = 0;
        for (Prize p : prizes) {
            long minTokens = getPrizeMinTokens(p, 0, false);
            if (minTokens != Integer.MAX_VALUE) {
                sum += minTokens;
            }
        }
        return sum;
    }

    private static long getSecondPartAnswer(List<Prize> prizes) {
        long sum = 0;
        for (Prize p : prizes) {
            long minTokens = getPrizeMinTokens(p, 10000000000000L, true);
            if (minTokens != Integer.MAX_VALUE) {
                sum += minTokens;
            }
        }
        return sum;
    }

    private static List<Prize> getPrizes(List<String> list) {
        List<Prize> prizes = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 4) {
            String buttA = list.get(i).split(":")[1].trim();
            int axDelta = Integer.parseInt(buttA.substring(buttA.indexOf('+') + 1, buttA.indexOf(',')));
            int ayDelta = Integer.parseInt(buttA.substring(buttA.lastIndexOf('+') + 1));
            String buttB = list.get(i + 1).split(":")[1].trim();
            int bxDelta = Integer.parseInt(buttB.substring(buttB.indexOf('+') + 1, buttB.indexOf(',')));
            int byDelta = Integer.parseInt(buttB.substring(buttB.lastIndexOf('+') + 1));
            String prizeStr = list.get(i + 2).split(":")[1].trim();
            int prizeX = Integer.parseInt(prizeStr.substring(prizeStr.indexOf('=') + 1, prizeStr.indexOf(',')));
            int prizeY = Integer.parseInt(prizeStr.substring(prizeStr.lastIndexOf('=') + 1));

            AxisPoint prizeLocation = new AxisPoint(prizeX, prizeY);
            Prize prize = new Prize(axDelta, ayDelta, bxDelta, byDelta, prizeLocation);
            prizes.add(prize);
        }
        return prizes;
    }

    private static long getPrizeMinTokens(Prize p, long shift, boolean part2) {
        /* think as a pair of 2 equations:
            Ax*a + Bx*b = LocationX
            Ay*a + By*b = LocationY

            if we solve
            a = (LocationY - b*By) / Ay
            b = (Ax*LocationY - LocationX*Ay) / (Ax*By - Bx*Ay)
         */

        p.getLocation().setX(p.getLocation().getX() + shift);
        p.getLocation().setY(p.getLocation().getY() + shift);

        double b = (double) (p.getAxDelta() * p.getLocation().getY() - p.getLocation().getX() * p.getAyDelta())
                / (double) (p.getAxDelta() * p.getByDelta() - p.getBxDelta() * p.getAyDelta());
        double a = (p.getLocation().getY() - b * p.getByDelta()) / p.getAyDelta();

        if (a < 0 || b < 0) {
            return 0;
        }

        if (!part2 && (Double.compare(a, 100d) > 0 || Double.compare(b, 100d) > 0)) {
            return 0;
        }

        return (isInteger(a) && isInteger(b)) ? (3 * (long)a + (long)b) : 0;
    }

    private static boolean isInteger(double b) {
        return b % 1 == 0;
    }

}