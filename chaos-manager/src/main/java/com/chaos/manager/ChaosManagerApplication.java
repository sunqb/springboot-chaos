package com.chaos.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Chaos Manager 运营管理平台启动类
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.chaos")
public class ChaosManagerApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ChaosManagerApplication.class, args);
        Environment env = application.getEnvironment();

        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8081");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n\t" +
                        "API Doc: \thttp://localhost:{}{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name", "Chaos-Manager"),
                port, contextPath,
                ip, port, contextPath,
                port, contextPath);
    }
}
