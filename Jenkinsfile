pipeline {
    agent any
    environment {
        JURL = 'https://soleng.jfrog.io'
        RT_URL = 'https://soleng.jfrog.io/artifactory'
        TOKEN = credentials('soleng-io-token')
        ARTIFACTORY_LOCAL_DEV_REPO = 'carlosmm-maven-dev-local'
        ARTIFACTORY_VIRTUAL_REPO = 'carlosmm-maven-virtual'
        SERVER_ID = 'soleng'
        BUILD_NAME = 'Acme-Spring-Boot-Application'
    }
    tools {
        maven "maven-3.6.3"
        jfrog "jfrog-cli"
    }

    stages {
        stage ('Config JFrgo CLI') {
            steps {
                jf 'c add ${SERVER_ID} --interactive=false --overwrite=true --access-token=${TOKEN} --url=${JURL}'
                jf 'config use ${SERVER_ID}'
            }
        }
        stage ('Ping to Artifactory') {
            steps {
               jf 'rt ping'
            }
        }
        stage ('Config Maven'){
            steps {
               jf 'mvnc --repo-resolve-releases=${ARTIFACTORY_VIRTUAL_REPO} --repo-resolve-snapshots=${ARTIFACTORY_VIRTUAL_REPO} --repo-deploy-releases=${ARTIFACTORY_VIRTUAL_REPO} --repo-deploy-snapshots=${ARTIFACTORY_VIRTUAL_REPO}'
            }
        }
        stage('Compile') {
            steps {
                echo 'Compiling'
                jf 'mvn clean test-compile -Dcheckstyle.skip -DskipTests'
            }
        }
        stage ('Upload artifact') {
            steps {
                 jf 'mvn clean deploy -Dcheckstyle.skip -DskipTests --build-name=${BUILD_NAME} --build-number=${BUILD_ID}'
            }
        }
        stage ('Publish build info') {
            steps {
                // Collect environment variables for the build
                jf 'rt bce ${BUILD_NAME} ${BUILD_ID}'
                //Collect VCS details from git and add them to the build
                jf 'rt bag ${BUILD_NAME} ${BUILD_ID}'
                //Publish build info
                jf 'rt bp ${BUILD_NAME} ${BUILD_ID} --build-url=${BUILD_URL}'
                //Promote the build
                jf 'rt bpr --status=Development ${BUILD_NAME} ${BUILD_ID} ${ARTIFACTORY_LOCAL_DEV_REPO}'
            }
        }
    }
}