# Kubernetes Deployment für Holiday Analyzer

## Voraussetzungen
- Google Cloud SDK installiert
- kubectl installiert
- GKE Cluster erstellt
- Docker Images in Google Container Registry (GCR)

## Deployment Reihenfolge
```bash
# 1. Namespace erstellen
kubectl apply -f namespace.yaml

# 2. ConfigMap und Secrets
kubectl apply -f configmap.yaml
kubectl apply -f secrets.yaml

# 3. PostgreSQL
kubectl apply -f postgres/

# 4. Backend
kubectl apply -f backend/

# 5. Frontend
kubectl apply -f frontend/

# 6. Ingress (optional)
kubectl apply -f ingress.yaml
```

## Status prüfen
```bash
# Alle Pods anzeigen
kubectl get pods -n holiday-analyzer

# Services anzeigen
kubectl get svc -n holiday-analyzer

# Logs anzeigen
kubectl logs -f deployment/backend -n holiday-analyzer
kubectl logs -f deployment/frontend -n holiday-analyzer
```

## Wichtige Hinweise

- **PROJECT_ID** in Deployments durch echte GCP Project ID ersetzen
- **Secrets** vor Production-Deployment ändern!
- **Domain** in ingress.yaml anpassen
