package Contacts;
import java.util.Comparator;
public class NameSorter implements Comparator<Contact> 
{
    @Override
    public int compare(Contact o1, Contact o2) 
    {
    	int i = o1.getContactName().compareTo(o2.getContactName());
        return i;
    }
}
