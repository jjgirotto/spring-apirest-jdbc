# Sistema de Estacionamento

Esta é uma aplicação de estacionamento utilizando Springboot, JDBC e API Rest, que possui autenticações e autorizações referentes aos clientes e vagas de um estacionamento. O cliente deve ser um usuário cadastrado, que pode entrar e sair do estacionamento.
O perfil admin também está presente gerenciando operações específicas permitidas, assim como perfil cliente.

## Funcionalidades Principais

- Gerenciamento de usuários como criação, atualização de senha, consulta de usuários e consulta por id.
- Autenticação de usuário por meio de token utilizando Spring Security e JWT.
- Criação e consulta de clientes;
- Criação e consulta de vagas;
- Criação e consulta de recibos;
- Gerenciamento de operações de estacionamento: entrada e saída de veículos.

## Requisitos

Certifique-se de ter as seguintes ferramentas instaladas:
- Java Development Kit (JDK) 17 ou superior;
- Maven 3.9.4 ou superior;
- MySQL Workbench;
- JasperSoft 6.20.5 ou superior;
- As dependências necessárias estão disponíveis no arquivo `pom.xml`
- Utilize ferramentas como o Postman para teste de requisições.

## Instalação

Siga os passos abaixo para configurar o projeto no seu ambiente de execução:
1. **Clone o repositório**
```bash
git clone https://github.com/jjgirotto/spring-apirest-jdbc.git
```
3. **Execute o projeto**

Para executar o projeto no IntelliJ, use `Alt + F10` ou Run as Java Project no Eclipse.

4. **Acesse o bando de dados**

Crie uma database com o nome `demo_park` no MySQL.

### Contato

Para dúvidas ou problemas, entre em contato com:
* Nome: Juliana Girotto
* Email: ads.jjgirotto@gmail.com
* GitHub: github.com/jjgirotto
