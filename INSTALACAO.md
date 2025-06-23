# Guia de Instalação - Espaço La Música

## Pré-requisitos

### Software Necessário
- **Java 17+** (OpenJDK ou Oracle JDK)
- **Maven 3.6+**
- **Node.js 18+** e **npm/pnpm**
- **PostgreSQL 12+**
- **Git**
- **IDE** (IntelliJ IDEA, VS Code, etc.)

### Verificação dos Pré-requisitos
```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Node.js
node -version
npm -version

# Verificar PostgreSQL
psql --version

# Verificar Git
git --version
```

## Configuração do Banco de Dados

### 1. Instalar PostgreSQL
- **Windows**: Baixar do site oficial postgresql.org
- **macOS**: `brew install postgresql`
- **Linux**: `sudo apt-get install postgresql postgresql-contrib`

### 2. Configurar Banco de Dados
```sql
-- Conectar ao PostgreSQL como superusuário
psql -U postgres

-- Criar banco de dados
CREATE DATABASE sistema_escolar_db;

-- Criar usuário (opcional)
CREATE USER sistema_user WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE sistema_escolar_db TO sistema_user;

-- Sair do psql
\q
```

### 3. Configurar PgAdmin (Opcional)
1. Baixar e instalar PgAdmin
2. Conectar ao servidor PostgreSQL
3. Criar o banco de dados `sistema_escolar_db`

## Configuração do Backend

### 1. Clonar o Repositório
```bash
git clone <url-do-repositorio>
cd sistema-escolar/backend
```

### 2. Configurar application.properties
Editar `src/main/resources/application.properties`:
```properties
# Configurações do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_escolar_db
spring.datasource.username=postgres
spring.datasource.password=sua_senha_do_postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações do JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Configurações do servidor
server.port=8080

# CORS (para desenvolvimento)
spring.web.cors.allowed-origins=http://localhost:5173
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,PATCH
spring.web.cors.allowed-headers=*
```

### 3. Instalar Dependências e Executar
```bash
# Instalar dependências
mvn clean install

# Executar aplicação
mvn spring-boot:run

# Ou via IDE: executar a classe principal com @SpringBootApplication
```

### 4. Verificar Backend
- Acesse `http://localhost:8080/api/pessoas` no navegador
- Deve retornar uma lista vazia `[]`

## Configuração do Frontend

### 1. Navegar para o Diretório Frontend
```bash
cd ../frontend
```

### 2. Instalar Dependências
```bash
# Usando npm
npm install

# Ou usando pnpm (recomendado)
pnpm install
```

### 3. Configurar Variáveis de Ambiente (Opcional)
Criar arquivo `.env.local`:
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 4. Executar Frontend
```bash
# Usando npm
npm run dev

# Ou usando pnpm
pnpm run dev
```

### 5. Verificar Frontend
- Acesse `http://localhost:5173` no navegador
- Deve exibir a página inicial do Sistema Escolar

## Configuração do Ambiente de Desenvolvimento

### IntelliJ IDEA (Backend)
1. Abrir o projeto backend
2. Configurar SDK do Java 17+
3. Importar como projeto Maven
4. Configurar run configuration para Spring Boot
5. Instalar plugins: Spring Boot, JPA

### VS Code (Frontend)
1. Abrir o diretório frontend
2. Instalar extensões recomendadas:
   - ES7+ React/Redux/React-Native snippets
   - Tailwind CSS IntelliSense
   - Auto Rename Tag
   - Prettier
   - ESLint

## Testando a Integração

### 1. Verificar Backend
```bash
# Testar endpoint de pessoas
curl http://localhost:8080/api/pessoas

# Testar endpoint de cursos
curl http://localhost:8080/api/cursos

# Testar endpoint de matrículas
curl http://localhost:8080/api/matriculas
```

### 2. Testar Frontend
1. Acessar `http://localhost:5173`
2. Navegar entre as páginas (Pessoas, Cursos, Matrículas)
3. Tentar criar uma nova pessoa
4. Verificar se os dados aparecem no banco

### 3. Verificar Banco de Dados
```sql
-- Conectar ao banco
psql -U postgres -d sistema_escolar_db

-- Verificar tabelas criadas
\dt

-- Verificar dados
SELECT * FROM pessoa;
SELECT * FROM curso;
SELECT * FROM matricula;
```

## Executando Testes

### Backend
```bash
cd backend

# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=PessoaServiceTest
```

### Frontend
```bash
cd frontend

# Executar testes (quando implementados)
npm test
```

## Solução de Problemas Comuns

### Erro de Conexão com Banco
1. Verificar se PostgreSQL está rodando
2. Confirmar credenciais no `application.properties`
3. Verificar se o banco de dados existe
4. Testar conexão manual com psql

### Erro de CORS
1. Verificar configuração de CORS no backend
2. Confirmar URL do frontend nas configurações
3. Verificar se ambos os serviços estão rodando

### Erro de Porta em Uso
```bash
# Verificar processos na porta 8080
lsof -i :8080

# Verificar processos na porta 5173
lsof -i :5173

# Matar processo se necessário
kill -9 <PID>
```

### Problemas com Dependências
```bash
# Backend - limpar cache Maven
mvn clean install -U

# Frontend - limpar cache npm
rm -rf node_modules package-lock.json
npm install
```

## Configuração para Produção

### Backend
1. Alterar `spring.jpa.hibernate.ddl-auto` para `validate`
2. Configurar variáveis de ambiente para credenciais
3. Configurar logging apropriado
4. Configurar CORS para domínio de produção

### Frontend
1. Configurar variável de ambiente para API de produção
2. Executar build: `npm run build`
3. Servir arquivos estáticos via nginx ou similar

### Banco de Dados
1. Usar ferramentas de migração (Flyway/Liquibase)
2. Configurar backup automático
3. Configurar monitoramento

## Próximos Passos

Após a instalação bem-sucedida:
1. Explorar todas as funcionalidades
2. Cadastrar dados de teste
3. Verificar relatórios e estatísticas
4. Planejar customizações necessárias
5. Configurar ambiente de produção

---

Para suporte adicional, consulte a documentação completa ou entre em contato com a equipe de desenvolvimento.

