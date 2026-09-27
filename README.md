# 🎓 Smart Internship Platform

A full-stack, AI-powered internship recommendation and management web application built with **Spring Boot 3 (Java 21)**, **Spring Security**, **JWT Authentication**, **MySQL 8.0**, and **HTML5/CSS3/JavaScript**.

---

## 🏗️ Architecture Layers

```
                     SMART INTERNSHIP PLATFORM
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
              FRONTEND                    BACKEND
           HTML5 / CSS / JS             Spring Boot 3
                 │                           │
                 │                    ┌──────┴──────┐
                 │                    │             │
                 │                REST APIs      Security
                 │                    │          JWT + BCrypt
                 │                    │             │
                 │               Controller         │
                 │                    ↓             │
                 │                 Service          │
                 │                    ↓             │
                 │                Repository        │
                 │                    ↓             │
                 └──────────────→   MySQL  ←────────┘
```

---

## 🚀 Key Features

1. **User Management & Profiles**
   - Registration with role selection (`ROLE_STUDENT` or `ROLE_ADMIN`).
   - Profile management with university, degree, graduation year, and bio.
   - Interactive skill management (add, delete, and view tags).

2. **Security & Authentication**
   - Stateless JWT authentication via `Bearer <token>`.
   - Strong password hashing with `BCryptPasswordEncoder`.
   - Role-based authorization (`@PreAuthorize("hasRole('ADMIN')")`).

3. **Internship Management (Admin Portal)**
   - Create, edit, and delete internships.
   - Company name, position title, location, application deadline, description, and required skill tags.

4. **Skill Matching Engine**
   - Matches student skills with internship requirements using case-insensitive normalization.
   - Formula:
     $$\text{Match \%} = \left(\frac{|\text{Student Skills} \cap \text{Internship Required Skills}|}{|\text{Internship Required Skills}|}\right) \times 100$$
   - Highlights **Matched Skills** (green) and identifies **Skills to Learn** (orange roadmap).

5. **Student Dashboard**
   - Dynamic profile updater.
   - Instant re-calculation of match percentages upon adding or removing skills.
   - Filtering by High (≥ 70%), Medium (40–69%), and Upskilling needed (< 40%).

---

## ⚡ Default Accounts (Pre-seeded)

| Role | Email | Password |
|---|---|---|
| **Student** | `student@platform.com` | `student123` |
| **Admin** | `admin@platform.com` | `admin123` |

> *Quick 1-Click demo login buttons are provided on the login page.*

---

## 🌐 Running & Accessing the Application

1. **Backend & Frontend Server**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
2. **Open in Browser**:
   - Web App URL: **`http://localhost:8085`**
   - Student Dashboard: **`http://localhost:8085/dashboard.html`**
   - Admin Portal: **`http://localhost:8085/admin.html`**
   - Login Page: **`http://localhost:8085/login.html`**

---

## 📡 API Reference

### Authentication
- `POST /api/auth/register` - Create student or admin account
- `POST /api/auth/login` - Authenticate & obtain JWT token

### Student & Profile
- `GET /api/student/profile` - Fetch current student profile & skills
- `PUT /api/student/profile` - Update degree, university, bio, and skills

### Matching Engine
- `GET /api/matching/dashboard` - Get internships ranked by student skill match percentage

### Internships
- `GET /api/internships` - Public list of all available internships
- `GET /api/internships/{id}` - Single internship details

### Admin (Protected: `ROLE_ADMIN`)
- `POST /api/admin/internships` - Create new internship
- `PUT /api/admin/internships/{id}` - Edit existing internship
- `DELETE /api/admin/internships/{id}` - Delete internship
