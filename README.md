# SpendWise

SpendWise is a single-user personal-finance learning project. It records income and expenses, shows HQL-powered totals, filters transactions, and tracks one budget per calendar month.

## Stack

- Java 17, Maven WAR, Apache Tomcat 10.1
- Jakarta Servlets 6 and JSP/JSTL (no JavaScript, REST, or framework)
- Hibernate ORM 6 with a Supabase PostgreSQL database

The project brief originally specified MySQL. The owner approved PostgreSQL as the one exception because the selected Supabase project provides PostgreSQL.

## MVC and layers

- **Model:** entity classes and services.
- **View:** JSP files under `WEB-INF/views`; JSPs only render HTML and cannot be opened directly.
- **Controller:** servlets read HTTP requests, call services, and forward or redirect.
- Calls flow in one direction: `servlet → service → dao → Hibernate → PostgreSQL`.

`Category` is a foreign key on each transaction rather than free text. This keeps category names consistent and makes reliable category totals possible. `Transaction.category` is lazy-loaded, while list HQL uses `JOIN FETCH` to load the category before Hibernate closes its Session; this avoids `LazyInitializationException` in a JSP.

## Database setup

1. Open the **Spendwise** project SQL Editor in Supabase.
2. Run [sql/schema.sql](sql/schema.sql).
3. Copy `src/main/resources/hibernate.properties.example` to `src/main/resources/hibernate.properties`.
4. Fill in the PostgreSQL connection URL, username, and database password. Do not commit this file.

Hibernate uses `hbm2ddl.auto=validate`: it checks that the database matches the mapped entities and never creates or changes tables.

The schema enables Row Level Security and adds no public policies because this app connects from its server to PostgreSQL, not from a browser through the Supabase Data API.

## Run locally

1. Install JDK 17, Maven, and Tomcat 10.1.
2. Run `mvn clean package` from the project folder.
3. Copy `target/spendwise.war` to Tomcat's `webapps` folder.
4. Start Tomcat and open `http://localhost:8080/spendwise/dashboard`.

## Deploy to Render

Render does not provide a native Java runtime, so this project uses a small Docker deployment image with Tomcat 10.1. The `Dockerfile` builds the WAR and then runs it as Tomcat's root application. `render.yaml` creates a web service and prompts for these secret runtime environment variables:

- `SPENDWISE_DB_URL` — the Supabase **Session pooler JDBC URL**, without `user` or `password` query parameters. The application automatically adds `sslmode=require` when it is missing.
- `SPENDWISE_DB_USERNAME` — the Session pooler username, normally `postgres.<your-project-ref>`.
- `SPENDWISE_DB_PASSWORD` — the database password you chose in Supabase.

Copy the host and port from the **Session pooler → JDBC** card in Supabase's Connect dialog. Do not use a Supabase API URL, anon key, service-role key, or the transaction pooler. The `/health` endpoint runs `SELECT 1`; Render only marks the service healthy after both Tomcat and the database are reachable.

After pushing this repository to GitHub, create a Render Blueprint from the repository. Render reads `render.yaml`, builds the Docker image, and serves the application at `/dashboard`.

This deployment is intentionally unauthenticated. Do not publish it as a real personal-finance product or store sensitive information until authentication and per-user authorization have been added.

## Important learning concepts

- A **Servlet** has `doGet` for safe page reads and `doPost` for data changes.
- A Hibernate **Session** is one short unit of work with the database. DAOs open and close one per operation.
- A Hibernate **Transaction** makes a write atomic: it commits fully or rolls back on an error.
- **PRG (Post/Redirect/Get)** redirects after every successful POST, preventing accidental duplicate submissions on refresh.
- Named HQL parameters bind input separately from the query text, avoiding injection.

## Security boundary

This is intentionally a single-user, local learning app. It has no authentication, user sessions, or CSRF tokens, so it must not be hosted publicly. It validates server-side input, escapes JSP output with `c:out`, uses POST for changes, and never stores database credentials in Git.
