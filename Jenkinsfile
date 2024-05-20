pipeline {
    agent any
    environment {
        JURL = 'http://artifactory.artifactory:8082'
        RT_URL = 'http://artifactory.artifactory:8082/artifactory'
        TOKEN = credentials('main.jfrog.local')
        ARTIFACTORY_LOCAL_DEV_REPO = 'acme-maven-dev-local'
        ARTIFACTORY_VIRTUAL_REPO = 'acme-maven-virtual'
        SERVER_ID = 'local'
        BUILD_NAME = 'Acme-Spring-Boot-Application'
    }
    tools {
        maven "maven-3.9.6"
        jfrog "jfrog-cli-2.56.1"
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
        stage ('Scan the project') {
            when {
                expression { TYPE_OF_SCAN == 'Audit'}
            }
            steps {
                script {
                    if (params.WATCHES!=null && params.WATCHES!=''){
                        jf 'audit --mvn --watches ${WATCHES}'
                    } else {
                        jf 'audit --mvn'
                    }
                }
            }
        }
        stage('Package') {
            steps {
                echo 'Packaging'
                jf 'mvn clean package -Dcheckstyle.skip -DskipTests'
            }
        }
        stage ('Scan the jar') {
            when {
                expression { TYPE_OF_SCAN == 'On-demand'}
            }
            steps {
                jf 's --mvn --watches ${WATCHES}'
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
            }
        }
        stage ('Scan the build info') {
            when {
                expression { TYPE_OF_SCAN == 'Build'}
            }
            steps {
                jf 'bs ${BUILD_NAME} ${BUILD_ID} --fail=${FAIL_BUILD} --vuln=${RETURN_ALL_VULNERABILITIES}'
            }
        }
        stage ('Promote build info'){
            steps{
                //Promote the build
                jf 'rt bpr --status=Development ${BUILD_NAME} ${BUILD_ID} ${ARTIFACTORY_LOCAL_DEV_REPO}'
            }
        }
    }
}