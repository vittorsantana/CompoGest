package br.com.compotree.api;
import br.com.compotree.estrutura.*;
import br.com.compotree.model.ItemFormula;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.sql.*;
import java.math.*;
import java.util.*;

@RestController @RequestMapping("/api")
public class ProdutoController {
 private final JdbcTemplate db;
 public ProdutoController(JdbcTemplate db){this.db=db;}
 public record ProdutoEntrada(String nome,Double precoReferencia){}
 public record ComponenteEntrada(String nome,Long paiId,Integer quantidade,Double custoUnitario){}
 public record PrecoEntrada(Double custoUnitario){}
 public record Produto(Long id,String nome,double precoReferencia){}
 public record Componente(Long id,Long produtoId,Long paiId,String nome,int quantidade,double custoUnitario){}
 public record No(Long id,String nome,int quantidade,double custoUnitario,double custoCalculado,List<No> filhos){}
 public record Insumo(Long id,String nome,long quantidade,double custoUnitario,double valorTotal){}
 public record Explosao(Produto produto,int quantidade,No arvore,List<Insumo> insumos,double custoUnitario,double custoTotal){}
 private static String nome(String s){if(s==null||s.isBlank()||s.length()>150)throw new IllegalArgumentException("Informe um nome (até 150 caracteres).");return s.trim();}
 private static double preco(Double v){if(v==null||!Double.isFinite(v)||v<0||v>1e12)throw new IllegalArgumentException("Informe um valor válido e não negativo.");return BigDecimal.valueOf(v).setScale(2,RoundingMode.HALF_UP).doubleValue();}
 private static int qtd(Integer n){if(n==null||n<1||n>1000000)throw new IllegalArgumentException("Quantidade entre 1 e 1.000.000.");return n;}
 private long inserir(String sql,Object... args){GeneratedKeyHolder k=new GeneratedKeyHolder();db.update(c->{PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);for(int i=0;i<args.length;i++)ps.setObject(i+1,args[i]);return ps;},k);if(k.getKey()==null)throw new IllegalStateException("ID não retornado.");return k.getKey().longValue();}
 private Produto mapProduto(ResultSet r,int n)throws SQLException{return new Produto(r.getLong("id"),r.getString("nome"),r.getDouble("preco_referencia"));}
 private Componente mapComp(ResultSet r,int n)throws SQLException{long p=r.getLong("pai_id");Long pai=r.wasNull()?null:p;return new Componente(r.getLong("id"),r.getLong("produto_id"),pai,r.getString("nome"),r.getInt("quantidade"),r.getDouble("custo_unitario"));}
 private Produto obter(long id){return db.query("SELECT * FROM produtos WHERE id=?",this::mapProduto,id).stream().findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado."));}
 private Componente componente(long id){return db.query("SELECT * FROM componentes WHERE id=?",this::mapComp,id).stream().findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Componente não encontrado."));}
 @GetMapping("/produtos") public List<Produto> listar(){return db.query("SELECT * FROM produtos ORDER BY id",this::mapProduto);}
 @PostMapping("/produtos") public Produto criar(@RequestBody ProdutoEntrada e){long id=inserir("INSERT INTO produtos(nome,preco_referencia) VALUES(?,?)",nome(e.nome()),preco(e.precoReferencia()));return obter(id);}
 @PutMapping("/produtos/{id}") public Produto editar(@PathVariable long id,@RequestBody ProdutoEntrada e){obter(id);db.update("UPDATE produtos SET nome=?,preco_referencia=? WHERE id=?",nome(e.nome()),preco(e.precoReferencia()),id);return obter(id);}
 @DeleteMapping("/produtos/{id}") public void excluir(@PathVariable long id){obter(id);db.update("DELETE FROM componentes WHERE produto_id=?",id);db.update("DELETE FROM produtos WHERE id=?",id);}
 @GetMapping("/produtos/{id}/componentes") public List<Componente> listarComponentes(@PathVariable long id){obter(id);return db.query("SELECT * FROM componentes WHERE produto_id=? ORDER BY id",this::mapComp,id);}
 private void validarPai(long produtoId,Long paiId,Long atual){if(paiId==null)return;if(Objects.equals(paiId,atual))throw new IllegalArgumentException("Componente não pode ser seu próprio pai.");Componente pai=componente(paiId);if(pai.produtoId()!=produtoId)throw new IllegalArgumentException("Pai pertence a outro produto.");if(atual!=null){Set<Long> visto=new HashSet<>();Long proximo=paiId;while(proximo!=null){if(!visto.add(proximo)||proximo.equals(atual))throw new IllegalArgumentException("Relação circular entre componentes.");proximo=componente(proximo).paiId();}}}
 @PostMapping("/produtos/{id}/componentes") public Componente inserirComponente(@PathVariable long id,@RequestBody ComponenteEntrada e){obter(id);validarPai(id,e.paiId(),null);long cid=inserir("INSERT INTO componentes(produto_id,pai_id,nome,quantidade,custo_unitario) VALUES(?,?,?,?,?)",id,e.paiId(),nome(e.nome()),qtd(e.quantidade()),preco(e.custoUnitario()));return componente(cid);}
 @PutMapping("/componentes/{id}") public Componente editarComponente(@PathVariable long id,@RequestBody ComponenteEntrada e){Componente c=componente(id);validarPai(c.produtoId(),e.paiId(),id);db.update("UPDATE componentes SET nome=?,pai_id=?,quantidade=?,custo_unitario=? WHERE id=?",nome(e.nome()),e.paiId(),qtd(e.quantidade()),preco(e.custoUnitario()),id);return componente(id);}
 @PatchMapping("/componentes/{id}/preco") public Componente atualizarPreco(@PathVariable long id,@RequestBody PrecoEntrada e){componente(id);db.update("UPDATE componentes SET custo_unitario=? WHERE id=?",preco(e.custoUnitario()),id);return componente(id);}
 @DeleteMapping("/componentes/{id}") public void excluirComponente(@PathVariable long id){Componente c=componente(id);List<Componente> todos=listarComponentes(c.produtoId());Set<Long> ids=new HashSet<>();ids.add(id);boolean mudou;do{mudou=false;for(Componente x:todos)if(x.paiId()!=null&&ids.contains(x.paiId()))mudou|=ids.add(x.id());}while(mudou);for(int i=todos.size()-1;i>=0;i--)if(ids.contains(todos.get(i).id()))db.update("DELETE FROM componentes WHERE id=?",todos.get(i).id());}
 private static double arred(double d){return BigDecimal.valueOf(d).setScale(2,RoundingMode.HALF_UP).doubleValue();}
 private No montar(Long id,Map<Long,List<Componente>> porPai,Map<Long,Componente> mapa,Set<Long> visitados){if(!visitados.add(id))throw new IllegalArgumentException("Ciclo detectado.");Componente c=mapa.get(id);List<No> filhos=new ArrayList<>();double soma=0;for(Componente x:porPai.getOrDefault(id,List.of())){No filho=montar(x.id(),porPai,mapa,visitados);filhos.add(filho);soma+=x.quantidade()*filho.custoCalculado();}return new No(c.id(),c.nome(),c.quantidade(),c.custoUnitario(),arred(filhos.isEmpty()?c.custoUnitario():soma),filhos);}
 private void recolher(No no,long fator,Map<Long,Insumo> agregados){long quantidade=Math.multiplyExact(fator,no.quantidade());if(no.filhos().isEmpty()){Insumo anterior=agregados.get(no.id());long total=Math.addExact(quantidade,anterior==null?0:anterior.quantidade());agregados.put(no.id(),new Insumo(no.id(),no.nome(),total,no.custoUnitario(),arred(total*no.custoUnitario())));}else for(No f:no.filhos())recolher(f,quantidade,agregados);}
 @GetMapping("/produtos/{id}/explosao") public Explosao explodir(@PathVariable long id,@RequestParam(defaultValue="1") Integer quantidade){Produto produto=obter(id);int q=qtd(quantidade);List<Componente> itens=listarComponentes(id);Map<Long,List<Componente>> porPai=new HashMap<>();Map<Long,Componente> mapa=new HashMap<>();for(Componente c:itens){mapa.put(c.id(),c);porPai.computeIfAbsent(c.paiId(),k->new ArrayList<>()).add(c);}Set<Long> vistos=new HashSet<>();List<No> filhos=new ArrayList<>();double soma=0;for(Componente c:porPai.getOrDefault(null,List.of())){No n=montar(c.id(),porPai,mapa,vistos);filhos.add(n);soma+=n.quantidade()*n.custoCalculado();}if(vistos.size()!=itens.size())throw new IllegalArgumentException("Árvore inválida: componentes órfãos.");No arvore=new No(null,produto.nome(),1,produto.precoReferencia(),arred(filhos.isEmpty()?produto.precoReferencia():soma),filhos);Map<Long,Insumo> agregado=new LinkedHashMap<>();recolher(arvore,q,agregado);return new Explosao(produto,q,arvore,new ArrayList<>(agregado.values()),arvore.custoCalculado(),arred(q*arvore.custoCalculado()));}
 @ExceptionHandler({IllegalArgumentException.class,ArithmeticException.class}) public ResponseEntity<Map<String,String>> erro(Exception e){return ResponseEntity.badRequest().body(Map.of("erro",e.getMessage()));}
}
