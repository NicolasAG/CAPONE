
package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestParty
{

	Party p1 = new Party("name", "shortName");
	@Test
	public void testGetName()
	{
		assertEquals(this.p1.getName(), "name");
	}
	
	@Test
	public void testGetShortName()
	{
		assertEquals(this.p1.getShortName(), "shortName");
	}
	
	@Test
	public void testEquals()
	{
		Party p2 = new Party("name", "shortName2");
		Party p3 = new Party("aaa", "shortName");
		
		assertFalse(this.p1.equals(null));
		assertFalse(this.p1.equals(new MP("key")));
		assertFalse(this.p1.equals(p2));
		assertFalse(this.p1.hashCode() == p2.hashCode());
		assertTrue(this.p1.equals(p3));
		assertTrue(this.p1.hashCode() == p3.hashCode());
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.p1.toString(), "Party[name=name, short name=shortName]");
	}

}
