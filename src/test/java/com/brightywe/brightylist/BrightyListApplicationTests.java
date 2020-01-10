package com.brightywe.brightylist;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BrightyListApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testFail() {
		assertEquals("123", "lol");
	}

}
