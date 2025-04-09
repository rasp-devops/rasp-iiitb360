pipeline {
    agent any

    environment {
        // Replace with your Docker Hub repo and credential ID
        DOCKERHUB_REPO = 'varn03/iiitb360' 
        DOCKERHUB_CREDS = 'dockerhub-creds'
        IMAGE_TAG = 'latest'
        // Adjust this path if your docker-compose file isn’t at the root.
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                // Check out the repository containing the Jenkinsfile, Dockerfile, docker-compose.yml, etc.
                checkout scm
            }
        }
        stage('Build with Maven') {
            steps {
                // Build the application using Maven. This should create the JAR in the /target folder.
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image. The image built is defined in your Dockerfile.
                    def image = docker.build("${DOCKERHUB_REPO}:${IMAGE_TAG}")
                    echo "Docker image built: ${DOCKERHUB_REPO}:${IMAGE_TAG}"
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKERHUB_CREDS) {
                        def image = docker.image("${DOCKERHUB_REPO}:${IMAGE_TAG}")
                        image.push()
                        echo "Docker image pushed to Docker Hub: ${DOCKERHUB_REPO}:${IMAGE_TAG}"
                    }
                }
            }
        }
        stage('Deploy using docker-compose') {
            steps {
                script {
                    // Optionally, bring down any existing environment before deploying.
                    sh "docker-compose -f ${COMPOSE_FILE} down || true"
                    // Bring the environment up in detached mode.
                    sh "docker-compose -f ${COMPOSE_FILE} up -d"
                    echo "Deployment initiated via docker-compose."
                }
            }
        }
    }
    post {
        success {
            echo 'CI/CD pipeline completed successfully.'
            // Add notifications if needed.
        }
        failure {
            echo 'CI/CD pipeline encountered an error. Check the logs for details.'
            // Optionally: send alerts, rollback steps, etc.
        }
    }
}
