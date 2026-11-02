# CompoTree — Projeto completo

Aplicação em **Java 17 + Spring Boot + HTML/CSS/JavaScript + banco H2 persistente**. Usa uma **árvore genérica** (`TreeNode<T>`) e estruturas de pai-filho por ID (permitindo componentes repetidos com o mesmo nome, como "Plástico").

## Iniciar
1. Instale **JDK 17+** e **Maven 3.9+**.
2. Abra o terminal **na pasta `CompoTree`** (onde fica `pom.xml`).
3. Execute `mvn spring-boot:run`.
4. Acesse **http://localhost:8080**.
5. Clique em **Carregar teste: Caneta** e **Carregar teste: Computador**.

Para testes automatizados: `mvn test`. Para gerar um JAR: `mvn clean package` e executar `java -jar target/compotree-1.0.0.jar`.

## Recursos
- Cadastro, listagem, edição e exclusão de produtos.
- Cadastro, edição e remoção em cascata de componentes, com pai opcional; inclui prevenção de ciclos.
- Visualização da árvore da fórmula (explosão), cálculo para N unidades e lista de componentes finais para aquisição.
- Alteração de custo unitário e **implosão automática** (recalcula subárvore e custo final).
- Banco H2 gravado no disco em `dados/compotree.mv.db`, persistindo após reiniciar a aplicação.

## Regra de cálculo
O custo *calculado* de um nó **folha** é seu custo unitário. O custo de um nó **com filhos** é a soma de `quantidade_do_filho × custo_calculado_do_filho`, sem somar novamente o preço de referência do pai. A quantidade da raiz no simulador multiplica os custos e os insumos finais. Valores intermediários informados são preços de referência, mas **não** são somados sobre os preços dos filhos.

**Caneta:** custo 0,30 + 0,50 + 0,70 + 0,30 + 0,50 + 0,20 = **R$ 2,50** por unidade. **Computador:** custo **R$ 3.380,00** por unidade, considerando todos os componentes finais do exemplo (Memória RAM ×2).

**Observação:** o Teste 2 do enunciado tem pequenas inconsistências de nomes de variáveis (`itens` em vez de `itensPC` e texto "caneta" em vez de "computador"); os exemplos já foram corrigidos na interface.

## API REST
- `GET /api/produtos` / `POST /api/produtos` / `PUT /api/produtos/{id}` / `DELETE /api/produtos/{id}`
- `GET /api/produtos/{id}/componentes` / `POST /api/produtos/{id}/componentes`
- `PUT /api/componentes/{id}` / `DELETE /api/componentes/{id}` / `PATCH /api/componentes/{id}/preco`
- `GET /api/produtos/{id}/explosao?quantidade=10`

H2 Console (uso local): http://localhost:8080/h2-console · JDBC URL `jdbc:h2:file:./dados/compotree` · usuário `sa` · senha vazia.
