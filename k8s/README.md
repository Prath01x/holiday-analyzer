# Kubernetes Deployment für Holiday Analyzer

## Voraussetzungen
- Google Cloud SDK installiert
- kubectl installiert
- GKE Cluster erstellt
- Docker Images in Google Container Registry (GCR)

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

# 6. Ingress (optional)
kubectl apply -f ingress.yaml //we dont have domain yet

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
