# teacher-finder

## Getting Started

### 1. Clone the Repository

First, clone this repository to your local machine:

```bash
git clone git@github.com:Rikkytjock/teacher-finder-backend.git
```
### 2. Create Public/Private PEM Files for JWT

To generate the public and private PEM files required for JWT authentication:

1. Navigate to the `src/main/resources/jwt/` directory in your project (create the folder if you do not have it already).

2. Generate the private key by running the following command:

   ```bash
   openssl genpkey -algorithm RSA -out src/main/resources/jwt/private_key.pem -pkeyopt rsa_keygen_bits:2048
   ```
3. Extract the public key from the private key using this command:

  ```bash
  openssl rsa -in src/main/resources/jwt/private.pem -pubout -out src/main/resources/jwt/public_key.pem
  ```

These two files (private.pem and public.pem) will be used for signing and verifying JWTs in your application.

### 3. Create .env File ###

In the root directory of the project, create a .env file. This file will hold the environment variables for your development environment.
    1. Open a text editor and create a new .env file.
    2. Add the following lines to it:

    ```bash
    MONGODB_CONNECTION_STRING_DEV="your-mongodb-connection-string"
    JWT_ISSUER="your-jwt-issuer"
    ```
Replace your-mongodb-connection-string with the MongoDB connection string from your MongoDB Atlas cluster and your-jwt-issuer with your JWT issuer (e.g., https://your-app.com).

### 4. Export the Connection String ###

Before starting the project, export the MONGODB_CONNECTION_STRING_DEV environment variable in your terminal. The .env file will be sourced, and the values will be available during development.

Bash (Linux/macOS):
```bash
source .env
```

PowerShell (Windows):
```bash
Get-Content .env | foreach { $name, $value = $_ -split '='; Set-Item -Path Env:\$name.Trim() -Value $value.Trim() }
```

### 5. Start the Application in Development Mode ###

Once everything is set up, you can start the Quarkus application in development mode:

Bash (Linux/macOS):
```bash
./mvnw compile quarkus:dev
```

PowerShell (Windows):
```bash
.\mvnw.cmd compile quarkus:dev
```

### Docs ###

- Quarkus Documentation: https://quarkus.io/guides
- MongoDB Atlas Documentation: https://docs.atlas.mongodb.com/
- JWT Documentation: https://jwt.io/
   

**Everything below is automatically generated by the Quarkus framework**
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/teacher-finder-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- Narayana JTA - Transaction manager ([guide](https://quarkus.io/guides/transaction)): Offer JTA transaction support (included in Hibernate ORM)
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
