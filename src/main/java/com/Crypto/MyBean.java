package com.Crypto;

import com.Crypto.serviceImpl.DecryptionImpl;
import com.Crypto.serviceImpl.EncryptionImpl;
import com.Crypto.serviceIntefaces.DecryptionInterface;
import com.Crypto.serviceIntefaces.EncryptionInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBean {
    @Bean
    public EncryptionInterface encryptionInterface() {
        return new EncryptionImpl();
    }

    @Bean
    public DecryptionInterface decryptionInterface(){ return new DecryptionImpl(); }
}
