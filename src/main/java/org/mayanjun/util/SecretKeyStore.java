package org.mayanjun.util;

import javax.crypto.SecretKey;

/**
 * AES key store
 */
public class SecretKeyStore {

    private String keyString;

    private SecretKey key;

    private String iv;

    public SecretKeyStore(String keyString, String iv) {
        this.keyString = keyString;
        this.iv = iv;
    }

    private void init() {
        try {
            this.key = AES.generateKey(keyString);
        } catch (Exception e) {
            throw new RuntimeException("Can not init AES key", e);
        }
    }

    public SecretKey key() {
        return key;
    }

    public byte [] iv() {
        return iv.getBytes();
    }

    public String getKeyString() {
        return keyString;
    }

    public String getIv() {
        return iv;
    }
}
