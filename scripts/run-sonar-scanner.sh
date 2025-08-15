#!/bin/bash

# SonarQube Scanner Script
# This script runs SonarQube analysis with proper configuration

set -e

echo "🔍 Running SonarQube Code Quality Analysis..."
echo "=============================================="

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Load environment variables
if [ -f .env ]; then
    source .env
    print_status "Environment variables loaded from .env"
else
    print_warning ".env file not found, using default values"
    SONAR_HOST_URL=${SONAR_HOST_URL:-http://localhost:9000}
    SONAR_PROJECT_KEY=${SONAR_PROJECT_KEY:-calculator-app}
fi

# Check if SonarQube is running
print_status "Checking SonarQube server availability..."
if ! curl -s -f "$SONAR_HOST_URL/api/system/status" > /dev/null; then
    print_error "SonarQube server is not accessible at $SONAR_HOST_URL"
    print_status "Please make sure SonarQube is running: docker-compose up -d sonarqube"
    exit 1
fi

print_success "SonarQube server is accessible!"

# Check if token is configured
if [ -z "$SONAR_TOKEN" ] || [ "$SONAR_TOKEN" = "your-sonarqube-token-here" ]; then
    print_error "SONAR_TOKEN is not configured!"
    echo
    echo "To configure SonarQube token:"
    echo "1. Go to $SONAR_HOST_URL"
    echo "2. Login with admin/admin"
    echo "3. Go to My Account > Security > Generate Tokens"
    echo "4. Create a new token"
    echo "5. Update SONAR_TOKEN in .env file"
    exit 1
fi

# Ensure project is built
print_status "Ensuring project is built and tested..."
if [ ! -f target/*.jar ] || [ ! -d target/surefire-reports ]; then
    print_status "Building project..."
    ./mvnw clean compile test jacoco:report package -B
fi

print_success "Project is ready for analysis!"

# Run SonarQube analysis
print_status "Running SonarQube analysis..."

./mvnw sonar:sonar \
    -Dsonar.projectKey="$SONAR_PROJECT_KEY" \
    -Dsonar.projectName="Calculator Application" \
    -Dsonar.host.url="$SONAR_HOST_URL" \
    -Dsonar.login="$SONAR_TOKEN" \
    -Dsonar.java.binaries=target/classes \
    -Dsonar.java.test.binaries=target/test-classes \
    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
    -Dsonar.junit.reportPaths=target/surefire-reports \
    -Dsonar.sources=src/main/java \
    -Dsonar.tests=src/test/java \
    -Dsonar.java.source=17 \
    -Dsonar.sourceEncoding=UTF-8 \
    -Dsonar.exclusions="**/*Application.java" \
    -Dsonar.coverage.exclusions="**/*Application.java,**/config/**" \
    -Dsonar.qualitygate.wait=true \
    -B

if [ $? -eq 0 ]; then
    print_success "✅ SonarQube analysis completed successfully!"
    echo
    echo "📊 View detailed results at: $SONAR_HOST_URL/dashboard?id=$SONAR_PROJECT_KEY"
    echo
    print_status "Quality Gate Status: PASSED ✅"
else
    print_error "❌ SonarQube analysis failed or Quality Gate did not pass!"
    echo
    echo "📊 View detailed results at: $SONAR_HOST_URL/dashboard?id=$SONAR_PROJECT_KEY"
    echo
    print_status "Quality Gate Status: FAILED ❌"
    exit 1
fi
