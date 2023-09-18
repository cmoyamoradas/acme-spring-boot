pipeline {
    agent any
    environment {
        JURL = 'http://artifactory.artifactory:8082'
        RT_URL = 'http://artifactory.artifactory:8082/artifactory'
        TOKEN = credentials('7ae3e03b-c72b-4a71-9f92-26069898d209')
        ARTIFACTORY_LOCAL_DEV_REPO = 'acme-maven-dev-local'
        SERVER_ID = 'local'
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
               sh 'mvn dependency:purge-local-repository'
               jf 'mvnc --repo-resolve-releases=acme-maven-virtual --repo-resolve-snapshots=acme-maven-virtual --repo-deploy-releases=acme-maven-virtual --repo-deploy-snapshots=acme-maven-virtual'
            }
        }
        stage('Compile') {
            steps {
                echo 'Compiling'
                sh 'mvn dependency:purge-local-repository'
                jf 'mvn clean test-compile -Dcheckstyle.skip -DskipTests'
            }
        }
        stage ('Upload artifact') {
            steps {
                 sh 'mvn dependency:purge-local-repository'
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
                jf 'rt bpr --status=Development --props="status=Development" ${BUILD_NAME} ${BUILD_ID} ${ARTIFACTORY_LOCAL_DEV_REPO}'
            }
        }
    }
}