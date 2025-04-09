pipeline {
    agent any
    environment {
        // Private Docker registry details
        DOCKER_REGISTRY = '172.16.202.56:5000'
        IMAGE_NAME = "${DOCKER_REGISTRY}/iiitb720"
        // Jenkins credentials IDs – these need to be set up in Jenkins:
        // - dockerRegistryCreds: credentials for logging into your private Docker registry
        // - devServerSSH: SSH credentials (private key) to access the development server
        DOCKER_REGISTRY_CREDENTIALS = 'private_registry_creds'
        SSH_CREDENTIALS_ID = 'private_registry_creds'
        // Development server IP
        DEV_SERVER = '172.16.202.57'
        // Directory on the remote dev server where your docker-compose.yml is located.
        REMOTE_DEPLOY_DIR = '/home/ctri/iiitb720'
        // Git repository branch (adjust as needed)
        GIT_BRANCH = 'main'
    }
    stages {
        stage('Checkout') {
            steps {
                // Replace the URL with your Git repository
                git url: 'https://github.com/rasp-devops/rasp-iiitb360', branch: "${GIT_BRANCH}"
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in your repository
                    dockerImage = docker.build("${IMAGE_NAME}:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    // Log in and push the image to the private registry
                    docker.withRegistry("http://${DOCKER_REGISTRY}", "${DOCKER_REGISTRY_CREDENTIALS}") {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Deploy to Development Server') {
            steps {
                // Use the sshagent plugin to run deployment commands on your dev server
                sshagent (credentials: [env.SSH_CREDENTIALS_ID]) {
                    // The deployment assumes that your remote deployment directory contains the docker-compose.yml
                    // which refers to the image as ${DOCKER_REGISTRY}/iiitb720:latest.
                    sh """
                        ssh -o StrictHostKeyChecking=no yourSSHUser@${DEV_SERVER} '
                            cd ${REMOTE_DEPLOY_DIR} &&
                            docker-compose pull &&
                            docker-compose up -d
                        '
                    """
                }
            }
        }
    }
    post {
        failure {
            echo "Build, push or deployment failed!"
        }
    }
}
