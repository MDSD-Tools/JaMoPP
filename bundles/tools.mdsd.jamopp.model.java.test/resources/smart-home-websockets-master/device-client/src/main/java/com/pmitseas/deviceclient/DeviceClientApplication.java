package com.pmitseas.deviceclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DeviceClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceClientApplication.class, args);
    }

}
