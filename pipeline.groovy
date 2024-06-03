pipeline {
    agent any
    tools{
        maven '3.9.7'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Building...'
                    bat 'mvn clean package'
                    archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    echo 'Testing...'
                    bat 'mvn test'
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Code Quality Analysis') {
            steps {
                script {
                    echo 'Code Quality Analysis...'
                    withSonarQubeEnv('SonarQube'){
                        bat 'mvn package sonar:sonar'
                    }
                }
            }
        }
        stage('Docker Image'){
            steps{
                script{
                    echo 'Building Docker Image...'
                    def dockerImage = docker.build("my-web-app:${env.BUILD_ID}")
                }
            }
        }
        stage('Release to Production') {
            steps {
                script {
                    bat 'docker-compose -f docker-compose-prod.yml up -d'
                }
            }
        }
    }
    post {
        always {
            script {
                bat 'docker-compose down'
            }
        }
    }
}
