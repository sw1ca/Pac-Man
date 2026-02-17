# 🍒 Chase & Chew - Arcade Java Game

![pac-man](https://github.com/user-attachments/assets/68dd824c-4fc4-4f27-a5d8-f766d2977fe9)

My own clone of the classic Pac-Man game built entirely in Java Swing.
Unlike typical game loops, this project utilizes Swing's JTable for grid-based rendering and demonstrates complex multithreading for managing enemy AI, collision detection, and animations.

# 🚀 Key Features

* **Algorithmic Map Generation:** Every game features a unique maze generated using a **Depth-First Search (DFS)** algorithm with random wall removal logic.
* **Customizable Grid:** Players can define the board size (e.g., 20x20, 50x50 or even 38x23), and the engine scales the user's interface properly.
* **MVC Architecture:** Strict separation between logic (`Model`), user's interface (`View`), and input handling (`Controller`).
* **Power-Up System:** Includes Speed Boosts, Score Multipliers, HP restoration, and mechanics to Freeze/Slow ghosts.
* **High Scores:** Using Java **Serialization** to save top scores locally.

# 🧵 Multithreading & Concurrency
The game runs multiple parallel threads to guarantee smooth performance without blocking the user's interface:
* **Ghost AI Thread:** Updates enemy positions independently.
* **Physics Thread:** Handles player movement and interpolation.
* **Collision Thread:** Runs a separate loop to check interactions between hitboxes (Player vs Ghost vs PowerUp).
* **Animation Thread:** Handles sprite toggling (mouth opening/closing).

# 🧠 Ghost Logic
Ghosts utilize a coordinate-tracking system (`targetX`, `targetY`) and semi-random pathfinding to navigate the maze, ensuring they don't get stuck in walls.

# 🏗️ Architecture
The code follows the **Model-View-Controller** pattern:
* **Model:** `GameCell`, `Player`, `Ghost`, `Map` (Data & Logic).
* **View:** `GameWindow`, `GameCellRenderer` (JTable customization).
* **Controller:** `GameController`, `MenuController` (Input & Game Loop).

# 🎮 Controls

| Key | Action |
| :--- | :--- |
| **Arrow Keys** | Movement (Up, Down, Left, Right) |
| **Ctrl + Shift + Q** | Instant Exit / Force Close |

---
*This project was created as part of the Computer Science curriculum at PJAIT (Polish-Japanese Academy of Information Technology).*






