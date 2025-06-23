# Espaço La Música - Documentação Completa

## Visão Geral

O Espaço La Música é uma aplicação web completa desenvolvida para gerenciar pessoas, cursos e matrículas em instituições de ensino. O sistema é composto por um backend em Java com Spring Boot e um frontend em React, oferecendo uma interface moderna e intuitiva para administração escolar.

## Arquitetura do Sistema

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.x
- **Linguagem**: Java 17+
- **Banco de Dados**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Arquitetura**: REST API com padrão MVC
- **Porta**: 8080

### Frontend (React)
- **Framework**: React 18+ com Vite
- **Linguagem**: JavaScript (JSX)
- **UI Library**: shadcn/ui + Tailwind CSS
- **Roteamento**: React Router DOM
- **Ícones**: Lucide React
- **Porta**: 5173

## Funcionalidades Principais

### 1. Gerenciamento de Pessoas
- Cadastro de pessoas (alunos, professores, funcionários)
- Listagem com busca por nome, CPF ou email
- Edição e exclusão de registros
- Validação de CPF único

### 2. Gerenciamento de Cursos
- Cadastro de cursos com informações completas
- Controle de status (ativo/inativo)
- Listagem com busca por nome ou descrição
- Edição e exclusão de cursos
- Validação para cursos com matrículas

### 3. Gerenciamento de Matrículas
- Realização de matrículas de alunos em cursos
- Controle de status de pagamento (Pendente, Pago, Vencido)
- Listagem por aluno ou curso
- Cancelamento de matrículas
- Histórico completo de matrículas

### 4. Dashboard
- Estatísticas em tempo real
- Contadores de pessoas, cursos e matrículas
- Visão geral do sistema

## Estrutura do Projeto

```
sistema-escolar/
├── backend/                          # Aplicação Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/sistemaescolar/
│   │   │   │   ├── controller/       # Controllers REST
│   │   │   │   ├── service/          # Lógica de negócio
│   │   │   │   ├── model/            # Entidades JPA
│   │   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   └── repository/       # Repositórios JPA
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                     # Testes unitários
│   └── pom.xml                       # Dependências Maven
└── frontend/                         # Aplicação React
    ├── src/
    │   ├── components/ui/            # Componentes UI reutilizáveis
    │   ├── pages/                    # Páginas da aplicação
    │   ├── App.jsx                   # Componente principal
    │   └── main.jsx                  # Ponto de entrada
    ├── package.json                  # Dependências npm
    └── vite.config.js               # Configuração do Vite
```

## Endpoints da API

### Pessoas
- `GET /api/pessoas` - Listar todas as pessoas
- `GET /api/pessoas/{id}` - Buscar pessoa por ID
- `GET /api/pessoas/cpf/{cpf}` - Buscar pessoa por CPF
- `POST /api/pessoas` - Criar nova pessoa
- `PUT /api/pessoas/{id}` - Atualizar pessoa
- `DELETE /api/pessoas/{id}` - Excluir pessoa

### Cursos
- `GET /api/cursos` - Listar todos os cursos
- `GET /api/cursos/ativos` - Listar apenas cursos ativos
- `GET /api/cursos/{id}` - Buscar curso por ID
- `POST /api/cursos` - Criar novo curso
- `PUT /api/cursos/{id}` - Atualizar curso
- `PATCH /api/cursos/{id}/status/{ativo}` - Alterar status do curso
- `DELETE /api/cursos/{id}` - Excluir curso

### Matrículas
- `GET /api/matriculas` - Listar todas as matrículas
- `GET /api/matriculas/{id}` - Buscar matrícula por ID
- `GET /api/matriculas/aluno/{alunoId}` - Listar matrículas por aluno
- `POST /api/matriculas` - Realizar nova matrícula
- `PATCH /api/matriculas/{id}/status-pagamento` - Atualizar status de pagamento
- `DELETE /api/matriculas/{id}` - Cancelar matrícula

## Modelos de Dados

### Pessoa
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "dataNascimento": "1990-05-15",
  "email": "joao.silva@email.com",
  "telefone": "(11) 98765-4321"
}
```

### Curso
```json
{
  "id": 1,
  "nome": "Programação Java com Spring Boot",
  "descricao": "Curso completo de desenvolvimento backend",
  "valor": 1200.00,
  "cargaHoraria": 120,
  "ativo": true
}
```

### Matrícula
```json
{
  "id": 1,
  "aluno": { /* objeto Pessoa */ },
  "curso": { /* objeto Curso */ },
  "dataMatricula": "2024-01-15",
  "dataVencimento": "2024-02-15",
  "valorCobrado": 1200.00,
  "statusPagamento": "PENDENTE"
}
```

## Tecnologias Utilizadas

### Backend
- **Spring Boot**: Framework principal
- **Spring Data JPA**: Persistência de dados
- **Spring Web**: APIs REST
- **PostgreSQL**: Banco de dados relacional
- **Maven**: Gerenciamento de dependências
- **JUnit 5**: Testes unitários
- **Mockito**: Mocking para testes

### Frontend
- **React**: Biblioteca para interfaces
- **Vite**: Build tool e dev server
- **React Router DOM**: Roteamento
- **Tailwind CSS**: Framework CSS
- **shadcn/ui**: Componentes UI
- **Lucide React**: Ícones
- **Fetch API**: Comunicação com backend

## Padrões e Boas Práticas

### Backend
- **Arquitetura em Camadas**: Controller → Service → Repository
- **DTOs**: Separação entre entidades e dados de transferência
- **Injeção de Dependência**: Via construtor
- **Tratamento de Exceções**: Respostas HTTP apropriadas
- **Validações**: Bean Validation e validações customizadas
- **Testes Unitários**: Cobertura das regras de negócio

### Frontend
- **Componentização**: Componentes reutilizáveis
- **Hooks**: useState, useEffect para gerenciamento de estado
- **Responsividade**: Design adaptável a diferentes telas
- **Acessibilidade**: Componentes acessíveis
- **Organização**: Separação clara entre páginas e componentes

## Próximos Passos e Melhorias

### Funcionalidades
1. **Autenticação e Autorização**
   - Sistema de login
   - Controle de acesso por perfis (admin, professor, aluno)
   - JWT para autenticação

2. **Relatórios**
   - Relatórios de matrículas
   - Relatórios financeiros
   - Exportação em PDF/Excel

3. **Notificações**
   - Lembretes de vencimento
   - Notificações por email
   - Sistema de alertas

4. **Validações Avançadas**
   - Validação de formato de CPF
   - Validação de formato de telefone
   - Máscaras de entrada

### Melhorias Técnicas
1. **Testes**
   - Testes de integração
   - Testes end-to-end
   - Cobertura de testes no frontend

2. **Performance**
   - Cache de dados
   - Paginação
   - Lazy loading

3. **Monitoramento**
   - Logs estruturados
   - Métricas de performance
   - Health checks

4. **Deployment**
   - Containerização com Docker
   - CI/CD pipeline
   - Deploy automatizado

## Considerações de Segurança

1. **Validação de Entrada**: Todos os dados são validados no backend
2. **CORS**: Configurado para permitir apenas origens autorizadas
3. **SQL Injection**: Prevenido pelo uso de JPA/Hibernate
4. **XSS**: Prevenido pela sanitização automática do React

## Suporte e Manutenção

Para suporte técnico ou dúvidas sobre o sistema:
- Documentação da API disponível via Swagger (quando implementado)
- Logs detalhados para debugging
- Estrutura modular facilita manutenção e extensões

---

**Versão**: 1.0.0  
**Data**: Junho 2025  
**Desenvolvido com**: Java Spring Boot + React

