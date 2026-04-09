# Submission Steps (1-5)

## Step 1 - Build and push Jenkins agent image

Run these commands from workspace root:

1. `docker build -t jenkins-laravel-agent:latest -f agent/dockerfile .`
2. `docker login`
3. `docker tag jenkins-laravel-agent:latest <dockerhub-username>/jenkins-laravel-agent:latest`
4. `docker push <dockerhub-username>/jenkins-laravel-agent:latest`

Expected image contains Ansible, PHP, Composer, and Node tools.

## Step 2 - Add Jenkins agent with label `laravel`

In Jenkins UI:

1. Manage Jenkins > Nodes > New Node
2. Name: `laravel-agent`
3. Remote root dir: `/home/jenkins/agent`
4. Labels: `laravel`
5. Launch method: Inbound agent or your preferred method

Or use Script Console with file `jenkins/setup-node-and-job.groovy`.

## Step 3 - Install required plugins

Install these plugins:

1. Pipeline
2. Git
3. SSH Agent
4. Credentials Binding
5. Email Extension

## Step 4 - Create required credentials

Required IDs used by pipeline:

1. `deploy-ssh` (SSH Username with private key)
2. `telegram-bot-token` (Secret text, optional)
3. `telegram-chat-id` (Secret text, optional)

You can create them from Jenkins UI or Script Console using `jenkins/create-credentials.groovy`.

## Step 5 - Create pipeline job from SCM

1. New Item > Pipeline
2. Name: `laravel-ci-cd`
3. Definition: Pipeline script from SCM
4. SCM: Git
5. Repository URL: `https://github.com/C-PANHA/TP03.git`
6. Branch Specifier: `*/master`
7. Script Path: `C-TP03-2026/Jenkinsfile`
8. Save and run Build with Parameters

## Build parameters to verify before run

1. DEPLOY_HOST=178.128.93.188
2. DEPLOY_USER=root (or your server user)
3. DEPLOY_PATH=/var/www/your_name
4. SSH_CREDENTIALS_ID=deploy-ssh
5. MAIL_TO=<your_email_optional>
6. ENABLE_TELEGRAM=true/false
