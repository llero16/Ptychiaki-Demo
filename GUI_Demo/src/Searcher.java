
public abstract class Searcher
{
	String indexDirectory = "indexed\\uDataIndex";
	String queryString;
	
	public Searcher(String queryString)
	{
		this.queryString = queryString;
	}
	

}
