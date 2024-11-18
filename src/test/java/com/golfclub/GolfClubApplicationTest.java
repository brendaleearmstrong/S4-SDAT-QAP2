// src/test/java/com/golfclub/GolfClubApplicationTests.java
package com.golfclub;

import com.golfclub.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
class GolfClubApplicationTests {

	@Test
	void contextLoads() {
	}
}