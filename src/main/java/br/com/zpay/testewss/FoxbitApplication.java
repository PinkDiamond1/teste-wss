package br.com.zpay.testewss;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
public class FoxbitApplication {

    @Autowired
    AlphapointWS ws;

    public static void main(String[] args) {
        SpringApplication.run(FoxbitApplication.class, args);
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void test() throws IOException {
        ws.authenticate();
    }
}
