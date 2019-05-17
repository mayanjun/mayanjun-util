package org.mayanjun.util;

import org.apache.commons.codec.binary.Base64;
import org.mayanjun.core.Assert;
import org.mayanjun.core.ServiceException;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Encryptions utils
 * @author mayanjun(3/18/16)
 */
public class Encryptions {

    private Encryptions() {}

    public static String SIGN_ALGORITHMS = "SHA512withRSA";

    public static String sign(String source, KeyPairStore store) {
        return sign(source, store, SIGN_ALGORITHMS);
    }

    public static String sign(String source, KeyPairStore store, String signAlg) {
        Assert.notNull(source, "签名失败:无法对空内容进行签名");
        try {
            Signature signature = Signature.getInstance(signAlg);
            signature.initSign(store.getPrivateKey());
            byte bytes[] = source.getBytes();
            signature.update(bytes);

            byte result[] = signature.sign();

            return Base64.encodeBase64String(result);

        } catch (Exception e) {
            throw new ServiceException("签名失败");
        }
    }

    public static boolean verify(String signature, String plain, KeyPairStore store) {
        return verify(signature, plain, store.getPublicKey());
    }

    public static boolean verify(String signature, String plain, PublicKey key) {
        return verify(signature, plain, key, SIGN_ALGORITHMS);
    }

    public static boolean verify(String signature, String plain, PublicKey key, String alg) {
        Assert.notNull(signature, "验证失败:签名错误");
        Assert.notNull(plain, "验证失败:签名错误");
        try {
            Signature s = Signature.getInstance(alg);
            s.initVerify(key);
            s.update(plain.getBytes());
            byte bytes[] = Base64.decodeBase64(signature);

            return s.verify(bytes);
        } catch (Exception e) {
            throw new ServiceException("验证失败:签名错误");
        }
    }

    public static String encrypt(String plain, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte ret[] = cipher.doFinal(plain.getBytes());
            return Base64.encodeBase64String(ret);
        } catch (Exception e) {
        }
        return null;
    }

    public static String decrypt(String signature, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte bytes[] = Base64.decodeBase64(signature);
            byte ret[] = cipher.doFinal(bytes);
            return new String(ret);
        } catch (Exception e) {
        }
        return null;
    }

    public static String encrypt(String plain, KeyPairStore store) {
        return encrypt(plain, store.getPublicKey());
    }

    public static String decrypt(String signature, KeyPairStore store) {
        return decrypt(signature, store.getPrivateKey());
    }

    public static KeyPairStore generateKeys() {
        KeyPairGenerator keygen = null;
        try {
            keygen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        SecureRandom random = new SecureRandom();
        // random.setSeed(keyInfo.getBytes());
        // 初始加密，512位已被破解，用1024位,最好用2048位
        keygen.initialize(1024, random);
        // 取得密钥对
        KeyPair kp = keygen.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();
        String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());

        RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
        String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());

        System.out.println("PrivateKey:" + privateKeyString);
        System.out.println("PublicKey:" + publicKeyString);

        return new KeyPairStore(privateKeyString, publicKeyString);
    }
}