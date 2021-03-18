package Movie;
import java.util.Date;
import java.util.List;

public class Movies implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	private int movieId;
	private String movieName; 
	private String movieType;
	private String language; 
	private Date releaseDate; 
	private List<String> casting;
	private Double rating;
	private Double totalBusinessDone;
	
	public int getMovieId() 
	{
		return movieId;
	}
	public void setMovieId(int movieId) 
	{
		this.movieId = movieId;
	}
	public String getMovieName() 
	{
		return movieName;
	}
	public void setMovieName(String movieName) 
	{
		this.movieName = movieName;
	}
	public String getMovieType() 
	{
		return movieType;
	}
	public void setMovieType(String movieType) 
	{
		this.movieType = movieType;
	}
	public String getLanguage() 
	{
		return language;
	}
	public void setLanguage(String language) 
	{
		this.language = language;
	}
	public Date getReleaseDate() 
	{
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) 
	{
		this.releaseDate = releaseDate;
	}
	public List<String> getCasting() 
	{
		return casting;
	}
	public void setCasting(List<String> casting) 
	{
		this.casting = casting;
	}
	public double getRating() 
	{
		return rating;
	}
	public void setRating(double rating) 
	{
		this.rating = rating;
	}
	public double getTotalBusinessDone() 
	{
		return totalBusinessDone;
	}
	public void setTotalBusinessDone(double totalBusinessDone) 
	{
		this.totalBusinessDone = totalBusinessDone;
	}
	public String toShowString()
	{
		return movieId+" "+movieName+" "+language+" "+releaseDate+" "+casting+" "+rating+" "+totalBusinessDone;
	}

}