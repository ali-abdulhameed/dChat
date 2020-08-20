# dChat
Decenterlized P2P fully encrypted chat application that connects all devices on a given network. 

# Background
I created this app as a final project for my cryptography class. The Prof asked for a simple app that utilizes the concepts learned and let us choose our own project ideas. It was meant to be simple but I wanted to impress him and came up with this app idea.

# Features

- Allows users connected to a given network to chat with each other.
- Allows users to create and join groups.
- All messages are fully encrypted.

# Implementation
User-to-user messages:
- Upon running the application, a pair of RSA keys are generated and stored.
- The RSA public key is then broadcasted on the network (every 5 seconds by default. Can be changed later in settings).
- Users running the app will then receive the broadcasted public key and store it in a map containing the user IP address and it's associated public key.
- When we want to send a message to a user, a new AES 128bits key will be generated and the message will be encrypted with it in CBC mode. Since a new AES key is generated for each message, the key itself is used as IV. Once the encryption is done, the newly generated AES key will be then encrypted with the recipient public key. Then the encrypted AES key along with the encrypted message will be sent to the user.
- Once the user receives the messages, it wil decrypt the encrypted AES key using its private key, then the AES key will be used to decrypt the message.

User-to-Group messages:
- You can be in a group either by creating or joining one.
- When you create a new group, a random group ID and AES 128bits key will be generated, stored, and displayed.
- The same group ID and AES key will be needed if a person wanted to join that group.
- When a user sends a message to a group, the message will be encrypted with the group's AES key and a new randomly generated IV. Then the group ID, encrypted message, and the IV will be broadcasted. The message will be received by all users. It will be discarded by those who don't have a group with an ID identical to the one in the message. Those that have a group with the same ID will process the message and decrypt it using the AES key stored in the group.
- In case a user entered the wrong AES key for that group ID, the message will not be decrypted and an error will be thrown.
# User Manual
- Welcome page
<img src="https://i.imgur.com/aDAfziH.png" height="300">

- Activity Page
<img src="https://i.imgur.com/BFfmOIH.png" height="300">

- Friends
<img src="https://i.imgur.com/nm5rKLv.png" height="300">

- Groups (Create)
<img src="https://i.imgur.com/1Jc6voO.png" height="300">
<img src="https://i.imgur.com/6ozQo2g.png" height="300">

- Groups (Join)
<img src="https://i.imgur.com/J6AihB9.png" height="300">

- Settings
<img src="https://i.imgur.com/JbTqBgD.png" height="300">
