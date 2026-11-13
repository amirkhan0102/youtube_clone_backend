# YouTube Clone — Backend

REST API backend for a video-sharing platform inspired by YouTube: channels, videos, playlists, subscriptions, comments, likes, reports and notifications.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5 (Web, Data JPA, Validation, Mail)
- **Security:** Spring Security, JWT (jjwt)
- **Database:** PostgreSQL, Hibernate
- **Testing:** JUnit 5, Testcontainers
- **Build:** Maven, Lombok

## Features

- **Authentication:** registration with email verification, JWT login, role-based access (`ADMIN`, `MODERATOR`, `USER`)
- **Profiles:** update details, change password and email
- **Channels:** create, update and manage channel status
- **Videos:** upload, categories, tags, status (public / private), watch history
- **Likes:** like / dislike for videos and comments
- **Comments** on videos
- **Playlists** with ordered videos
- **Subscriptions** with configurable notification type
- **Notifications** for subscribers
- **Reports** (complaints) on channels and videos
- **File storage:** upload and download attachments
- **Admin:** email history, moderation endpoints
- Global exception handling and request validation

## API Overview

84 endpoints across 17 controllers:

| Module | Base path |
|---|---|
| Auth | `/auth` |
| Attachments | `/attach` |
| Profile | `/api/profile` |
| Channel | `/api/channel` |
| Video | `/api/video` |
| Video like / tag | `/api/video-like`, `/api/video-tag` |
| Category / Tag | `/api/category`, `/api/tag` |
| Comment / Comment like | `/api/comment`, `/api/comment-like` |
| Playlist / Playlist video | `/api/playlist`, `/api/playlist-video` |
| Subscription | `/api/subscription` |
| Notification | `/api/notification` |
| Report | `/api/report` |
| Email history (admin) | `/api/email-history` |

Ready-to-run HTTP request files for every module are in [`src/main/resources/http`](src/main/resources/http) (IntelliJ HTTP Client).

## Project Structure

```
src/main/java/dasturlash/uz/youtube
├── config       # Spring Security, JWT filter, user details
├── controller   # REST controllers
├── dto          # request / response objects
├── entity       # JPA entities
├── enums
├── exception    # custom exceptions, global handler
├── repository   # Spring Data repositories
├── service      # business logic
└── util         # JWT and security helpers
```

## Getting Started

**Requirements:** JDK 21, PostgreSQL, Maven (wrapper included)

```bash
git clone https://github.com/amirkhan0102/youtube_clone_backend.git
cd youtube_clone_backend
```

1. Create a PostgreSQL database.
2. Set the database, mail (SMTP) and JWT settings in `src/main/resources/application.properties`.
3. Run the application:

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## Author

**Amriddin Javqochdiyev** — Backend Developer
[LinkedIn](https://www.linkedin.com/in/amriddin-j-0a92a8293) · [Telegram](https://t.me/amriddin_javqochdiyev)
