package org.mayanjun.util;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * KeyPairStore
 * @author mayanjun(6/3/16)
 */
public class KeyPairStore {

    private String privateKeyString;

    private String publicKeyString;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    public KeyPairStore(String privateKeyString, String publicKeyString) {
        this.privateKeyString = privateKeyString;
        this.publicKeyString = publicKeyString;
        init();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public KeyPairStore(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;

    }

    private void init() {
        this.privateKey = createPrivateKey(privateKeyString);
        this.publicKey = createPublicKey(publicKeyString);
    }

    public PrivateKey createPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyString() {
        return privateKeyString;
    }

    public void setPrivateKeyString(String privateKeyString) {
        this.privateKeyString = privateKeyString;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }

    public void setPublicKeyString(String publicKeyString) {
        this.publicKeyString = publicKeyString;
    }


    public static PrivateKey createPrivateKey(String privateKeyString) {
        try {
            byte[] encodedKey = Base64.decodeBase64(privateKeyString);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey createPublicKey(String publicKeyString) {
        try {
            byte[] encodedKey = Base64.decodeBase64(publicKeyString);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
