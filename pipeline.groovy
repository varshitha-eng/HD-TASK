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
        stage('Deploy to Staging'){
            steps{
                echo 'Deploy to Staging...'
                sh 'docker-compose -f docker-compose.staging.yml up -d'
            }
        }
        stage('Release to Production') {
            steps {
                    input 'Promote to production?'
                    echo 'Releasing to Production...'
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
        }
    
    post {
        always {
            echo 'Cleaning up...'
            sh 'docker-compose down'
            success{
                echo 'Pipeline completed successfully'
            }
            failure{
                echo 'Pipeline failed'
            
            }
        }
    }
}
}
