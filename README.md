# 🌸 Jennie (Codename: Venus)

Welcome to **Jennie**, an educational game engine built from the ground up in Java. Formerly known by its codename **Venus**, Jennie is a labor of love designed to explore the intricate inner workings of game development—from low-level rendering to physics and event-driven architectures.

### 🚀 About the Project
Jennie isn't just a game engine; it's a learning journey. The project aims to demystify how games work by implementing core systems from scratch without relying on heavy external frameworks. It's built for those who want to see exactly what's "under the hood."

### ✨ Key Features
- **Custom AWT Rendering Pipeline:** A hand-crafted rendering engine featuring:
  - **Tile-based Rendering:** Efficient handling of large-scale maps.
  - **Dynamic Lighting:** A custom light-masking system for atmospheric effects.
  - **UI System:** A dedicated UI renderer for menus and overlays.
- **Physics Engine:** Built-in 2D physics supporting:
  - **AABB Collision Detection:** Fast and reliable box collisions.
  - **Spatial Grid Partitioning:** Efficient broad-phase collision detection for high entity counts.
- **Event-Driven Architecture:** A robust `EventDispatcher` and `Mailbox` system for decoupled communication between engine components.
- **Sound System:** Multi-channel audio support powered by the JavaSound backend.
- **Developer Toolkit:** In-game debug tools including scene inspectors, physics visualizers, and performance profilers.
- **Modern Java:** Leveraging the latest features and performance of **Java 25**.

### 🛠 Project Structure
The project is organized into a modular Maven structure:
- `engine-core`: The heart of Jennie, containing all fundamental systems (Graphics, Physics, Sound, etc.).
- `engine-annotations` & `engine-processor`: Custom annotation processing logic for streamlined engine tasks.
- `game`: A reference implementation using the engine to create a playable experience.

### 🎮 Getting Started
Since this is a Maven project, you can get it up and running with:

```bash
mvn clean install
cd game
mvn exec:exec
```

### 🌸 Why "Jennie"?
The project began as **Venus**, the younger sibling of project **Mercury**. My original intent was to improve the old engine slightly and then move on to a new **Mars** version. However, the project grew much more than expected, so much so that I couldn’t bring myself to discard it and start from scratch. Instead, I decided to keep it as a standalone project and rename it.

So, why **Jennie**? For a while, I played with "JEngine" and tweaked it a bit. I thought the name "enJennie" was quite funny, but I eventually dropped the "en" to stick with **Jennie**, which felt more polished.

Simply put: "J" is for Java, "en" is for engine, and the rest is just sugar.

---
*Built with ❤️ for the love of game dev.*
