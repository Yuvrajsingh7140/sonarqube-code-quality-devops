# Troubleshooting Guide

This guide helps resolve common issues you might encounter while setting up or running the SonarQube DevOps project.

## Common Issues

### 1. SonarQube Server Issues

#### SonarQube won't start

**Symptoms:**
- Container exits immediately
- "Connection refused" errors
- Web UI not accessible at http://localhost:9000

**Solutions:**

1. **Check system resources:**
   ```bash
   # Ensure you have enough memory (minimum 2GB recommended)
   docker stats

   # Check disk space
   df -h
   ```

2. **Increase Docker memory allocation:**
   ```bash
   # For Docker Desktop, go to Settings > Resources > Memory
   # Allocate at least 4GB for smooth operation
   ```

3. **Check if port 9000 is already in use:**
   ```bash
   # Check what's using port 9000
   netstat -tulpn | grep 9000
   # or
   lsof -i :9000

   # Kill the process or use different port in docker-compose.yml
   ```

4. **View detailed logs:**
   ```bash
   docker-compose logs sonarqube
   ```

#### Database connection issues

**Symptoms:**
- "Failed to connect to database" errors
- SonarQube logs show PostgreSQL connection failures

**Solutions:**

1. **Ensure PostgreSQL is running:**
   ```bash
   docker-compose ps postgres
   docker-compose logs postgres
   ```

2. **Reset database:**
   ```bash
   # Stop services
   docker-compose down

   # Remove volumes (WARNING: This deletes all data)
   docker volume rm sonarqube-devops_postgresql_data
   docker volume rm sonarqube-devops_sonarqube_data

   # Restart
   docker-compose up -d
   ```

### 2. Maven Build Issues

#### "mvnw: Permission denied"

**Solution:**
```bash
chmod +x mvnw
```

#### Maven dependencies download fails

**Symptoms:**
- Build fails with "Could not resolve dependencies"
- Network timeout errors

**Solutions:**

1. **Check internet connection and proxy settings:**
   ```bash
   # If behind corporate proxy, configure Maven settings
   cp ~/.m2/settings.xml ~/.m2/settings.xml.backup
   ```

2. **Clear Maven cache:**
   ```bash
   rm -rf ~/.m2/repository
   ./mvnw clean
   ```

3. **Use Maven wrapper offline:**
   ```bash
   ./mvnw -o compile  # Offline mode
   ```

#### Tests fail with "ClassNotFoundException"

**Solution:**
```bash
# Clean and reinstall dependencies
./mvnw clean dependency:purge-local-repository
./mvnw clean compile test
```

### 3. SonarQube Analysis Issues

#### "Project not found" error

**Symptoms:**
- Analysis fails with project key not found
- 404 errors in SonarQube logs

**Solutions:**

1. **Create project manually:**
   - Go to SonarQube UI → Projects → Create Project
   - Use project key: `calculator-app`

2. **Check project key consistency:**
   ```bash
   # Ensure same key in all files:
   # - sonar-project.properties
   # - pom.xml
   # - .env file
   ```

#### Authentication token issues

**Symptoms:**
- "Unauthorized" (401) errors
- "Insufficient privileges" errors

**Solutions:**

1. **Generate new token:**
   - Go to SonarQube → My Account → Security → Generate Tokens
   - Copy the token immediately (it's shown only once)

2. **Update token in all locations:**
   ```bash
   # Update .env file
   SONAR_TOKEN=your-new-token

   # For Jenkins: Update credentials
   # For GitHub Actions: Update repository secrets
   ```

3. **Verify token permissions:**
   ```bash
   # Test token with curl
   curl -u YOUR_TOKEN: http://localhost:9000/api/authentication/validate
   ```

#### Quality Gate failures

**Symptoms:**
- Pipeline fails at Quality Gate step
- Red status in SonarQube dashboard

**Solutions:**

1. **Check specific failures:**
   - Go to SonarQube project dashboard
   - Review "Quality Gate" section
   - Click on failed conditions for details

2. **Common fixes:**
   ```bash
   # Increase test coverage
   ./mvnw test jacoco:report

   # Fix code issues shown in SonarQube
   # Add more unit tests
   # Reduce code duplication
   ```

3. **Temporarily adjust Quality Gate:**
   - In SonarQube: Quality Gates → Edit
   - Adjust thresholds temporarily
   - Plan to meet original standards

### 4. Docker Issues

#### "docker: command not found"

**Solutions:**

1. **Install Docker:**
   ```bash
   # Ubuntu/Debian
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh

   # macOS - Install Docker Desktop
   # Windows - Install Docker Desktop
   ```

2. **Add user to docker group (Linux):**
   ```bash
   sudo usermod -aG docker $USER
   # Logout and login again
   ```

#### Docker build fails

**Symptoms:**
- "No space left on device"
- "Build context too large"

**Solutions:**

1. **Clean Docker resources:**
   ```bash
   # Remove unused containers, networks, images
   docker system prune -a

   # Remove specific volumes
   docker volume prune
   ```

2. **Check .dockerignore:**
   ```bash
   # Ensure target/ is ignored
   echo "target/" >> .dockerignore
   echo "*.log" >> .dockerignore
   echo ".git" >> .dockerignore
   ```

### 5. Jenkins Issues

#### Plugin installation fails

**Solutions:**

1. **Install plugins manually:**
   - Go to Jenkins → Manage Plugins → Available
   - Search and install required plugins
   - Restart Jenkins

2. **Check Jenkins logs:**
   ```bash
   docker-compose logs jenkins
   ```

#### Pipeline fails to checkout

**Symptoms:**
- "Failed to checkout" errors
- Git authentication issues

**Solutions:**

1. **Configure Git credentials:**
   - Jenkins → Manage Credentials → Global
   - Add Git credentials (username/password or SSH key)

2. **Use HTTPS instead of SSH:**
   ```groovy
   // In Jenkinsfile
   git url: 'https://github.com/user/repo.git'
   ```

### 6. Network and Port Issues

#### Port conflicts

**Symptoms:**
- "Port already in use" errors
- Services not accessible

**Solutions:**

1. **Check port usage:**
   ```bash
   # Check what's using your ports
   netstat -tulpn | grep -E ":(8080|9000|5432)"
   ```

2. **Change ports in docker-compose.yml:**
   ```yaml
   services:
     sonarqube:
       ports:
         - "9001:9000"  # Use different host port
     calculator-app:
       ports:
         - "8081:8080"  # Use different host port
   ```

#### Cannot access services from host

**Solutions:**

1. **Check firewall settings:**
   ```bash
   # Ubuntu/Debian
   sudo ufw status
   sudo ufw allow 9000
   sudo ufw allow 8080
   ```

2. **Verify Docker network:**
   ```bash
   docker network ls
   docker network inspect sonarqube-devops_sonar-network
   ```

### 7. Performance Issues

#### Slow build times

**Solutions:**

1. **Use Maven dependency cache:**
   ```bash
   # In GitHub Actions, cache is already configured
   # For local: dependencies are cached in ~/.m2/repository
   ```

2. **Increase Docker resources:**
   - Docker Desktop → Settings → Resources
   - Increase CPU and Memory allocation

3. **Parallel build:**
   ```bash
   ./mvnw clean compile -T 2  # Use 2 threads
   ```

#### SonarQube analysis is slow

**Solutions:**

1. **Exclude unnecessary files:**
   ```properties
   # In sonar-project.properties
   sonar.exclusions=**/target/**,**/*.log,**/node_modules/**
   ```

2. **Optimize analysis scope:**
   ```properties
   sonar.sources=src/main/java
   sonar.tests=src/test/java
   # Don't analyze generated code
   ```

## Getting Help

### Check Logs

1. **Application logs:**
   ```bash
   docker-compose logs calculator-app
   ```

2. **SonarQube logs:**
   ```bash
   docker-compose logs sonarqube
   ```

3. **Build logs:**
   ```bash
   ./mvnw clean compile test -X  # Debug mode
   ```

### Useful Commands

```bash
# Check service health
docker-compose ps

# Restart specific service
docker-compose restart sonarqube

# Check resource usage
docker stats

# View container details
docker inspect sonarqube-server

# Execute into container
docker exec -it sonarqube-server bash

# Check Java version in container
docker exec sonarqube-server java -version
```

### Reset Everything

If all else fails, start fresh:

```bash
# Stop all services
docker-compose down

# Remove all containers and volumes (WARNING: Deletes all data)
docker-compose down -v
docker system prune -a

# Remove project directories
rm -rf target/ logs/ .scannerwork/

# Start over
./scripts/setup.sh
```

### Support Resources

- **SonarQube Documentation:** https://docs.sonarqube.org/
- **Jenkins Documentation:** https://www.jenkins.io/doc/
- **Docker Documentation:** https://docs.docker.com/
- **Maven Documentation:** https://maven.apache.org/guides/

### Creating an Issue

If you need to create an issue, include:

1. **Environment details:**
   - OS and version
   - Docker version
   - Java version
   - Maven version

2. **Steps to reproduce**

3. **Error messages and logs**

4. **Expected vs actual behavior**

---

**Back to:** [Setup Guide](SETUP.md) | [README](../README.md)
