package com.github.haojiankang.framework.commons.utils.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 密码盐渍算法工具类,生成密码hash,密码长度可以调整SALT_BYTE_SIZE,HASH_BYTE_SIZE来改变。
 * <br>
 * how to use:
 * <pre>
 * String password = &quot;123456&quot;
 * String ars = PasswordHash.createHashArray(password);
 * boolean success = PasswordHash.validatePassword(password, ars);
 * </pre>
 */
public class PasswordHash {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 16;
    public static final int HASH_BYTE_SIZE = 16;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int PBKDF2_INDEX = 1;
    public static final int SALT_INDEX = 2;

    public static final String SEPARATOR = ":";

    /**
     * 加盐处理口令,返回处理后的密文。
     * @param password 口令明文
     * @return iterations:hash:salt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String createHashString(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] charPass = password.toCharArray();
    	byte[] salt = generateSalt(SALT_BYTE_SIZE);

        // Hash the password
        byte[] hash = pbkdf2(charPass, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

        // format iterations:hash:salt
        StringBuffer sbf = new StringBuffer(500);
        sbf.append(PBKDF2_ITERATIONS).append(SEPARATOR)
        .append(toHex(hash)).append(SEPARATOR).append(toHex(salt));
        return sbf.toString();
    }

    /**
     * 验证明文与密文 是否匹配。
     * @param password 口令明文
     * @param correctHash 口令密文，iterations:hash:salt
     * @return 匹配返回真，否则返回假
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean validatePassword(String password, String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || correctHash == null) return false;
        // 兼容非加密字符串比较
        if (password == correctHash || password.equals(correctHash)) return true;

        char[] charPass = password.toCharArray();
        // Decode the hash into its parameters
        String[] params = correctHash.split(SEPARATOR);
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        // Compute the hash of the provided password, using the same salt, iteration count, and hash length
        byte[] testHash = pbkdf2(charPass, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a
     *            the first byte array
     * @param b
     *            the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password
     *            the password to hash.
     * @param salt
     *            the salt
     * @param iterations
     *            the iteration count (slowness factor)
     * @param bytes
     *            the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex
     *            the hex string
     * @return the hex string decoded into a byte array
     */
    public static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array
     *            the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    public static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    /**
     * 生成随机的Byte[]作为salt.
     * @param numBytes byte数组的大小
     * @return 随机salt
     */
    public static byte[] generateSalt(int numBytes) {
        if (numBytes < 1) return null;
        byte[] bytes = new byte[numBytes];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }
  
}
