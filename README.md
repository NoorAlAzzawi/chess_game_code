# Java Chess Engine

A full-featured Java Chess project built with object-oriented design principles, Maven, and automated JUnit testing.  
The project supports both CLI and GUI gameplay, legal move validation, check/checkmate detection, and comprehensive rule testing.

---

# Overview

This project is a custom-built chess engine developed in Java.  
It was designed to simulate real chess gameplay while focusing on clean architecture, modular design, and automated testing.

The application includes:

- Command Line Interface (CLI)
- Graphical User Interface (GUI)
- Legal move validation
- Check, checkmate, and stalemate detection
- Unit and integration tests
- Maven project structure

The goal of the project was to create a maintainable and expandable chess system using professional software engineering practices.

---

# Features

## Core Chess Features

- Full chess board implementation
- Piece movement validation
- Turn-based gameplay
- Capturing system
- Check detection
- Checkmate detection
- Stalemate detection
- Illegal move prevention
- Safe move simulation

---

## Interfaces

### CLI Mode

- Play chess directly in the terminal
- Coordinate-based move input
- Board rendering in text format

### GUI Mode

- Visual chess board interface
- Interactive gameplay
- Java Swing-based rendering

---

## Testing

- Automated JUnit tests
- Rule validation tests
- Checkmate and stalemate tests
- Piece movement tests
- Fool’s Mate verification
- Chess utility testing

---

# Technologies Used

- Java
- Maven
- JUnit 5
- Java Swing
- Object-Oriented Programming (OOP)
- Git & GitHub

---

# Project Structure

```text
src/
 ├── main/
 │    └── java/
 │         └── oopchesscode/
 │              ├── board/
 │              ├── game/
 │              ├── pieces/
 │              ├── player/
 │              ├── position/
 │              └── utils/
 │
 └── test/
      └── java/
           └── oopchesscode/




How to Run
Run the Application
From the project root directory:
./mvnw exec:java -Dexec.mainClass="oopchesscode.game.ChessApp"


The application will ask whether to start in GUI mode or CLI mode.

How to Run Tests
Run all unit tests:
./mvnw test

Current test status:
Tests run: 63
Failures: 0
Errors: 0
BUILD SUCCESS

# Screenshots

## GUI Gameplay



## CLI Gameplay




Architecture
The project follows an object-oriented architecture with separation of concerns.
Main Components
Board Layer
Handles:
Board state
Piece placement
Square tracking
Piece Layer
Each chess piece contains:
Movement rules
Legal move generation
Piece-specific behavior
Game Layer
Controls:
Turn management
Game flow
Win/loss conditions
Check/checkmate logic
Test Layer
Contains:
Rule verification
Integration testing
Regression testing

Known Limitations
The current version does not yet include:
Castling
En passant
Pawn promotion GUI selection
Move history
Undo functionality
AI opponent
These features are planned for future development.

Future Improvements
Planned enhancements include:
Castling support
En passant support
Pawn promotion interface
Move history tracking
Undo/redo system
AI opponent
Improved GUI graphics
Sound effects
Online multiplayer support

Learning Outcomes
This project strengthened experience with:
Object-oriented programming
Software architecture
Java application development
Automated testing
Maven project management
Debugging and validation
Git workflow

Author
Noor AlAzzawi
```
