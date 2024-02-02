#!/bin/bash

# Function to configure RabbitMQ
configure_rabbitmq() {
    /usr/sbin/rabbitmq-server -detached  
    sleep 5
    rabbitmqctl start_app
    /usr/sbin/rabbitmq-plugins enable rabbitmq_management  
    /usr/sbin/rabbitmqctl add_user lsnow shaarei1234
    /usr/sbin/rabbitmqctl set_user_tags lsnow administrator
    /usr/sbin/rabbitmqctl set_permissions -p / lsnow ".*" ".*" ".*"
    # /usr/sbin/rabbitmqctl stop
}

# Configure RabbitMQ
configure_rabbitmq

java -jar app.jar
