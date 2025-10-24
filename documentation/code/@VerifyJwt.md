This annotation is made to make it easier to handle JWT.
# Why JWT
I added JWT to my API for two reasons: security and maintainability.
## Security:
JWT is an easy way to limit people from calling your API. You can easily add a JWT Token to your API call, by adding it to the header and authorization, as such:
```ts
fetch("https://example.org/get", {
  method: "GET",
  headers: {
    Authorization: bearer {JWT-Token-Here}
  }
});
```
Validating your JWT token like I have on the backend, will require methods to have a JWT Token (which is valid). If they don't have it, return a 401, if they do, continue the code.
## Maintainability:
The reason I made this a annotation is because of maintainability. The reason I believe this falls under it is because you counteract code duplication, you don't have to paste the same code in every method to validate the JWT.
In addition, once a change needs to be made to the JWT Validation, you can just update the annotation, and it's in use everywhere, other than having to change the code in every method.

# How to use:
Using the annotation is quite easy. You just need a class which acts as an API route. Then you create a method, and above that method you put @VerifyJwt, and the other required annotations by your framework (in my case, one to determine the request type and path).
```java
@Path("/api/users")
public class UserAPI {

    @GET
    @VerifyJwt  
    @Path("/getAll")  
    public String getAllUsers() {  
        return new Result<>(StatusCodes.OK, User.listAll()).toJson();  
    }
}
```
