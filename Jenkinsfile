pipeline {
  agent any

  stages {
    stage('Test') {
      steps {
        sh 'pwd'
        sh './gradlew clean test'
      }
    }

    stage('Build') {
      steps {
        sh 'pwd'
        sh './gradlew :services:lifepuzzle-api:buildZip'
      }
    }

    stage('S3 Upload') {
      steps {
        echo 'Uploading'
        sh 'aws s3 cp services/lifepuzzle-api/build/lifepuzzle-api.zip s3://itmca-deploy/${JOB_NAME}-${BUILD_TIMESTAMP}.zip --region ***REMOVED***'
      }
    }

    stage('Deploy') {
      steps {
        echo 'Deploying'
        sh 'aws elasticbeanstalk create-application-version --region ***REMOVED*** --application-name lifepuzzle-api --version-label ${JOB_NAME}-${BUILD_TIMESTAMP} --description ${BUILD_TAG} --source-bundle S3Bucket="itmca-deploy",S3Key="${JOB_NAME}-${BUILD_TIMESTAMP}.zip"'
        sh 'aws elasticbeanstalk update-environment --region ***REMOVED*** --environment-name Lifepuzzleapi-env --version-label ${JOB_NAME}-${BUILD_TIMESTAMP}'
      }
    }

  }
  environment {
    SLACK_COLOR_DANGER = '#E01563'
    SLACK_COLOR_INFO = '#6ECADC'
    SLACK_COLOR_WARNING = '#FFC300'
    SLACK_COLOR_GOOD = '#3EB991'
  }
}
