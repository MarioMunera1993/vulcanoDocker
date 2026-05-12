#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Script para levantar todo el stack de Vulcano localmente con Docker Compose

.DESCRIPTION
    Este script automatiza:
    - Verificación de Docker
    - Build de imágenes
    - Levantamiento de servicios
    - Acceso a endpoints

.EXAMPLE
    PS> .\start-local.ps1
    
.NOTES
    Requiere Docker Desktop instalado y ejecutándose
#>

param(
    [Switch]$CleanBuild,
    [Switch]$RemoveVolumes,
    [Switch]$Logs
)

function Show-Banner {
    Write-Host @"
╔═══════════════════════════════════════════════════════╗
║     🌋 VULCANO - Local Development Stack               ║
║       Backend + Frontend + PostgreSQL                   ║
╚═══════════════════════════════════════════════════════╝
"@ -ForegroundColor Cyan
}

function Test-Docker {
    Write-Host "`n✓ Verificando Docker..." -ForegroundColor Yellow
    
    try {
        $docker = docker --version
        Write-Host "  $docker" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "  ✗ Docker no encontrado. Instala Docker Desktop." -ForegroundColor Red
        return $false
    }
}

function Check-Ports {
    Write-Host "`n✓ Verificando puertos..." -ForegroundColor Yellow
    
    $ports = @{
        3000 = "Frontend"
        8080 = "Backend"
        5432 = "PostgreSQL"
    }
    
    foreach ($port in $ports.GetEnumerator()) {
        $connection = Test-NetConnection -ComputerName localhost -Port $port.Key -WarningAction SilentlyContinue
        
        if ($connection.TcpTestSucceeded) {
            Write-Host "  ⚠ Puerto $($port.Key) ($($port.Value)) está en uso" -ForegroundColor Yellow
        } else {
            Write-Host "  ✓ Puerto $($port.Key) ($($port.Value)) disponible" -ForegroundColor Green
        }
    }
}

function Start-Stack {
    Write-Host "`n✓ Iniciando stack..." -ForegroundColor Yellow
    
    $dockerComposeCmd = "docker-compose up"
    
    if ($CleanBuild) {
        $dockerComposeCmd += " --build"
        Write-Host "  (Modo: Rebuild completo)" -ForegroundColor Cyan
    }
    
    if ($RemoveVolumes) {
        Write-Host "  (Nota: Los volúmenes existentes serán removidos)" -ForegroundColor Yellow
        docker-compose down -v
    }
    
    Write-Host "  Ejecutando: $dockerComposeCmd`n" -ForegroundColor Gray
    
    & cmd /c $dockerComposeCmd
}

function Show-Endpoints {
    Write-Host @"

╔═══════════════════════════════════════════════════════╗
║                  🎉 STACK LISTO                       ║
╚═══════════════════════════════════════════════════════╝

📍 Endpoints disponibles:

  🎨 Frontend:
     http://localhost:3000

  🔌 Backend API:
     http://localhost:8080

  📖 Swagger UI (Documentación API):
     http://localhost:8080/swagger-ui.html

  🗄️  PostgreSQL:
     localhost:5432
     Usuario: postgres
     Contraseña: postgres_secure_change_me

╔═══════════════════════════════════════════════════════╗
║                  📋 COMANDOS ÚTILES                   ║
╚═══════════════════════════════════════════════════════╝

Ver logs en tiempo real:
  docker-compose logs -f

Ver logs de un servicio específico:
  docker-compose logs -f backend    # o frontend, postgres

Detener todo:
  docker-compose down

Eliminar datos (base de datos limpia):
  docker-compose down -v

Reconstruir un servicio:
  docker-compose up --build backend  # o frontend

╔═══════════════════════════════════════════════════════╗
║                  🔗 SIGUIENTE PASO                    ║
╚═══════════════════════════════════════════════════════╝

Para desplegar a Railway.app, lee:
  RAILWAY_DEPLOYMENT.md

Para más información sobre testing local:
  LOCAL_TESTING.md

" -ForegroundColor Cyan
}

# Main
Show-Banner

if (-not (Test-Docker)) {
    exit 1
}

Check-Ports

Write-Host "`n" -ForegroundColor Gray

$proceed = Read-Host "¿Deseas continuar? (S/n)"
if ($proceed -eq "n" -or $proceed -eq "N") {
    Write-Host "Abortado." -ForegroundColor Yellow
    exit 0
}

Start-Stack

Show-Endpoints
