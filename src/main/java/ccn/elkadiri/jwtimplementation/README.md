# 🔐 Projet JWT Authentication avec Spring Boot

## 📋 Description

Application de démonstration d'authentification JWT (JSON Web Token) avec Spring Boot et Spring Security.

## 🚀 Démarrage rapide

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- Un IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. Cloner le projet
2. Ouvrir avec votre IDE
3. Lancer l'application:
```bash
   mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## 🧪 Test de l'API

### Utilisateurs disponibles

| Username | Password   | Rôle            |
|----------|-----------|-----------------|
| user     | password  | USER            |
| admin    | admin123  | ADMIN           |
| manager  | manager123| USER, MANAGER   |

### 1. Obtenir un token JWT

**Requête:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
```

**Réponse:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Utiliser le token
```bash
curl -X GET http://localhost:8080/api/hello \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Réponse:**
```json
{
  "message": "Bonjour, endpoint protégé OK ✅"
}
```

### 3. Vérifier le statut (endpoint public)
```bash
curl -X GET http://localhost:8080/api/auth/status
```

## 📡 Endpoints disponibles

| Méthode | Endpoint          | Authentification | Description                    |
|---------|------------------|------------------|--------------------------------|
| POST    | /api/auth/login  | Non              | Obtenir un token JWT           |
| GET     | /api/auth/status | Non              | Vérifier le statut de l'API    |
| GET     | /api/hello       | Oui              | Endpoint protégé de test       |
| GET     | /api/profile     | Oui              | Profil utilisateur             |

## 🔍 Test avec Postman

### Collection Postman

1. **Login**
    - Method: POST
    - URL: `http://localhost:8080/api/auth/login`
    - Body (raw JSON):
```json
     {
       "username": "user",
       "password": "password"
     }
```

2. **Hello (Protégé)**
    - Method: GET
    - URL: `http://localhost:8080/api/hello`
    - Headers:
        - Key: `Authorization`
        - Value: `Bearer YOUR_TOKEN`

## 🛠️ Configuration

### Modifier la durée du token

Dans `application.properties`:
```properties
# 1 heure (défaut)
app.jwt.expiration-ms=3600000

# 24 heures
app.jwt.expiration-ms=86400000

# 7 jours
app.jwt.expiration-ms=604800000
```

### Changer le secret JWT

⚠️ **IMPORTANT EN PRODUCTION:**
```properties
# Mauvais (exemple de démo)
app.jwt.secret=ChangeThisSecretToAStrongOne_32chars_min

# Bon (production avec variable d'environnement)
app.jwt.secret=${JWT_SECRET}
```

Puis définir la variable:
```bash
export JWT_SECRET="VotreCléSecrèteTrèsLongueEtComplexe123456789"
```

## 🏗️ Architecture