Mock Data Setup (Local Only)

This document explains how to safely use mock data for local development only, without affecting cloud or production environments.
Mock data is inserted using Liquibase and enabled only when running the backend locally.

Why Do We Use Mock Data?

Mock data allows developers to:
test UI screens and features without waiting for real data
develop features simultaneously without blocking backend work
demonstrate the system during early sprints
simulate realistic users, issues, and offices
Production/cloud environments must NOT contain test data.

How Mock Data Is Structured

Mock data is split into multiple Liquibase changelog files:
File	Content	Context
005-insert-mock-users.yaml	Inserts 5 test users	local
007-insert-mock-offices.yaml	Inserts 5 Cognizant office records	local
006-insert-mock-issues.yaml	Inserts 20 mock issues referencing both users & offices	local

All mock files contain:
context: local

This ensures they only run in local environment.

📂 Folder Structure

src/main/resources/db/changelog/

├─ db.changelog-001-create-issue-table.yaml

├─ db.changelog-002-create-users-table.yaml

├─ db.changelog-003-create-office-table.yaml

├─ db.changelog-004-update-issue-table.yaml

├─ db.changelog-005-insert-mock-users.yaml       ← mock

├─ db.changelog-006-insert-mock-issues.yaml      ← mock

├─ db.changelog-007-insert-mock-offices.yaml     ← mock

└─ db.changelog-master.yaml

 Master Changelog Order (IMPORTANT)

Your db.changelog-master.yaml must include mock data in this order:
databaseChangeLog:
- include:
  file: db/changelog/db.changelog-001-create-issue-table.yaml
- include:
  file: db/changelog/db.changelog-002-create-users-table.yaml
- include:
  file: db/changelog/db.changelog-003-create-office-table.yaml
- include:
  file: db/changelog/db.changelog-004-update-issue-table.yaml

# Mock data (local only)
- include:
  file: db/changelog/db.changelog-005-insert-mock-users.yaml
- include:
  file: db/changelog/db.changelog-007-insert-mock-offices.yaml
- include:
  file: db/changelog/db.changelog-006-insert-mock-issues.yaml


  ✔ Why this order matters:

  Users must exist before issues (because issues reference created_by).
  Offices must exist before issues (because issues reference office_id).
  Issues must be inserted last.

  Local Environment Setup

  Add this to your application.properties:
  spring.liquibase.contexts=local

  This tells Liquibase:

  "Run ALL changeSets with context: local in local environment."

  When you run:

  ./gradlew bootRun
  Liquibase automatically loads:
  Users
  Offices
  Issues
  ONLY because context=local is active.


  ☁️ Cloud / Production Environment Setup

  To prevent mock data from running in cloud deployments, the production configuration must use a different context.
  In application-prod.properties:


  spring.liquibase.contexts=schema

  Result:

  Environment	Behavior
  Local	Schema + mock data
  Cloud/Prod	Schema only (no mock inserts)
  This ensures no dummy data ever reaches production.

  Verifying Mock Data Loaded Correctly

  Using IntelliJ Database Tools
  Open Database panel
  Connect to your local DB (Docker/Postgres)

  Run:

  SELECT * FROM users;
  SELECT * FROM office;
  SELECT * FROM issue;

  You should see:
  5 mock users
  5 mock offices
  20 mock issues

