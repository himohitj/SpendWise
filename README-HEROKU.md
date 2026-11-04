# Deploy SpendWise to Heroku

SpendWise is a Java 17 Maven WAR application (Jakarta Servlet/JSP) that runs on Tomcat 10.1 and connects to PostgreSQL through Hibernate.

## Important security warning

The current application has no authentication or per-user authorization. Anyone who can reach the public Heroku URL can potentially view, add, or delete the same finance data. Do not use real financial or personal data or share the URL publicly until authentication, authorization, and CSRF protection are implemented.

## Prerequisites

- A verified Heroku account with sufficient platform credits.
- Heroku CLI.
- Docker Desktop (running).
- Access to the existing PostgreSQL database, or a separately prepared PostgreSQL database.

## Deploy using the Heroku Container Registry

From the repository root:

```bash
heroku login
heroku create YOUR-UNIQUE-APP-NAME --stack container
heroku container:login
heroku container:push web -a YOUR-UNIQUE-APP-NAME -f Dockerfile.heroku
heroku container:release web -a YOUR-UNIQUE-APP-NAME
```

Configure the database connection variables. Supply the PostgreSQL JDBC URL and credentials from your database provider. Do not commit credentials:

```bash
heroku config:set SPENDWISE_DB_URL="jdbc:postgresql://HOST:5432/DATABASE?sslmode=require" -a YOUR-UNIQUE-APP-NAME
heroku config:set SPENDWISE_DB_USERNAME="DATABASE_USERNAME" -a YOUR-UNIQUE-APP-NAME
heroku config:set SPENDWISE_DB_PASSWORD="DATABASE_PASSWORD" -a YOUR-UNIQUE-APP-NAME
```

Then restart and inspect logs:

```bash
heroku restart -a YOUR-UNIQUE-APP-NAME
heroku logs --tail -a YOUR-UNIQUE-APP-NAME
heroku open -a YOUR-UNIQUE-APP-NAME
```

The application is deployed as the ROOT context. Open `https://YOUR-UNIQUE-APP-NAME.herokuapp.com/dashboard`.

## Database notes

The application currently uses the existing PostgreSQL schema and Hibernate `validate` mode. This deployment does not automatically migrate data. Keep using the existing database only after verifying that the schema and credentials are correct. If you want a separate production database, provision it and run `sql/schema.sql` first; migrate data separately if required.

Heroku Container Registry deploys are documented at https://devcenter.heroku.com/articles/container-registry-and-runtime.

## Keep deployments separate

This branch adds a Heroku-specific Dockerfile and entrypoint. The original `Dockerfile`, `docker/entrypoint.sh`, and `render.yaml` are left unchanged for the current Render/Railway workflow.
