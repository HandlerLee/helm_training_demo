{{- define "standard-application.deployment.properties.env" -}}
{{- $propertiesContent := .Files.Get .Values.configmap.fileName -}}
{{- $configMapName := (cat (include "standard-application.fullname" .) "-configuration") -}}
{{- $propertiesDict := dict -}}
{{- $lines := .Files.Lines .Values.configmap.fileName -}}
  {{- range $line := $lines -}}
    {{- if $line -}}
      {{- $pair := splitList "=" $line -}}
      {{- if eq (len $pair) 2 -}}
        {{- $key := trim (index $pair 0) -}}
        {{- $value := trim (index $pair 1) -}}
        {{- if and $key $value -}}
          {{- $_ := set $propertiesDict $key $value -}}
        {{- end -}}
      {{- end -}}
    {{- end -}}
  {{- end -}}
{{- $keyCount := len $propertiesDict -}}
{{- $index := 0 -}}
{{- range $key, $value := $propertiesDict -}}
{{- if eq $index (sub $keyCount 1) -}}
- name: {{ $key | upper | replace "-" "_" }}
  valueFrom:
    configMapKeyRef:
      name: {{ $configMapName }}
      key: {{ $key | quote }}
{{- else -}}
- name: {{ $key | upper | replace "-" "_" }}
  valueFrom:
    configMapKeyRef:
      name: {{ $configMapName }}
      key: {{ cat ($key | quote) "\n" }}
{{- end -}}
{{ $index = add $index 1 }}
{{- end -}}
{{- end -}}


{{- define "standard-application.deployment.env.secret" -}}
{{- range $key, $value := .Values.deployment.secret -}}
- name: {{ $key | upper | replace "-" "_" }}
  valueFrom:
    secretKeyRef:
      name: {{ $value }}
      key: {{ $key }}
{{- end -}}
{{- end -}}

