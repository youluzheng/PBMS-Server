package org.pbms.pbmsserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("org.pbms.pbmsserver.dao")
public class PbmsServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(PbmsServerApplication.class, args);

    }

}
