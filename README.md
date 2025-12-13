# Obsidian-Garden
A gamified full-stack LeetCode companion web app to make the LeetCode grind more engaging. It was primarily inspired by Tamagotchi! 

## Live Demo 
Frontend (GitHub Pages): https://metha0105.github.io/Obsidian-Garden/ 
Backend API is hosted on Render.

## NOTES
Unfortunately, sandbox execution in the live demo is limited as Render's free tier is currently being used. This is also to help prevent arbitrary code execution on public servers. The local setup enables full code judging. For more details on how exactly the judging is done, please refer to the `evaluator` folder and the `EvaluationServiceImpl.java` file within the backend. Parsing of user's code is still being tested. Changes will be pushed once testing is finished and another method is found for sandbox execution in the live demo.

None of the art used belongs to me! The sprite used for the character panel is Mew from Pokemon. 

## Features
### Web App
* Clean React interface with TypeScript
* Fully client-side routing (React Router)
* Code editor powered by Monaco Editor
* GIF-based animated landing page
* Problem listings with metadata and tags
* Submission history tracking

### Backend API
* Spring Boot REST
* PostgreSQL persistence (Neon in production)
* Service-layer architecture
* Code evaluator runner (Docker sandbox)

### Code Evaluation
* Local execution uses Dockerized C++ sandbox
* Isolated execution environment
* JSON-based input/output for judging
* As noted above, it is disabled on free-tier hosting

## Project Structure
Please refer to the backend for more details on the REST API, controllers, services, code evaluator, entities, etc. For more details about the UI, code editor, and animations, please refer to the frontend.

## Local Development
The repo can be cloned with HTTP (git clone)! 

### Backend (Spring Boot)
Requirements are,
* Java 17+
* Maven
* C++
* Docker
* SQL
* PostgreSQL

The DB connection also needs to be configured through the `application.yaml` file. To run the backend, use the command `mvn spring-boot:run` and it should run on port 8080.

### Frontend (React + TypeScript)
Dependencies need to be installed (which can be done with npm). To start the development server, use the command `npm run dev` and it should run on port 5173. Something to note is an `.env` file isn't required for the API routing logic since the `config.ts` file auto-switches based on environment.

## Deployment
### Frontend (GitHub Pages)
It was configured using GitHub Pages and Vite. The command used to deploy it was `npm run deploy` which builds the app and pushes `/dist` to GitHub Pages.

### Backend (Render)
The setup process is,
1. Create new web service
2. Set root directory to `/backend`
3. Build command = `/mvn clean install`
4. Start command is `java -jar target/garden-0.0.1-SNAPSHOT.jar`
5. Add environment variables

### Database (Neon)
You can import your local PostgreSQL data using the commands `pg_dump` and `pg_restore`. Replace the password, host, and database name accordingly.
