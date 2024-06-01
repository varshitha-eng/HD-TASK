pipeline{
    agent any

    environment {
        DIRECTORY_PATH = "C:\\Users\\Varshitha V\\OneDrive\\Desktop\\pipeline.groovy"
        TESTING_ENVIRONMENT = 'First Pipeline'
        PRODUCTION_ENVIRONMENT = 'VARSHITHA'
    }

    stages{
        stage ('Build'){
            steps{
                echo 'Fetching the source code from the directory path specified by the environment variable'
                echo 'Compiling the code and generate any necessary artifacts'
            }
        }
        stage ('Test'){
            steps{
                echo 'Unit testing'
                echo 'Integration testing'
            }
        }
        stage ('Code Quality Check'){
            steps{
                echo 'Checking the quality of the code'
            }
        }
        stage ('Deploy'){
            steps{
                echo 'Deploying the application to a testing environment specified by the environment variable: $TESTING_ENVIRONMENT'
            }
        }
        stage ('Approval'){
            steps{
                echo 'On a hold for manual approval'
                sleep 10
            }
        }
        stage ('Deploy to Production'){
            steps{
                echo 'Deploying the code to the production environment using the environment variable: $PRODUCTION_ENVIRONMENT'
            }
        }
    }

}