package cmd;

public class TenkaichiChar 
{
	//data fields
	int DP = 0;
	String name;
	
	public String getName() 
	{ 
		return name; 
	}
	
    public int getDP() 
    { 
    	return DP; 
    }

    public TenkaichiChar(String name, int DP) 
    {
        this.DP = DP;
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("%-40s %d", this.name, this.DP);
    }

}
