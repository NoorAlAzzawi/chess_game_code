# Java Chess Game

A fully playable chess game built in Java using object-oriented programming principles.  
This project includes both a command-line mode and a Java Swing graphical interface.

---

## Overview

This project simulates a real chess game with:

- Legal move validation
- Turn-based gameplay
- Capturing pieces
- Check detection
- Checkmate handling
- GUI and CLI support

The project was designed to strengthen understanding of Java, object-oriented programming, and game development concepts.

---

# Features

✔️ Full 8x8 chess board  
✔️ Object-oriented piece system  
✔️ Legal move validation  
✔️ Turn-based gameplay  
✔️ Piece capturing  
✔️ Check detection  
✔️ Checkmate detection  
✔️ Command-line gameplay  
✔️ Java Swing graphical interface  
✔️ Unicode chess pieces in GUI mode  
✔️ Forfeit option in terminal mode  

---

# Tech Stack

- Java
- Java Swing
- Object-Oriented Programming (OOP)
- Git & GitHub

---

# Project Structure

```text
oopchesscode/
├── board/
│   └── ChessBoard.java
├── game/
│   └── ChessApp.java
├── pieces/
│   ├── BasePiece.java
│   ├── BishopPiece.java
│   ├── KingPiece.java
│   ├── KnightPiece.java
│   ├── PawnPiece.java
│   ├── QueenPiece.java
│   └── RookPiece.java
├── player/
├── position/
└── utils/
```

---

# How to Run

## 1. Clone the Repository

```bash
git clone https://github.com/NoorAlAzzawi/chess_game_code.git
```

## 2. Navigate Into the Project

```bash
cd chess_game_code
```

## 3. Compile the Java Files

```bash
javac oopchesscode/**/*.java
```

## 4. Run the Application

```bash
java oopchesscode.game.ChessApp
```

---

# Gameplay

When the game starts, you will see:

```text
Start game in GUI mode? (y/n):
```

Type:

```text
y
```

for the graphical interface, or:

```text
n
```

for command-line mode.

---

# CLI Commands

Move pieces using chess coordinates:

```text
e2 e4
```

To forfeit the game:

```text
f
```

---

# Main Components

## Board Layer

Handles:

- Board state
- Piece placement
- Piece movement
- Captures
- Threat detection

---

## Piece Layer

Each chess piece has its own class and movement logic.

Examples:

- Pawn
- Rook
- Knight
- Bishop
- Queen
- King

---

## Game Layer

Controls:

- Turn management
- Check detection
- Checkmate logic
- Game flow
- CLI / GUI switching

---

## GUI Layer

Built using Java Swing.

Features:

- Interactive chess board
- Unicode chess symbols
- Clickable interface
- Visual board updates

---

# What I Learned

This project improved my skills in:

- Java programming
- Object-oriented design
- Game logic implementation
- Java Swing GUI development
- Debugging
- Code organization
- Git & GitHub workflows

---

# Future Improvements

- Add castling
- Add en passant
- Add pawn promotion
- Add move history
- Add undo/redo support
- Improve GUI styling
- Add AI opponent
- Add multiplayer support
- Add unit testing

---

# Author

### Noor AlAzzawi

GitHub:  
https://github.com/NoorAlAzzawi
