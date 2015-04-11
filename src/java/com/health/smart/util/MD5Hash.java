/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.util;

import java.security.MessageDigest;

/**
 *
 * @author HMTSystem
 */
public class MD5Hash {

    public static String hexToASCII(String hexValue) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2) {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String hash(String original) throws Exception {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(original.getBytes());
        byte messageDigest[] = algorithm.digest();

        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            String s = Integer.toHexString(0xFF & messageDigest[i]);
            hexString.append(s.length() == 1 ? "0" : "").append(s);
        }

        return hexString.toString();
    }
}
