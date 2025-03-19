# Spring Security with Custom JWT
Template with custom JWT authentication implementation
___
# Dependencies
- Spring Boot, Spring Web, Spring Security
- JWT library: https://github.com/jwtk/jjwt
___
`/` and `/login` endpoints are available for everyone

`/customer` is available only authenticated users
___
## Implementation Details

The authentication is implemented in `JwtAuthenticationFilter.java`.
We try to receive JWT from `Authorization` request header.
If token is present we validate it and to authorize user

In `SecurityChainConfiguration.java` we configure main filter chain and:
- Make session management stateless: 
`http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)`
- Add `JwtAuthenticationFilter.java` filter to filter chain: 
`http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);`


