package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {
    private CardUtils() {
    }

    public static StringBuilder getStringBuilder(Random random) {
        StringBuilder cardNumberBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int digit = random.nextInt(10);
                cardNumberBuilder.append(digit);
            }
            if (i < 3) {
                cardNumberBuilder.append("-");
            }
        }
        return cardNumberBuilder;
    }
    public static int getCvv(Random random) {
        int cvv = random.nextInt(900) + 100;
        return cvv;
    }
    public static String getRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return "VIN-" + randomNumber;
    }
}
