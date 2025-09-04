{{/*
Expand the name of the chart.
*/}}
{{- define "lifepuzzle-infrastructure.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "lifepuzzle-infrastructure.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "lifepuzzle-infrastructure.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "lifepuzzle-infrastructure.labels" -}}
helm.sh/chart: {{ include "lifepuzzle-infrastructure.chart" . }}
{{ include "lifepuzzle-infrastructure.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: lifepuzzle
{{- end }}

{{/*
Selector labels
*/}}
{{- define "lifepuzzle-infrastructure.selectorLabels" -}}
app.kubernetes.io/name: {{ include "lifepuzzle-infrastructure.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
MySQL labels
*/}}
{{- define "lifepuzzle-infrastructure.mysql.labels" -}}
{{ include "lifepuzzle-infrastructure.labels" . }}
app.kubernetes.io/component: mysql
{{- end }}

{{/*
RabbitMQ labels
*/}}
{{- define "lifepuzzle-infrastructure.rabbitmq.labels" -}}
{{ include "lifepuzzle-infrastructure.labels" . }}
app.kubernetes.io/component: rabbitmq
{{- end }}

{{/*
MySQL service name
*/}}
{{- define "lifepuzzle-infrastructure.mysql.serviceName" -}}
{{- printf "%s-mysql" (include "lifepuzzle-infrastructure.fullname" .) }}
{{- end }}

{{/*
RabbitMQ service name
*/}}
{{- define "lifepuzzle-infrastructure.rabbitmq.serviceName" -}}
{{- printf "%s-rabbitmq" (include "lifepuzzle-infrastructure.fullname" .) }}
{{- end }}

{{/*
Common environment variables for applications
*/}}
{{- define "lifepuzzle-infrastructure.env.database" -}}
- name: DB_HOST
  value: {{ include "lifepuzzle-infrastructure.mysql.serviceName" . }}
- name: DB_PORT
  value: "3306"
- name: DB_NAME
  value: {{ .Values.mysql.auth.database }}
- name: DB_USER
  value: {{ .Values.mysql.auth.username }}
- name: DB_PASSWORD
  valueFrom:
    secretKeyRef:
      name: {{ include "lifepuzzle-infrastructure.mysql.serviceName" . }}
      key: mysql-password
{{- end }}

{{/*
Common environment variables for RabbitMQ
*/}}
{{- define "lifepuzzle-infrastructure.env.rabbitmq" -}}
- name: RABBITMQ_HOST
  value: {{ include "lifepuzzle-infrastructure.rabbitmq.serviceName" . }}
- name: RABBITMQ_PORT
  value: "5672"
- name: RABBITMQ_USER
  value: {{ .Values.rabbitmq.auth.username }}
- name: RABBITMQ_PASSWORD
  valueFrom:
    secretKeyRef:
      name: {{ include "lifepuzzle-infrastructure.rabbitmq.serviceName" . }}
      key: rabbitmq-password
- name: RABBITMQ_VHOST
  value: "lifepuzzle"
{{- end }}