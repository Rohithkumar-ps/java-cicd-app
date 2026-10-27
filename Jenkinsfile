
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    environment {
        AWS_REGION         = 'ap-southeast-2'
        AWS_ACCOUNT_ID     = '952837501939'
        ECR_REPOSITORY     = 'java-cicd-app'
        ECR_REGISTRY       = '952837501939.dkr.ecr.ap-southeast-2.amazonaws.com'
        SONAR_PROJECT_KEY  = 'Rohithkumar-ps_java-cicd-app'
        SONAR_ORGANIZATION = 'rohithkumar-ps'
        IMAGE_TAG          = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh '''
                    set -eu
                    java -version
                    mvn clean verify
                    test -f target/java-cicd-app-1.0.0.jar
                    test -f target/site/jacoco/jacoco.xml
                '''
            }
        }

        stage('SonarQube Cloud Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeCloud') {
                    sh '''
                        set -eu
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.8.0.7211:sonar \
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
                    set -eu
                    docker build \
                      -t "$ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG" \
                      .
                '''
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh '''
                    set -eu
                    trivy --version

                    trivy image \
                      --scanners vuln \
                      --severity HIGH,CRITICAL \
                      --exit-code 1 \
                      "$ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG"
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
                      "$ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG"
                '''
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Build, tests, SonarQube Quality Gate, Docker build, Trivy scan and ECR push completed.'
        }

        failure {
            echo 'FAILURE: Check the Console Output for the failed stage.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
