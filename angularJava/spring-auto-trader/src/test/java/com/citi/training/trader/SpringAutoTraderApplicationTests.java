package com.citi.training.trader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("h2test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAutoTraderApplicationTests {

	@Test
	public void contextLoads() {
	}

}
