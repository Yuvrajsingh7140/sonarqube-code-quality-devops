# SonarQube Code Quality DevOps Project

A complete DevOps project demonstrating automated code quality checks using SonarQube integration with Jenkins CI/CD pipeline and GitHub Actions.

## 🎯 Project Overview

This project showcases:
- **Automated Code Quality Analysis** using SonarQube
- **CI/CD Pipeline** with Jenkins and GitHub Actions
- **Quality Gates** to prevent poor code from reaching production
- **Docker containerization** for easy deployment
- **Maven** build automation
- **JUnit testing** integration

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Developer     │    │   CI/CD         │    │   SonarQube     │
│   commits code  │───▶│   Pipeline      │───▶│   Analysis      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Build &       │    │   Quality       │
                       │   Test          │    │   Gate Check    │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Docker        │    │   Deploy or     │
                       │   Build         │    │   Block         │
                       └─────────────────┘    └─────────────────┘
```

## 🛠️ Technologies Used

- **Java 17** - Application development
- **Maven 3.8+** - Build automation
- **JUnit 5** - Unit testing
- **SonarQube** - Code quality analysis
- **Jenkins** - CI/CD automation
- **GitHub Actions** - Alternative CI/CD
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📁 Project Structure

```
sonarqube-code-quality-devops/
├── src/
│   ├── main/java/com/example/calculator/
│   │   ├── Calculator.java
│   │   └── CalculatorApplication.java
│   ├── test/java/com/example/calculator/
│   │   └── CalculatorTest.java
│   └── main/resources/
│       └── application.properties
├── .github/workflows/
│   └── ci-cd.yml
├── docker/
│   ├── Dockerfile
│   └── docker-compose.override.yml
├── scripts/
│   ├── setup.sh
│   └── run-sonar-scanner.sh
├── config/
│   ├── sonarqube-server.yml
│   └── jenkins-plugins.txt
├── docs/
│   ├── SETUP.md
│   └── TROUBLESHOOTING.md
├── Jenkinsfile
├── docker-compose.yml
├── sonar-project.properties
├── pom.xml
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/sonarqube-code-quality-devops.git
cd sonarqube-code-quality-devops
```

### 2. Start SonarQube Server

```bash
# Using Docker Compose
docker-compose up -d sonarqube

# Wait for SonarQube to start (usually takes 2-3 minutes)
# Access SonarQube at http://localhost:9000
# Default credentials: admin/admin
```

### 3. Build and Test the Application

```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Run SonarQube analysis
mvn sonar:sonar \
  -Dsonar.projectKey=calculator-app \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=your-sonarqube-token
```

### 4. Run with Docker

```bash
# Build the application
docker build -f docker/Dockerfile -t calculator-app .

# Run the application
docker run -p 8080:8080 calculator-app
```

## 📊 SonarQube Integration

### Configuration Files

1. **sonar-project.properties** - SonarQube project configuration
2. **pom.xml** - Maven SonarQube plugin configuration
3. **Jenkinsfile** - Jenkins pipeline with SonarQube analysis
4. **ci-cd.yml** - GitHub Actions workflow with SonarQube

### Quality Gates

The project includes custom quality gates that check for:
- **Code Coverage** > 80%
- **Duplicated Lines** < 3%
- **Maintainability Rating** = A
- **Reliability Rating** = A
- **Security Rating** = A

## 🔄 CI/CD Workflows

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') { ... }
        stage('Build') { ... }
        stage('Test') { ... }
        stage('SonarQube Analysis') { ... }
        stage('Quality Gate') { ... }
        stage('Build Docker Image') { ... }
        stage('Deploy') { ... }
    }
}
```

### GitHub Actions

Automated workflows for:
- **Pull Request Analysis** - Code quality checks on PRs
- **Main Branch Analysis** - Full analysis on main branch
- **Quality Gate Enforcement** - Block merge if quality gate fails

## 🐳 Docker Support

### Services Included

- **SonarQube Server** - Code analysis server
- **PostgreSQL** - SonarQube database
- **Application Container** - Java calculator app

### Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f sonarqube

# Stop services
docker-compose down
```

## 📈 Monitoring and Reports

### SonarQube Dashboard

Access comprehensive reports at `http://localhost:9000`:

- **Overview** - Project health summary
- **Issues** - Code issues and technical debt
- **Security Hotspots** - Security vulnerabilities
- **Coverage** - Test coverage reports
- **Activity** - Analysis history and trends

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Run tests and SonarQube analysis
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

## 📝 Additional Documentation

- [Setup Guide](docs/SETUP.md) - Detailed setup instructions
- [Troubleshooting](docs/TROUBLESHOOTING.md) - Common issues and solutions

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎯 Learning Objectives

After completing this project, you will understand:
- How to integrate SonarQube with CI/CD pipelines
- Setting up automated code quality checks
- Implementing quality gates to prevent bad code deployment
- Docker containerization best practices
- Jenkins and GitHub Actions automation
- Maven build lifecycle and testing

## 🔗 Useful Links

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Happy Coding!** 🚀
