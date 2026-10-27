
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
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

        stage('SonarCloud Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeCloud') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                          -Dsonar.organization=rohithkumar-ps \
                          -Dsonar.projectKey=Rohithkumar-ps_java-cicd-app \
                          -Dsonar.host.url=https://sonarcloud.io
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build, tests, and SonarCloud analysis completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
    }
}
