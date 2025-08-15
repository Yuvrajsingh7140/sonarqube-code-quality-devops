pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven-3.9.0'
        JAVA_HOME = tool 'JDK-17'
        SONAR_HOME = tool 'SonarQube-Scanner'
        PATH = "\${MAVEN_HOME}/bin:\${JAVA_HOME}/bin:\${SONAR_HOME}/bin:\${PATH}"

        // SonarQube configuration
        SONAR_PROJECT_KEY = 'calculator-app'
        SONAR_PROJECT_NAME = 'Calculator Application'
        SONAR_HOST_URL = 'http://localhost:9000'

        // Docker configuration
        DOCKER_IMAGE = 'calculator-app'
        DOCKER_TAG = "\${env.BUILD_NUMBER}"
        DOCKER_REGISTRY = 'localhost:5000'
    }

    options {
        buildDiscarder(logRotator(
            numToKeepStr: '10',
            artifactNumToKeepStr: '10'
        ))
        timeout(time: 20, unit: 'MINUTES')
        timestamps()
    }

    triggers {
        pollSCM('H/5 * * * *')  // Poll every 5 minutes
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Checking out source code...'
                checkout scm

                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()
                }

                echo "📋 Build Information:"
                echo "  - Branch: \${env.BRANCH_NAME}"
                echo "  - Commit: \${env.GIT_COMMIT_SHORT}"
                echo "  - Build Number: \${env.BUILD_NUMBER}"
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Building the application...'
                sh '''
                    mvn clean compile -B -V
                    echo "✅ Build completed successfully"
                '''
            }
            post {
                failure {
                    echo '❌ Build failed!'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                echo '🧪 Running unit tests...'
                sh '''
                    mvn test -B
                    echo "✅ Unit tests completed"
                '''
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                }
                failure {
                    echo '❌ Unit tests failed!'
                }
            }
        }

        stage('Code Quality Analysis') {
            environment {
                SCANNER_HOME = tool 'SonarQube-Scanner'
            }
            steps {
                echo '📊 Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube-Server') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=\${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName="\${SONAR_PROJECT_NAME}" \
                            -Dsonar.host.url=\${SONAR_HOST_URL} \
                            -Dsonar.login=\${SONAR_AUTH_TOKEN}
                    '''
                }
                echo '✅ SonarQube analysis completed'
            }
        }

        stage('Quality Gate') {
            steps {
                echo '🚪 Waiting for SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo '✅ Quality Gate passed!'
            }
            post {
                failure {
                    echo '❌ Quality Gate failed! Pipeline aborted.'
                }
            }
        }

        stage('Package') {
            steps {
                echo '📦 Packaging the application...'
                sh '''
                    mvn package -DskipTests -B
                    echo "✅ Application packaged successfully"
                '''
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
                failure {
                    echo '❌ Packaging failed!'
                }
            }
        }

        stage('Docker Build') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo '🐳 Building Docker image...'
                script {
                    def dockerImage = docker.build("\${DOCKER_IMAGE}:\${DOCKER_TAG}", "-f docker/Dockerfile .")
                    dockerImage.tag("\${DOCKER_IMAGE}:latest")

                    echo "✅ Docker image built: \${DOCKER_IMAGE}:\${DOCKER_TAG}"
                }
            }
            post {
                failure {
                    echo '❌ Docker build failed!'
                }
            }
        }
    }

    post {
        always {
            echo '🧹 Cleaning up workspace...'
            cleanWs()
        }

        success {
            echo '🎉 Pipeline completed successfully!'
        }

        failure {
            echo '❌ Pipeline failed!'
        }

        unstable {
            echo '⚠️ Pipeline completed with warnings!'
        }
    }
}