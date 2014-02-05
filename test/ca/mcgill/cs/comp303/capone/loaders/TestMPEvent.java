package ca.mcgill.cs.comp303.capone.loaders;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.cs.comp303.capone.loaders.MPEvent.Descriptor;

public class TestMPEvent
{
//	UNCLASSIFIED, MEDIA, VOTE, SPEECH

	MPEvent unclassified = new MPEvent(Descriptor.UNCLASSIFIED, "unTitle", "unLink", "unDes");
	MPEvent media = new MPEvent(Descriptor.MEDIA, "meTitle", "meLink", "meDes");
	MPEvent vote = new MPEvent(Descriptor.VOTE, "voTitle", "voLink", "voDes");
	MPEvent speech = new MPEvent(Descriptor.SPEECH, "spTitle", "spLink", "spDes");
	
	@Test
	public void testGetType()
	{
		assertEquals(this.unclassified.getType(), Descriptor.UNCLASSIFIED);
		assertEquals(this.media.getType(), Descriptor.MEDIA);
		assertEquals(this.vote.getType(), Descriptor.VOTE);
		assertEquals(this.speech.getType(), Descriptor.SPEECH);
	}
	
	@Test
	public void testGetTitle()
	{
		assertEquals(this.unclassified.getTitle(), "unTitle");
		assertEquals(this.media.getTitle(), "meTitle");
		assertEquals(this.vote.getTitle(), "voTitle");
		assertEquals(this.speech.getTitle(), "spTitle");
	}
	
	@Test
	public void testGetLink()
	{
		assertEquals(this.unclassified.getLink(), "unLink");
		assertEquals(this.media.getLink(), "meLink");
		assertEquals(this.vote.getLink(), "voLink");
		assertEquals(this.speech.getLink(), "spLink");
	}
	
	@Test
	public void testGetDescription()
	{
		assertEquals(this.unclassified.getDescription(), "unDes");
		assertEquals(this.media.getDescription(), "meDes");
		assertEquals(this.vote.getDescription(), "voDes");
		assertEquals(this.speech.getDescription(), "spDes");
	}
	
	@Test
	public void testToString()
	{
		assertEquals(this.unclassified.toString(), "MPEvent [aTitle=unTitle, aLink=unLink, aDescription=unDes]");
		assertEquals(this.media.toString(), "MPEvent [aTitle=meTitle, aLink=meLink, aDescription=meDes]");
		assertEquals(this.vote.toString(), "MPEvent [aTitle=voTitle, aLink=voLink, aDescription=voDes]");
		assertEquals(this.speech.toString(), "MPEvent [aTitle=spTitle, aLink=spLink, aDescription=spDes]");

	}
}
