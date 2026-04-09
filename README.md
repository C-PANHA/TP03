# TP03 Jenkins + Laravel CI/CD Setup

## 1) Build Jenkins Agent (with Ansible)

The Docker image for Jenkins agent is in `agent/dockerfile` and includes:
- PHP + Composer
- Node/NPM
- Ansible + SSH tools

Build and push (or build locally) from the workspace root:

```bash
docker build -t your-dockerhub/jenkins-laravel-agent:latest -f agent/dockerfile .
docker push your-dockerhub/jenkins-laravel-agent:latest
```

Configure this image as a Jenkins agent with label `laravel`.

## 2) Jenkins Pipeline Job

Use `C-TP03-2026/Jenkinsfile` as Pipeline script from SCM.

What it does:
- Poll SCM every minute
- Build Laravel app:
  - `composer install`
  - `php artisan key:generate`
  - `npm install`
  - `npm run build`
- Run tests: `php artisan test`
- Deploy using Ansible to `178.128.93.188` with default path `/var/www/your_name`
- On deploy failure, automatically rollback to previous release symlink

## 3) Required Jenkins Plugins

- Pipeline
- SSH Agent
- Credentials Binding
- Email Extension Plugin (for email notification)

## 4) Required Jenkins Credentials

- SSH key credential (type: SSH Username with private key):
  - Default ID in pipeline: `deploy-ssh`
- Optional Telegram credentials (type: Secret text):
  - `telegram-bot-token`
  - `telegram-chat-id`

You can change these IDs in job parameters.

## 5) Deploy Target

Deployment files are in:
- `C-TP03-2026/deploy/deploy.yml`
- `C-TP03-2026/deploy/inventory.ini`
- `C-TP03-2026/deploy/rollback.yml`

Default target host/path:
- Host: `178.128.93.188`
- Path: `/var/www/your_name`

The pipeline rewrites `deploy/inventory.ini` from parameters at runtime.

Release strategy:
- New code is deployed to `releases/<timestamp>`
- `current` symlink points to active release
- Before switching, old `current` is saved as `previous`
- If deploy stage fails, pipeline runs rollback playbook to restore `current` from `previous`

## 6) Failure Notifications

In Jenkins job parameters:
- Set `MAIL_TO` to receive failure email
- Enable `ENABLE_TELEGRAM=true` to send Telegram alerts

If email plugin or Telegram credentials are missing, pipeline continues and logs a skip message.

## 7) Fast Jenkins Setup for Submission

Use these helper files inside `C-TP03-2026/jenkins`:
- `SUBMISSION_STEPS.md` for complete step-by-step checklist
- `setup-node-and-job.groovy` for creating/updating agent node + pipeline job in Jenkins Script Console
- `create-credentials.groovy` for creating required Jenkins credentials from Script Console