::@echo off
javac Main.java View.java Controller.java Model.java splash.java Player.java PlayerPanel.java udpServer.java Audio.java
if %errorlevel% neq 0 (
	echo Error Compiling	
) else (
	echo Compiled correctly
	java Main
)

