package org.hzero.sample.rabbitmq;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableChoerodonResourceServer
@SpringBootApplication
public class HzeroSampleRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzeroSampleRabbitmqApplication.class, args);
    }

}
