# Create an application response wrapper.

## What is it?
It's just a POJO (a record) acting as an immutable DTO.

## The propouse?
Force application responses to always follow the same structure: http code, message, and data (any type).

So:

```
public record ApplicationResponse<T>(
  int code,
  String message,
  T data
){}
```

## Why a record instead of a class?
The data is immutable: we don't need to change the final response data, so, no need of a class here.

We return from our API an ApplicationResponse object as the body of an Spring ResponseEntity object, so:

```
ResponseEntity.status(HttpStatus.OK).body(applicationResponse);
```

I decided to follow this pattern since nowadays it is commonly used to communicate your Backend with Frontend teams in a easier and more structured way, til the point they know exactly what are you going to return as response. 


### Why include http code in the response wrapper if response entity already includes it?
When communicating with clients, many of them preffer to access this info in your own application response data than in the http response attributes.
Also, working with GraphQL, it's harder to read this info from the http response.


### Why I would include a message like "User created successfully" for create user endpoint if returning 201 code means the user was created?
Yes, but this is flexible, and it is there to help you with other cases like: "User {name} created successuflly using default profile image".
Also, again, if you are going to expose your API for public clients, it's good practice to always bring the neccessary details on what is happening to the consumers since relaying in just an http code can be anbiguous sometimes.

### So, in short, how the response structure looks like on the client side?

While creating a book resource, for example, we passed from this:

```
{
  "id": "1",
  "name": "Java Learning",
  "genre": "Programming"
}
```

To this:

```
{
  "code": 201,
  "message": "Book 'Java Learning' created successfully",
  "data": {
    "id": "1",
    "name": "Java Learning",
    "genre": "Programming"
  }
}
```

### But then, why did I decide to change (again) the structure (the "data" response field) from this:

```
"data": {
  ...,
  ...
}
```

to this:

```
"data": {
  "book": {
    "id": "1",
    "name": "Java Learning",
    "genre": "Programming"
  }
}
```

This is not needed when your API is going to return specifically always a type of data (books, in this case). However, if the API could return different types like users, authors, books, genres, editorials, etc, then it's good practice to specify even further what we are sending to the client.
So, this would look like this:

```
{
  "code": 201,
  "message": "Book 'Java Learning' created successfully",
  "data": {
    "book": {
      "id": "1",
      "name": "Java Learning",
      "genre": "Programming"
    }
  }
}
```

But it could look like this too (bringing and enpoint to client to fetch more required data in just one request, reducing server requests (and costs)):

```
{
  "code": 200,
  "message": "Best and worst rating editorials data and books fetched successfully",
  "data": {
    "bestRatingEditorial": {
      "id": "1",
      "name": "The book editorial",
      "location": "Somewhere",
      "books": [
        {
          "id": "34",
          "name": "Dracula",
          "genre": "horror"
        },
        {
          "id": "181",
          "name": "Java to the Moon!",
          "genre": "Programming"
        }
      ]
    },
    "worstRatingEditorial": {
      "id": "304",
      "name": "A bad rating editorial",
      "location": "Somewhere else",
      "books": [
        {
          "id": "1",
          "name": "The magic book!",
          "genre": "Fantasy"
        },
        {
          "id": "3",
          "name": "Recipes",
          "genre": "Cooking"
        }
      ]
    },
  }
}
```

### And how can we achieve this?

With a simple ```Map<String, Object>``` as the application response wrapper data field.

