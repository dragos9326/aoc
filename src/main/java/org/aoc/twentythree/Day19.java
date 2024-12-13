package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    protected static Map<String, Flow> flows = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day19Input.txt");

        List<String> list = Files.readAllLines(path);
        List<Part> parts = initFlowsAndGetParts(list);


        System.out.println("Day 19 part 1 result:");
        System.out.println(getFirstPartAnswer(parts));
        System.out.println("Day 19 part 2 result:");
        System.out.println(getSecondPartAnswer());
    }

    private static List<Part> initFlowsAndGetParts(List<String> list) {
        boolean readingFlows = true;
        List<Part> parts = new ArrayList<>();
        for (String l : list) {
            if (l.isBlank()) {
                readingFlows = false;
                continue;
            }
            if (readingFlows) {
                readFlow(l);
            } else {
                parts.add(readPart(l));
            }
        }

        return parts;
    }

    private static Part readPart(String l) {
        l = l.substring(1, l.length() - 1);
        String[] parts = l.split(",");
        int x = Integer.parseInt(parts[0].substring(2));
        int m = Integer.parseInt(parts[1].substring(2));
        int a = Integer.parseInt(parts[2].substring(2));
        int s = Integer.parseInt(parts[3].substring(2));
        return new Part(x, m, a, s);
    }

    private static void readFlow(String l) {
        l = l.substring(0, l.length() - 1);
        String[] parts = l.split("\\{");
        String flowCode = parts[0];
        List<Rule> rules = getRules(parts[1]);

        flows.put(flowCode, new Flow(flowCode, rules));
    }

    private static List<Rule> getRules(String part) {
        List<Rule> rules = new ArrayList<>();
        String[] ruleArr = part.split(",");
        for (String s : ruleArr) {
            String[] ruleParts = s.split(":");
            String condition = ruleParts.length > 1 ? ruleParts[0] : null;
            String nextFlow = ruleParts.length > 1 ? ruleParts[1] : ruleParts[0];
            rules.add(new Rule(condition, nextFlow));
        }

        return rules;
    }


    private static long getSecondPartAnswer() {
        List<Range> ranges = new ArrayList<>();
        Range first = new Range();
        String firstFlow = "in";
        checkFlow(first, firstFlow, ranges);
        return ranges.stream().mapToLong(Range::getCombinations).sum();
    }

    private static void checkFlow(Range range, String nextFlowCode, List<Range> ranges) {
        if (range == null) {
            return;
        }

        if ("A".equals(nextFlowCode)) {
            ranges.add(range);
            return;
        }

        if ("R".equals(nextFlowCode)) {
            return;
        }

        Flow flow = flows.get(nextFlowCode);
        for (Rule r : flow.rules) {
            Range[] rangeSplit = range.getNextRanges(r);
            checkFlow(rangeSplit[0], r.nextFlow, ranges);
            if (rangeSplit[1] != null) {
                range = rangeSplit[1];
            }
        }
    }

    private static boolean isAccepted(Part p) {
        String nextFlowCode = "in";
        while (!"R".equals(nextFlowCode) && !"A".equals(nextFlowCode)) {
            nextFlowCode = getNextFlow(p, nextFlowCode);
        }
        return "A".equals(nextFlowCode);
    }

    private static String getNextFlow(Part p, String nextFlowCode) {
        Flow nextFlow = flows.get(nextFlowCode);
        for (int i = 0; i < nextFlow.rules.size() - 1; i++) {
            Rule r = nextFlow.rules.get(i);
            if (r.matches(p)) {
                return r.nextFlow;
            }
        }

        return nextFlow.rules.get(nextFlow.rules.size() - 1).getNextFlow();
    }


    private static long getFirstPartAnswer(List<Part> parts) {
        List<Part> accepted = parts.stream().filter(Day19::isAccepted).toList();
        return accepted.stream().map(p -> p.x + p.m + p.a + p.s).reduce(0, Integer::sum);
    }


    public static class Flow {
        private String code;
        private List<Rule> rules;

        public Flow(String code, List<Rule> rules) {
            this.code = code;
            this.rules = rules;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }

        @Override
        public String toString() {
            return "Flow{" +
                    "code='" + code + '\'' +
                    ", rules=" + rules +
                    '}';
        }
    }

    public static class Rule {
        private String condition;
        private String nextFlow;

        public Rule(String condition, String nextFlow) {
            this.condition = condition;
            this.nextFlow = nextFlow;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getNextFlow() {
            return nextFlow;
        }

        public void setNextFlow(String nextFlow) {
            this.nextFlow = nextFlow;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "condition='" + condition + '\'' +
                    ", nextFlow='" + nextFlow + '\'' +
                    '}';
        }

        public boolean matches(Part p) {
            int num = getNumOfPart(p);
            int condNum = Integer.parseInt(condition.substring(2));
            char cond = condition.charAt(1);
            if (cond == '<') {
                return num < condNum;
            } else if (cond == '=') {
                return num == condNum;
            } else {
                return num > condNum;
            }
        }

        private int getNumOfPart(Part p) {
            switch (condition.charAt(0)) {
                case 'x':
                    return p.x;
                case 'm':
                    return p.m;
                case 'a':
                    return p.a;
                default:
                    return p.s;
            }
        }
    }

    public static class Part {
        private int x;
        private int m;
        private int a;
        private int s;

        public Part(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getM() {
            return m;
        }

        public void setM(int m) {
            this.m = m;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return "Part{" +
                    "x=" + x +
                    ", m=" + m +
                    ", a=" + a +
                    ", s=" + s +
                    '}';
        }
    }

    public static class Range {
        private long maxX;
        private long minX;
        private long maxM;
        private long minM;
        private long maxA;
        private long minA;
        private long maxS;
        private long minS;

        public Range() {
            this.maxX = 4000;
            this.maxM = 4000;
            this.maxA = 4000;
            this.maxS = 4000;
            this.minX = 1;
            this.minM = 1;
            this.minA = 1;
            this.minS = 1;
        }

        public Range(Range r) {
            this.maxX = r.maxX;
            this.maxM = r.maxM;
            this.maxA = r.maxA;
            this.maxS = r.maxS;
            this.minX = r.minX;
            this.minM = r.minM;
            this.minA = r.minA;
            this.minS = r.minS;
        }

        public long getCombinations() {
            return (maxX - minX + 1) * (maxM - minM + 1) * (maxA - minA + 1) * (maxS - minS + 1);
        }

        @Override
        public String toString() {
            return "Range{" +
                    "maxX=" + maxX +
                    ", minX=" + minX +
                    ", maxM=" + maxM +
                    ", minM=" + minM +
                    ", maxA=" + maxA +
                    ", minA=" + minA +
                    ", maxS=" + maxS +
                    ", minS=" + minS +
                    '}';
        }

        /**
         * split by the condition
         * returns valid interval first, and then the invalid one to proceed to next rules
         *
         * @param r
         * @return
         */
        public Range[] getNextRanges(Rule r) {
            Range[] newRanges = new Range[2];
            Range validRange = new Range(this);
            Range invalidRange = new Range(this);
            if (r.condition == null || r.condition.isBlank()) {
                newRanges[0] = validRange;
                return newRanges;
            }

            char prop = r.condition.charAt(0);
            long condNum = Long.parseLong(r.condition.substring(2));
            char cond = r.condition.charAt(1);
            if (cond == '<') {
                switch (prop) {
                    case 'x':
                        if (condNum < minX) {
                            return newRanges;
                        }
                        validRange.maxX = Math.min(maxX, condNum - 1);
                        invalidRange.minX = Math.min(4000, validRange.maxX + 1);
                        break;
                    case 'm':
                        if (condNum < minM) {
                            return newRanges;
                        }
                        validRange.maxM = Math.min(maxM, condNum - 1);
                        invalidRange.minM = Math.min(4000, validRange.maxM + 1);
                        break;
                    case 'a':
                        if (condNum < minA) {
                            return newRanges;
                        }
                        validRange.maxA = Math.min(maxA, condNum - 1);
                        invalidRange.minA = Math.min(4000, validRange.maxA + 1);
                        break;
                    default:
                        if (condNum < minS) {
                            return newRanges;
                        }
                        validRange.maxS = Math.min(maxS, condNum - 1);
                        invalidRange.minS = Math.min(4000, validRange.maxS + 1);
                        break;
                }
            } else if (cond == '=') {
                switch (prop) {
                    case 'x':
                        if (minX != maxX && minX != condNum) {
                            return newRanges;
                        }
                        break;
                    case 'm':
                        if (minM != maxM && minM != condNum) {
                            return newRanges;
                        }
                        break;
                    case 'a':
                        if (minA != maxA && minA != condNum) {
                            return newRanges;
                        }
                        break;
                    default:
                        if (minS != maxS && minS != condNum) {
                            return newRanges;
                        }
                        break;
                }
            } else {
                switch (prop) {
                    case 'x':
                        if (maxX < condNum) {
                            return newRanges;
                        }
                        validRange.minX = Math.max(minX, condNum + 1);
                        invalidRange.maxX = Math.max(1, validRange.minX - 1);
                        break;
                    case 'm':
                        if (maxM < condNum) {
                            return newRanges;
                        }
                        validRange.minM = Math.max(minM, condNum + 1);
                        invalidRange.maxM = Math.max(1, validRange.minM - 1);
                        break;
                    case 'a':
                        if (maxA < condNum) {
                            return newRanges;
                        }
                        validRange.minA = Math.max(minA, condNum + 1);
                        invalidRange.maxA = Math.max(1, validRange.minA - 1);
                        break;
                    default:
                        if (maxS < condNum) {
                            return newRanges;
                        }
                        validRange.minS = Math.max(minS, condNum + 1);
                        invalidRange.maxS = Math.max(1, validRange.minS - 1);
                        break;
                }
            }

            newRanges[0] = validRange;
            newRanges[1] = invalidRange;
            return newRanges;
        }
    }
}