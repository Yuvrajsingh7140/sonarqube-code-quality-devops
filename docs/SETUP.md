# Setup Guide

This guide provides detailed instructions for setting up the SonarQube Code Quality DevOps project.

## Prerequisites

Before you begin, ensure you have the following installed:

### Required
- **Docker** (20.10+) and **Docker Compose** (2.0+)
- **Git** (2.30+)
- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.8+** (or use the included wrapper)

### Optional (for advanced usage)
- **Jenkins** (2.400+)
- **Node.js** (16+) for additional tooling
- **curl** for API testing

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/sonarqube-code-quality-devops.git
cd sonarqube-code-quality-devops
```

### 2. Run the Setup Script

```bash
# Make the script executable (if needed)
chmod +x scripts/setup.sh

# Run the setup script
./scripts/setup.sh
```

This script will:
- Check prerequisites
- Create necessary directories
- Set up environment configuration
- Start SonarQube and PostgreSQL
- Build and test the application

### 3. Configure SonarQube Token

1. Access SonarQube at http://localhost:9000
2. Login with default credentials: `admin` / `admin`
3. Change the password when prompted
4. Go to **My Account** → **Security** → **Generate Tokens**
5. Create a new token (e.g., "jenkins-token")
6. Update the `.env` file with your token:

```bash
# Edit .env file
SONAR_TOKEN=your-actual-token-here
```

### 4. Run SonarQube Analysis

```bash
./scripts/run-sonar-scanner.sh
```

### 5. Start the Application

```bash
# Start all services
docker-compose up -d

# Or start just the application
docker-compose up calculator-app
```

## Manual Setup

If you prefer to set up manually:

### 1. Start Infrastructure Services

```bash
# Start SonarQube and PostgreSQL
docker-compose up -d sonarqube postgres

# Wait for services to be ready (2-3 minutes)
docker-compose logs -f sonarqube
```

### 2. Build the Application

```bash
# Clean and compile
./mvnw clean compile

# Run tests with coverage
./mvnw test jacoco:report

# Package the application
./mvnw package -DskipTests
```

### 3. Run SonarQube Analysis

```bash
# With Maven
./mvnw sonar:sonar \
  -Dsonar.projectKey=calculator-app \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_TOKEN_HERE

# Or with SonarScanner CLI
sonar-scanner \
  -Dsonar.projectKey=calculator-app \
  -Dsonar.sources=src/main/java \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_TOKEN_HERE
```

### 4. Build Docker Image

```bash
# Build the application image
docker build -f docker/Dockerfile -t calculator-app:latest .

# Run the container
docker run -d -p 8080:8080 --name calculator calculator-app:latest
```

## Jenkins Setup

### 1. Install Jenkins Plugins

Install the required plugins from `config/jenkins-plugins.txt`:

- SonarQube Scanner
- Maven Integration
- Docker Pipeline
- JUnit
- JaCoCo

### 2. Configure SonarQube Server

1. Go to **Manage Jenkins** → **Configure System**
2. Find **SonarQube servers** section
3. Add server:
   - Name: `SonarQube-Server`
   - Server URL: `http://localhost:9000`
   - Server authentication token: Add your SonarQube token as Secret Text

### 3. Configure Tools

Go to **Manage Jenkins** → **Global Tool Configuration**:

**Maven:**
- Name: `Maven-3.9.0`
- MAVEN_HOME: `/usr/share/maven` (or auto-install)

**JDK:**
- Name: `JDK-17`
- JAVA_HOME: `/usr/lib/jvm/java-17-openjdk` (or auto-install)

**SonarQube Scanner:**
- Name: `SonarQube-Scanner`
- Install automatically from Maven Central

### 4. Create Pipeline Job

1. Create new **Pipeline** job
2. Configure **Pipeline script from SCM**:
   - SCM: Git
   - Repository URL: Your repository URL
   - Script Path: `Jenkinsfile`

## GitHub Actions Setup

### 1. Repository Secrets

Add the following secrets to your GitHub repository:

- `SONAR_TOKEN`: Your SonarQube authentication token
- `SONAR_HOST_URL`: `http://your-sonarqube-server:9000`

### 2. Enable Actions

1. Go to repository **Settings** → **Actions**
2. Enable **Actions** for the repository
3. Workflows will run automatically on push/PR

## Environment Configuration

### Production Environment

Create a production configuration:

```bash
# Copy and modify for production
cp .env .env.production

# Update production values
SONAR_HOST_URL=https://sonarqube.yourdomain.com
APP_ENV=production
```

### Development Environment

The default configuration is optimized for development:

```bash
# Development settings in .env
APP_ENV=development
LOGGING_LEVEL=DEBUG
```

## Troubleshooting

See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for common issues and solutions.

## Next Steps

1. **Customize Quality Gates**: Adjust rules in SonarQube UI
2. **Add More Tests**: Increase code coverage
3. **Configure Notifications**: Set up email/Slack alerts
4. **Security Scanning**: Add OWASP Dependency Check
5. **Performance Testing**: Add JMeter or similar tools

## Support

For issues and questions:
1. Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
2. Review SonarQube logs: `docker-compose logs sonarqube`
3. Check application logs: `docker-compose logs calculator-app`
4. Create an issue in the repository

---

**Next**: [Troubleshooting Guide](TROUBLESHOOTING.md)
