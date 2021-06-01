package br.com.techcraftbrasil.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class RestAPI {

    private static boolean started = false;

    public static boolean start(){
        if(!started) {
            SpringApplication app = new SpringApplication();
            app.setDefaultProperties(new HashMap<String, Object>(){{
                put("server.ssl.key-store-type", "PKCS12");
                put("server.ssl.key-store", "classpath:keystore/baeldung.p12");
                put("server.ssl.key-store-password", "password");
                put("server.ssl.key-alias", "baeldung");
                put("server.ssl.enabled", true);
            }});
            app.run(RestAPI.class);
            started = true;
            return true;
        } else  return false;
    }

}
