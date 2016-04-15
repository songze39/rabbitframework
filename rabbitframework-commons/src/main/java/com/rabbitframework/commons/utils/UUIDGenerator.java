package com.rabbitframework.commons.utils;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

public class UUIDGenerator {
    public static String DEFAULT_UUID36 = "00000000-0000-0000-0000-000000000000";
    public static String DEFAULT_UUID32 = "00000000000000000000000000000000";
    private static NoArgGenerator timeGenerator;
    private static NoArgGenerator randomGenerator;

    static {
        ensureGeneratorInitialized();
    }

    private static void ensureGeneratorInitialized() {
        if (timeGenerator == null) {
            synchronized (UUIDGenerator.class) {
                if (timeGenerator == null) {
                    timeGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
        if (randomGenerator == null) {
            synchronized (com.fasterxml.uuid.UUIDGenerator.class) {
                if (randomGenerator == null) {
                    randomGenerator = Generators.randomBasedGenerator();
                }
            }
        }
    }

    public static String getRandomUUID36() {
        return randomGenerator.generate().toString();
    }

    public static String getRandomUUID32() {
        return getUUID32(getRandomUUID36());
    }


    public static String getTimeUUID36() {
        return timeGenerator.generate().toString();
    }

    public static String getTimeUUID32() {
        return getUUID32(timeGenerator.generate().toString());
    }


    private static String getUUID32(String generator) {
        return generator.replace("-", "");
    }
}
