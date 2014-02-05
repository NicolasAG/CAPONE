package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestMP
{

	MP test01 = new MP("key");	
	@Test
	public void testInitMP()
	{
		assertEquals(this.test01.getPrimaryKey(), "key");
	}
	
	@Test
	public void testFamilyName()
	{
		this.test01.setFamilyName("Family");
		assertEquals(this.test01.getFamilyName(), "Family");
	}
	
	@Test
	public void testGivenName()
	{
		this.test01.setGivenName("Given");
		assertEquals(this.test01.getGivenName(), "Given");
	}
	
	@Test
	public void testName()
	{
		this.test01.setName("Full Name");
		assertEquals(this.test01.getName(), "Full Name");
	}

	@Test
	public void testEmail()
	{
		this.test01.setEmail("key");
		assertEquals(this.test01.getEmail(), "key");
	}
	
	@Test
	public void testPhoneNumber()
	{
		this.test01.setPhoneNumber("1111");
		assertEquals(this.test01.getPhoneNumber(), "1111");
	}
	
	@Test
	public void testRSS()
	{
		this.test01.setImage("/media/polpics/5555_1.jpg");
		assertEquals(this.test01.getRSSFeedURL(), "http://openparliament.ca/politicians/5555/rss/activity/");
		
		this.test01.setImage(null);
		assertNull(this.test01.getRSSFeedURL());
	}
	
	@Test
	public void testMembership()
	{
		Party pa = new Party("Party", "pa");
		Riding ra = new Riding(1, "riding", "ride");
		Membership mem1 = new Membership(pa, ra, "2013-01-01","2013-02-01");
		Membership mem2 = new Membership(pa, ra, "2013-02-01","2014-03-01");
		Membership mem3 = new Membership(pa, ra, "2013-02-01","2014-02-02");
		
		this.test01.addMembership(mem1);
		Membership tMem1 = this.test01.getCurrentMembership();
		assertEquals(tMem1, null);
		
		this.test01.addMembership(mem2);
		this.test01.addMembership(mem3);
		int sizeOfMem1 = this.test01.getMemberships().size();
		int sizeOfMem2 = this.test01.getNumberOfMemberships();
		assertEquals(sizeOfMem1, sizeOfMem2);
		assertEquals(sizeOfMem2, 2);
		
		Membership tMem2 = this.test01.getCurrentMembership();
		assertEquals(tMem2, mem2);
	}
	
	@Test
	public void testSpeech()
	{
		Speech speech01 = new Speech(this.test01.getPrimaryKey(), "header1", "header2", "content", "2010-01-01");
		this.test01.addSpeech(speech01);
		assertEquals(this.test01.getSpeech("header1"), speech01);
		HashMap<String, Speech> speeches = this.test01.getSpeeches();
		assertEquals(speeches.size(), 1);
	}
	
	
	@Test
	public void testEquals()
	{
		MP test02 = new MP("key");
		MP test03 = new MP("key1");
		MP test04 = null;
		Party testParty = new Party("test", "party");
		
		assertTrue(this.test01.equals(test02));
		assertFalse(this.test01.equals(test03));
		assertFalse(this.test01.hashCode() == test03.hashCode());
		assertFalse(this.test01.equals(testParty));
		assertFalse(this.test01.equals(test04));
		assertTrue(this.test01.equals(this.test01));
		assertEquals(this.test01.hashCode(), test02.hashCode());
	}
	
	@Test
	public void testToString()
	{
		this.test01.setName("Full Name");
		assertEquals(this.test01.toString(), "Full Name");
	}
}
