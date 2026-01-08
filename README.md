<img width="1920" height="1080" alt="MyPlaceholders_Cover" src="https://github.com/user-attachments/assets/103a854b-97cc-4149-86d0-e07578e58e4d" />

# MyPlaceholder 🌟

**MyPlaceholder** is a powerful and versatile Minecraft plugin that allows administrators to create their own custom placeholders dynamically. Bring your messages, menus, and scoreboards to life with custom logic and advanced conditions!

---

## 🔗 Official Links
* **Creator Profile (BuiltByBit):** https://builtbybit.com/creators/nutellim.455402
* **Official Wiki (Documentation):** https://nute-setups.gitbook.io/docs/plugins/myplaceholder
* **Official Discord (Support):** https://discord.com/invite/ZermkrzMDg
* **GitHub Repository:** https://github.com/pgprimitz/MyPlaceholder

---

## ✨ Main Features
* **Universal Compatibility:** Works perfectly from **1.16.1 to 1.21.x** thanks to its optimized compilation in Java 8.
* **Logic System:** Performs mathematical and text comparisons in real-time.
* **Color Formatting:** Full support for Legacy colors (`&6`) and **Hexadecimal** codes (`&#FF3D00`).
* **Adventure Integration:** Native support for **MiniMessage** and modern text components for a superior visual experience.
* **Dynamic Placeholders:** Support for external arguments using `{0}`, `{1}`, etc., allowing for interactive placeholders.
* **Advanced Conditionals:** Display different results based on whether a player meets specific requirements, such as economy balance or permissions.

---

## 🛠️ Configuration and Usage

### Placeholders Folder
The plugin automatically loads all `.yml` files located inside the `/placeholders/` folder. This allows you to organize your creations by categories (e.g., `ranks.yml`, `stats.yml`).

### Available Placeholder Types
You can configure various behaviors according to your needs:

#### 1. Colored Text (`COLORED_TEXT`)
Ideal for server names or aesthetic labels with gradients.
```yaml
example-title:
  value: "&#61FF57&lSERVER &8| &7Welcome!"
  type: "COLORED_TEXT"
```

#### 2. Random Messages (RANDOM)
Selects a random line from a list every time the placeholder refreshes—perfect for announcements.
```yaml
welcome-message:
  values:
    - "Welcome back!"
    - "We hope you have fun!"
    - "Remember to visit our store!"
  type: "RANDOM"
```

#### 3. With Requirements (REQUIREMENT)
Allows for conditional logic by comparing an input (usually another placeholder) against an output.
- Operators: >=, <=, ==, !=, string equals, string contains.
```yaml
vip-rank:
  value: "&a[VIP]"
  type: "REQUIREMENT"
  requirements:
    check_perm:
      type: "=="
      input: "%luckperms_has_permission_group.vip%"
      output: "yes"
      deny: "&7[User]"
```

#### Commands and Permissions
Command messages are fully editable within the config.yml:
| Comando | Descripción | Permiso |
| :--- | :--- | :--- |
| `/mp help` | Displays the plugin help. | `myplaceholder.help` |
| `/mp reload` | Reloads the configuration and placeholders. | `myplaceholder.reload` |
| `/mp config` | Change any placeholder ingame. | `myplaceholder.config` |

#### Installation
1. Download the MyPlaceholder-1.0.0.jar file.
2. Ensure PlaceholderAPI is installed on your server.
3. Upload the plugin to your /plugins/ folder.
4. Restart the server to generate the initial configuration files.
5. Start creating your placeholders in the /placeholders/ folder!

#### 📂 System Files
- config.yml: Controls the plugin prefix and administrative messages.
- example.yml: Example file with detailed comments to help you learn all functions.

Developed with ❤️ by Risas | Distributed by nutellim
