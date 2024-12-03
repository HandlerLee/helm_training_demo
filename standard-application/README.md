# Standard Application Helm Chart

A Helm chart for deploying standard applications on Kubernetes with production-ready defaults.

## Overview

This Helm chart provides a standardized way to deploy applications on Kubernetes with common configurations and best practices. It includes support for:

- Deployments with configurable strategies
- Services (ClusterIP, NodePort, LoadBalancer)
- Ingress with TLS support
- ConfigMaps and Secrets management
- Service Accounts with RBAC
- Horizontal Pod Autoscaling (HPA)
- Resource management and limits
- Health checks and probes
- Pod disruption budgets
- Pod security contexts

## Prerequisites

- Kubernetes 1.16+
- Helm 3.x
- Ingress controller (if using Ingress)
- Metrics server (if using HPA)

## Installation

```bash
# Add the repository

# Install the chart
helm install my-release standard-application/standard-application

# Install with custom values
helm install my-release standard-application/standard-application -f values.yaml
```

## Configuration

### Global Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `nameOverride` | Override the name of the chart | `"fdal-api"` |
| `fullnameOverride` | Override the full name of the resources | `""` |
| `commonLabels` | Labels to add to all deployed objects | `{}` |
| `commonAnnotations` | Annotations to add to all deployed objects | `{}` |
| `podAnnotations` | Annotations to add to pods | `{}` |
| `podLabels` | Labels to add to pods | `{}` |

### Image Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `image.repository` | Container image repository | `nginx` |
| `image.tag` | Container image tag | `""` (Chart's appVersion) |
| `image.pullPolicy` | Container image pull policy | `IfNotPresent` |
| `imagePullSecrets` | Image pull secrets | `[]` |

### Deployment Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `replicaCount` | Number of replicas | `1` |
| `strategy.type` | Deployment strategy type | `RollingUpdate` |
| `strategy.rollingUpdate` | Rolling update configuration | `{}` |
| `resources.limits.cpu` | CPU resource limits | `100m` |
| `resources.limits.memory` | Memory resource limits | `128Mi` |
| `resources.requests.cpu` | CPU resource requests | `100m` |
| `resources.requests.memory` | Memory resource requests | `128Mi` |
| `nodeSelector` | Node selector configuration | `{}` |
| `tolerations` | Pod tolerations | `[]` |
| `affinity` | Pod affinity rules | `{}` |

### Service Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `service.type` | Kubernetes service type | `ClusterIP` |
| `service.port` | Service port | `80` |
| `service.targetPort` | Service target port | `http` |
| `service.nodePort` | Node port (when type is NodePort) | `""` |
| `service.annotations` | Service annotations | `{}` |

### Ingress Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `ingress.enabled` | Enable ingress | `true` |
| `ingress.className` | Ingress class name | `""` |
| `ingress.annotations` | Ingress annotations | `{}` |
| `ingress.hosts` | Ingress hosts configuration | See values.yaml |
| `ingress.tls` | Ingress TLS configuration | `[]` |

### Security Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `podSecurityContext` | Pod security context | `{}` |
| `containerSecurityContext` | Container security context | `{}` |
| `serviceAccount.create` | Create service account | `true` |
| `serviceAccount.annotations` | Service account annotations | `{}` |
| `serviceAccount.name` | Service account name | `""` |

### Autoscaling Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `autoscaling.enabled` | Enable autoscaling | `true` |
| `autoscaling.minReplicas` | Minimum number of replicas | `1` |
| `autoscaling.maxReplicas` | Maximum number of replicas | `100` |
| `autoscaling.targetCPUUtilizationPercentage` | Target CPU utilization | `80` |
| `autoscaling.targetMemoryUtilizationPercentage` | Target memory utilization | `""` |

### ConfigMap Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `configmap.enabled` | Enable ConfigMap creation | `true` |
| `configmap.fileName` | Configuration file path | `"files/myconfig.properties"` |
| `configmap.data` | ConfigMap data | `{}` |
| `configmap.customLabels` | Custom labels for ConfigMap | `{}` |
| `configmap.annotations` | Annotations for ConfigMap | See values.yaml |

### Probe Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `livenessProbe.enabled` | Enable liveness probe | `true` |
| `livenessProbe.initialDelaySeconds` | Initial delay for liveness probe | `20` |
| `livenessProbe.periodSeconds` | Period for liveness probe | `30` |
| `livenessProbe.failureThreshold` | Failure threshold for liveness probe | `3` |
| `readinessProbe.enabled` | Enable readiness probe | `true` |
| `readinessProbe.initialDelaySeconds` | Initial delay for readiness probe | `20` |
| `readinessProbe.periodSeconds` | Period for readiness probe | `30` |
| `readinessProbe.failureThreshold` | Failure threshold for readiness probe | `3` |

## Examples

### Basic Installation

```bash
helm install my-app ./standard-application \
  --set nameOverride=my-application \
  --set image.repository=my-repo/my-app \
  --set image.tag=1.0.0
```

### Production Configuration

```yaml
# values.yaml
replicaCount: 3

resources:
  limits:
    cpu: 1000m
    memory: 1Gi
  requests:
    cpu: 500m
    memory: 512Mi

autoscaling:
  enabled: true
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 75

ingress:
  enabled: true
  className: nginx
  annotations:
    cert-manager.io/cluster-issuer: letsencrypt-prod
  hosts:
    - host: myapp.example.com
      paths:
        - path: /
          pathType: Prefix
  tls:
    - secretName: myapp-tls
      hosts:
        - myapp.example.com
```

### Configuring Probes

```yaml
livenessProbe:
  enabled: true
  httpGet:
    path: /health
    port: http
  initialDelaySeconds: 30
  periodSeconds: 10
  timeoutSeconds: 5
  failureThreshold: 3

readinessProbe:
  enabled: true
  httpGet:
    path: /ready
    port: http
  initialDelaySeconds: 20
  periodSeconds: 10
  timeoutSeconds: 3
  failureThreshold: 3
```

## Best Practices

- Always set resource requests and limits
- Enable security contexts for pods and containers
- Configure appropriate health checks
- Use rolling updates with appropriate maxSurge and maxUnavailable values
- Set up monitoring and logging
- Use pod disruption budgets for high-availability applications
- Configure appropriate node affinity and anti-affinity rules

## Troubleshooting

Common issues and solutions:

1. Pods not scheduling
   - Check resource requests and limits
   - Verify node resources availability
   - Check node selectors and tolerations

2. Ingress not working
   - Verify ingress controller is installed
   - Check ingress class name
   - Validate TLS configuration

3. Autoscaling issues
   - Verify metrics-server is installed
   - Check HPA configuration
   - Validate resource metrics

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This Helm chart is available under the Apache 2.0 license.
