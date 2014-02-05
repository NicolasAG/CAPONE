package ca.mcgill.cs.comp303.capone.loaders.op.stubs;

public class JParty
{
	private JTextEnglishOnly name;
	private JTextEnglishOnly short_name;

	public String getName()
	{
		return this.name.getEn();
	}

	public String getShort_name()
	{
		return this.short_name.getEn();
	}
}
