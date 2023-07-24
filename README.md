# Blogging Platform
---

## Spring Boot Application
---

### Summary
There are 5 documents in this project
- Author
- Post
- Comment
- Role
- Confirmation Token
- Notification

There are 2 roles among authors
- Admin
- User

### Requirements
- Each author must have a unique username and email address.
- Author's essential fields are first name, last name, username, password and email address. Other fields are optional.
- Each author must activate their account with the activation link sent to their email address within 15 minutes in order to register on the platform.
- If author does not verify the account, author may try to register again with the same credentials.
- Each post must have title, text and category.
- Each comment must belongs to a specific post.
- Each author has an User role.
- Authors may follow or unfollow each other.
- Followers may receive a notification when the author creates a new post.
- Authors may receive notifications when comments are posted on their posts.

### Tech Stack
- Java 18
- Spring Boot
- Spring Security
- Spring Email
- Kafka
- Spring Data MongoDB
- MongoDB
- Docker
- Hateoas
- OpenAPI Documentation

### Prerequisties
---
- Maven
- Docker

### Run & Build
---

In order to pull the maildev/maildev image from the Dockerhub, you should run the below command

`$ docker pull maildev/maildev`

Then, you should run the below commands in order to run the application

1) Run the container for maildev/maildev
2) Build the application
3) Execute the application

```
$ docker run -p 1080:1080 -p 1025:1025 maildev/maildev
$ mvn clean install
$ mvn spring-boot:run
```
### MailDev Page
---

You may use MailDev page to access the incoming activation mails.<br/>
You may use one of these urls to access MailDev Page.<br/>
```
http://0.0.0.0:1080
http://127.0.0.1:1080
```

### Api Documentation
---

You may use swagger-ui with the port of the application to access the project's api documentation.<br/>
`http://localhost:${PORT}/swagger-ui.html`

