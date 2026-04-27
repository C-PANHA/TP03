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
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAACFwAAAAdzc2gtcn
NhAAAAAwEAAQAAAgEAvb9IXNL3kXb53yQ4GOeAcA4YM6Kog/KadPB8ZIy3vbJxq27W8J1F
AcKGfS8SC0+9puM4BYiIVWX7qkEY1gupauH5PsnjmocFnfso/MrXc66ibJSp28ks01LWN8
bc8eZ63PtNvAPEZ+JhVUnSI7jMCqDO059mF0iia9HN8Yr7T9GT4Oe93nUjHwJi7+vIMQ+y
PnOOGWuMsHk9Am1zW2WtaGckOXbsfvoxzBrvnj1O5reu8f/VWJR+Z0vQsARJzoswHnAY9S
0AuhIyTSQ2kHuaFKrTcA16AAUjODyjeRNiWhlNqACa79Psn0p34Fb+2ir6KWxic6nsHKTd
rBfjAzVvo3YNlvkkIVQ4/VjSQzOVJmAKXmOcJLsQCKQpXBRGHyEh5in4DoQAQw7VQZrmiy
Srj3lQCq26Sssarhiooop/r1hpRtMog/sJq8hdzJl4XQbKZ6bCTI0dVkIwlfnNPckxCUYA
HkdIjOMMSZiL05eJdRIr0FUe6L2CtOeLm8MPE3lrHv77n+6tTdl8q/4jQv9CFYbSET1tRp
EmfxOOVI81Q6oBOGly8oSTLT/ZL/l4Ih++O6URINUJI6TTkWu2P7EtTQfUu2oS9d14RQP2
puotPTc5nS4pZS2rwMXlO4Fikp+LFed7BLcIbKiRUiEtA+pzNOQUhduKubjU5X9OX7IzMM
kAAAdIL3DmOy9w5jsAAAAHc3NoLXJzYQAAAgEAvb9IXNL3kXb53yQ4GOeAcA4YM6Kog/Ka
dPB8ZIy3vbJxq27W8J1FAcKGfS8SC0+9puM4BYiIVWX7qkEY1gupauH5PsnjmocFnfso/M
rXc66ibJSp28ks01LWN8bc8eZ63PtNvAPEZ+JhVUnSI7jMCqDO059mF0iia9HN8Yr7T9GT
4Oe93nUjHwJi7+vIMQ+yPnOOGWuMsHk9Am1zW2WtaGckOXbsfvoxzBrvnj1O5reu8f/VWJ
R+Z0vQsARJzoswHnAY9S0AuhIyTSQ2kHuaFKrTcA16AAUjODyjeRNiWhlNqACa79Psn0p3
4Fb+2ir6KWxic6nsHKTdrBfjAzVvo3YNlvkkIVQ4/VjSQzOVJmAKXmOcJLsQCKQpXBRGHy
Eh5in4DoQAQw7VQZrmiySrj3lQCq26Sssarhiooop/r1hpRtMog/sJq8hdzJl4XQbKZ6bC
TI0dVkIwlfnNPckxCUYAHkdIjOMMSZiL05eJdRIr0FUe6L2CtOeLm8MPE3lrHv77n+6tTd
l8q/4jQv9CFYbSET1tRpEmfxOOVI81Q6oBOGly8oSTLT/ZL/l4Ih++O6URINUJI6TTkWu2
P7EtTQfUu2oS9d14RQP2puotPTc5nS4pZS2rwMXlO4Fikp+LFed7BLcIbKiRUiEtA+pzNO
QUhduKubjU5X9OX7IzMMkAAAADAQABAAACAQCBoT2plMNnkJWymYbtlLLNWwOzuwtPYZ2e
CHGjK/OXWGKJ0SZsqbTsN94zhcXZDngtnIu4VAd3+D1PmjuCd+MdTxY9jCW/e0EeJelhkp
5QvNzgO0w0CCNkl/IblvN5G5GSiOkZaqVyp1kBsHYsy2IyXJwQPZS8kXXNHT+KJf39ENvr
SccQRIxi8flvmXBh/Cw0uDM7MJukMJ+6mdW2eidW7Sf1TkbRlNISc9CCFbZirtiJIdO6OX
CF12jR54wY3ERadxDF5yHY/Pd6diQE3zxrpCzhH4PaoafnEp5H6DNIBINmJEu1Tt4YEQMG
s8hWegf/5rj39f2FsCh1GriPh6ElKHR0mh94Ve7lMkhwEHKV34RHaijl0KtRP030L2h0Mx
rM4R+bKsHrhJHKfuvJ4AGmp4hNz29tu35o+5Bcw0YwyiQ4cB3wRPwufI09U2BKiPfM0jPp
JFb3qbwrEMeo49hV+xHfI8pBWoOSOM0byY89O6Ro7mvcfNUBAL8jVpuwD+x8u3jPQLYRXf
6Qoxo7D4NKZGp6eGkQwaiHLG5tpP2W745Q9VADAQAy6nRpxGtmWV3WACWGHTtVotvOGr2J
qqPpM90N0BFug4zyIoV0lOVDXBYbzAFLh174pPEkFKPUO7sW2TjMVXbx3jAkBmzUqFadqb
9GdD2iTHINfesrenU6VQAAAQAnZGIgaYoSvf9eLRbBoFFNbiCdkpsYKEIjE3i0iM2l/U/O
902N/QSN7uDvclkXzjiSOG7UVwl5Kx6PEqb+r/aRCOqW9BVZz2+sWsZl2wktT1zOQ6wVT5
AgDqjr/FGO1eoKRBBbVTA/o9baKXm+iTLEWa7CXt9lH8S+42ccOy3gEfUSBqv+deL+XbbC
ydcLyfQhEQPKd/sbLg/MKd15Yln/HYBhfO2EMgMxmPXR6dPcjTFqB/ge9BNcWnZRCK0tHi
u+ksjAOah0cc5skDXIox2dbUIYj4T6wkYgc1K1w5kA4zudC0UCIthsC+OXf6bT648E9K1Y
O0DrOevFpl30DJ04AAABAQDsIml5QQeGr8uxhyTXzJn8ST3Z5U3svurn/1/bDMwr3+Dskv
I5PuDXAWLGrJ9fTqJ22fK0Zmu8uhwz3tsZ2VIeHzDz0ToHHfGAu74M5/TK00c3dlCoXFX1
4cbxpOSeliCax72azQh0KK3242t1woZK7mqEuox3/3+K0fEbRv+fu1W7zNFKzsKhKK2xvT
Z7h1U+JGCVsm91hr2mDPwDYHxHjSmubT/eZHnxnWlph0HUPoegdcAVJy3ZqHaw8Cr5JT0Y
AczgUzB0z2yGn7PfS5m1hjkmP3MuTJhRsF9LzxEPR0phcZ1ynWoaSPFzTpVT653l1Tc25F
czvvPWgKeCvzfrAAABAQDNtdYxNHqIfL1wh/WAEfNny8SoPr/tEOmHUFkyjyHePQGQUnmK
WPcB/8bJn7tpWyBtmbOkAxE/McR9dJPITBeePbKQn3BQQnUNEWBE44g00PZIm0CrtR0uFz
GIEg/+ZPIm+E20Smgqf+6mm4V3Pt56Suq3y6hRsbeoyB7dMNQJdfzTPmO7krzZfoM0CraB
Ew/gW/SrElfuEzEbUiPNr8kZXLWHxMhahL3G2N/wQsiy/R0jes1x7K5/hS//FeXsh0VwXO
EvvTqoH63YFo5Cf6h/CJpPBlYlQSmnuACAQBLdc82xzWGVE5KRSkq4SBzCtYDRVbmroXtO
R30NWJtLuSEbAAAADm1zaSBhMTJ1QFBhbmhhAQIDBA==
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
