<#
  run-with-mail.ps1
  Helper script to start the Spring Boot app with MAIL_USERNAME and MAIL_PASSWORD set
  This script prompts for credentials at run-time and does NOT store them on disk.

  Usage: Open PowerShell in project root and run:
    .\run-with-mail.ps1

  The script will prompt for the sending email and for the App Password (masked),
  set them as environment variables for the current process, then run `mvn spring-boot:run`.
#>

# Set email credentials directly
$mailUser = "taquochai12345@gmail.com"
$mailPass = "hhsljzfsemtgcfkt"

# Export to environment for this PowerShell session / child processes
$env:MAIL_USERNAME = $mailUser
$env:MAIL_PASSWORD = $mailPass

Write-Host "Environment variables set for this session. Starting Spring Boot..."

# Run the app (this runs in the same session so env vars are available)
mvn spring-boot:run

# Clear the sensitive variable from memory after process ends
$env:MAIL_PASSWORD = $null
$mailPass = $null
Write-Host "Finished. MAIL_PASSWORD cleared from environment variable."
