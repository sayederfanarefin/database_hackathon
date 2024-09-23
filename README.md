# Database Hackathon
#### Integrating Hackathons in Computer Science Education for Real-World Teamwork and Problem-Solving Skills

![License: CC BY 4.0](https://img.shields.io/badge/License-CC%20BY%204.0-lightgrey.svg)
[![build status](https://gitlab.com/sayederfanarefin/database_hackathon/badges/main/pipeline.svg)](https://gitlab.com/sayederfanarefin/database_hackathon/-/pipelines)

![Contributions welcome](https://img.shields.io/badge/contributions-welcome-orange.svg)

<img src="images/image.webp" alt="Hackathon" width="150">

## Description
Run your own instance of the database hackathon using Docker!

## Authors
- Sayed Erfan Arefin
- Abdul Serwadda


## How to Run

Follow these steps to get the application up and running on your local machine using Docker and Docker Compose.

### Prerequisites

- Ensure Docker and Docker Compose are installed on your machine.

### Step 1: Clone the Repository

First, clone this repository to your local machine:

```
git clone https://github.com/sayederfanarefin/database_hackathon.git
cd database_hackathon
```

### Step 2: Create a .env File
Next, you'll need to create a .env file in the root directory of the project. This file will contain your environment variables, including database credentials.

There is an example .env included on the repository named .env.example, you use that too, just rename it. Replace yourpassword and yourrootpassword with secure passwords of your choice. Do not commit this file to version control to keep your secrets safe.

### Example .env file content
```
# Change all the values here please before use.

DB_HOST=db
DB_NAME=database_hackathon
DB_USERNAME=rootx
DB_PASSWORD=2243
DB_ROOT_PASSWORD=2243

# User admin credentials
ADMIN_USERNAME=user_admin
ADMIN_EMAIL=user@admin
ADMIN_FIRSTNAME=User
ADMIN_LASTNAME=Admin
ADMIN_PASSWORD=1234

# Regular user credentials
USER_USERNAME=user
USER_EMAIL=erfanjordison@gmail.com
USER_FIRSTNAME=User
USER_LASTNAME=One
USER_PASSWORD=1234
```

### Step 3: Pull the Docker Image (Optional)
Pull the latest Docker image from Docker Hub:

```
docker pull sayederfanarefin/database-hackathon:latest
```

### Step 4: Start the Application
To start the application along with its dependencies, run:

```
docker-compose up
```

This command starts the application and MySQL database services as defined in your docker-compose.yml file, making the application accessible at http://localhost:8080.

### Step 5: Stopping the Application
To stop and remove the containers, use:

```
docker-compose down
```

For stopping and removing containers, networks, and the default volumes attached, use:
```
docker-compose down -v
```

## Default Credentials 

- Stored in .env file
- Change all the values of .env file before use
- Or participants can change the grades



## Entity Diagram

<img src="images/entityManagerFactory(EntityManagerFactoryBuilder).png" alt="Entity Diagram" width="350">



## Use Case Diagram
<img src="images/use-case.drawio.png" alt="Use Case Diagram" width="400">


## Supporting Flow Diagrams

### Task creation flow of a question
<img src="images/hackathon_task_admin_creation.drawio.png" alt="Hackathon Task Admin Creation" width="650">


### Grading scenario of a question
<img src="images/flow.png" alt="Grading Scenario" width="650">




## How to Cite

Please cite this work as follows:

```
@misc{database_hackathon,
  author = {Sayed Erfan Arefin, Abdul Serwadda},
  title = {Integrating Hackathons in Computer Science Education for Real-World Teamwork and Problem-Solving Skills},
  year = {2024},
  publisher = {GitHub},
  journal = {GitHub repository},
  howpublished = {\url{https://github.com/sayederfanarefin/database_hackathon}}
}
```
