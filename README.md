# Starvation Evasion Simulator, Sever and Visualizer

[JavaDoc](http://castellanos70.github.io/StarvationEvasion/)

#### Table of Contents

1. [Overview](#overview)
2. [Compilation and Project Configuration](#Compilation and Project Configuration)
3. [Starting up the Game](#Starting up the Game)
6. [Simulator](#simulator)
7. [Visualizer](#visualizer)

## Overview

The Starvation Evasion Game is a multiplayer Client/Server game.<br>
First, the server should be started on one machine. The server will create an instance
of the simulator and manage client connections. The server then starts n=0 through
7 AI clients and waits for 7-n player client connections.<br><br>

Each player client is started on a separate computer and connects to the server via TCP/IP.<br><br>


## Compilation and Project Configuration

<ul>
<li>Project Settings: -> Libraries -> javafx-mx, sqlite-jdbc-3.8.11.2</li>
<li>Source Folders: src, libs</li>
<li>Resource Folders: data, assets</li>
<li>Run Folder bin</li>
<li>Dependencies: JDK 1.8, javafx-mx, sqlite-jdbc-3.8.11.2</li>
</ul>

## Starting up the Game
The client entry point is:<br>
<tt>starvationevasion.client.Setup.Main</tt> Running this should ask you for single or multi-player.
Multi-player will attempt to connect to <tt>foodgame.cs.unm.edu:5555</tt>.
Assuming the server is up and running, this will work. <br><br>

Selecting single player requires that you first start a local server. the server entry point is:
<tt>starvationevasion.server.Server</tt>. The default port is 5555.

## Server
* Javier Chavez - javierc@cs.unm.edu

Take a look at it [here](https://github.com/castellanos70/StarvationEvasion/tree/master/src/starvationevasion/server).





