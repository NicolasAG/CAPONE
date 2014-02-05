package ca.mcgill.cs.comp303.capone.loaders.op;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader;
import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoaderException;
import ca.mcgill.cs.comp303.capone.model.Capone;
import ca.mcgill.cs.comp303.capone.loaders.op.OpenParliamentFileLoader;
import ca.mcgill.cs.comp303.capone.loaders.op.OpenParliamentFileLoader.DataDirectory;

public class TestOpenParliamentFileLoader
{
		
	@SuppressWarnings("static-method")
	@Test
	public void testLoadMP()
	{
		ParliamentLoader l = new OpenParliamentFileLoader();
		String key = l.loadMP("thomas-mulcair", Capone.getParliament(), DataDirectory.M1);
		l.loadRecentEvents(key, Capone.getParliament(), DataDirectory.M1);
		assertEquals(Capone.getParliament().getMP("thomas.mulcair@parl.gc.ca").getFamilyName(), "Mulcair");
		
		String key2 = l.loadMP("stephen-harper", Capone.getParliament(), DataDirectory.M2BEFOREJUNE);
		l.loadRecentEvents(key2, Capone.getParliament(), DataDirectory.M2BEFOREJUNE);
		assertEquals(Capone.getParliament().getMP("stephen.harper@parl.gc.ca").getFamilyName(), "Harper");
		
		String key3 = l.loadMP("alice-wong", Capone.getParliament(), DataDirectory.M2AFTERJUNE);
		l.loadRecentEvents(key3, Capone.getParliament(), DataDirectory.M2AFTERJUNE);
		assertEquals(Capone.getParliament().getMP("alice.wong@parl.gc.ca").getFamilyName(), "Wong");
		
		try
		{
			String key4 = l.loadMP("", Capone.getParliament(), DataDirectory.M1);
			l.loadRecentEvents(key4, Capone.getParliament(), DataDirectory.M1);
			fail();
		}
		catch(ParliamentLoaderException e)
		{
			//Test is passed.
		}
	}
	
}
