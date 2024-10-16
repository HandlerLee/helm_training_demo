{{- define "configmap-properties.labels" -}}
labels:
  app.kubernetes.io/name: {{ include "standard-application.name" . }}
  helm.sh/chart: {{ include "standard-application.chart" . }}
  app.kubernetes.io/instance: {{ .Release.Name }}
  app.kubernetes.io/managed-by: {{ .Release.Service }}
  namespace: {{ .Release.Namespace }}
  {{- with .Values.configmapProperties.customLabels }}
  {{- toYaml . | nindent 2 }}
  {{- end }}
{{- end -}}

{{- define "configmap-properties.annotations" -}}
annotations:
  description: "This is a application configmap for properties"
  {{- with .Values.configmapProperties.annotations -}}
  {{- toYaml . | nindent 2 }}
  {{- end }}
{{- end -}}

{{- define "configmap-properties.filedata" -}}
  {{- $configMapName := .ConfigMapName }}
  {{- $lines := .Root.Files.Lines $configMapName }}
  {{- range $line := $lines }}
    {{- if $line }}
      {{- $pair := splitList "=" $line }}
      {{- if eq (len $pair) 2 }}
        {{- printf "%s: %q" (trim (index $pair 0)) (index $pair 1) | nindent 4 }}
      {{- end }}
    {{- end }}
  {{- end }}
{{- end }}

