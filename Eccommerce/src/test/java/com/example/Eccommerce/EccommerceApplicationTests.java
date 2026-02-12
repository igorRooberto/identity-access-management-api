package com.example.Eccommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "api.security.secret=segredo-teste-apenas-para-build")
class EccommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
