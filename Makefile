JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Main.java \
        SimulationPane.java \
        SoccerBatch.java \
        SoccerGUI.java \
	SoccerGame.java \
	Team.java \
	TeamLoader.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) Main.class
	$(RM) SimulationPane.class
	$(RM) SoccerBatch.class
	$(RM) SoccerGUI.class
	$(RM) SoccerGame.class
	$(RM) Team.class
	$(RM) TeamLoader.class
	$(RM) SimulationPane\$$*
	$(RM) *~
