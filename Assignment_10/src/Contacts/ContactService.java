package Contacts;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ContactService 
{
	public static void addContact(Contact contact,List<Contact> contacts)
	{
		int s=contacts.size();
		contacts.add(contact);
		if(s<contacts.size())
			System.out.println("Added Contact to List : \n"+contact.toShowString());
		else
			System.out.println("ERROR : Contact has not been added");
	}
	public static void removeContact(Contact contact, List<Contact> contacts) throws ClassNotFoundException
	{
		int count=0;
		Iterator<Contact> iter=contacts.iterator();
		while(iter.hasNext())
		{
			Contact e=iter.next();
			if(contact.getContactID()==e.getContactID())
			{
				contacts.remove(e);
				count=1;
				break;
			}
		}
		if(count==1)
			System.out.println("Removed Contact from List : \n"+contact.toShowString());
		else
			System.out.println("ERROR : Contact has not been removed");
	}
	public static Contact searchContactByName(String name, List<Contact> contacts) throws ClassNotFoundException
	{
		Contact c=new Contact();
		int count=0;
		Iterator<Contact> it=contacts.iterator();
		while(it.hasNext())
		{
			Contact e=(Contact) it.next();
			if(e.getContactName().equalsIgnoreCase(name))
			{
				c=e;
				count=1;
				break;
			}
		}
		if(count==1)
			System.out.println("Searched Contact found : \n"+c.toShowString());
		else
			System.out.println("ERROR : Contact has not been found");
		return c;
	}
	public static List<Contact> searchContactByNumber(String number, List<Contact> contacts) throws ClassNotFoundException
	{
		List<Contact> searchResult= new ArrayList<Contact>();
		int count=0;
		Iterator<Contact> it=contacts.iterator();
		while(it.hasNext())
		{
			Contact e=(Contact) it.next();
			List<String> cNo=e.getContactNumber();
			Iterator<String> iter=cNo.iterator();
			while(iter.hasNext())
			{
				String num=iter.next();
				if(num.contains(number) && !searchResult.contains(e))
				{
					searchResult.add(e);
					count=1;
				}
			}
		}
		if(count==1)
		{
			System.out.println("Searched Contact found : \n List Returned.");
			Iterator<Contact> it2=searchResult.iterator();
			while(it2.hasNext())
			{
				Contact e2=(Contact) it2.next();
				System.out.println(e2.toShowString());
			}
		}
		else
			System.out.println("ERROR : Contact has not been found");
		return searchResult;
	}
	public static void addContactNumber(int contactId, String contactNo, List<Contact> contacts)
	{
		Iterator<Contact> it=contacts.iterator();
		while(it.hasNext())
		{
			Contact e=(Contact) it.next();
			if(e.getContactID()==contactId)
			{
				List<String> updatedList= new ArrayList<String>();
				List<String> catchedList=e.getContactNumber();
				updatedList=catchedList;
				updatedList.add(contactNo);
				e.setContactNumber(updatedList);
				System.out.println("Contact Numbers Updated :");
				System.out.println(e.toShowString());
			}
		}	
	}
	public static void sortContactsByName(List<Contact> contacts)
	{
		 Collections.sort(contacts, new NameSorter());
	}
	public static void readContactsFromFile(List<Contact> contacts, String fileName) throws FileNotFoundException
	{	
		  int count=0;
		  File file=new File(fileName);
		  Scanner sc=new Scanner(file);
		  String text[] = null;						
		  while(sc.hasNextLine())
		  {
				Contact m=new Contact();
				text=sc.nextLine().split(",");
				m.setContactID(Integer.valueOf(text[0]));
				m.setContactName(text[1]);
				m.setEmailAddress(text[2]);
				//Contact Numbers are provided in file separated by : "-".
				m.setContactNumber(new ArrayList<>(Arrays.asList(text[3].split("-"))));

				contacts.add(m);
				count=1;
		  }	
		  if(count==1)
				System.out.println("Contacts added from file.");
			else
				System.out.println("ERROR : Contacts has not been added from file.");
		  sc.close();
	}
	public static void serializeContactDetails(List<Contact> contacts , String fileName)
	{
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(contacts);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in :"+fileName);
		} 
		catch (IOException i) 
		{
			i.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Contact> deserializeContactDetails(String fileName)
	{
		List<Contact> list=new ArrayList<Contact>();
		try 
		{
			 FileInputStream fileIn = new FileInputStream(fileName);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         list=(List<Contact>) in.readObject();
	         in.close();
	         fileIn.close();
		} 
		catch (IOException i) 
		{
			i.printStackTrace();
		}
		catch (ClassNotFoundException c) 
		{
	         System.out.println("Movie class not found");
	         c.printStackTrace();
	    }
		return list;
	}
	public static Set<Contact> populateContactFromDb()
	{
		Set<Contact> c=new HashSet<>();
		
		String sql="select * from contact_tbl ";
		Connection con=null;
		PreparedStatement p=null;
		ResultSet rs=null;
		try 
		{
			con=MyConnection.connectDB();
			p =con.prepareStatement(sql);
			rs =p.executeQuery();
			
			while (rs.next()) 
			{ 
				  
                int id = rs.getInt("contactId"); 
                String name = rs.getString("contactName"); 
                String email = rs.getString("contactEmail"); 
                String contactList=rs.getString("contactList");
                List<String> cList= new ArrayList<>(Arrays.asList(contactList.split(",")));
                
                System.out.println(id + "\t\t" + name  + "\t\t" + email + "\t\t" +contactList);
                Contact m=new Contact();
				m.setContactID(id);
				m.setContactName(name);
				m.setEmailAddress(email);
				m.setContactNumber(cList);

				c.add(m);	
            } 
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	public static boolean addContacts(List<Contact> existingContact,Set<Contact> newContacts)
	{
		int s=existingContact.size();
		boolean flag=false;
		Iterator<Contact> it=newContacts.iterator();
		while(it.hasNext())
		{
			Contact e=(Contact) it.next();
			if(!existingContact.contains(e))
			{
			    existingContact.add(e);
			    flag=true;
			}
		}
		
		if(s<existingContact.size())
			System.out.println("Added Contacts to List.");
		else
			System.out.println("ERROR : Contacts has not been added");
		return flag;
	}
	
	public static void populateInDb() throws FileNotFoundException
	{
		Connection con=null;

		try 
		{
			con=MyConnection.connectDB();
			String sql ="Create table contact_tbl (contactId  int primary key, contactName varchar(30), contactEmail varchar(40), contactList varchar(100))";
			Statement stmt=con.createStatement();
			stmt.executeUpdate("DROP TABLE contact_tbl");
			stmt.executeUpdate(sql);
			List<Contact> tempContact=new ArrayList<Contact>();
			readContactsFromFile(tempContact,"src/Contacts/TempContact");
			Iterator<Contact> iter=tempContact.iterator();
			PreparedStatement ps;
			while(iter.hasNext())
			{
				Contact tempCon=iter.next();
				String query = "INSERT INTO contact_tbl VALUES(?,?,?,?)";
				ps = con.prepareStatement(query);
				ps.setInt(1,tempCon.getContactID());
	        	ps.setString(2,tempCon.getContactName());
	        	ps.setString(3,tempCon.getEmailAddress());
	        	Iterator<String> iter2=tempCon.getContactNumber().iterator();
	        	String abc =new String();
	        	while(iter2.hasNext())
	        		abc=abc+","+iter2.next();
	        	abc=abc.substring(1);
	        	ps.setString(4,abc);
	        	ps.execute();
			}
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main (String args[]) throws ClassNotFoundException, FileNotFoundException
	{
		List<Contact> myList =new ArrayList<Contact>();
		
		//Adding a contact to the list
		Contact c1=new Contact();
		c1.setContactID(1001);
		c1.setContactName("Shantanu");
		c1.setEmailAddress("shantanuabc@gmail.com");
		List<String> List1= new ArrayList<>(Arrays.asList("81012","82506"));
		c1.setContactNumber(List1);
		System.out.println("1-> Adding a contact to the list :");
		addContact(c1,myList);
		
		//Removing a contact from the list
		System.out.println("2-> Removing a contact from the list :");
		removeContact(c1,myList);
		
		//Reading contacts from file
		System.out.println("3-> Reading contacts from file - Contact.txt :");
		readContactsFromFile(myList,"src/Contacts/Contact");
		
		//Searching a contact by name
		System.out.println("4-> Searching a contact by name - Rohan :");
		searchContactByName("Rohan",myList);
		
		//Searching a contact by number
		System.out.println("5-> Searching a contact by number - 234 :");
		searchContactByNumber("234",myList);
	
		//Adding a contact number to an existing contact
		System.out.println("6-> Adding a contact number to an existing contact - Rohan :");
		addContactNumber(1,"654321",myList);
		
		//Sorting contacts by name
		System.out.println("7-> Sorting contacts by Name :");
		sortContactsByName(myList);
		Iterator<Contact> it2=myList.iterator();
		while(it2.hasNext())
		{
			Contact c4 =it2.next();
			System.out.println(c4.toShowString());
		}
		
		//Serializing contact details
		System.out.println("8-> Serializing contact details in file - Contactser.ser :");
		serializeContactDetails(myList,"src/Contacts/Contactser.ser");
		
		//De-Serializing contact details
		System.out.println("9-> De-Serializing contact details from file - Contactser.ser :");
		List<Contact> list2 = deserializeContactDetails("src/Contacts/Contactser.ser");
		Iterator<Contact> it3=list2.iterator();
		//Showing values accepted.
		while(it3.hasNext())
		{
			Contact c5=(Contact) it3.next();
			System.out.println(c5.toShowString());
		}
		
		//Creating Database and Populating Contact to database
		System.out.println("10-> Creating Database and Populating Contact to database :");
		populateInDb();
				
		//Populating Contact from database to List
		System.out.println("11-> Populating Contact from database to List :");
		Set<Contact> set1=populateContactFromDb();
				
		//Adding new contacts fetched from database to the existing contact list
		System.out.println("12-> Adding new contacts fetched from database to the existing contact list :");
		addContacts(myList,set1);
		Iterator<Contact> it4=myList.iterator();
		while(it4.hasNext())
		{
			Contact c6 =it4.next();
			System.out.println(c6.toShowString());
		}
				
		System.out.println("End of Operations");
		
	}
	
}
