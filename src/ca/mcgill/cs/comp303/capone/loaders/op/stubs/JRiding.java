package ca.mcgill.cs.comp303.capone.loaders.op.stubs;

public class JRiding
{
	private String province;
	private JTextEnglishOnly name;
	private int id;

	public String getProvince()
	{
		return this.province;
	}

	public String getName()
	{
		return this.name.getEn();
	}

	public int getId()
	{
		return this.id;
	}
}
