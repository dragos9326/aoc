package org.aoc.twentythree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    protected static Map<Character, Integer> cardsPower;

    static {
        cardsPower = new HashMap<>();
        cardsPower.put('2', 2);
        cardsPower.put('3', 3);
        cardsPower.put('4', 4);
        cardsPower.put('5', 5);
        cardsPower.put('6', 6);
        cardsPower.put('7', 7);
        cardsPower.put('8', 8);
        cardsPower.put('9', 9);
        cardsPower.put('T', 10);
        cardsPower.put('J', 12);
        cardsPower.put('Q', 13);
        cardsPower.put('K', 14);
        cardsPower.put('A', 15);
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day7Input.txt");

        List<String> list = Files.readAllLines(path);

        System.out.println("Day 7 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 7 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static long getFirstPartAnswer(List<String> list) {
        cardsPower.put('J', 2);
        List<Hand> sortedHands = list.stream().map(s -> {
                    String[] parts = s.split(" ");
                    return new Hand(parts[0], Integer.parseInt(parts[1]));
                })
                .sorted()
                .toList();

        int sum = 0;

        for (int i = 0; i < sortedHands.size(); i++) {
            Hand currentHand = sortedHands.get(i);
            sum += currentHand.getBid() * (i + 1);
        }

        return sum;
    }

    private static long getSecondPartAnswer(List<String> list) {
        cardsPower.put('J', 1);
        List<Hand> sortedHands = list.stream().map(s -> {
                    String[] parts = s.split(" ");
                    return new Hand(parts[0], Integer.parseInt(parts[1]));
                })
                .sorted(Hand::compareWithJoker)
                .toList();

        System.out.println(sortedHands);

        int sum = 0;

        for (int i = 0; i < sortedHands.size(); i++) {
            Hand currentHand = sortedHands.get(i);
            sum += currentHand.getBid() * (i + 1);
        }

        return sum;
    }

    public static class Hand implements Comparable<Hand> {
        private String cards;
        private int bid;

        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
        }

        public String getCards() {
            return cards;
        }

        public void setCards(String cards) {
            this.cards = cards;
        }

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "cards='" + cards + '\'' +
                    ", bid=" + bid +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hand hand = (Hand) o;
            return bid == hand.bid && Objects.equals(cards, hand.cards);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cards, bid);
        }

        @Override
        public int compareTo(Hand o) {
            Map<Character, Long> ourCards = getCards().chars()
                    .mapToObj(e -> (char) e).collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.counting()));
            Map<Character, Long> theirCards = (o).getCards().chars()
                    .mapToObj(e -> (char) e).collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.counting()));

            if (ourCards.size() < theirCards.size()) {
                return 1;
            } else if (theirCards.size() < ourCards.size()) {
                return -1;
            } else {
                return compareEqualCards(ourCards, theirCards, o);
            }
        }

        public int compareWithJoker(Hand o) {
            Map<Character, Long> ourCards = getCards().chars()
                    .mapToObj(e -> (char) e).collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.counting()));
            readjustForJokers(ourCards);
            Map<Character, Long> theirCards = (o).getCards().chars()
                    .mapToObj(e -> (char) e).collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.counting()));
            readjustForJokers(theirCards);

            if (ourCards.size() < theirCards.size()) {
                return 1;
            } else if (theirCards.size() < ourCards.size()) {
                return -1;
            } else {
                return compareEqualCards(ourCards, theirCards, o);
            }
        }

        private void readjustForJokers(Map<Character, Long> ourCards) {
            long jokers = 0;
            Map.Entry<Character, Long> maxEntry = null;
            for (Map.Entry<Character, Long> entry : ourCards.entrySet()) {
                if (entry.getKey() == 'J') {
                    jokers = entry.getValue();
                } else if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }

            if (jokers > 0 && maxEntry != null) {
                ourCards.remove('J');
                ourCards.put(maxEntry.getKey(), maxEntry.getValue() + jokers);
            }
        }

        private int compareEqualCards(Map<Character, Long> ourCards, Map<Character, Long> theirCards, Hand other) {
            int ourSize = ourCards.size();
            switch (ourSize) {
                case 2: {
                    //check size 2 => four of a kind or full house
                    boolean isFirstFourOfAKind = isFourOfAKind(ourCards);
                    boolean isSecondFourOfAKind = isFourOfAKind(theirCards);
                    if (isFirstFourOfAKind && isSecondFourOfAKind) {
                        return compareCardsStrings(getCards(), other.getCards());
                    } else if (isFirstFourOfAKind) {
                        return 1;
                    } else if (isSecondFourOfAKind) {
                        return -1;
                    } else {
                        return compareCardsStrings(getCards(), other.getCards());
                    }
                }
                case 3: {
                    //check size 3 => three of a kind or two pair
                    boolean isFirstThreeOfAKind = isThreeOfAKind(ourCards);
                    boolean isSecondThreeOfAKind = isThreeOfAKind(theirCards);
                    if (isFirstThreeOfAKind && isSecondThreeOfAKind) {
                        return compareCardsStrings(getCards(), other.getCards());
                    } else if (isFirstThreeOfAKind) {
                        return 1;
                    } else if (isSecondThreeOfAKind) {
                        return -1;
                    } else {
                        return compareCardsStrings(getCards(), other.getCards());
                    }
                }
                default: {
                    //check size 1 => five of a kind
                    //check size 4 => two pairs
                    //check size 5 => high card
                    //since no multiple options to compare hand weight, check the strings;
                    return compareCardsStrings(getCards(), other.getCards());
                }
            }
        }

        private boolean isThreeOfAKind(Map<Character, Long> ourCards) {
            return isNOfAKind(ourCards, 3);
        }

        private int compareCardsStrings(String ours, String theirs) {
            for (int i = 0; i < ours.length(); i++) {
                int compareResult = compareCards(ours.charAt(i), theirs.charAt(i));
                if (compareResult != 0) {
                    return compareResult;
                }
            }

            return 0;
        }

        private int compareCards(Character c, Character c1) {
            return cardsPower.get(c) - cardsPower.get(c1);
        }

        private boolean isFourOfAKind(Map<Character, Long> ourCards) {
            return isNOfAKind(ourCards, 4);
        }

        private boolean isNOfAKind(Map<Character, Long> ourCards, int n) {
            for (Map.Entry<Character, Long> entry : ourCards.entrySet()) {
                if (entry.getValue() == n) {
                    return true;
                }
            }

            return false;
        }
    }


}