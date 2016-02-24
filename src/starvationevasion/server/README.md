# Server
I have supplied a ```Client.java``` file. Run it and try out some of these commands.


# Commands
Commands shall be formatted as:
```bash
time command args
```

### User

* ```user_create username pwd [region]``` Region is optional. If not supplied a region will be randomly chosen
* ```user_read username```  returns the region of that user
* ```user_update ``` Update your user (login is required)
* ```users``` Get a list of all the users.


### Login
**NOTE** Default login is: admin admin

* ```login username pwd```

### Chat
Chat data will need to be formatted carefully ```chat region json```.

##### Example chat command
```
chat CALIFORNIA {"card":null,"text":"normal text"}
```

Here are some examples of JSON. These are examples, *the actual data you are sending might be different!!*

###### Sending a card, with text
```json
{"card":{"name":"name"},"text":"text"}
```
###### Sending only text
```json
{"card":null,"text":"text"}
```

