
package io.helidon.microprofile.examples.staticcontentgame;

public class RegUser {
	
    private String UserName;
    private int guess = -1;
    private int AttemptIndex = 0;
    
    public void setUserName(String username)
    {
    	this.UserName = username;
    }
    
    public void setGuess(int guess)
    {
    	this.guess = guess;
    }
    
    public void setAttempt(int attempt)
    {
        this.AttemptIndex = attempt;
    }

    public String getUserName()
    {
    	return this.UserName;
    }

    public int getAttempt()
    {
        return this.AttemptIndex;
    }

    public int getGuess()
    {
    	return this.guess;
    }
}
