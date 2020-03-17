package io.nayasis.basica.spring.web.client;

import lombok.experimental.UtilityClass;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@UtilityClass
public class SslChecker {

    private final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers(){
                return null;
            }
            public void checkClientTrusted( X509Certificate[] certs, String authType ){}
            public void checkServerTrusted( X509Certificate[] certs, String authType ){}
        }
    };

    public void turnOff() {
        // Install the all-trusting trust manager
        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch ( KeyManagementException | NoSuchAlgorithmException e ) {
            new RuntimeException( e );
        }
    }

    public void turnOn() {
        // Return it to the initial state (discovered by reflection, now hardcoded)
        try {
            SSLContext.getInstance("SSL").init( null, null, null );
        } catch ( KeyManagementException | NoSuchAlgorithmException e ) {
            new RuntimeException( e );
        }
    }

}