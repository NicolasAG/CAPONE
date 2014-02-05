package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRiding
{

	Riding ride1 = new Riding(1, "riding1", "province1");
	@Test
	public void testGetID()
	{
		assertEquals(this.ride1.getId(), 1);
	}
	
	@Test
	public void testGetName()
	{
		assertEquals(this.ride1.getName(), "riding1");
	}
	
	@Test
	public void testGetProvince()
	{
		assertEquals(this.ride1.getProvince(), "province1");
	}

	@Test
	public void testEquals()
	{
		Riding ride2 = new Riding(1, "riding1", "province1");
		Riding ride3 = new Riding(1, "riding1", "province2");
		
		assertFalse(this.ride1.equals(null));
		assertFalse(this.ride1.equals(new MP("key")));
		assertTrue(this.ride1.equals(ride2));
		assertTrue(this.ride1.hashCode() == ride2.hashCode());
		assertFalse(this.ride1.equals(ride3));
		assertFalse(this.ride1.hashCode() == ride3.hashCode());
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.ride1.toString(), "Riding[ID=1, Name=riding1, Province=province1]");
	}
}
