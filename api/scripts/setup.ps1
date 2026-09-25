# Windows setup script (PowerShell). Run from an elevated PowerShell prompt:
#   Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
#   .\scripts\setup.ps1

$ErrorActionPreference = "Stop"

function Write-Info($msg)  { Write-Host "[info] $msg" -ForegroundColor Cyan }
function Write-Ok($msg)    { Write-Host "[ ok ] $msg" -ForegroundColor Green }
function Write-Warn($msg)  { Write-Host "[warn] $msg" -ForegroundColor Yellow }

$RootDir = Split-Path -Parent $PSScriptRoot

function Install-Chocolatey {
    if (Get-Command choco -ErrorAction SilentlyContinue) {
        Write-Ok "Chocolatey already installed"
        return
    }
    Write-Info "Installing Chocolatey..."
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
}

function Install-Java {
    if (Get-Command java -ErrorAction SilentlyContinue) {
        $v = (java -version 2>&1 | Out-String)
        if ($v -match '"21') {
            Write-Ok "Java 21 already installed"
            return
        }
    }
    Write-Info "Installing Temurin 21 (OpenJDK)..."
    choco install -y temurin21
}

function Install-Docker {
    if (Get-Command docker -ErrorAction SilentlyContinue) {
        Write-Ok "Docker already installed"
        return
    }
    Write-Info "Installing Docker Desktop..."
    choco install -y docker-desktop
    Write-Warn "Start Docker Desktop manually once, then re-run this script."
}

function Set-EnvVars {
    $javaHome = (Get-Command java -ErrorAction SilentlyContinue)
    if ($javaHome) {
        $javaBin = Split-Path -Parent $javaHome.Source
        $javaRoot = Split-Path -Parent $javaBin
        [System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaRoot, "User")
        Write-Ok "JAVA_HOME set to $javaRoot (User scope)"
    }
    [System.Environment]::SetEnvironmentVariable("MAVEN_OPTS", "-Xmx1536m", "User")
    Write-Ok "MAVEN_OPTS set (User scope)"
    Write-Warn "Restart your terminal so the new environment variables are picked up."
}

Install-Chocolatey
Install-Java
Install-Docker
Set-EnvVars

$envFile = Join-Path $RootDir ".env"
$envExample = Join-Path $RootDir ".env.example"
if (-not (Test-Path $envFile)) {
    Copy-Item $envExample $envFile
    Write-Ok "Created .env from .env.example"
} else {
    Write-Info ".env already exists, leaving it untouched"
}

Write-Info "Starting local dependencies (docker compose up -d)..."
Push-Location $RootDir
docker compose up -d
Write-Info "Downloading Maven dependencies..."
.\mvnw.cmd -q -DskipTests dependency:resolve
Pop-Location

Write-Ok "Setup complete! Run 'make run' (or '.\mvnw.cmd spring-boot:run') to start the app."
