package com.guerrero.backend.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating random IDs using the UUID class.
 */
public class RandomIdGenerator {

    /**
     * Generates a random identifier with the provided length (max 32).
     * @param idLength The length of the identifier.
     * @return The unique identifier.
     */
    public static String generateRandomId(int idLength) {
        if (idLength > 32) {
            throw new IllegalArgumentException("The maximum id length is 32.");
        }
        // uuid of 32 characters
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // The randomStartIndex in the range [0, 32 - idLength]
        int randomStartIndex = ThreadLocalRandom.current().nextInt(0, uuid.length() - idLength + 1);
        return uuid.substring(randomStartIndex, randomStartIndex + idLength);
    }
}
