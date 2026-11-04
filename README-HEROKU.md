# Deploy SpendWise to Heroku

SpendWise is a Java 17 Maven WAR application using Jakarta Servlets/JSP, Hibernate 6, and PostgreSQL. It runs on Tomcat 10.1.

## Important security warning

The current application has no authentication or per-user authorization. Anyone who can reach the public Heroku URL can potentially view, add, or delete the same finance data. Deploy only with dummy/test data. Do not use real financial or personal data or share the URL publicly until authentication, authorization, and CSRF protection are implemented.

## What this Heroku branch adds

- `Dockerfile.web`: Heroku-specific Docker image, built from the existing Maven WAR.
- `docker/heroku-entrypoint.sh`: configures Tomcat to listen on Heroku's runtime `PORT`.
- The original `Dockerfile`, `docker/entrypoint.sh`, and `render.yaml` are left unchanged for the existing deployment.

## Prerequisites

- Heroku CLI installed and logged in.
- Docker Desktop installed and running.
- A PostgreSQL database with the SpendWise schema already applied.
- Available Heroku credits / an eligible dyno plan.

## 1. Get the Heroku-ready branch

```bash
git clone https://github.com/himohitj/SpendWise.git
cd SpendWise
git fetch origin
git switch heroku-deployment
```

Or, if you already have the repository cloned:

```bash
git fetch origin
git switch heroku-deployment
git pull
```

## 2. Create the Heroku app

Choose a globally unique app name:

```bash
heroku login
heroku create YOUR-UNIQUE-APP-NAME --stack container
```

## 3. Configure the database

SpendWise currently uses PostgreSQL and reads these environment variables when there is no local `hibernate.properties` file in the packaged app:

- `SPENDWISE_DB_URL`: JDBC URL, for example `jdbc:postgresql://HOST:5432/DATABASE?sslmode=require`.
- `SPENDWISE_DB_USERNAME`: PostgreSQL username.
- `SPENDWISE_DB_PASSWORD`: PostgreSQL password.

Set them as Heroku config vars (never commit credentials):

```bash
heroku config:set SPENDWISE_DB_URL="jdbc:postgresql://HOST:5432/DATABASE?sslmode=require" -a YOUR-UNIQUE-APP-NAME
heroku config:set SPENDWISE_DB_USERNAME="DATABASE_USERNAME" -a YOUR-UNIQUE-APP-NAME
heroku config:set SPENDWISE_DB_PASSWORD="DATABASE_PASSWORD" -a YOUR-UNIQUE-APP-NAME
```

The app already uses the PostgreSQL database configured for your current deployment; you can keep that database if it is reachable from Heroku and the schema matches. You do not need to provision Heroku Postgres just to host the web app. Verify your provider permits external connections, use its correct host/port/username, and retain SSL. Hibernate is configured with `hbm2ddl.auto=validate`; it does not create or migrate tables. If using a fresh database, run `sql/schema.sql` first and migrate data separately.

## 4. Build and deploy the Docker image

Run these commands from the repository root. The `--recursive` flag makes the Heroku CLI discover `Dockerfile.web` as the `web` process image:

```bash
heroku container:login
heroku container:push web --recursive -a YOUR-UNIQUE-APP-NAME
heroku container:release web -a YOUR-UNIQUE-APP-NAME
```

## 5. Check the deployment

```bash
heroku logs --tail -a YOUR-UNIQUE-APP-NAME
heroku ps -a YOUR-UNIQUE-APP-NAME
heroku open -a YOUR-UNIQUE-APP-NAME
```

SpendWise is deployed as Tomcat's root application. Open:

`https://YOUR-UNIQUE-APP-NAME.herokuapp.com/dashboard`

If the app fails to start, check the logs for missing `SPENDWISE_DB_*` values, a PostgreSQL connection error, or schema validation failures.

## Database and storage notes

Heroku dyno filesystems are ephemeral. Do not store user uploads, database files, or application data on the local filesystem. SpendWise stores its finance records in PostgreSQL, so those records remain in the database across dyno restarts. This deploy does not copy or migrate any existing data.

## Official Heroku documentation

- [Container Registry & Runtime](https://devcenter.heroku.com/articles/container-registry-and-runtime)
- [Heroku config vars](https://devcenter.heroku.com/articles/config-vars)
