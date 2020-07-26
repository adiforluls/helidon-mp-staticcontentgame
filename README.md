# Helidon MP Example

Helidon MP example to demonstrate static content support.

# Prerequisites:
   ``` 
    • Java SE 11+ (Open JDK 11) or newer
    • Maven 3.6.1+
    • Docker 18.09+
    • Operating System: Linux 
  ```

## Build and run

With JDK11+
```bash
mvn package
java -jar target/helidon-examples-microprofile-static-content.jar
```

## Exercise the application

```
Through CLI:
    • Get winner's name (GET): curl -X GET http://localhost:8080/game/winner   
    • Create a new user (POST): curl -d '{"username" : "qwerty"}' -H 'Content-Type: application/json' http://localhost:8080/game/user
    • Enter the registered user's guess (PUT): 
    • curl -H 'Content-Type: application/json' -X PUT -d '{"username" : "qwerty", "guess" :"10"}' http://localhost:8080/game
    • Note: guess is an integer string.

UI: http://localhost:8080/index.html
	
```



