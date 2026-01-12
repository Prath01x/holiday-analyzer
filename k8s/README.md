# Kubernetes Deployment für Holiday Analyzer

## Voraussetzungen
- Google Cloud SDK installiert (inkl. `gcloud auth login`)
- kubectl installiert
- Projekt: `august-impact-479818-r1`
- Cloud SQL Instanz `holiday-analyzer-db` (PostgreSQL) vorhanden
- Docker Images in Google Container Registry (GCR)

## Infrastruktur hochfahren (nur CLI, keine YAML-Änderungen nötig)
```bash
# 0. Cloud SQL Instanz starten (falls pausiert, ich habe es pausiert)
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=ALWAYS \
  --project=august-impact-479818-r1

# 1. GKE Autopilot Cluster erstellen
gcloud container clusters create-auto holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1

# 2. kubectl auf neuen Cluster richten
gcloud container clusters get-credentials holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```

## Deployment Reihenfolge (nachdem Images gebaut & gepusht wurden)
```bash
# 1. Namespace erstellen
kubectl apply -f namespace.yaml

# 2. ConfigMap und Secrets
kubectl apply -f configmap.yaml
kubectl apply -f secrets.yaml

# 3. Cloud SQL Proxy Secrets (falls geändert)
kubectl apply -f secrets.yaml   # enthält CLOUD_SQL_CONNECTION_NAME etc.

# 4. Backend
kubectl apply -f backend/

# 5. Frontend
kubectl apply -f frontend/

# 6. Ingress (optional – nur mit Domain nötig)
kubectl apply -f ingress.yaml

# 7. Aktuellen Stand prüfen
kubectl get pods -n holiday-analyzer
kubectl get svc -n holiday-analyzer
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

- **PROJECT_ID** in Deployments durch echte GCP Project ID ersetzen
- **Secrets** vor Production-Deployment ändern!
- **Domain** in ingress.yaml anpassen
- Nach jedem Code- oder Manifest-Update: `git add`, `git commit -m "<message>"`, `git push` ausführen, damit die Änderungen versioniert bleiben.

## Infrastruktur stoppen (Kosten sparen)
```bash
# Deployments skalieren/entfernen (optional, der Namespace-Delete erledigt das ebenfalls)
kubectl delete -f frontend/ -n holiday-analyzer
kubectl delete -f backend/ -n holiday-analyzer

# Namespace inkl. Services/LB entfernen
kubectl delete -f namespace.yaml

# Cloud SQL pausieren
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=NEVER \
  --project=august-impact-479818-r1

# GKE Cluster löschen
gcloud container clusters delete holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```
Alle Docker-Images verbleiben in GCR, d.h. bei der nächsten Inbetriebnahme müssen keine Dateien geändert werden – einfach die obigen „hochfahren“-Schritte wiederholen.
