package ca.mcgill.cs.comp303.capone.model;

import org.junit.Test;

import static org.junit.Assert.*;




/**
 * Complete with your test methods for the Speech class.
 */
public class TestSpeech
{
	Speech speech1 = new Speech("key", "header1", "header2", "content", "2013-01-01");
	
	@Test
	public void testGetMP()
	{
		assertEquals(this.speech1.getMPKey(), "key");
	}
	
	@Test
	public void testGetHeader1()
	{
		assertEquals(this.speech1.getHeader1(), "header1");
	}
	
	@Test
	public void testGetHeader2()
	{
		assertEquals(this.speech1.getHeader2(), "header2");
	}
	
	@Test
	public void testGetContent()
	{
		assertEquals(this.speech1.getContent(), "content");
	}
	
	@Test
	public void testGetTime()
	{
		Speech speech2 = new Speech("key", "header12", "header22", "content2", "2013-01-01");
		assertEquals(this.speech1.getTime(), speech2.getTime());
		Speech speech4 = new Speech("key", "header12", "header22", "content2", "555");
		assertNull(speech4.getTime()); // Used for block statement
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.speech1.toString(), "header1 - header2");
	}
}
