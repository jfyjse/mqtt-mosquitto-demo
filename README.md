# mqtt-mosquitto-demo

## postman details
api- http://localhost:8080/sendMessage
sample data
{
    "topic":"topicsz",
    "message":"msg from postman"
}
# setting up mosquitto in local

## How to set up mosquitto in the local environment.
    Install latest version of mosquitto (2.0 and above).
    
    Navigate to the installed location /etc/mosquitto/ (in linux) with root access. To access location with root access use command 
    sudo nautilus.


## To enable authentication for mosquitto: [refer https://mosquitto.org/documentation/authentication-methods/  for more authentication methods]


    Open terminal in the location /etc/mosquitto/.
    
    Type command - mosquitto_passwd -c pwd admin to create a user with the name admin and click enter. Note pwd is the password file name.
    
    The terminal will ask for password and re enter password.
    
    Instead of above method, we can also manually create a file with the desired name and add the username and password as in the format 

    
   username : password

    Note that we have to encrypt the password using the command mosquitto_passwd -U pwd. This will encrypt the human readable password to non-readable form.
    
        
    Add the following to the configuration file mosquitto.config.
[
per_listener_settings true
listener 1883
password_file /etc/mosquitto/pwd
allow_anonymous false
] 
note: square bracket not needed, per_listener_settings true should be at the top.
The password_file location is upto the user and the location has to be valid.

## Run mosquitto. 
Before running make sure that :
    
    All instances are killed if not then use the command sudo pkill mosquitto.
    
    Run command sudo mosquitto -v -c /etc/mosquitto/mosquitto.conf . again the location of config is entirely up to the user.

## Testing mosquitto.
There are 2 users, subscribers and publishers. 
Because we have implemented authentication, we have to give username and password to test out the same.


The publisher can publish by using the command :

mosquitto_pub -t topics -m "hai hello" -u admin -P 12345.
Here “topics” is the topic name , “hai hello” is the message and username and password are admin and 12345 respectively.

Note : -P should be in capital letters.

The subscriber can subscribe to a topic by using the command:

mosquitto_sub -t topics -u admin -P 12345. Here “topics” is the topic name ,username and password are admin and 12345 respectively.
Any message sent by the publisher is received by the subscriber as long as the terminal is kept live.

Note : -P should be in capital letters.

## Notes:
To get all logs and displayed on terminal add these to the config file.


log_type all

log_dest topic

log_dest stdout

log_dest file /var/log/mosquitto/mosquitto.log
