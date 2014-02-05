package ca.mcgill.cs.comp303.capone.loaders.op.stubs;


/**
 * 
 * @author aying1
 * 
 */
public class JMembership
{

	String politician_url;
	String end_date;
	JRiding riding;

	String url;
	JParty party;

	String start_date;
	JTextEnglishOnly label;

	public String getPolitician_url()
	{
		return this.politician_url;
	}

	public String getEnd_date()
	{
		return this.end_date;
	}

	public JRiding getRiding()
	{
		return this.riding;
	}

	public String getUrl()
	{
		return this.url;
	}

	public JParty getParty()
	{
		return this.party;
	}

	public String getStart_date()
	{
		return this.start_date;
	}

	public JTextEnglishOnly getLabel()
	{
		return this.label;
	}

}
