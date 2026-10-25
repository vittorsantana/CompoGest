# CompoTree

Sistema acadêmico de gestão de produtos e custos usando Java, Spring Boot e HTML/CSS.

## Requisitos
- JDK 17 ou superior
- Maven (ou Maven Wrapper)

## Como executar
Na pasta que contém o `pom.xml`, execute:

```powershell
mvn spring-boot:run
```

Se o Maven Wrapper estiver disponível no seu projeto Windows, também pode usar:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois, abra http://localhost:8080 no navegador.

## Estrutura inicial
- `CompoTreeApplication.java`: inicia a aplicação web.
- `model/ItemFormula.java`: representa um item e seu custo.
- `static/index.html`: tela inicial.
- `static/style.css`: estilos da tela.

Esta é a base inicial. A árvore genérica, os cálculos de explosão/implosão, CRUD e banco de dados serão adicionados nas próximas etapas.
