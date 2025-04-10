pipeline {
    agent any
    environment {
        // Private Docker registry details
        DOCKER_REGISTRY = '172.16.202.56:5000'
        IMAGE_NAME = "${DOCKER_REGISTRY}/iiitb720"
        // Jenkins credentials IDs – ensure these are set up in Jenkins:
        // - private_registry_creds: for logging into your private Docker registry
        // - github-credentials: for accessing your private Git repository
        // - devServerSSH: SSH credentials (private key) to access the development server
        DOCKER_REGISTRY_CREDENTIALS = 'private_registry_creds'
        GIT_CREDENTIALS_ID = 'github-credentials'
        SSH_CREDENTIALS_ID = 'devServerSSH'
        // Development server IP
        DEV_SERVER = '172.16.202.57'
        // Remote directory on the development server containing your docker-compose.yml file.
        REMOTE_DEPLOY_DIR = '/home/ctri/iiitb720'
        // Git repository branch to check out (adjust as needed)
        GIT_BRANCH = 'main'
    }
    stages {
        stage('Checkout') {
            steps {
                // Check out the repository using provided credentials since it's private.
                git url: 'https://github.com/rasp-devops/rasp-iiitb360',
                    branch: "${GIT_BRANCH}",
                    credentialsId: "${GIT_CREDENTIALS_ID}"
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in your repository.
                    dockerImage = docker.build("${IMAGE_NAME}:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    // Log in to the private Docker registry and push the image.
                    docker.withRegistry("http://${DOCKER_REGISTRY}", "${DOCKER_REGISTRY_CREDENTIALS}") {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Deploy to Development Server') {
            steps {
                // Use SSH agent for remote connection using the provided SSH credentials.
                sshagent (credentials: [env.SSH_CREDENTIALS_ID]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ctri@${env.DEV_SERVER} '
                            echo "Changing directory to ${env.REMOTE_DEPLOY_DIR}" &&
                            cd ${env.REMOTE_DEPLOY_DIR} &&

                            echo "Explicitly pulling image ${env.IMAGE_NAME}..." &&
                            /usr/bin/docker pull ${env.IMAGE_NAME} &&

                            echo "Starting containers using docker compose up -d..." &&
                            /usr/bin/docker compose up &&

                            echo "Deployment commands finished."
                        '
                    """
                }
            }
        }
    }
    post {
        failure {
            echo "Build, push, or deployment failed!"
        }
    }
}
