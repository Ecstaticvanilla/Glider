# Glider
My first game

MyGameProject/
│── src/
│   ├── Main.java           // entry point
│   ├── GamePanel.java      // handles drawing, input, states
│   ├── GameState.java      // enum for START, PLAYING,GAME_OVER
│   ├── TimerManager.java   // handles game timer
│   ├── ScoreManager.java   // reads/writes JSON using org.json
│   └── Player.java         // (optional later)
│
│── data/
│   └── score.json          // stores fastest time
│
│── lib/
│   └── json.jar            // JSON library
