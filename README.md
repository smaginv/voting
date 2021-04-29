REST API Voting System
===============================

Task:

---

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
   - If it is before 11:00 we assume that he changed his mind.
   - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.


---
Application launch:
1. `git clone https://github.com/smaginv/voting.git`
2. `cd voting`
3. `mvn clean package install spring-boot:run`

---

**Swagger/OpenAPI Specification:**  
http://localhost:8080/swagger-ui.html  
http://localhost:8080/v3/api-docs  

---

**REST API**

##Administrator

Administrator controller:

| Method | URL                       | Description       |
| ------ | ------------------------- | ----------------- |
| GET    | /api/admin/accounts/{id}  | Get user by id    |
| PUT    | /api/admin/accounts/{id}  | Update user by id |
| DELETE | /api/admin/accounts/{id}  | Delete user by id |
| GET    | /api/admin/accounts       | Get all users     |
| GET    | /api/admin/accounts/email | Get user by email |
| POST   | /api/admin/accounts       | Create user       |

GET (user by id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/admin/accounts/1' --user admin@mail.ru:admin`


PUT Curl:  
`curl -X 'PUT' 'http://localhost:8080/api/admin/accounts/2' -H 'Content-Type: application/json' -d '{"email": "update@mail.ru", "password": "pass", "name": "update"}' --user admin@mail.ru:admin`


DELETE Curl:  
`curl -X 'DELETE' 'http://localhost:8080/api/admin/accounts/2' --user admin@mail.ru:admin`


GET (all users) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/admin/accounts' --user admin@mail.ru:admin`


GET (user by email) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/admin/accounts/email?email=user@mail.ru' --user admin@mail.ru:admin`


POST Curl:  
`curl -X 'POST' 'http://localhost:8080/api/admin/accounts' -H 'Content-Type: application/json' -d '{"email": "new@mail.ru", "password": "pass", "name": "new user"}' --user admin@mail.ru:admin`

---

Dish Controller:


| Method | URL                                            | Description                             |
| ------ | ---------------------------------------------- | --------------------------------------- |
| GET    | /api/restaurants/{restaurantId}/dishes/{id}    | Get dish by id and restaurant id        |
| PUT    | /api/restaurants/{restaurantId}/dishes/{id}    | Update dish for restaurant              |
| DELETE | /api/restaurants/{restaurantId}/dishes/{id}    | Delete dishes by id and restaurant id   |
| GET    | /api/restaurants/{restaurantId}/dishes         | Get all dishes by restaurant id         |
| POST   | /api/restaurants/{restaurantId}/dishes         | Create dish for restaurant              |
| GET    | /api/restaurants/{restaurantId}/dishes/today   | Get all dishes today by restaurant id   |
| GET    | /api/restaurants/{restaurantId}/dishes/on-date | Get all dishes on date by restaurant id |


GET (dish by id and restaurant id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/1/dishes/1' --user admin@mail.ru:admin`

PUT Curl:  
`curl -X 'PUT' 'http://localhost:8080/api/restaurants/1/dishes/1' -H 'Content-Type: application/json' -d '{"name": "update", "price": 10, "date": "2021-04-27"}' --user admin@mail.ru:admin`

DELETE Curl:  
`curl -X 'DELETE' 'http://localhost:8080/api/restaurants/1/dishes/5' --user admin@mail.ru:admin`

GET (all dishes by restaurant id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/1/dishes' --user admin@mail.ru:admin`

POST Curl:  
`curl -X 'POST' 'http://localhost:8080/api/restaurants/1/dishes' -H 'Content-Type: application/json' -d '{"name": "new dish", "price": 11, "date": "2021-04-27"}' --user admin@mail.ru:admin`

GET (all dishes today by restaurant id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/5/dishes/today' --user admin@mail.ru:admin`

GET (all dishes on date by restaurant id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/1/dishes/on-date?date=2021-04-11' --user admin@mail.ru:admin`

---

Restaurant Controller:


| Method | URL                                | Description                            |
| ------ | ---------------------------------- | -------------------------------------- |
| GET    | /api/restaurants/{id}              | Get restaurant by id                   |
| PUT    | /api/restaurants/{id}              | Update restaurant by id                |
| DELETE | /api/restaurants/{id}              | Delete restaurant by id                |
| GET    | /api/restaurants                   | Get all restaurants                    |
| POST   | /api/restaurants                   | Create restaurant                      |
| GET    | /api/restaurants/{id}/menu-today   | Get restaurant by id with today menu   |
| GET    | /api/restaurants/{id}/menu-on-date | Get restaurant by id with menu on date |
| GET    | /api/restaurants/menu-today        | Get all restaurants with today menu    |
| GET    | /api/restaurants/menu-on-date      | Get all restaurants with menu on date  |

GET (restaurant by id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/3' --user admin@mail.ru:admin`

PUT Curl:  
`curl -X 'PUT' 'http://localhost:8080/api/restaurants/4' -H 'Content-Type: application/json' -d '{"name": "update restaurant"}' --user admin@mail.ru:admin`

DELETE Curl:  
`curl -X 'DELETE' 'http://localhost:8080/api/restaurants/5' --user admin@mail.ru:admin`

GET (all restaurants) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants' --user admin@mail.ru:admin`

POST Curl:  
`curl -X 'POST' 'http://localhost:8080/api/restaurants' -H 'Content-Type: application/json' -d '{"name": "new restaurant"}' --user admin@mail.ru:admin`

GET (restaurant by id with today menu) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/5/menu-today' --user admin@mail.ru:admin`

GET (restaurant by id with menu on date) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/1/menu-on-date?date=2021-04-11' --user admin@mail.ru:admin`

GET (all restaurants with today menu) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/menu-today' --user admin@mail.ru:admin`

GET (all restaurants with menu on date) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/menu-on-date?date=2021-04-11' --user admin@mail.ru:admin`

---

Vote Controller:



| Method | URL                               | Description                      |
| ------ | --------------------------------- | -------------------------------- |
| GET    | /api/votes/{id}                   | Get vote by id                   |
| DELETE | /api/votes/{id}                   | Delete vote by id                |
| GET    | /api/votes/users/{userId}/today   | Get user's vote today by user id |
| GET    | /api/votes/users/{userId}/on-date | Get user vote by user id on date |
| GET    | /api/votes/users/{userId}/all     | Get user votes by user id        |
| GET    | /api/votes/today                  | Get all today votes              |
| GET    | /api/votes/on-date                | Get all votes on date            |

GET (vote by id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/3' --user admin@mail.ru:admin`

DELETE Curl:  
`curl -X 'DELETE' 'http://localhost:8080/api/votes/2' --user admin@mail.ru:admin`

GET (user's vote today by user id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/users/1/today' --user admin@mail.ru:admin`

GET (user vote by user id on date) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/users/2/on-date?date=2021-04-12' --user admin@mail.ru:admin`

GET (user votes by user id) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/users/2/all' --user admin@mail.ru:admin`

GET (all today votes) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/today' --user admin@mail.ru:admin`

GET (all votes on date) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/votes/on-date?date=2021-04-11' --user admin@mail.ru:admin`

---

## User

Account controller:


| Method | URL          | Description         |
| ------ | ------------ | ------------------- |
| GET    | /api/account | Get your details    |
| PUT    | /api/account | Update your details |
| DELETE | /api/account | Delete your account |


GET Curl:  
`curl -X 'GET' 'http://localhost:8080/api/account' --user user@mail.ru:user`

PUT Curl:  
`curl -X 'PUT' 'http://localhost:8080/api/account' -H 'Content-Type: application/json' -d '{"email": "update@mail.ru", "password": "pass", "name": "update"}' --user user@mail.ru:user`

DELETE Curl:  
`curl -X 'DELETE' 'http://localhost:8080/api/account' --user user@mail.ru:user`

---

Vote controller:


| Method | URL        | Description                |
| ------ | ---------- | -------------------------- |
| POST   | /api/votes | To vote, only before 11:00 |

POST Curl:  
`curl -X 'POST' 'http://localhost:8080/api/votes?restaurantId=3' --user user@mail.ru:user`

---

Restaurant Controller:



| Method | URL                           | Description                           |
| ------ | ----------------------------- | ------------------------------------- |
| GET    | /api/restaurants/menu-today   | Get all restaurants with today menu   |
| GET    | /api/restaurants/menu-on-date | Get all restaurants with menu on date |

GET (all restaurants with today menu) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/menu-today' --user user@mail.ru:user`

GET (all restaurants with menu on date) Curl:  
`curl -X 'GET' 'http://localhost:8080/api/restaurants/menu-on-date?date=2021-04-11' --user user@mail.ru:user`

---

## Anonymous

Account controller:


| Column 1 | Column 2              | Column 3      |
| -------- | --------------------- | ------------- |
| POST     | /api/account/register | Register user |

POST Curl:  
`curl -X 'POST' 'http://localhost:8080/api/account/register' -H 'Content-Type: application/json' -d '{"email": "register@mail.ru", "password": "pass", "name": "register"}'`