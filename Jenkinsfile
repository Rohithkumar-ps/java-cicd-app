
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    environment {
        AWS_REGION       = 'ap-southeast-2'
        AWS_ACCOUNT_ID   = '952837501939'
        ECR_REPOSITORY   = 'java-cicd-app'
        ECR_REGISTRY     = '952837501939.dkr.ecr.ap-southeast-2.amazonaws.com'
        SONAR_PROJECT_KEY = 'Rohithkumar-ps_java-cicd-app'
        SONAR_ORGANIZATION = 'rohithkumar-ps'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'java -version'
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Cloud Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeCloud') {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey="$SONAR_PROJECT_KEY" \
                          -Dsonar.organization="$SONAR_ORGANIZATION"
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                      -t "$ECR_REGISTRY/$ECR_REPOSITORY:$BUILD_NUMBER" \
                      .
                '''
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh '''
                    trivy --version

                    trivy image \
                      --scanners vuln \
                      --severity HIGH,CRITICAL \
                      --exit-code 1 \
                      "$ECR_REGISTRY/$ECR_REPOSITORY:$BUILD_NUMBER"
                '''
            }
        }

        stage('Push to Amazon ECR') {
            steps {
                sh '''
                    set -eu

                    aws sts get-caller-identity

                    aws ecr get-login-password \
                      --region "$AWS_REGION" |
                    docker login \
                      --username AWS \
                      --password-stdin "$ECR_REGISTRY"

                    docker push \
                      "$ECR_REGISTRY/$ECR_REPOSITORY:$BUILD_NUMBER"
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline successful: build, tests, SonarQube Quality Gate, Docker, Trivy and ECR push completed.'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs to identify the issue.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
