package com.walmart.ticketservice.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VanueDetailsDTOTest {
	@Test
	public void testBean(){
		VanueDetailsDTO dto=new VanueDetailsDTO(1, "test", "100", 100, 1);
		dto.setPrice("300");
		assertTrue(dto.getLevelId()==1);
		assertTrue(dto.getLevelName().equals("test"));
		assertTrue(dto.getPrice().equals("300"));
		
	}
}
