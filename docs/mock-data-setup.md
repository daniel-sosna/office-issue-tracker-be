# Mock Data Setup (Local Only)

This document explains how to safely use mock data for local development only, without affecting cloud or production environments.
Mock data is inserted using Liquibase and enabled only when running the backend locally.

## Why Do We Use Mock Data?

Mock data allows developers to:
- Test UI screens and features without waiting for real data
- Develop features simultaneously without blocking backend work
- Demonstrate the system during early sprints
- Simulate realistic users, issues, and offices

**Production/cloud environments must NOT contain test data.**

## Local Environment Setup

All mock files must contain:
```yaml
context: local
```

Add this to your `application.properties`:

```properties
spring.liquibase.contexts=local
```

This tells Liquibase: *"Run ALL changeSets with `context: local` in local environment."*

When you run:
```bash
./gradlew bootRun
```

Liquibase automatically loads mock data into your local database.

## Cloud / Production Environment Setup

To prevent mock data from running in cloud deployments, the production configuration must use a different context.

In `application-prod.properties`:

```properties
spring.liquibase.contexts=schema
```

**Result:**

| Environment | Behavior |
|-------------|----------|
| Local | Schema + mock data |
| Cloud/Prod | Schema only |

This ensures no dummy data ever reaches production.
