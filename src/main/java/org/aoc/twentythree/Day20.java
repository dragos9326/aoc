package org.aoc.twentythree;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day20 {
    protected static Map<String, Module> modules = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/twentythree/day20Input.txt");

        List<String> list = Files.readAllLines(path);

        System.out.println("Day 20 part 1 result:");
        System.out.println(getFirstPartAnswer(list));
        System.out.println("Day 20 part 2 result:");
        System.out.println(getSecondPartAnswer(list));
    }

    private static long getSecondPartAnswer(List<String> list) {
        initModules(list);

        //get module that goes into rx
        //after adventofcode subreddit it is a conjucture
        Module conjToRx = modules.values().stream().filter(m -> m.toBroadcastNames.size() == 1 && m.toBroadcastNames.get(0).equals("rx")).findFirst().orElseThrow();
        if (conjToRx.type != Types.CONJUNCTION) {
            throw new RuntimeException("not supported");
        }

        //find the cycles for each of the nodes going into conjuction
        Map<String, Long> cycles = getCycleForNode(conjToRx.name);


        BigInteger a = null;
        for (Long n : cycles.values()) {
            BigInteger b = BigInteger.valueOf(n);
            if (a != null) {
                b = a.multiply(b.divide(b.gcd(a)));
            }
            a = b;
        }

        System.out.println(cycles);

        return a.longValue();

    }

    private static Map<String, Long> getCycleForNode(String to) {
        long counter = 0;
        Map<String, Long> cycles = new HashMap<>();
        Map<String, Long> seen = new HashMap<>();
        while (true) {
            counter++;
            Queue<Pulse> q = new LinkedList<>();
            q.add(new Pulse(Types.BUTTON.symbol, '-', Types.BROADCASTER.symbol));
            while (!q.isEmpty()) {
                Pulse p = q.remove();
                if (p.receiver.equals(to) && p.pulse == '+') {
                    if (!cycles.containsKey(p.sender)) {
                        cycles.put(p.sender, counter);
                    }

                    if (seen.containsKey(p.sender)) {
                        seen.put(p.sender, seen.get(p.sender) + 1);
                    } else {
                        seen.put(p.sender, 1L);
                    }

                    if (seen.values().stream().filter(v -> v > 1).toList().size() == 4) {
                        return cycles;
                    }
                }
                p.sendNextSignals(q);
            }
        }
    }

    private static long getFirstPartAnswer(List<String> list) {
        initModules(list);
        Counter counter = new Counter();
        for (int i = 0; i < 1000; i++) {
            Queue<Pulse> q = new LinkedList<>();
            q.add(new Pulse(Types.BUTTON.symbol, '-', Types.BROADCASTER.symbol));
            while (!q.isEmpty()) {
                Pulse p = q.remove();
                if (p.pulse == '-') {
                    counter.low++;
                } else {
                    counter.high++;
                }
                p.sendNextSignals(q);
            }
        }

        return counter.high * counter.low;
    }

    private static void initModules(List<String> list) {
        modules = new HashMap<>();
        for (String l : list) {
            String[] parts = l.split("->");
            Types type = null;
            String name = null;
            List<Character> pulses = new ArrayList<>();
            pulses.add('-');
            if (parts[0].charAt(0) == Types.FLIP_FLOP.symbol.charAt(0)) {
                type = Types.FLIP_FLOP;
                name = parts[0].strip().substring(1);
            } else if (parts[0].charAt(0) == Types.CONJUNCTION.symbol.charAt(0)) {
                type = Types.CONJUNCTION;
                name = parts[0].strip().substring(1);
            } else if (parts[0].startsWith(Types.BROADCASTER.symbol)) {
                type = Types.BROADCASTER;
                name = parts[0].strip();
            }

            if (type == null) {
                continue;
            }

            List<String> toBroadcastNames = Arrays.stream(parts[1].strip().split(",")).map(String::strip).toList();

            modules.put(name, new Module(type, name, pulses, toBroadcastNames, null));
        }

        for (Module m : modules.values()) {
            for (String n : m.toBroadcastNames) {
                Module module = modules.get(n);
                if (module != null) {
                    module.addConnectedInput(m.name);
                }
            }
        }
    }

    public static class Counter {
        public long high;
        public long low;

        @Override
        public String toString() {
            return "Counter{" +
                    "high=" + high +
                    ", low=" + low +
                    '}';
        }
    }

    public static class Pulse {
        private String sender;
        private char pulse;
        private String receiver;

        public Pulse(String sender, char pulse, String receiver) {
            this.sender = sender;
            this.pulse = pulse;
            this.receiver = receiver;
        }

        public void sendNextSignals(Queue<Pulse> q) {
            //send from the receiver to each of their children
            Module senderMod = modules.get(receiver);
            if (senderMod == null) {
                return;
            }
            senderMod.pulses.add(pulse);

            char pulseToSend = pulse;

            if (senderMod.type == Types.FLIP_FLOP) {
                if (pulse == '-') {
                    if (!senderMod.onOff) {
                        pulseToSend = '+';
                    }
                    senderMod.onOff = !senderMod.onOff;
                } else {
                    return;
                }
            } else if (senderMod.type == Types.CONJUNCTION) {
                for (int i = 0; i < senderMod.connectedInputs.size(); i++) {
                    if (senderMod.connectedInputs.get(i).equals(sender)) {
                        senderMod.connectedInputsPulses.set(i, pulse);
                        break;
                    }
                }
                boolean isAnyLow = senderMod.connectedInputsPulses.stream().anyMatch(cip -> cip == '-');
                pulseToSend = isAnyLow ? '+' : '-';
            } else if (senderMod.type == Types.BROADCASTER) {
                pulseToSend = '-';
            }


            if (senderMod.toBroadcastNames != null) {
                for (String m : senderMod.toBroadcastNames) {
                    q.add(new Pulse(senderMod.name, pulseToSend, m));
                }
            }
        }

        @Override
        public String toString() {
            return "Pulse{" +
                    "sender='" + sender + '\'' +
                    ", pulse=" + pulse +
                    ", receiver='" + receiver + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pulse pulse1 = (Pulse) o;
            return pulse == pulse1.pulse && Objects.equals(sender, pulse1.sender) && Objects.equals(receiver, pulse1.receiver);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sender, pulse, receiver);
        }
    }

    public enum Types {
        FLIP_FLOP("%"),
        CONJUNCTION("&"),
        BUTTON("button"),
        BROADCASTER("broadcaster");

        private String symbol;

        Types(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public static class Module {
        private Types type;
        private String name;
        private List<Character> pulses;
        //used for flipflop
        private boolean onOff;
        private List<String> toBroadcastNames;

        //used for conjunction
        private List<String> connectedInputs;
        private List<Character> connectedInputsPulses;

        public Module(Types type, String name, List<Character> pulses, List<String> toBroadcastNames, List<String> connectedInputs) {
            this.type = type;
            this.name = name;
            this.pulses = pulses;
            this.toBroadcastNames = toBroadcastNames;
            this.connectedInputs = connectedInputs;
            this.connectedInputsPulses = new ArrayList<>();
            if (connectedInputs != null) {
                for (int i = 0; i < connectedInputs.size(); i++) {
                    this.connectedInputsPulses.add('-');
                }
            }
        }

        public void addConnectedInput(String name) {
            if (connectedInputs == null) {
                connectedInputs = new ArrayList<>();
            }
            if (connectedInputsPulses == null) {
                connectedInputsPulses = new ArrayList<>();
            }

            connectedInputs.add(name);
            connectedInputsPulses.add('-');
        }

        @Override
        public String toString() {
            return "Module{" +
                    "type=" + type +
                    ", pulse=" + (!pulses.isEmpty() ? pulses.get(pulses.size() - 1) : "null") +
                    ", name='" + name + '\'' +
                    ", onOff=" + onOff +
                    ", toBroadcastNames=" + toBroadcastNames +
                    '}';
        }
    }

}