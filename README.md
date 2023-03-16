# spring-boot-camel-sftp


# Demo Camel SFTP with Spring Boot

basata su :
https://github.com/nwdong/camel-sftp

# To run
## to start sftp server Docker container
start server unix ftp su docker:

```
start-server.sh
```


```
mvnw spring-boot:run
```


copiare qualsiasi file nella directory myshare

questo verrà copiato in sftp sulla macchina docker che condivide la dir docker-ssh-sftp/tmp
