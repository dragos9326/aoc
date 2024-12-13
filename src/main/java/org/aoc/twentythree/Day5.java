package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {

    private static List<Long> seeds;
    private static Map<Long, Range> seedSoilMap = new HashMap<>();
    private static Map<Long, Range> soilFertilizerMap = new HashMap<>();
    private static Map<Long, Range> fertilizerWaterMap = new HashMap<>();
    private static Map<Long, Range> waterLightMap = new HashMap<>();
    private static Map<Long, Range> lightTemperatureMap = new HashMap<>();
    private static Map<Long, Range> temperatureHumidityMap = new HashMap<>();
    private static Map<Long, Range> humidityLocationMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day5Input.txt");

        List<String> list = Files.readAllLines(path);

        setupMapsValues(list);

        System.out.println("Day 5 part 1 result:");
        System.out.println(getFirstPartAnswer());
        System.out.println("Day 5 part 2 result:");
        System.out.println(getSecondPartAnswer());
    }

    private static long getFirstPartAnswer() {
        long min = Integer.MAX_VALUE;
        for (Long seed : seeds) {
            min = Math.min(min, getSeedLocationValue(seed));
        }

        return min;
    }

    private static long getSeedLocationValue(Long seed) {
        Long soilValue = getCorrespondingValue(seedSoilMap, seed);
        Long fertilizerValue = getCorrespondingValue(soilFertilizerMap, soilValue);
        Long waterValue = getCorrespondingValue(fertilizerWaterMap, fertilizerValue);
        Long lightValue = getCorrespondingValue(waterLightMap, waterValue);
        Long temperatureValue = getCorrespondingValue(lightTemperatureMap, lightValue);
        Long humidityValue = getCorrespondingValue(temperatureHumidityMap, temperatureValue);
        return getCorrespondingValue(humidityLocationMap, humidityValue);
    }

    private static Long getCorrespondingValue(Map<Long, Range> valuesMap, Long seed) {
        for (Long key : valuesMap.keySet()) {
            Range range = valuesMap.get(key);
            if (seed >= key && seed < (key + range.getDistance())) {
                return range.getStart() + (seed - key);
            }
        }
        return seed;
    }

    private static void setupMapsValues(List<String> list) {
        seeds = Arrays.stream(list.get(0).split(":")[1].strip().split("\\s+")).map(Long::parseLong).collect(Collectors.toList());

        for (int i = 1; i < list.size(); i++) {
            String s = list.get(i);

            switch (s) {
                case "seed-to-soil map:" -> {
                    int read = setMapValues(seedSoilMap, list, i);
                    i = i + read;
                }
                case "soil-to-fertilizer map:" -> {
                    int read = setMapValues(soilFertilizerMap, list, i);
                    i = i + read;
                }
                case "fertilizer-to-water map:" -> {
                    int read = setMapValues(fertilizerWaterMap, list, i);
                    i = i + read;
                }
                case "water-to-light map:" -> {
                    int read = setMapValues(waterLightMap, list, i);
                    i = i + read;
                }
                case "light-to-temperature map:" -> {
                    int read = setMapValues(lightTemperatureMap, list, i);
                    i = i + read;
                }
                case "temperature-to-humidity map:" -> {
                    int read = setMapValues(temperatureHumidityMap, list, i);
                    i = i + read;
                }
                case "humidity-to-location map:" -> {
                    int j = i + 1;
                    s = list.get(j);
                    while (!s.isEmpty()) {
                        String[] nums = s.split("\\s+");
                        humidityLocationMap.put(Long.parseLong(nums[1]), new Range(Long.parseLong(nums[0]), Long.parseLong(nums[2])));
                        j++;
                        if (j >= list.size()) {
                            break;
                        }
                        s = list.get(j);
                    }
                    i = j;
                }
            }
        }
    }

    private static int setMapValues(Map<Long, Range> seedSoilMap, List<String> list, int i) {
        int j = i + 1;
        String s = list.get(j);
        while (!s.isEmpty()) {
            String[] nums = s.split("\\s+");
            seedSoilMap.put(Long.parseLong(nums[1]), new Range(Long.parseLong(nums[0]), Long.parseLong(nums[2])));
            j++;
            s = list.get(j);
        }

        return j - i;
    }

    private static long getSecondPartAnswer() {
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            if (correspondsToASeed(i)) {
                return i;
            }
        }
        return Long.MAX_VALUE;
    }

    private static boolean correspondsToASeed(long location) {

        Long humidityValue = getBackwardsCorrespondingValue(humidityLocationMap, location);
        Long temperatureValue = getBackwardsCorrespondingValue(temperatureHumidityMap, humidityValue);
        Long lightValue = getBackwardsCorrespondingValue(lightTemperatureMap, temperatureValue);
        Long waterValue = getBackwardsCorrespondingValue(waterLightMap, lightValue);
        Long fertilizerValue = getBackwardsCorrespondingValue(fertilizerWaterMap, waterValue);
        Long soilValue = getBackwardsCorrespondingValue(soilFertilizerMap, fertilizerValue);
        Long seedValue = getBackwardsCorrespondingValue(seedSoilMap, soilValue);

        return isSeedInInput(seedValue);
    }

    private static boolean isSeedInInput(Long seedValue) {
        for (int i = 0; i < seeds.size(); i = i + 2) {
            if (seedValue >= seeds.get(i) && seedValue <= seeds.get(i) + seeds.get(i + 1)) {
                return true;
            }
        }
        return false;
    }

    private static Long getBackwardsCorrespondingValue(Map<Long, Range> valuesMap, Long seed) {
        for (Long key : valuesMap.keySet()) {
            Range range = valuesMap.get(key);
            if (seed >= range.getStart() && seed < (range.getStart() + range.getDistance())) {
                return key + (seed - range.getStart());
            }
        }
        return seed;
    }

    public static class Range {
        private Long start;
        private Long distance;

        public Range(Long start, Long distance) {
            this.start = start;
            this.distance = distance;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getDistance() {
            return distance;
        }

        public void setDistance(Long distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "Range{" + "start=" + start + ", finish=" + distance + '}';
        }
    }
}