# Task Management API

---

## Tech Stack

- **Java**: JDK 17
- **Framework**: Spring Boot
- **Database**: MySQL
- **Deployment**: AWS EC2 Instance

---
## Relationship, Entity & Enum Details

### Enums: 
 - `TaskPriority(HIGH, MEDIUM, LOW)`
 - `TaskStatus(TODO, IN_PROGRESS, DONE)`

### Default Fields Used In Entities: `id, createdBy, modifyBy, createdAt, modifiedAt, enable` used as per neended!
### Roles:
- `id & name`

### User:
- `default fields, email, name, roles (M-M), {team(1) -> user(M)}`

### Team:
- `default fields, name, description, {team(1) -> user(M)}`

### Task:
- `default fields, name, dueDate, title, description, status(Enum: TODO, IN_PROGRESS, DONE), priority(Enum: LOW, MEDIUM, HIGH), {task(M) -> user(1)}, {task(M) -> team(1)}`

### Comment:
- `default fields, commentText, timestamp, {task(1) -> comments(M)}`

---

### 1. **Clone the repository**
 -  git clone https://github.com/technical-mindset/Task-Management.git
### 2. **Maven Command**
- mvn clean install TaskManagementDB
- Then run the project in IntelliJ with `TaskManagementApi` class set in configuration

## API Details

### 1. User
- POST /users – Add a user `In this API you post the body of user for addition`
- GET /users – List all users `Retrieving all the users`

### 2. Team
- POST /teams – Create a team `Creating a team with name and their description`
- GET /teams – List teams `Retreiving the List of teams with respected members`
- POST /teams/{teamId}/members – Add user to team `This end point is for adding members(user's id in a list form) to respected team (team id) `

### 3. Task
- POST /tasks – Create task and assign to user `Create task and assigning to member (user's id)`
- GET /tasks – List tasks `Fetch the all the task with support of 'status', 'memeber' (user id), and 'team' (team id) filters with respect to pagination`
- PUT /tasks/{id} – Update task `Updating the task`
- PUT /tasks/{id}/status – Change task status `Changes the status passing into request params (TODO, IN_PROGRESS, DONE) with respect to Task`
- GET /users/{id}/tasks – List tasks assigned to user `Fetched all the tasks against the user`

### 4. Comment
- GET /users/{id}/tasks – List tasks assigned to user `Create a commnet against a task`
- GET /tasks/{taskId}/comments `List down all the comments of specific task`

### 5. User
- GET /roles – List roles `Retrieving all roles in role's table`
---
