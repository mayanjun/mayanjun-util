package org.mayanjun.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;

public abstract class Crypto {

    private static final Logger LOG = LoggerFactory.getLogger(Crypto.class);

    /**
     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
     */
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private SecretKeyStore secretKeyStore;

    public Crypto() {
    }

    public Crypto(SecretKeyStore secretKeyStore) {
        this.secretKeyStore = secretKeyStore;
    }

    public byte[] encrypt(byte content[]) throws Exception {
        return encrypt(content, secretKeyStore());
    }

    public abstract byte[] encrypt(byte content[], SecretKeyStore store) throws Exception;

    public String encrypt(String content) {
        return encrypt(content, this.secretKeyStore());
    }

    public String encrypt(String content, SecretKeyStore store) {
        try {
            if (content == null) return null;
            byte bs[] = content.getBytes("utf-8");
            byte es[] = encrypt(bs, store);

            if (es == null) return null;
            return Base64.encodeBase64String(es);

        } catch (Exception e) {
            LOG.error("Encrypt error: content=" + content, e);
        }

        return null;
    }

    public byte[] decrypt(byte content[]) throws Exception {
        return decrypt(content, this.secretKeyStore());
    }

    public abstract byte[] decrypt(byte content[], SecretKeyStore store) throws Exception;

    public String decrypt(String content) {
        return decrypt(content, secretKeyStore());
    }

    public String decrypt(String content, SecretKeyStore store){
        try {
            if (content == null) return null;
            byte bs[] = Base64.decodeBase64(content);
            byte es[] = decrypt(bs, store);
            if (es == null) return null;
            return new String(es, "utf-8");

        } catch (Exception e) {
            LOG.error("Encrypt error: content=" + content, e);
        }

        return null;
    }




    public SecretKeyStore secretKeyStore() {
        return this.secretKeyStore;
    }
}
