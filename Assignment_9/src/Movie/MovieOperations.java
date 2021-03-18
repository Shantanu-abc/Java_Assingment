package Movie;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
public class MovieOperations 
{
	public static List<Movies> populateMovies(File file) throws FileNotFoundException, ParseException
	{
		  List<Movies> list=new ArrayList<Movies>();	
		  Scanner sc=new Scanner(file);
		  DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		  String text[] = null;						
			while(sc.hasNextLine())
			{
				Movies m=new Movies();
				text=sc.nextLine().split(",");
				m.setMovieId(Integer.valueOf(text[0]));
				m.setMovieName(text[1]);
				m.setMovieType(text[2]);
				m.setLanguage(text[3]);
				m.setReleaseDate(df.parse(text[4]));
				m.setCasting(new ArrayList<>(Arrays.asList(text[5].split("-"))));
				m.setRating(Double.valueOf(text[6]));
				m.setTotalBusinessDone(Double.valueOf(text[7]));
				list.add(m);	
			}	
			sc.close();
			return list;
	}
	public static void getMoviesRealeasedInYear(List<Movies> m,int year)
	{
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		Iterator<Movies> it=m.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			String dt[]=df.format(e.getReleaseDate()).split("/");
			int y=Integer.valueOf(dt[2]);		
			if(year==y)
			{				
				System.out.println(e.toShowString());
			}	
		}
		
	}
	public static void getMoviesByActor(List<Movies> m,String... actorNames)
	{
		Iterator<Movies> it=m.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			int count=0;
			for(String actor : actorNames)
			{
				if(e.getCasting().contains(actor))
					count=1;
				else
					count=0;
			}
			if(count==1)
			    System.out.println(e.toShowString());
		}
	}
	public static void addMovie(List<Movies> movies,Movies movie)
	{
		int s=movies.size();
		movies.add(movie);
		if(s<movies.size())
			System.out.println(movie.toShowString());
		else
			System.out.println("Data has not added");
		
		
	}	
	public static void updateRatings(String movieName, double rating ,List<Movies> movies)
	{		
		Iterator<Movies> it=movies.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			if(e.getMovieName().equals(movieName))
			{
				e.setRating(rating);
				System.out.println("Rating Updated");
				System.out.println(e.toShowString());
			}
		}		
	}
	public static void updateBusiness(String movieName, double amount,List<Movies> movies)
	{
		Iterator<Movies> it=movies.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			if(e.getMovieName().equals(movieName))
			{
				e.setTotalBusinessDone(amount);
				System.out.println("TotalBusinessCost Updated");
				System.out.println(e.toShowString());
			}
		}	
	}
	public static Set<Movies> businessDone(List<Movies> m,double amount)
	{
		Set<Movies> s=new HashSet<>();
		Iterator<Movies> it=m.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			if(e.getTotalBusinessDone()>amount)
			    s.add(e);
		}
		return s;
		
	}
	
	public static boolean allAllMoviesInDb(List<Movies> movies)
	{
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String driverClass = "oracle.jdbc.driver.OracleDriver";
		String user = "SYSTEM";
		String pswd = "Shantanu456";
		Connection con;
		boolean flag=false;
		try 
		{
			Class.forName(driverClass);
			con= DriverManager.getConnection(url,user,pswd);
			Statement stmt=con.createStatement();
			stmt.executeUpdate("DROP TABLE movies");
			stmt.executeUpdate("create table movies(MovieId int primary key, MovieName varchar(50), MovieType varchar(30), Language varchar(20), ReleaseDate date, Casting varchar(200), Rating decimal(4,2), TotalBusiness decimal(10,2))");			
            Iterator<Movies> i=movies.iterator();
            while(i.hasNext())
            {
            	Movies m=(Movies) i.next();
            	String query = "INSERT INTO movies VALUES(?,?,?,?,?,?,?,?)";
            	PreparedStatement ps = con.prepareStatement(query);
            	ps.setInt(1, m.getMovieId());
            	ps.setString(2, m.getMovieName());
            	ps.setString(3, m.getMovieType());
            	ps.setString(4, m.getLanguage());
            	
            	java.sql.Date sqlDate = new java.sql.Date(m.getReleaseDate().getTime());
            	String casting=new String();
            	List<String> casting1=m.getCasting();
            	Iterator<String> casting2=casting1.iterator();
                while(casting2.hasNext())
                	casting=casting+"-"+casting2.next();
                
                ps.setDate(5,sqlDate);
            	ps.setString(6, casting);
            	ps.setDouble(7, m.getRating());
            	ps.setDouble(8, m.getTotalBusinessDone());
            	ps.execute();
            	ps.close();
            	flag=true;
            }
		}
		catch (SQLException e) 
		{
			//handles exception
			e.printStackTrace();
		}	
		catch (ClassNotFoundException e) 
		{
			//handles exception
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			//handles exception
			e.printStackTrace();
		}
		return flag;
		
	}
	
    public static void main(String[] args) throws FileNotFoundException, ParseException 
    {
    	DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		File file=new File("src/Movie/MovieDetails");
		
		//Populating Data from MovieDetails.
		List<Movies> list=new ArrayList<Movies>();
		list=populateMovies(file);
		Iterator<Movies> it=list.iterator();
		while(it.hasNext())
		{
			Movies e=(Movies) it.next();
			System.out.println(e.toShowString());
		}
		
		
		//Showing movies released in a particular year.
		System.out.println("1-> Movies Released in the year 2012 :");
		getMoviesRealeasedInYear(list,2012);
		
		//Showing movies having a selected cast.
		System.out.println("2-> Movies having cast: Anupam Kher ->");
		getMoviesByActor(list, "Anupam Kher");
		
		//Showing set of movies whose business is more then the specified amount.
		Set<Movies> s=new HashSet<>();
		s=businessDone(list,1000.0);
		it=s.iterator();
		System.out.println("3-> Movies whose business is more than 1000.0:");
		while(it.hasNext())
		{
			Movies m1=(Movies) it.next();
			System.out.println(m1.toShowString());
		}
		
		//Updating Ratings of movie - Avengers :
		System.out.println("4-> Updating Ratings for Avengers:");
		updateRatings("Avengers",8.5, list);
		
		//Updating Business done of movie - Avengers :
		System.out.println("5-> Updating Business Done for Avengers:");
		updateBusiness("Avengers",15000.0, list);
		
		//adding a new movie to the list.
		System.out.println("6-> Adding a new movie - Baby.");
		Movies m=new Movies();		
		m.setMovieId(11);
		m.setMovieName("Baby");
		m.setMovieType("AGrade");
		m.setLanguage("Hindi");
		m.setReleaseDate(df.parse("10/10/2014"));
		m.setCasting(new ArrayList<>(Arrays.asList("Akshay Kumar-Anupam Kher".split("-"))));
		m.setRating(Double.valueOf("7.5"));
		m.setTotalBusinessDone(Double.valueOf("1000"));		
		addMovie(list,m);
		
		//Storing all movie details into database.
		boolean f=allAllMoviesInDb(list);
		if(f)
			System.out.println("7-> Movies stored in database.");
		else
			System.out.println("ERROR : Movies cannot be stored in database.");
		
		//Serializing movie data in the provided file : src/Movie/Movieser.ser - in the given project directory.
		System.out.println("8-> Saving Serialized Data.");
		serializeMovies(list,"src/Movie/Movieser.ser");
		
		//Deserializing movies from : "src/Movie/Movieser.ser".
		System.out.println("9-> Getting Serialized Data.");
		List<Movies> list2 = deserializeMovie("src/Movie/Movieser.ser");
		Iterator<Movies> it2=list2.iterator();
		//Showing values accepted.
		while(it2.hasNext())
		{
			Movies m2=(Movies) it2.next();
			System.out.println(m2.toShowString());
		}
		
		System.out.println("End of Operations");
		
	}
    
    public static void serializeMovies(List<Movies> movies,String fileName) 
    {
			try 
			{
				FileOutputStream fileOut = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(movies);
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
	public static List<Movies> deserializeMovie(String fileName)
    {
    	    List<Movies> list=new ArrayList<Movies>();
			try 
			{
				 FileInputStream fileIn = new FileInputStream(fileName);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         list=(List<Movies>) in.readObject();
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
 }
