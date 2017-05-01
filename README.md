# *pos-atv-twitter*
Atividade da disciplina de POS, que consiste em uma aplicação que avalia o quão um usuário é popular no *Twitter*. A avaliação vai mensurar dois fatores: a influência e popularidade do usuário.

## *DOCKER*
Este projeto deve ser executado com o *Docker*, então antes de utilizá-lo certifique-se de ter instalado o *docker-compose* em seu SO

## Iniciando os serviços
Para iniciar os serviços, execute o arquivo *startup.sh*

## Encerrando os serviços
Para encerrar os serviços, execute o arquivo *shutdown.sh*

## *Endpoints* da aplicação
  * Principal - *URL*: *http://localhost:8081/twitter*

# Módulos
### auth-core
Módulo criado pelo professor da disciplina para autenticações utilizando a tecnologia *OAuth*
### twitter-auth
Módulo genérico criado pelo aluno para realizar autenticação *OAuth* no *Twitter*
* **application.properties**  
Arquivo de propriedades onde se é informado a *apiKey* e *apiSecret* para sua aplicação *Twitter*
 
### twitter
Módulo com as regras de negócio proposta pela atividade.

### Issues conhecidas
* **Erro no envio de parâmetros para o *Twitter***  
Erro está provavemnete no modo como o *twitter-auth* faz a montagem do *header* de requisição
* **Falha no *deploy* no ambiente** ***Heroku***  
Erro no *binding* da porta, *$PORT*, fornecida pelo *Heroku*, a causa mais aceita é a mal formatação do arquivo *Dockerfile* no módulo ***twitter***
