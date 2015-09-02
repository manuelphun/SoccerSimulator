# SoccerSimulator

This is the soccer simulator for 4210.  Feel free to make changes as you see fit.

## Compiling

You will need java 8.

To compile use the command `./sbt compile while in the parent directory.

## Running

To run the program use the command `./sbt run`.

## Entering Teams

Two sample teams have been included, BrutalBeaters and the default team NowhereNancy.  To add your own team, you must implement the Team interface located in `src/main/java` and make sure the class is in the package `soccer`.  Place the compiled `.class` file in the parent directory and the `.java` source code in `src/main/java`.

