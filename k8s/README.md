# Kubernetes Deployment für Holiday Analyzer

## ⚠️ Secrets Management

**WICHTIG**: Vor dem Deployment müssen Sie die Secrets konfigurieren!

```bash
# 1. Kopieren Sie die Beispieldatei
cp secrets.yaml.example secrets.yaml

# 2. Bearbeiten Sie secrets.yaml und ersetzen Sie die Platzhalter:
#    - POSTGRES_PASSWORD: Ihr Cloud SQL Passwort
#    - JWT_SECRET: Generieren Sie einen sicheren Key (z.B. mit: openssl rand -base64 64)
#    - CLOUD_SQL_CONNECTION_NAME: Ihr Projekt:Region:Instanz (z.B. august-impact-479818-r1:europe-west3:holiday-analyzer-db)

# 3. secrets.yaml wird NICHT in Git committed (ist in .gitignore)
```

## Voraussetzungen
- Google Cloud SDK installiert (inkl. `gcloud auth login`)
- kubectl installiert
- Projekt: `august-impact-479818-r1`
- Cloud SQL Instanz `holiday-analyzer-db` (PostgreSQL) vorhanden
- Docker Images in Google Container Registry (GCR)
- **Zugriff auf GCP Projekt** (Owner/Editor Rolle erforderlich)
- **Secrets konfiguriert** (siehe oben)

## Infrastruktur hochfahren (nur CLI, keine YAML-Änderungen nötig)

### 1. Cloud SQL Instanz starten
```bash
# Falls pausiert, Instanz aktivieren
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=ALWAYS \
  --project=august-impact-479818-r1
```

### 2. GKE Autopilot Cluster erstellen
```bash
gcloud container clusters create-auto holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```

### 3. kubectl konfigurieren
```bash
# kubectl auf neuen Cluster richten
gcloud container clusters get-credentials holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```

### 4. Workload Identity für Cloud SQL einrichten

**WICHTIG**: Diese Schritte sind nur **einmalig** beim ersten Setup nötig!

```bash
# 4.1 Kubernetes ServiceAccount erstellen
kubectl create serviceaccount backend-sa -n holiday-analyzer

# 4.2 Google ServiceAccount erstellen
gcloud iam service-accounts create backend-cloudsql-sa \
  --display-name="Backend Cloud SQL Service Account" \
  --project=august-impact-479818-r1

# 4.3 Cloud SQL Client Rolle zuweisen
gcloud projects add-iam-policy-binding august-impact-479818-r1 \
  --member="serviceAccount:backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com" \
  --role="roles/cloudsql.client"

# 4.4 Workload Identity Binding erstellen
gcloud iam service-accounts add-iam-policy-binding \
  backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com \
  --role roles/iam.workloadIdentityUser \
  --member "serviceAccount:august-impact-479818-r1.svc.id.goog[holiday-analyzer/backend-sa]" \
  --project=august-impact-479818-r1

# 4.5 ServiceAccount Token Creator Rolle zuweisen
gcloud iam service-accounts add-iam-policy-binding \
  backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com \
  --role roles/iam.serviceAccountTokenCreator \
  --member "serviceAccount:august-impact-479818-r1.svc.id.goog[holiday-analyzer/backend-sa]" \
  --project=august-impact-479818-r1

# 4.6 Kubernetes ServiceAccount annotieren
kubectl annotate serviceaccount backend-sa -n holiday-analyzer \
  iam.gke.io/gcp-service-account=backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com
```

**Was macht Workload Identity?**
- Ermöglicht Backend-Pods sicheren Zugriff auf Cloud SQL
- Verwendet `serviceAccountName: backend-sa` in backend-deployment.yaml
- Ohne diese Konfiguration: **403 Forbidden** Fehler beim Cloud SQL Zugriff

## Deployment Reihenfolge (nachdem Images gebaut & gepusht wurden)
```bash
# 1. Namespace erstellen
kubectl apply -f namespace.yaml

# 2. ConfigMap und Secrets
kubectl apply -f configmap.yaml
kubectl apply -f secrets.yaml

# 3. Backend (inkl. Cloud SQL Proxy Sidecar)
kubectl apply -f backend/

# 4. Frontend
kubectl apply -f frontend/

# 5. Ingress (optional – nur mit Domain nötig)
kubectl apply -f ingress.yaml

# 6. Aktuellen Stand prüfen
kubectl get pods -n holiday-analyzer
kubectl get svc -n holiday-analyzer

# Frontend URL abrufen (LoadBalancer External IP)
kubectl get svc frontend-service -n holiday-analyzer
```

## Container Images bauen & pushen
```bash
# Backend
docker build -t gcr.io/august-impact-479818-r1/holiday-backend:latest -f backend/Dockerfile backend
docker push gcr.io/august-impact-479818-r1/holiday-backend:latest

# Frontend
docker build -t gcr.io/august-impact-479818-r1/holiday-frontend:latest -f frontend/Dockerfile frontend
docker push gcr.io/august-impact-479818-r1/holiday-frontend:latest

# Rollout der neuen Images
kubectl rollout restart deploy/backend -n holiday-analyzer
kubectl rollout restart deploy/frontend -n holiday-analyzer
kubectl rollout status deploy/backend -n holiday-analyzer
kubectl rollout status deploy/frontend -n holiday-analyzer
```

## Wichtige Hinweise

- **Secrets**: `secrets.yaml` enthält Passwörter - vor echtem Production-Deployment ändern!
- **Domain**: `ingress.yaml` anpassen falls eigene Domain verwendet wird
- **Workload Identity**: Muss nur einmal beim ersten Setup konfiguriert werden (Schritt 4)
- **ddl-auto**: Aktuell auf `update` gesetzt - erstellt Schema automatisch bei leerem DB
- Nach jedem Code- oder Manifest-Update: `git add`, `git commit -m "<message>"`, `git push` ausführen

## Für Lucas (Neues Team-Mitglied Setup)

### Erstmaliges Setup (wenn Cluster nicht existiert):
1. Führe **alle Schritte 1-4** aus (inkl. Workload Identity)
2. Deploye die Anwendung (Deployment Reihenfolge)
3. Baue und pushe Docker Images falls nötig

### Wenn Cluster bereits existiert (von anderem Team-Mitglied erstellt):
1. **Nur Schritt 3** ausführen (kubectl konfigurieren)
2. Workload Identity überspringen - ist bereits konfiguriert!
3. Deploye die Anwendung falls nötig

### Zugriff auf GCP Projekt erhalten:
Lucas braucht IAM Rolle im Projekt `august-impact-479818-r1`:
```bash
# Als Projekt-Owner ausführen:
gcloud projects add-iam-policy-binding august-impact-479818-r1 \
  --member="user:lucas@example.com" \
  --role="roles/editor"
```

Ersetze `lucas@example.com` mit Lucas' Google Account Email.

## Graceful Shutdown (Kosten sparen)

**Einfacher 3-Schritte Prozess:**

```bash
# Schritt 1: Cluster löschen (entfernt automatisch alle Pods, Services, Load Balancer)
gcloud container clusters delete holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1 \
  --quiet

# Schritt 2: Cloud SQL pausieren (kostet nur ~$0.50/Monat wenn pausiert)
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=NEVER \
  --project=august-impact-479818-r1

# Schritt 3: Fertig! 
# - Docker Images bleiben in GCR (kostenlos bis 0.5GB)
# - Service Accounts bleiben erhalten (kostenlos)
# - Workload Identity Konfiguration bleibt erhalten (kostenlos)
```

**Was wird gelöscht:**
- GKE Cluster (~$70-100/Monat gespart)
- Load Balancer (automatisch mit Cluster gelöscht)
- Alle Pods und Services (automatisch mit Cluster gelöscht)

**Was bleibt erhalten (kostenlos):**
- Docker Images in GCR
- Service Accounts (backend-cloudsql-sa)
- Workload Identity Bindings
- Cloud SQL Daten (pausiert, ~$0.50/Monat)

## Restart (Super einfach!)

**Nur 3 Schritte - keine YAML Änderungen, keine Workload Identity Setup!**

```bash
# Schritt 1: Cloud SQL aktivieren
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=ALWAYS \
  --project=august-impact-479818-r1

# Schritt 2: Cluster erstellen
gcloud container clusters create-auto holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1

# Schritt 3: kubectl konfigurieren
gcloud container clusters get-credentials holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1

# Schritt 4: Anwendung deployen
kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f secrets.yaml
kubectl apply -f backend/
kubectl apply -f frontend/

# Schritt 5: Status prüfen
kubectl get pods -n holiday-analyzer
kubectl get svc frontend-service -n holiday-analyzer
```

**Wichtig:** 
- **KEINE Workload Identity Setup nötig** - ist bereits konfiguriert!
- **KEINE YAML Dateien ändern** - alles bleibt gleich!
- Dauert ~5-10 Minuten bis alles läuft
- Daten bleiben erhalten (ddl-auto=update)

## Quick Commands

### Status prüfen:
```bash
kubectl get pods -n holiday-analyzer
kubectl get svc -n holiday-analyzer
```

### Frontend URL abrufen:
```bash
kubectl get svc frontend-service -n holiday-analyzer -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

### Logs ansehen:
```bash
# Backend logs
kubectl logs -l app=backend -n holiday-analyzer -c backend --tail=50

# Frontend logs
kubectl logs -l app=frontend -n holiday-analyzer --tail=50
```
