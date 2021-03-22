### Rodar aplicação
```bash
$ mvn clean install
$ cd infra
$ docker-compose up -d
```
### Endpoints
- GET http://localhost/api/v1/hero?name=<name>
- POST http://localhost:8080/api/v1/hero
  
  Body: 
  ```json
  {
      "name": "Super-man",
      "race": "HUMAN",
      "power_stats": {
        "strength": 100,
        "agility": 100,
        "dexterity": 100,
        "intelligence": 100,
      },
      "enabled": true,
  }
  ```
- GET http://localhost:8080/api/v1/hero/<id>
- PUT http://localhost:8080/api/v1/hero/<id>

  Body:
  ```json
  {
      "id": "6b3c84e6-bf4d-4bb0-b4a4-db1ed06b2a80",
      "name": "Super-man",
      "race": "HUMAN",
      "power_stats": {
        "id": "6b3c84e6-bf4d-4bb0-b4a4-db1ed06b2a80",
        "strength": 100,
        "agility": 100,
        "dexterity": 100,
        "intelligence": 100,
        "created_at": "2021-03-20T23:02:24.384Z",
        "updated_at": "2021-03-20T23:02:24.384Z"
      },
      "enabled": true,
      "created_at": "2021-03-20T23:02:24.384Z",
      "updated_at": "2021-03-20T23:02:24.384Z"
    }
    ```
- DELETE http://localhost:8080/api/v1/hero/<id>
- POST http://localhost:8080/api/v1/hero/compare

    Body:
    ```json
      {
      "hero1":     {
        "id": "652e9328-4998-4014-a399-1108c6d45608",
        "name": "Spider-man",
        "race": "ALIEN",
        "enabled": true,
        "powerStats": {
          "id": "d5ca5f98-f9b2-4073-b8fb-598a773c82b8",
          "strength": 100,
          "agility": 100,
          "dexterity": 100,
          "intelligence": 100,
          "createdAt": "2021-03-22T02:11:30Z",
          "updatedAt": "2021-03-22T02:11:30Z"
        },
        "createdAt": "2021-03-22T02:11:30Z",
        "updatedAt": "2021-03-22T02:11:30Z"
      },
      "hero2":{
        "id": "cc69191d-7210-4c03-b68e-a3340d6df6a6",
        "name": "Birdwoman",
        "race": "ALIEN",
        "enabled": true,
        "powerStats": {
          "id": "040326dd-64f2-450b-b6c4-70be95748e27",
          "strength": 100,
          "agility": 100,
          "dexterity": 100,
          "intelligence": 100,
          "createdAt": "2021-03-22T02:11:30Z",
          "updatedAt": "2021-03-22T02:11:30Z"
        },
        "createdAt": "2021-03-22T02:11:30Z",
        "updatedAt": "2021-03-22T02:11:30Z"
      }
    }
    ```

# Teste Programador Backend Sênior
Teste destinado aos candidatos a vaga de Programador Backend <b>Sênior</b>. Se o seu nível é outro, por favor, dê uma olhada nos demais repositórios e escolha o que se adequa a sua skill. 
- [@brainweb-interview-junior](https://github.com/brainweb-br/brainweb-interview-junior)
- [@brainweb-interview-pleno](https://github.com/brainweb-br/brainweb-interview-pleno)

## Descrição
Um cliente chamado Bruce Wayne nos contratou para fazer um sistema com o objetivo de catalogar os super-heróis existentes.
</br>
Parece uma missão difícil, mas, não se preocupe, o seu papel não será o de sair por aí procurando por heróis, vamos deixar isso para o Sr. Wayne...
</br>
Seu papel é desenvolver uma API com as operações básicas de cadastro de um herói e algum mago (coff, coff) do front-end fará as telas.
</br>

## Requisitos
Bom, aqui começa a explicação do que você terá que nos entregar. Leia com atenção.
</br>
Ah, o Alfred (acho que ele é tipo um mordono do Sr. Wayne) começou o projeto para nós e o esqueleto do projeto já existe.
</br>Dito isso vamos deixar uma lista com as tarefas:
- [x] Criar endpoint de criação de heróis respeitando os campos obrigatórios. ***Olhe o script SQL dentro do projeto para saber quais são os campos obrigatórios.***;
- [x] Criar endpoint de busca de heróis e seus atributos por ID. ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found)***;
- [x] Criar endpoint de busca de heróis e seus atributos por filtro, nesse caso o filtro será apenas o nome. ***Caso não encontre nenhum herói o sistema deve retornar um sucesso 200 com o body vazio***;
- [x] Criar endpoint de atualização de heróis, todos os campos poderão ser atualizados. ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found)***;
- [x] Criar endpoint de exclusão de heróis. A exclusão será física, ok? (Física?! É, deleta o registro da base). ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found)***;
- [x] Criar testes unitários e de integração das funcionalidades desenvolvidas. ***As classes de teste unitário terminam com o prefixo `Test.java` e as classes de teste de integração terminam com `IT.java`. Temos um modelo de classe de exemplo dentro do projeto***;
- [x] O sistema deverá ter no mínimo 2 instâncias com carga balanceada através de alguma técnica de Round Robin;
- [x] As máquinas do cluster devem ser passíveis de monitoramento. (Ex: Spring Boot Actuator);
- [x] As chamadas para o banco de dados deverão ser cacheadas por alguma ferramenta de cache distribuído; 
- [x] Criar um `docker-compose.yml` funcional para execução da aplicação. (Banco de Dados + API).

Ah, tem algo mais! O Sr. Wayne nos pediu para criar um endpoint onde ele possa selecionar dois heróis e comparar seus atributos força, agilidade, destreza e inteligência. Como resultado, o sistema deve retornar um objeto contendo os id's e a diferença dos atributos (positivo se maior, negativo se menor) de cada herói. Dá uma pensada em como vai ficar esse objeto e o caminho do endpoint, tudo bem?
<p>
Agora sim, terminamos! Se você nos entregar isso que pedimos garanto que o Sr. Wayne vai pirar!!!

## Considerações
Leia essas instruções para ganhar tempo no desenvolvimento, ok? ;)
</br>
#### Primeiro Passo
Como primeiro passo faça um ***fork*** desse projeto na sua conta do GitHub, se não tiver uma conta é só criar uma nova.
</br>
***Não iremos avaliar provas que não estejam nesse padrão, então MUITA ATENÇÃO nessa dica.***
#### Correção
Ao término da prova, ***abra um PR (Pull Request)***, é assim que iremos avaliar o código proposto.
#### Configurações
- OpenJDK 11 instalado;
- Maven na versão 3.6+ instalado;
- IDE pode ser o de preferência, mas gostamos bastante do IntelliJ por aqui;
- Docker e docker-compose instalados.

#### Testes
Para rodar os testes (unitários e de integração) utilize o comando a seguir:
```
mvn clean verify
```

#### Bônus
Será considerado um plus os candidatos que entregarem:
- Bom uso dos padrões de REST;
- Uso de BDD para escrever os testes de integração;
- Uso de algum API Gateway para controle de rotas, trafêgo e resiliência da aplicação;
- O sistema ser capaz de se recuperar de falhas. (Circuit-Breaker);
- Monitoramento da aplicação por alguma ferramenta (Ex: Grafana);
- Mecanismo de Feature Toggle usando JMX;
