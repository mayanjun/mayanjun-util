/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        initKey();
    }

    public SecretKeyStore(SecretKey key, String iv) {
        this.iv = iv;
        this.key = key;
    }

    private void initKey() {
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
