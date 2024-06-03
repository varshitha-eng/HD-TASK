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
                    bat 'mvn clean package'
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    bat 'mvn test'
                }
            }
        }
        stage('Code Quality Analysis') {
            steps {
                script {
                    bat 'mvn sonar:sonar'
                }
            }
        }
        stage('Deploy to Test') {
            steps {
                script {
                    bat 'docker-compose up -d'
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
