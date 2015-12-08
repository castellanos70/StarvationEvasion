#StarvationEvasion Server
##Readme for users
This part of the project is probably not supposed to be invoked manually. If you **do** want to run it, run it with the following command line arguments:

    java -jar server.jar /path/to/password/file.tsv command to invoke AI

Password files are tab separated, with the first column being the user name, the second being the password, and the third (optional) column specifying a (case-sensitive, all caps) region from the US, as specified in the EnumRegion enumeration. The third column must be present in all rows, or absent in all rows.

The rest of the command line will be fed to the system shell, and be used to invoke an AI process. Note that you should ***not*** quote the command.

##Readme for Developers
###AI Processes
The AI process part of the command line must be fully qualified (`path/to/java blah bah`), and the launched process will receive information via environment variables (see the comments on Server.main for details).
###Protocol
The server will communicate with the clients using Java's serialization capabilities, sending and receiving objects in starvationevasion.common.messages. See the documentation for those classes for more information.
###State Diagram
A state diagram exists in the doc/ subdirectory.