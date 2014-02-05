package ca.mcgill.cs.comp303.capone.model;

import org.junit.Test;

import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoaderException;

import static org.junit.Assert.*;


/** 
 * Complete with your test methods for the Membership class.
 */
public class TestMembership
{
	Party pa = new Party("Party", "pa");
	Riding ra = new Riding(1, "riding", "ride");
	Membership mem1 = new Membership(this.pa, this.ra, "2013-01-01","2013-02-01");
	
	@Test
	public void testGetParty()
	{
		assertEquals(this.mem1.getParty(), this.pa);
	}
	
	@Test
	public void testGetRiding()
	{
		assertEquals(this.mem1.getRiding(), this.ra);
	}
	
	@Test
	public void testGetStartDate()
	{
		Membership mem2 = new Membership(this.pa, this.ra, "2013-01-01","2013-02-01");
		Membership mem3 = new Membership(this.pa, this.ra, "555", "666");
		assertEquals(this.mem1.getStartDate(), mem2.getStartDate());
		try
		{
			mem3.getStartDate();
			fail();
		}
		catch (ParliamentLoaderException e)
		{
			//Test is passed.
		}
	}
	
	@Test
	public void testGetEndDate()
	{
		Membership mem2 = new Membership(this.pa, this.ra, "2013-01-01","2013-02-01");
		Membership mem3 = new Membership(this.pa, this.ra, "555", "666");
		Membership mem4 = new Membership(this.pa, this.ra, null, null);
		assertEquals(this.mem1.getEndDate(), mem2.getEndDate());
		assertNull(mem3.getEndDate());
		assertFalse(mem2.getEndDate().equals(mem4.getEndDate())); //Used for block statement
	}
	
	@Test
	public void testCompareTo()
	{
		Membership mem2 = new Membership(this.pa, this.ra, "2013-03-01","2013-04-01");
		Membership mem3 = new Membership(this.pa, this.ra, "2013-01-01","2013-02-01");
		Membership mem4 = new Membership(this.pa, this.ra, "2012-01-01","2012-02-01");

		assertEquals(this.mem1.compareTo(mem2), 1);
		assertEquals(this.mem1.compareTo(mem3), 0);
		assertEquals(this.mem1.compareTo(mem4), -1);
	}
	
	@Test
	public void testEquals()
	{
		Membership mem2 = new Membership(this.pa, this.ra, "2013-01-01","2013-03-01");
		Membership mem3 = new Membership(this.pa, this.ra, "2013-01-03","2013-02-01");
		assertTrue(this.mem1.equals(this.mem1));
		assertFalse(this.mem1.equals(null));
		assertFalse(this.mem1.equals(new MP("key")));
		assertTrue(this.mem1.equals(mem2));
		assertTrue(this.mem1.hashCode() == mem2.hashCode());
		assertFalse(this.mem1.equals(mem3));
		assertFalse(this.mem1.hashCode() == mem3.hashCode());
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.mem1.toString(), "Membership[Party=Party[name=Party, short name=pa], Riding=Riding[ID=1, Name=riding, Province=ride], sartDate=2013-01-01, endDate=2013-02-01]");
	}
	
}

