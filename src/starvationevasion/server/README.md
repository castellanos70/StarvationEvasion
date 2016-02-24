#StarvationEvasion Server

commands are issued as

```
time command args
```

CRUD (create read update delete)

# Client Commands

## User

* ```user_create username pwd [region]``` if region is supplied it will try to create a user in that region
on success true is returned else false
* ```user_read username```  returns the region of that user
* ```user_update ```

## Login

* ```login username pwd```

## Chat
Chat data will need to be formatted carefully ```chat username data```. Data is a JSON

```
{
"card": data,
"text": "normal text"
}
```
or
```
{
"card": null,
"text": "normal text"
}
```

