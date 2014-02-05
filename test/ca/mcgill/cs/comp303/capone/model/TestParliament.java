package ca.mcgill.cs.comp303.capone.model;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.cs.comp303.capone.model.Parliament.UpdatedType;

public class TestParliament
{

	@SuppressWarnings("static-method")
	@Test
	public void testMP()
	{
		MP mp01 = new MP("key1");
		Capone.getParliament().addMP(mp01);
		mp01.setName("aaa");
		mp01.setName("bbb");
		Capone.getParliament().addMP(mp01);
		assertEquals(Capone.getParliament().getMP("key1"), mp01);
		assertEquals(Capone.getParliament().getMPs().size(), 1);
		assertEquals(Capone.getParliament().getMPs().get(0), mp01);
		assertEquals(Capone.getParliament().getMP("key1").getName(), "bbb");
		
		Capone.getParliament().removeMP(mp01);
		assertNull(Capone.getParliament().getMP("key1"));
		assertEquals(Capone.getParliament().getMPs().size(), 0);
	}
	
	@SuppressWarnings("static-method")
	@Test
	public void testParty()
	{
		Party p = new Party("name", "shortName");
		Capone.getParliament().addParty("shortName", "name");
		assertEquals(Capone.getParliament().getParties().size(), 1);
		assertEquals(Capone.getParliament().getParty("shortName").getName(), "name");
		
		Capone.getParliament().addParty("shortName", "name2");
		assertEquals(Capone.getParliament().getParties().size(), 1);
		assertEquals(Capone.getParliament().getParty("shortName").getName(), "name2");
		
		Capone.getParliament().removeParty(p);
		assertEquals(Capone.getParliament().getParties().size(), 0);
		assertNull(Capone.getParliament().getParty("shortName"));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testRiding()
	{
		Riding r = new Riding(1, "name", "province");
		Capone.getParliament().addRiding(1, "name", "province");
		assertEquals(Capone.getParliament().getRidings().size(), 1);
		assertEquals(Capone.getParliament().getRiding(1).getName(),"name");
		assertEquals(Capone.getParliament().getRiding(1).getProvince(),"province");
		
		Capone.getParliament().addRiding(1, "name2", "province2");
		assertEquals(Capone.getParliament().getRidings().size(), 1);
		assertEquals(Capone.getParliament().getRiding(1).getName(),"name2");
		assertEquals(Capone.getParliament().getRiding(1).getProvince(),"province2");
		
		Capone.getParliament().removeRiding(r);
		assertEquals(Capone.getParliament().getRidings().size(), 0);
		assertNull(Capone.getParliament().getRiding(1));
	}
	
	@SuppressWarnings("static-method")
	@Test
	public void testObserver()
	{
		UserProfile obs1 = UserProfile.getInstance("user1");
		assertTrue(Capone.getParliament().addObserver(obs1));
		MP mp1 = new MP("key");
		Capone.getParliament().addMP(mp1);
		obs1.addMP(mp1);
		Capone.getParliament().removeMP(mp1);
		Capone.getParliament().notifyObservers(mp1, UpdatedType.MP);
		assertNull(obs1.getMP("key"));
		
		assertFalse(Capone.getParliament().addObserver(UserProfile.getInstance()));
		assertEquals(Capone.getParliament().getObservers().size(), 1);
		assertEquals(Capone.getParliament().getObservers().get(0), obs1);
		
		Capone.getParliament().removeObserver(obs1);
		assertEquals(Capone.getParliament().getObservers().size(), 0);
	}
	
	@SuppressWarnings("static-method")
	@Test
	public void testToString()
	{
		assertEquals(Capone.getParliament().toString(), "Parliament");
	}
}
