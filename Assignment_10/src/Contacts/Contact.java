package Contacts;
import java.util.*;
public class Contact implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	private int contactID;
	private String contactName;
	private String emailAddress;
	private List<String> contactNumber;

	public int getContactID() 
	{
		return contactID;
	}
	public void setContactID(int contactID) 
	{
		this.contactID = contactID;
	}
	public String getContactName() 
	{
		return contactName;
	}
	public void setContactName(String contactName) 
	{
		this.contactName = contactName;
	}
	public String getEmailAddress() 
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) 
	{
		this.emailAddress = emailAddress;
	}
	public List<String> getContactNumber() 
	{
		return contactNumber;
	}
	public void setContactNumber(List<String> contactNumber) 
	{
		this.contactNumber = contactNumber;
	}
	public String toShowString()
	{
		String s= contactID+" "+contactName+" "+emailAddress+" "+contactNumber;
		return s;
	}
}

