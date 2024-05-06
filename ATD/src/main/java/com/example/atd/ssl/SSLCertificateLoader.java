package com.example.atd.ssl;

import java.io.FileInputStream;
import java.security.KeyStore;

public class SSLCertificateLoader {
    public static KeyStore loadP12Certificate(String p12Path, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(p12Path)) {
            keyStore.load(fis, password.toCharArray());
        }
        return keyStore;
    }
}
