{{- define "standard-application.deployment.properties.env" -}}
{{- $propertiesContent := .Files.Get .Values.configmap.fileName -}}
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
{{- range $key, $value := $propertiesDict -}}
- name: {{ $key | upper | replace "-" "_" }}
  valueFrom:
    configMapKeyRef:
      name: "xx-standard-application-configuration"
      key: {{ cat $key "\n" }}
{{- end -}}
{{- end -}}
