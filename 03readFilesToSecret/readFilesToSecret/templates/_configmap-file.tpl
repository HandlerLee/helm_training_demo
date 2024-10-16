{{- define "configmap" -}}
{{- $files := .Files -}}
{{- if $files -}}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  {{- $conifg_files := .Files.Glob "files/*" }}
  {{- range $path, $content := $conifg_files }}
  {{- $fileName := base $path }}
  {{ $fileName }}: |-
   {{ toString $content | nindent 4 }}
  {{- end }}
{{- end }}
{{- end -}}