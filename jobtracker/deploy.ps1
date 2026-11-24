mvn clean package
if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed. Fix the errors above." -ForegroundColor Red
    exit 1
}
Copy-Item target\jobtracker.war C:\apache-tomcat-10.1.34\webapps\ -Force
Write-Host "Deployed. Wait 5-10 seconds for Tomcat to reload." -ForegroundColor Green