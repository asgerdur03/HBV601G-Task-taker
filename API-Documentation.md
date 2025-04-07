# Task API documentation

`https://hugbo-1-api.onrender.com`

## Routes

| Method | Route         | Input | Returns                  | For                        |
|--------|---------------|:-----:|--------------------------|----------------------------| 
| GET    | `/admin`      |   -   | `Map.of("users", users)` | Testing purposes           | 
| POST   | `/signup`     |       |                          |                            |
| POST   | `/login`      |       |                          |                            |
| PATCH  | `/update`     |       |                          | Update email or password   |
| POST   | `/upload-pic` |       |                          | Uploads a profile picture  |
| DELETE | `/delete/me`  |   -   |                          | Deletes the logged in user |
|        |               |       |                          |                            |

# From Backend File

## 👤 User Routes

- [x] ``POST /login`` — Log in user and return a JWT token
- [x] ``POST /signup`` — Register a new user with username, email, and password
- [x] ``PATCH /update`` — Update current user’s username, email, or password
- [x] ``POST /upload-pic`` — Upload and update user’s profile picture (multipart/form-data, key = `image`)
- [x] ``DELETE /delete/me`` — Delete the currently authenticated user
- [x] ``GET /admin`` — Return all users (open for now, intended for admin use)


## ✅ Task Routes

- [x] ``GET /tasks`` — Get all tasks for the logged-in user (optionally filter with query params like `?favorite=true`)
- [x] ``GET /tasks/{id}`` — Get a specific task by its ID
- [x] ``POST /tasks`` — Create a new task (currently returns a placeholder)
- [x] ``PATCH /tasks/{id}`` — Update a task by ID (currently returns a placeholder)
- [x] ``DELETE /tasks/{id}`` — Delete a task by ID (currently returns a placeholder)

#### ⭐ Favorite-related

- [x] ``PUT /tasks/{id}/favorite`` — Mark a task as favorite
- [x] ``PUT /tasks/{id}/unfavorite`` — Unmark a task as favorite

#### 🗃 Archive-related

- [x] ``GET /tasks/archives`` — Get all archived tasks
- [x] ``PUT /tasks/{id}/archive`` — Archive a task
- [x] ``PUT /tasks/{id}/unarchive`` — Unarchive a task

---

## 🗂 Category Routes

- [x] ``GET /categories`` — Get all categories for the logged-in user
- [x] ``GET /categories/{id}`` — Get a specific category by ID
- [x] ``POST /categories`` — Create a new category
- [x] ``PATCH /categories/{id}`` — Update a category by ID (returns message placeholder)
- [x] ``DELETE /categories/{id}`` — Delete a category by ID 



