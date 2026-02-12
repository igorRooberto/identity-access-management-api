Boa 🔥 isso sobe MUITO o nível do projeto.

Vou atualizar o README incluindo:

✅ Testes Unitários

✅ Testes de Integração

✅ Testes de Gateways (infraestrutura)

✅ Testes da Camada Web (controllers)

Segue a versão final completa em Markdown:

# 🔐 Identity Access Management API

> ⚠️ Este projeto ainda está em desenvolvimento.

Este módulo faz parte de um **E-commerce em construção**, sendo responsável exclusivamente pela **camada de identidade, autenticação e autorização da aplicação**.

Ele representa o serviço de segurança do ecossistema, garantindo que apenas usuários autenticados e autorizados possam acessar os recursos do sistema.

---

# 🛒 Sobre o Projeto Maior (E-commerce)

O E-commerce completo será composto por múltiplos módulos, como:

- 🛍️ Catálogo de Produtos
- 🛒 Carrinho de Compras
- 💳 Pagamentos
- 📦 Pedidos
- 👤 Gestão de Usuários
- 🔐 Identity & Access Management (este módulo)

Este repositório contém **somente o módulo responsável por segurança e identidade**, que poderá futuramente funcionar como um serviço independente dentro de uma arquitetura distribuída.

---

# 🎯 Objetivo deste Módulo

Este serviço é responsável por:

- ✅ Cadastro de usuários
- ✅ Autenticação
- ✅ Emissão e validação de tokens (JWT)
- ✅ Controle de acesso baseado em papéis (RBAC)
- ✅ Proteção de rotas
- ✅ Aplicação do princípio do menor privilégio

Ele funciona como o **guardião de acesso** do sistema.

---

# 🛠️ Tecnologias Utilizadas

- ☕ Java 21
- 🌱 Spring Boot 3+
- 🔐 Spring Security
- 🗄️ Spring Data JPA
- 🐘 PostgreSQL
- 📜 JWT (JSON Web Token)
- 📚 OpenAPI / Swagger
- 🧪 JUnit
- 🧪 Mockito
- 🧪 Spring Boot Test
- 🐳 Docker (opcional)

---

# 🧪 Estratégia de Testes

O projeto foi desenvolvido com foco em **qualidade, segurança e testabilidade**, possuindo:

## ✅ Testes Unitários

Cobrem:

- Regras de negócio do domínio
- Casos de uso da aplicação
- Validações
- Serviços internos
- Componentes isolados com uso de mocks

Objetivo:
- Garantir invariantes do domínio
- Validar regras de autenticação e autorização
- Proteger contra regressões

---

## ✅ Testes de Integração

Cobrem:

- Integração entre camadas (application + infrastructure)
- Persistência com banco de dados
- Implementação de gateways
- Fluxos completos de autenticação

Objetivo:
- Garantir que as integrações funcionam corretamente
- Validar comportamento real do sistema

---

## ✅ Testes de Gateways (Infraestrutura)

Cobrem:

- Implementações concretas de repositórios
- Comunicação com banco de dados
- Adaptadores externos

Objetivo:
- Validar contratos definidos pelas interfaces do domínio
- Garantir que a infraestrutura respeita as regras do core

---

## ✅ Testes da Camada Web

Cobrem:

- Controllers
- Validação de requisições
- Status HTTP corretos
- Serialização e desserialização
- Segurança das rotas

Objetivo:
- Garantir comportamento correto da API
- Validar regras de segurança via Spring Security
