/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"charlie.feng"})
@EnableJpaRepositories("charlie.feng")
@EntityScan("charlie.feng")
public class SudokumasterservApplication {

    public static void main(String[] args) {
        SpringApplication.run(SudokumasterservApplication.class, args);
    }

}
