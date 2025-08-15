#!/bin/bash

# SonarQube DevOps Project Setup Script
# This script sets up the complete development environment

set -e  # Exit on any error

echo "🚀 Setting up SonarQube DevOps Project Environment..."
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
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

# Check if required tools are installed
check_prerequisites() {
    print_status "Checking prerequisites..."

    # Check Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi

    # Check Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi

    # Check Java
    if ! command -v java &> /dev/null; then
        print_warning "Java is not installed. Some features may not work."
    fi

    # Check Maven
    if ! command -v mvn &> /dev/null; then
        print_warning "Maven is not installed. Using Maven wrapper instead."
    fi

    print_success "Prerequisites check completed!"
}

# Create necessary directories
create_directories() {
    print_status "Creating necessary directories..."

    mkdir -p target/surefire-reports
    mkdir -p target/site/jacoco
    mkdir -p logs
    mkdir -p reports

    print_success "Directories created!"
}

# Set up environment files
setup_environment() {
    print_status "Setting up environment configuration..."

    # Create .env file if it doesn't exist
    if [ ! -f .env ]; then
        cat > .env << EOF
# SonarQube Configuration
SONAR_HOST_URL=http://localhost:9000
SONAR_TOKEN=your-sonarqube-token-here
SONAR_PROJECT_KEY=calculator-app

# Database Configuration
POSTGRES_USER=sonarqube
POSTGRES_PASSWORD=sonarpass123
POSTGRES_DB=sonarqube

# Application Configuration
APP_PORT=8080
APP_ENV=development

# Docker Configuration
COMPOSE_PROJECT_NAME=sonarqube-devops
EOF
        print_warning ".env file created. Please update SONAR_TOKEN with your actual token."
    else
        print_status ".env file already exists."
    fi

    print_success "Environment configuration completed!"
}

# Start infrastructure services
start_infrastructure() {
    print_status "Starting infrastructure services..."

    # Start SonarQube and PostgreSQL
    docker-compose up -d sonarqube postgres

    print_status "Waiting for services to be ready..."
    sleep 30

    # Check if SonarQube is ready
    max_attempts=12
    attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:9000/api/system/status | grep -q "UP"; then
            print_success "SonarQube is ready!"
            break
        else
            print_status "Waiting for SonarQube to start (attempt $attempt/$max_attempts)..."
            sleep 10
            ((attempt++))
        fi
    done

    if [ $attempt -gt $max_attempts ]; then
        print_error "SonarQube failed to start within the expected time."
        exit 1
    fi

    print_success "Infrastructure services started successfully!"
}

# Build and test the application
build_application() {
    print_status "Building and testing the application..."

    # Make Maven wrapper executable
    chmod +x mvnw

    # Clean and compile
    ./mvnw clean compile

    # Run tests
    ./mvnw test

    # Generate reports
    ./mvnw jacoco:report

    # Package the application
    ./mvnw package -DskipTests

    print_success "Application built and tested successfully!"
}

# Run SonarQube analysis
run_sonar_analysis() {
    print_status "Running SonarQube analysis..."

    # Check if SONAR_TOKEN is set
    if grep -q "your-sonarqube-token-here" .env; then
        print_warning "Please update SONAR_TOKEN in .env file and run: ./scripts/run-sonar-scanner.sh"
    else
        source .env
        ./mvnw sonar:sonar \
            -Dsonar.projectKey=$SONAR_PROJECT_KEY \
            -Dsonar.host.url=$SONAR_HOST_URL \
            -Dsonar.login=$SONAR_TOKEN

        print_success "SonarQube analysis completed!"
        print_status "View results at: http://localhost:9000"
    fi
}

# Main execution
main() {
    echo "Starting setup process..."
    echo

    check_prerequisites
    echo

    create_directories
    echo

    setup_environment
    echo

    start_infrastructure
    echo

    build_application
    echo

    run_sonar_analysis
    echo

    print_success "🎉 Setup completed successfully!"
    echo
    echo "Next steps:"
    echo "1. Access SonarQube at: http://localhost:9000 (admin/admin)"
    echo "2. Generate a token and update .env file"
    echo "3. Run: ./scripts/run-sonar-scanner.sh"
    echo "4. Start the application: docker-compose up calculator-app"
    echo "5. Access the app at: http://localhost:8080"
    echo
    echo "For more information, see docs/SETUP.md"
}

# Run main function
main "$@"
