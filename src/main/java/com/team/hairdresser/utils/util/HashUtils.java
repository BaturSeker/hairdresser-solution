package com.team.hairdresser.utils.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private static String hexEncode(byte[] input) {
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : input) {
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {
        byte[] requestId = input.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(requestId);
        return hexEncode(md.digest());
    }
}

