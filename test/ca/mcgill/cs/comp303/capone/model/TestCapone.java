package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCapone
{
	@SuppressWarnings("static-method")
	@Test
	public void testToString()
	{
		assertEquals(Capone.getInstance().toString(), "Capone");
	}
}
