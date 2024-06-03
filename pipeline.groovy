pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh 'mvn clean package'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                sh 'mvn test'
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Code Quality Analysis') {
            steps {
                echo 'Code Quality Analysis...'
                sh 'mvn sonar:sonar'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker Image...'
                script {
                    def dockerImage = docker.build("my-web-app:${env.BUILD_ID}")
                }
            }
        }
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to Staging...'
                sh 'docker-compose -f docker-compose.staging.yml up -d'
            }
        }
        stage('Release to Production') {
            steps {
                input 'Promote to production?'
                echo 'Releasing to Production...'
                sh 'docker-compose -f docker-compose.production.yml up -d'
            }
        }
        stage('Monitoring and Alerting') {
            steps {
                echo 'Monitoring and Alerting...'
                // Configuration for monitoring tools like Datadog/New Relic
            }
        }
    }
    post {
        always {
            echo 'Cleaning up...'
            sh 'docker-compose down'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
