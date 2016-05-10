# Starvation Evasion Simulator, Server and Visualizer

[JavaDoc](http://castellanos70.github.io/StarvationEvasion/)

#### Table of Contents

1. [Overview](#overview)
2. [Compilation and Project Configuration](#Compilation and Project Configuration)
3. [Starting up the Game](#Starting up the Game)

## Overview

The Starvation Evasion Game is a multiplayer Client/Server game.<br>
First, the server should be started on one machine. The server will create an instance
of the simulator and manage client connections. The server then starts n=0 through
7 AI clients and waits for 7-n player client connections.<br><br>

Each player client is started on a separate computer and connects to the server via TCP/IP.<br><br>


## Compilation and Project Configuration

<ul>
<li>Libraries: javafx-mx, sqlite-jdbc-3.8.11.2</li>
<li>Source Folders: src, libs</li>
<li>Resource Folders: data, assets</li>
<li>Run Folder: bin</li>
<li>Dependencies: JDK 1.8, javafx-mx, sqlite-jdbc-3.8.11.2</li>
</ul>

## Starting up the Game
The client entry point is:<br>
<tt>starvationevasion.client.UpdateLoop</tt> Running this should ask you for single or multi-player.
Multi-player will attempt to connect to <tt>foodgame.cs.unm.edu:5555</tt>.
Assuming the server is up and running, this will work. <br><br>

Selecting single player requires that you first start a local server. the server entry point is:
<tt>starvationevasion.server.Server</tt>. The default port is 5555.

## Server
* Javier Chavez - javierc@cs.unm.edu

Take a look at it [here](https://github.com/castellanos70/StarvationEvasion/tree/master/src/starvationevasion/server).

## Compiling A Lightweight Server.jar/Ai.jar

If you build a .jar file to run the server, as of writing it results in a .jar that is about 800 mb in size. To reduce the overhead in pushing updated Server.jar files to the repository, it is necessary to modify the result to include only what is needed.

1. Build an executable .jar file that points to starvationevasion.server.Server
2. Rename it "Server.jar" and move it to the top-level folder for the project (StarvationEvasion - not the package)
3. Open Server.jar inside of something like 7zip so that you can modify its contents.
4. Remove the following folders: ActionButtons, cardImages, farmProductIcons, sim/climate, visResources, starvationevasion/client
5. Remove the following files: background.png, Mollweide_projection.jpg, WorldMap_MollweideProjection.png, WorldMap_MollweideProjection2.png, WorldMap_MollweideProjection_With_Region_Boarders_Added.png
6. Remove any additional jars that are irrelevant, such as previous server or ai jars that you may have made in the past. These folders are usually found in your output file folder.

At the time of writing, this could reduce the .jar file from about 800 mb to ~15 mb. This makes it much more reasonable to rebuild and push to github.



