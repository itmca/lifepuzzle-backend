pipeline {
  agent any
  stages {
    stage('Build War') {
      steps {
        sh 'pwd'
        sh './gradlew clean buildZip'
      }
    }

    stage('Upload S3') {
      steps {
        echo 'Uploading'
        sh 'aws s3 cp /var/lib/jenkins/workspace/lifepuzzle-api/build/lifepuzzle.zip s3://itmca-deploy/${JOB_NAME}-${GIT_BRANCH}-${BUILD_NUMBER}.war                     --acl public-read-write                     --region ***REMOVED***'
      }
    }

    stage('Deploy') {
      steps {
        echo 'Deploying'
        sh 'aws elasticbeanstalk create-application-version --region ***REMOVED***                     --application-name lifepuzzle-api                     --version-label ${JOB_NAME}-${BUILD_NUMBER}                     --description ${BUILD_TAG}                     --source-bundle S3Bucket="itmca-deploy",S3Key="${JOB_NAME}-${GIT_BRANCH}-${BUILD_NUMBER}.war"'
        sh 'aws elasticbeanstalk update-environment --region ***REMOVED***                     --environment-name Lifepuzzleapi-env                     --version-label ${JOB_NAME}-${BUILD_NUMBER}'
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