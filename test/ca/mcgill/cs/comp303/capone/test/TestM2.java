package ca.mcgill.cs.comp303.capone.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.mcgill.cs.comp303.capone.loaders.TestMPEvent;
import ca.mcgill.cs.comp303.capone.loaders.op.TestOpenParliamentFileLoader;
import ca.mcgill.cs.comp303.capone.model.*;

/**
 * Add all of your test classes, as necessary.
 */
@RunWith(Suite.class)
@SuiteClasses({TestCapone.class, TestMembership.class, TestMP.class, TestParliament.class,
	TestParty.class, TestRiding.class, TestSpeech.class, TestUserProfile.class, TestMPEvent.class,
	TestOpenParliamentFileLoader.class, })
public class TestM2
{
	//Run the tests above.
}
