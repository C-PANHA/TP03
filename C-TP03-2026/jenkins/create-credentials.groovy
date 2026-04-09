import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.StringCredentialsImpl
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl as PlainStringCredentials
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey

// ===== EDIT THESE VALUES =====
def sshId = 'deploy-ssh'
def sshUsername = 'root'
def sshPrivateKey = '''-----BEGIN OPENSSH PRIVATE KEY-----
PASTE_YOUR_PRIVATE_KEY_HERE
-----END OPENSSH PRIVATE KEY-----'''

def telegramTokenId = 'telegram-bot-token'
def telegramTokenValue = 'PASTE_TELEGRAM_BOT_TOKEN'

def telegramChatIdId = 'telegram-chat-id'
def telegramChatIdValue = 'PASTE_TELEGRAM_CHAT_ID'
// =============================

def store = SystemCredentialsProvider.getInstance().getStore()
def domain = Domain.global()

def existing = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardCredentials.class,
    Jenkins.instance,
    null,
    null
)

def hasId = { id -> existing.any { it.id == id } }

if (!hasId(sshId)) {
    def sshCred = new BasicSSHUserPrivateKey(
        CredentialsScope.GLOBAL,
        sshId,
        sshUsername,
        new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(sshPrivateKey),
        '',
        'Deploy SSH key'
    )
    store.addCredentials(domain, sshCred)
    println("Added SSH credential: ${sshId}")
} else {
    println("SSH credential already exists: ${sshId}")
}

if (!hasId(telegramTokenId) && telegramTokenValue != 'PASTE_TELEGRAM_BOT_TOKEN') {
    def tgTokenCred = new PlainStringCredentials(
        CredentialsScope.GLOBAL,
        telegramTokenId,
        'Telegram bot token',
        hudson.util.Secret.fromString(telegramTokenValue)
    )
    store.addCredentials(domain, tgTokenCred)
    println("Added Telegram bot token credential: ${telegramTokenId}")
} else {
    println("Telegram bot token credential exists or placeholder not replaced.")
}

if (!hasId(telegramChatIdId) && telegramChatIdValue != 'PASTE_TELEGRAM_CHAT_ID') {
    def tgChatCred = new PlainStringCredentials(
        CredentialsScope.GLOBAL,
        telegramChatIdId,
        'Telegram chat id',
        hudson.util.Secret.fromString(telegramChatIdValue)
    )
    store.addCredentials(domain, tgChatCred)
    println("Added Telegram chat id credential: ${telegramChatIdId}")
} else {
    println("Telegram chat id credential exists or placeholder not replaced.")
}

Jenkins.instance.save()
println('Credential setup completed.')
