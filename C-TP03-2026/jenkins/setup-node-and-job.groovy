import jenkins.model.Jenkins
import hudson.model.Node
import hudson.slaves.DumbSlave
import hudson.slaves.JNLPLauncher
import hudson.slaves.RetentionStrategy
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.UserRemoteConfig
import hudson.plugins.git.GitSCM

// ===== EDIT THESE VALUES =====
def nodeName = 'laravel-agent'
def nodeRemoteFs = '/home/jenkins/agent'
def nodeExecutors = '1'
def nodeLabel = 'laravel'
def repoUrl = 'https://github.com/C-PANHA/TP03.git'
def repoBranch = '*/master'
def repoCredentialsId = '' // Optional for private repo

def jobName = 'laravel-ci-cd'
def scriptPath = 'C-TP03-2026/Jenkinsfile'
// =============================

def j = Jenkins.instance

// Create or update agent node.
def node = j.getNode(nodeName)
if (node == null) {
    node = new DumbSlave(
        nodeName,
        'Laravel Jenkins agent',
        nodeRemoteFs,
        nodeExecutors,
        Node.Mode.NORMAL,
        nodeLabel,
        new JNLPLauncher(),
        RetentionStrategy.Always.INSTANCE,
        new LinkedList<>()
    )
    j.addNode(node)
    println("Created node: ${nodeName}")
} else {
    node.setLabelString(nodeLabel)
    node.setNumExecutors(Integer.parseInt(nodeExecutors))
    node.setRemoteFS(nodeRemoteFs)
    j.save()
    println("Updated node: ${nodeName}")
}

// Create or update pipeline job.
def job = j.getItem(jobName)
if (job == null) {
    job = j.createProject(WorkflowJob, jobName)
    println("Created job: ${jobName}")
} else {
    println("Updated job: ${jobName}")
}

def remoteConfig = new UserRemoteConfig(repoUrl, null, null, repoCredentialsId ?: null)
def scm = new GitSCM([remoteConfig], [new BranchSpec(repoBranch)], false, [], null, null, [])
def flowDef = new CpsScmFlowDefinition(scm, scriptPath)
flowDef.setLightweight(true)
job.setDefinition(flowDef)

job.save()
j.save()

println('Node and pipeline job setup complete.')
