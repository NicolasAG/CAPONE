package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUserProfile
{
	UserProfile test1 = UserProfile.getInstance("user1");
	
	@Test
	public void testInit()
	{
		assertEquals(this.test1.getName(), "user1");
	}
	
	@Test
	public void testMP()
	{
		assertEquals(this.test1.getMPs().size(), 0);
		
		MP mp1 = new MP("key1");
		this.test1.addMP(mp1);
		assertEquals(this.test1.getMPs().size(), 1);
		
		MP mp2 = new MP("key2");
		this.test1.addMP(mp2);
		assertEquals(this.test1.getMPs().size(), 2);
		
		this.test1.addMP(mp2);
		assertEquals(this.test1.getMPs().size(), 2);
		
		MP mp3 = new MP("key3");
		this.test1.addMP(mp3);
		assertEquals(this.test1.getMPs().size(), 3);
		
	//	assertEquals(this.test1.getMP("key3"), mp3);
		
		this.test1.removeMP(mp2);
		assertEquals(this.test1.getMPs().size(), 2);
		assertEquals(this.test1.getMP("key2"), null);
	}
	
	@Test
	public void testExpression()
	{
		assertEquals(this.test1.getExpressions().size(), 0);
		
		this.test1.addExpression("aaa");
		this.test1.addExpression("aaa");
		assertEquals(this.test1.getExpressions().size(), 1);
		
		this.test1.addExpression("bbb");
		assertEquals(this.test1.getExpressions().size(), 2);
		assertEquals(this.test1.getExpressions().get(0), "aaa");
		
		this.test1.removeExpression("aaa");
		assertEquals(this.test1.getExpressions().size(), 1);
		assertEquals(this.test1.getExpressions().get(0), "bbb");
	}
	
	@Test
	public void testUpdateMP()
	{
		MP mp1 = new MP("key1");
		MP mp2 = new MP("key2");
		MP mp3 = new MP("key3");
		mp1.setName("mp1");
		mp2.setName("mp2");
		mp3.setName("mp3");
		
		Capone.getParliament().addMP(mp1);
		Capone.getParliament().addMP(mp2);
		Capone.getParliament().addMP(mp3);
		this.test1.addMP(mp1);
		this.test1.addMP(mp2);
		
		mp1.setName("aaa");
		Capone.getParliament().removeMP(mp2);
		mp3.setName("ccc");
		
		this.test1.update(mp1);
		this.test1.update(mp2);
		this.test1.update(mp3);
		
		//assertEquals(this.test1.getMP("key1").getName(), "aaa");
		assertNull(this.test1.getMP("key2"));
		assertNull(this.test1.getMP("key3"));
	}
	
	@Test
	public void testEquals()
	{
		
//		MP mp1 = new MP("key1");
//		UserProfile test2 = new UserProfile("user2");
//		UserProfile test3 = new UserProfile("user1");
//		
//		this.test1.addMP(mp1);
//		this.test1.addExpression("aaa");
//		test2.addMP(mp1);
//		test2.addExpression("aaa");
//		test3.addMP(new MP("key2"));
//		test3.addExpression("bbb");
//		
//		assertFalse(this.test1.equals(null));
//		assertFalse(this.test1.equals(mp1));
//		assertFalse(this.test1.equals(test2));
//		assertFalse(this.test1.hashCode() == test2.hashCode());
//		assertTrue(this.test1.equals(test3));
//		assertTrue(this.test1.hashCode() == test3.hashCode());
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.test1.toString(), "UserProfile[Name=user1]");
	}
}
