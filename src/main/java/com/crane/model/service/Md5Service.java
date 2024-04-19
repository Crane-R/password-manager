package com.crane.model.service;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密服务
 *
 * @author AXing
 * @date 2023/12/22 21:00:52
 */
public class Md5Service {

    public String convertMd5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            return new BigInteger(md5.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(String p1, String p2) {
        return MessageDigest.isEqual(p1.getBytes(), p2.getBytes());
    }
}
