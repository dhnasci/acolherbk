### Projeto Acolher backend
Este projeto é parte do sistema de atendimento do projeto Acolher do 
departamento de Psicologia da Igreja AD Madureira, que tem autenticação e
controle de acesso.

Ele está localizado na pasta **C:\Des\acolherbk** - projeto **acolherbk** no Intellij.

Ele faz paz com o projeto em **Vue.js** que está localizado em _**vue-acolher**_ 
na pasta **C:\Des\vue-acolher** onde está a parte do Front-end do sistema Acolher.

Ele está hospedado no servidor do **Heroku** com o nome de **acolher**

## Flyway
Para implantar novo flyway vindo de produção no ambiente local, precisa 
fazer o **repair** primeiro e depois configurar os endereços locais de acordo com o 
git apontando para ambiente local.

Para fazer migração suave, dar checkout na branch prod1 de produção, 
e rodar a aplicação localmente, colocando no main isMigration = true.  Quando terminar
de rodar vai ter aplicado em produção tudo.  Depois só dar o rollback no main para
voltar isMigration = false.  Ai dar o push na prod1 que vai subir redondo.

## Instruções
para rodar o projeto:
1. executar esta aplicação AcolherbkApplication apontando para prod ou dev
2. executar o frontend atraves de janela de cmd apontando para .env-dev: **yarn dev** 