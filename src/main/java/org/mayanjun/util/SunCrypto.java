package org.mayanjun.util;


public class SunCrypto extends Crypto {

    public SunCrypto() {
    }

    public SunCrypto(SecretKeyStore secretKeyStore) {
        super(secretKeyStore);
    }

    @Override
    public byte[] encrypt(byte[] content, SecretKeyStore store) throws Exception {
        return AES.encrypt(content, store.iv(), store.key().getEncoded());
    }

    @Override
    public byte[] decrypt(byte[] content, SecretKeyStore store) throws Exception {
        return AES.decrypt(content, store.iv(), store.key().getEncoded());
    }
}
