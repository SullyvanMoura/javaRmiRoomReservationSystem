# Java RMI - Room Reservation System

Sistema distribuído de reserva de salas utilizando Java RMI.

## Arquivos já compilados

Requisito: Java 8

Aqui estão os arquivos do projeto já compilados em formato .jar

[Jar do cliente](src/jars/client-jar-with-dependencies.jar)

[Jar do servidor](src/jars/server-jar-with-dependencies.jar)

Para executar o servidor:

```
java -jar server-jar-with-dependencies.jar
```

Para executar o cliente:

```
java -jar client-jar-with-dependencies.jar server_adress
```

OBS: _server_adress_ é o endereço do servidor. Caso nenhum valor seja passado, o endereço do servidor é considerado _localhost_

## Compilação

Requisito: Maven (https://maven.apache.org/)

No diretório raiz do projeto, executar o comando:
```
mvn clean package
```

Este comando gera os arquivos .jar do cliente e do servidor dentro da pasta _target_, no diretório raiz do projeto.