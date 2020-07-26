
package io.helidon.microprofile.examples.staticcontentgame;

import java.util.Collections;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import java.lang.Math;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/*
* Get winner's name (GET): curl -X GET http://localhost:8080/game/winner   
* Create a new user (POST): curl -d '{"username" : "qwerty"}' -H 'Content-Type: application/json' http://localhost:8080/game/user
* Enter the registered user's guess (PUT): 
* curl -H 'Content-Type: application/json' -X PUT -d '{"username" : "qwerty", "guess" :"10"}' http://localhost:8080/game
*/

@Path("/game")
@RequestScoped
public class GameTheoryResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    private static int sum = 0;
    private static int activeusers = 0;
    private static int attempts = 0;

    private static String Winner = "GameNotStartedYet";

    private static HashMap<String,Integer> UsedNames = new HashMap<>();
    private static HashMap<String,RegUser>  data = new HashMap<>();

    // API to display the winner's username
    @Path("/winner")
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner()
    {
        JsonObject jsonobject;

        if(activeusers == 0){
            jsonobject = JSON.createObjectBuilder()
                .add("Winner", "The game hasn't started yet!")
                .build();
        }

        else{
            jsonobject = JSON.createObjectBuilder()
                .add("Winner", Winner)
                .build();
        }

        return Response.status(Response.Status.ACCEPTED).entity(jsonobject).build();
    }


    //API for Handling a new user
    @Path("/user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response NewUser(JsonObject jsonobject)
    {

       // throw error if keys are missing
       if(!jsonobject.containsKey("username"))
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "Username missing")
                   .build();

            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       String username = jsonobject.getString("username").toString();

       // throw error if username is already taken
       if(UsedNames.containsKey(username) || username.isEmpty())
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "Sorry! This Username is taken or you didn't enter one")
                   .build();

            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       RegUser gamer = new RegUser();

       gamer.setUserName(username);

       UsedNames.put(username, 1);
       data.put(username, gamer);

       JsonObject entity = JSON.createObjectBuilder()
                       .add("success", "Your response was successfully recorded.")
                       .build();

        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }


    //API for handling a registered user's response
    @PUT 
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserGuess(JsonObject jsonobject)
    {

       // throw error if required keys are missing
       if(!jsonobject.containsKey("username"))
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "username missing")
                   .build();

            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       if(!jsonobject.containsKey("guess"))
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "guess missing")
                   .build();

           return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }


       // retrieve the username and numerical guess
       String username = jsonobject.getString("username").toString();
       int guess;
       
       try{
           guess = Integer.parseInt(jsonobject.getString("guess").toString());
       }
       catch(NumberFormatException e){
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "Enter a numerical guess.")
                   .build();

           return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       // throw error if username is not registered
       if(!UsedNames.containsKey(username))
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "Username does not exist!")
                   .build();

           return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       if(guess>100 || guess<0)
       {
           JsonObject entity = JSON.createObjectBuilder()
                   .add("error", "Guess is out of bounds. Please enter an integer between 0-100!")
                   .build();

           return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
       }

       RegUser gamer = data.get(username);

       data.remove(username);

       // check if it's the first time the user is guessing
       if(gamer.getGuess() == -1)
       {
           // increment the number of active users
           activeusers+=1;
           sum+=guess;
       }
       else sum+=(guess - gamer.getGuess());

       //increase the number of overall attempts
       attempts+=1;

       // change the user's guess
       gamer.setGuess(guess);
       gamer.setAttempt(attempts);

       data.put(username, gamer);

       // check if we have a new winner
       updateWinner();

       JsonObject entity = JSON.createObjectBuilder()
                       .add("success", "Your response was successfully recorded.")
                       .build();

        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }

    // check for any update in the winner after every user's guess
    private void updateWinner()
    {
        double mindifference = 105, twoThirdAvg = (2.0*((double)sum))/(3.0*((double)activeusers));
        int minIndex = attempts;
        
        // check for the absolute minimum difference
        for(RegUser gamer : data.values())
        {
            // Only consider the registered users who have responded
            if(gamer.getGuess()!=-1)
            {
                if(mindifference > Math.abs(twoThirdAvg - (double)(gamer.getGuess())))
                {
                    // update winner
                    mindifference = Math.abs(twoThirdAvg - (double)(gamer.getGuess()));
                }
            }
        }

        // Winner will be the one with the earliest attempt out of all the valid contenders
        for(RegUser gamer : data.values())
        {
            if(gamer.getGuess()!=-1)
            {
                if(mindifference == Math.abs(twoThirdAvg - (double)(gamer.getGuess())))
                {
                    if(minIndex>=gamer.getAttempt())
                    {
                        Winner = gamer.getUserName();
                        minIndex = gamer.getAttempt();
                    }
                }
            }
        }
    }
}
