# Starvation Evasion Simulator, Sever and Visualizer

[JavaDoc](http://castellanos70.github.io/StarvationEvasion/)

#### Table of Contents

1. [Overview](#overview)
2. [Compilation and Project Configuration](#config)
3. [Server](#server)
4. [Client Aegislash](#clientAegislash)
5. [Client Mega Mawile](#clientMegaMawile)
6. [Simulator](#simulator)
7. [Visualizer](#visualizer)

## Overview

The Starvation Evasion Game is a multiplayer Client/Server game.<br>
First, the server should be started on one machine. The server will create an instance
of the simulator and manage client connections. The server then starts n=0 through
7 AI clients and waits for 7-n player client connections.<br><br>

Each player client is started on a separate computer and connects to the server via TCP/IP.<br><br>


## Config

Search path must have the following roots:
<ul>
<li>src: Source</li>
<li>data: Resources</li>
<li>assets: Resources</li>
<li>doc: Documentation</li>
</ul>

## Server
* Javier Chavez - javierc@cs.unm.edu

Take a look at it [here](https://github.com/castellanos70/StarvationEvasion/tree/master/src/starvationevasion/server).


## Simulator
* Joel Castellanos - joel@unm.edu
* Alfred Sanchez - miggens@unm.edu
* Peter Blemel - pblemel@unm.edu
* Alex Baker - alexebaker@unm.edu

## Visualizer
* Anand Lalvani - alalvani@unm.edu
* Brett Hartel - bhartel@unm.edu
* Laurence Mirabal - lmirabal8689@unm.edu
* Tess Daughton - tdaughto@unm.edu


## ClientAegislash
* Ari Rappaport - arappaport@unm.edu
* Julian Weisburd - jmweisburd@unm.edu
* Nathan Gonzales - ngonzales@unm.edu
* Ryan Delao - rdelao@unm.edu

Usage: starvationevasion.client.Aegislash.MainClient_Aegislash


## ClientMegaMawile
* Chris Wu - cwu@unm.edu
* Javier Chavez - javierc@unm.edu
* Keira Haskins - wasp@unm.edu
* Mark Mitchell - mamitchell@unm.edu
* Evan King - eking522@unm.edu

Usage: starvationevasion.client.Aegislash.MainClient_MegaMawile


