# Office Issue Tracker - Backend

Office Issue Tracker is a backend service for reporting, tracking, and managing office-related issues. Users can also comment and vote on them to surface what matters most. It exposes a REST API and persists data in PostgreSQL.

You can find the frontend for this project here: https://github.com/daniel-sosna/office-issue-tracker-fe

## Prerequisites
- Java 21
- Gradle wrapper (`./gradlew`)
- Docker & Docker Compose
- A Google OAuth client ID for authentication
- A Cloudinary account for media storage

## Quick start
To run the backend locally with mock data, follow these steps:

1. Prepare the environment:
    - Clone the repository and navigate to the project root
    - Copy `.env.example` to `.env` and adjust any necessary environment variables
    - Ensure Docker is running on your machine

1. Start Postgres:
    ```bash
    docker compose up -d
    ```
    _* To stop it:_
    ```bash
    docker compose down
    ```

1. Build & run
    ```bash
    ./gradlew bootRun
    ```

1. Use it:
    - Access API docs at: http://localhost:8080/swagger-ui/index.html
    - Set up the [frontend](https://github.com/daniel-sosna/office-issue-tracker-fe) and access the app

## Mock data (local only)
For details on local mock data setup, see [mock-data-setup.md](docs/mock-data-setup.md).

## Features
- Users are created on first login via Google OAuth; they start with the `USER` role.
- `ADMIN` access is granted only via direct DB update.
- Issues move through a simple lifecycle: create, update, change status, delete.
- The issue list supports filtering, sorting, and pagination.
- Attachments let you add media files to an issue. (They are stored in [Cloudinary](https://cloudinary.com/))
- Profiles let users view and update their own details as needed.
- Offices group issues by location so teams can focus on their space.
- Comment on issues to discuss fixes and progress.
- Vote on issues to surface what matters most.
