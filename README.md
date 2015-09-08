# SoccerSimulator

This is the soccer simulator for 4210.  Feel free to make changes as you see fit.

## Compiling

You will need java 8.

To compile use the command `./sbt compile` while in the parent directory.

## Running

To run the program use the command `./sbt run`.

## Entering Teams

A sample team has been included, BrutalBeaters.  To add your own team, you must implement the Team interface located in `src/main/java`.  Place the compiled `.class` file in the parent directory and your source code in `src/main/java`.  Hopefully we will remove this dependancy on the source code by classtime.

## Rules
 
1. Teams may not block an opponents kicking position.
2. Infinite loops within teams are prohibitted.

