package ca.mcgill.cs.comp303.capone.loaders.op.stubs;

public class JBill
{
	String home_chamber;
	boolean law;
	JTextFrenchEnglish name;
	String[] vote_urls;
	String text_url;
	String number;
	JTextFrenchEnglish short_title;
	String url;
	String[] otherSessionUrls;
	String session;
	Related related;
	String legisinfo_url;
	boolean private_member_bill;
	String introduced;
	int legisinfo_id;
	String sponsor_politician_ur;
	String sponsor_politician_membership_url;

	public static class Related
	{
		String bills_url;
	}

	public String getHome_chamber()
	{
		return this.home_chamber;
	}

	public boolean isLaw()
	{
		return this.law;
	}

	public JTextFrenchEnglish getName()
	{
		return this.name;
	}

	public String[] getVote_urls()
	{
		return this.vote_urls;
	}

	public String getText_url()
	{
		return this.text_url;
	}

	public String getNumber()
	{
		return this.number;
	}

	public JTextFrenchEnglish getShort_title()
	{
		return this.short_title;
	}

	public String getUrl()
	{
		return this.url;
	}

	public String[] getOtherSessionUrls()
	{
		return this.otherSessionUrls;
	}

	public String getSession()
	{
		return this.session;
	}

	public Related getRelated()
	{
		return this.related;
	}

	public String getLegisinfo_url()
	{
		return this.legisinfo_url;
	}

	public boolean isPrivate_member_bill()
	{
		return this.private_member_bill;
	}

	public String getIntroduced()
	{
		return this.introduced;
	}

	public int getLegisinfo_id()
	{
		return this.legisinfo_id;
	}

	public String getSponsor_politician_ur()
	{
		return this.sponsor_politician_ur;
	}

	public String getSponsor_politician_membership_url()
	{
		return this.sponsor_politician_membership_url;
	}

}
