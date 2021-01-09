package org.mayanjun.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class BouncyCastleCrypto extends Crypto {

    public BouncyCastleCrypto() {
    }

    public BouncyCastleCrypto(SecretKeyStore secretKeyStore) {
        super(secretKeyStore);
    }

    @Override
    public byte[] encrypt(byte[] content, SecretKeyStore store) throws Exception {
        Key key = new SecretKeySpec(store.getKeyString().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(store.iv()));
        byte cs[] = cipher.doFinal(content);
        return cs;
    }

    @Override
    public byte[] decrypt(byte[] content, SecretKeyStore store) throws Exception {
        Key key = new SecretKeySpec(store.getKeyString().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(store.iv()));
        byte cs[] = cipher.doFinal(content);
        return cs;
    }
}
