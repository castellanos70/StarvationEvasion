# Server
I have supplied a ```Client.java``` file. Run it and try out some of these commands.

You can also use my hosted [web page](http://cs.unm.edu/~javierc/testing.html).

Client that is more like the ```Client.java``` can be found [here](http://cs.unm.edu/~javierc/client.html)

# Run

###### Local
Run everything locally. Add execute permission to Game
```bash
chmod +x Game
```

**Server**
```bash
./Game server
```

**Client**
```bash
./Game client
```


###### Remote
By remote I mean you connect a client to a remote server. Here is the
code to connect to a **RUNNING** server. You need to change ```666```
to the port that you plan on running your client on. So if your client
is running on port 2020 then change 666 to 2020.

This will create a SSH tunnel. It will hang so you can press CTRL-z to
send it to the background and fg will bring it back so you can stop it.
```bash
ssh -N -L 666:localhost:5555 username@peterbilt.cs.unm.edu
```

After you have your tunnel, you can start your client.


# Invoking
Commands shall be formatted as:
```bash
time command args
```

### User

* ```user_create {"username":"","password":"", "region":"california"}``` Region is optional. If not supplied a region will be randomly chosen
* ```user_read {"username":"user"}```  returns the region of that user
* ```user_update ``` Update your user (login is required)
* ```users``` Get a list of all the users.


### Login
**NOTE** Default login is: admin admin

* ```login {"username":"admin", "password":"admin"}```

### Chat
Chat data will need to be formatted carefully ```chat region json```.

##### Example chat command
```
chat {"to":"california",card":null,"text":"normal text"}
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

# Consuming

Consuming data should be fairly simple.

### Response
The server will always respond with JSON.

A reponse is layed out like this and will not change. 
```json
{
"time": 2938293992.23,
"message": "",
"type": "",
"data": null
}
```
###### Data
The key named ```data``` is special in that the value will either be a
object or an array depending on the type.

| Type  | data value type |
|-------|-----------------|
| chat  | object          |
| user  | object          |
| users | array           |






