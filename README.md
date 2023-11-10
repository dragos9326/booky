# Getting Started

### Reference Documentation
We have the following notions:
* Users
* Roles
* Bookings
* Property
* Block

In data.sql we insert initial data:
* User1 which has the roles: NORMAL
* User2 which has the roles: NORMAL, OWNER
* User3 which has the roles: NORMAL, OWNER, ADMIN
* Properties 1,2 which are owned by User1
* Property 3 which is owned by User2

Users with ADMIN role can update/create/delete everything.
Users with OWNER role can update/create/delete their bookings, or their properties bookings
Users with NORMAL role can update/create/delete their own bookings.

TODO:
* To add a mechanism to accept/deny bookings by owner
* To add a mechanism to not allow owner to modify booking without user accept
* To integrate with an Authorization server
* Add some pagination for bookings as it could be a lot, especially on "get all" endpoints
* Manage authentication. Integrate with organization authentication methods.
* Improve testing coverage
* Change Password Encode to something else
* Add some controller validations

OpenAPI definitions can be found at: [here](http://localhost:8080/swagger-ui/index.html)