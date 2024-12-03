# ArgoCD Applications Helm Chart

A Helm chart for managing ArgoCD applications declaratively.

## Overview

This Helm chart provides a way to manage multiple ArgoCD applications using Helm. It allows you to define your ArgoCD applications as values and generates the corresponding Application CRDs.

## Prerequisites

- Kubernetes 1.16+
- Helm 3.x
- ArgoCD installed in your cluster
- kubectl access to the cluster

## Installation

```bash
# Install the chart
helm install my-argocd-apps ./argocd-apps -n argocd

# Install with custom values
helm install my-argocd-apps ./argocd-apps -n argocd -f values.yaml
```

## Configuration

### Global Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `nameOverride` | Override the name of the chart | `""` |
| `fullnameOverride` | Override the full name of the resources | `""` |
| `commonLabels` | Labels to add to all resources | `{}` |
| `commonAnnotations` | Annotations to add to all resources | `{}` |

### Application Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `applications` | List of ArgoCD applications to create | `[]` |
| `projects` | List of ArgoCD projects to create | `[]` |
| `sourceRepos` | List of allowed source repositories | `[]` |
| `destinations` | List of allowed destination clusters/namespaces | `[]` |

## Examples

### Basic Application

```yaml
applications:
  - name: my-application
    namespace: default
    project: default
    source:
      repoURL: https://github.com/org/repo
      targetRevision: HEAD
      path: kubernetes/
    destination:
      server: https://kubernetes.default.svc
      namespace: my-app
    syncPolicy:
      automated:
        prune: true
        selfHeal: true
```

### Multiple Applications with Project

```yaml
projects:
  - name: my-project
    description: "My Project"
    sourceRepos:
      - "https://github.com/org/*"
    destinations:
      - namespace: "*"
        server: https://kubernetes.default.svc
    clusterResourceWhitelist:
      - group: "*"
        kind: "*"

applications:
  - name: frontend
    namespace: argocd
    project: my-project
    source:
      repoURL: https://github.com/org/frontend
      targetRevision: main
      path: k8s
    destination:
      server: https://kubernetes.default.svc
      namespace: frontend
    syncPolicy:
      automated:
        prune: true
        selfHeal: true
      syncOptions:
        - CreateNamespace=true

  - name: backend
    namespace: argocd
    project: my-project
    source:
      repoURL: https://github.com/org/backend
      targetRevision: main
      path: deploy
    destination:
      server: https://kubernetes.default.svc
      namespace: backend
    syncPolicy:
      automated:
        prune: true
        selfHeal: true
      syncOptions:
        - CreateNamespace=true
```

### Helm Application

```yaml
applications:
  - name: prometheus
    namespace: argocd
    project: monitoring
    source:
      repoURL: https://prometheus-community.github.io/helm-charts
      targetRevision: 15.5.3
      chart: prometheus
      helm:
        values: |
          server:
            persistentVolume:
              size: 50Gi
    destination:
      server: https://kubernetes.default.svc
      namespace: monitoring
```

### Application with Kustomize

```yaml
applications:
  - name: kustomize-app
    namespace: argocd
    project: default
    source:
      repoURL: https://github.com/org/kustomize-app
      targetRevision: main
      path: overlays/production
      kustomize:
        images:
          - name=nginx:1.21.1
    destination:
      server: https://kubernetes.default.svc
      namespace: production
```

## Best Practices

1. Project Organization
   - Create separate projects for different teams or environments
   - Use meaningful project names and descriptions
   - Define appropriate RBAC rules per project

2. Application Configuration
   - Always specify a concrete version/tag for production applications
   - Use automated sync for non-production environments
   - Enable pruning to remove deleted resources
   - Use self-healing to automatically correct drift

3. Security
   - Limit source repositories per project
   - Define specific destination clusters/namespaces
   - Use cluster resource whitelists carefully
   - Implement proper RBAC controls

## Troubleshooting

Common issues and solutions:

1. Application not syncing
   - Check if the source repository is accessible
   - Verify the path to the manifests
   - Check if the destination cluster is reachable
   - Review the ArgoCD application logs

2. Permission issues
   - Verify project permissions
   - Check RBAC settings
   - Ensure service account has necessary permissions

3. Resource conflicts
   - Check for naming conflicts
   - Verify namespace exists
   - Review resource quotas

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This Helm chart is available under the Apache 2.0 license.
