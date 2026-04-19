# 💬 Chat-Channels

**Chat-Channels** is a lightweight and flexible Paper plugin that allows players to switch between multiple chat channels, each with its own formatting and optional Discord integration (webhooks + bot sync).

Perfect for servers that want organized communication between players, staff, and communities.

---

## ✨ Features

* 💬 Multiple configurable chat channels
* 🔁 Easy channel switching with commands
* 🏷️ Custom prefixes per channel
* 👑 Staff-only channel (OP restricted)
* 🌐 Discord webhook integration (Minecraft → Discord)
* 🤖 Discord bot integration (Discord → Minecraft)
* 🔗 Channel linking via Discord channel IDs
* 🧑 Player avatars in Discord messages (webhooks)
* 🎨 Full color support (including HEX colors)
* 🧩 Optional Vault integration (prefix & suffix support)
* 🔄 Hot reload with automatic bot restart
* ⚡ Lightweight and efficient
* 📡 Global chat with vanilla behavior

---

## 🧩 Vault Support (Optional)

If Vault is installed (e.g. with LuckPerms):

* Player prefixes & suffixes are automatically applied
* Color formatting is preserved correctly
* Enhances chat formatting without additional configuration

If Vault is not installed, the plugin works normally without prefixes/suffixes.

---

## 📜 Commands

| Command           | Description             |
| ----------------- | ----------------------- |
| `/channel <name>` | 💬 Switch to a channel  |
| `/channel reload` | 🔄 Reload configuration |

### Examples

```
/channel global
/channel staff
/channel reload
```

---

## 📦 Installation

1. 📥 Download the latest `.jar` from Modrinth
2. 📂 Place it in your server's `plugins` folder
3. ▶️ Start or restart your server
4. 📝 Edit `config.yml` to configure channels

---

## ⚙️ How It Works

* 👤 Players are automatically assigned to the **global channel**
* 🔄 Players can switch channels using `/channel`
* 💬 Messages are only visible within the same channel
* 🌐 Messages can be sent to Discord using webhooks (optional)
* 🤖 Discord messages can appear in Minecraft using the bot (optional)

---

## 🛡️ Channel System

* 🌍 **Global channel**

  * Uses normal Minecraft chat
  * Optional Discord sync

* 👑 **Staff channel**

  * Only accessible by OP players
  * Private communication

* ⚙️ **Custom channels**

  * Fully configurable
  * Unlimited channels

---

## 🔧 Configuration

The plugin uses a YAML configuration file:

```
plugins/Chat-Channels/config.yml
```

Example:

```yaml
discord:
  token: "" # Optional

channels:
  global:
    prefix: "[Global]"
    webhook: "" # Optional
    discord-channel-id: "" # Optional

  staff:
    prefix: "[Staff]"
    webhook: "YOUR_WEBHOOK_URL" # Optional
    discord-channel-id: "" # Optional
```

### Options

* 🏷️ `prefix` → Chat prefix
* 🌐 `webhook` → Sends messages to Discord (optional)
* 🤖 `discord-channel-id` → Receives messages from Discord (optional)
* 🔑 `discord.token` → Enables Discord bot (optional)
* 🎨 Supports HEX colors (e.g. `&#FFAA00`)
* 🌐 Colors are automatically stripped/cleaned when sending to Discord

---

## 🌐 Discord Integration

### Webhooks (Minecraft → Discord)

* Sends messages from Minecraft to Discord
* Includes player avatar, name, and clean formatting
* One webhook per channel
* Color codes are cleaned automatically

### Bot (Discord → Minecraft)

* Sync messages from Discord to Minecraft
* Display format: `Discord [username]: message`
* Works per configured channel
* Requires bot token

---

## 🤖 Discord Bot Setup

### 1️⃣ Create Application

* Go to https://discord.com/developers/applications
* Click **New Application**

---

### 2️⃣ Create Bot

* Go to **Bot** tab
* Click **Add Bot**

Enable:

* ✅ Public Bot
* ✅ Server Members Intent
* ✅ Message Content Intent

Paste token in config:

```yaml
discord:
  token: "YOUR_BOT_TOKEN"
```

---

### 3️⃣ Invite Bot

* Go to **OAuth2 → URL Generator**

**Scopes:**

* `bot`

**Permissions:**

* View Channels

* Send Messages

* Read Message History

* Open generated URL and add to your server

---

## 🔗 Discord Setup Extras

### Channel ID

1. Enable Developer Mode in Discord
2. Right-click channel → Copy ID

```yaml
discord-channel-id: "123456789012345678"
```

---

### Webhook

1. Edit channel → Integrations → Webhooks
2. Create webhook and copy URL

```yaml
webhook: "https://discord.com/api/webhooks/..."
```

---

## 📝 Notes

* 👤 Only players can use commands
* ⚙️ Works with Paper 1.21+ and Java 21
* 📄 Uses YAML configuration
* 🌐 Discord features are fully optional

---

## 🛠️ Building from Source

This project uses Maven.

```
git clone https://github.com/yourusername/Chat-Channels.git
cd Chat-Channels
mvn clean package
```

The compiled plugin will appear in:

```
target/Chat-Channels-1.4.0.jar
```

---

## 🚀 Future Ideas

* 🔐 Permission-based channels (LuckPerms)
* 🎨 Improved chat formatting
* 📊 Channel settings (cooldowns, filters)
* 🖥️ GUI for channel selection

---

## 👤 Author

Developed by **Wilkcraft**

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

💬 Organize your server chat and connect it with Discord!
