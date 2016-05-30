package com.walmart;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={JerseyConfig.class})
public class JerseyConfigTest {

	@Inject
	JerseyConfig config;
	
	@Test
    public void testBean() {
		config.getConfiguration();
    }
	
}
