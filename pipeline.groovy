pipeline {
    agent any
    stages {
        stage('Test Docker') {
            steps {
                script {
                    bat 'docker run hello-world'
                }
            }
        }
    }
}

