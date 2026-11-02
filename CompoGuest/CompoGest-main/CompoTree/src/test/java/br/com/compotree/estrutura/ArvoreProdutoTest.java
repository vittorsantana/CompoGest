package br.com.compotree.estrutura;
import br.com.compotree.model.ItemFormula;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class ArvoreProdutoTest {
 @Test void testeCaneta(){var nomes=List.of("Caneta","Tinta","Pigmento","Solvente","Corpo","Plástico","Clip","Tampa","Plástico","Vedação");
 var pais=Arrays.asList(null,"Caneta","Tinta","Tinta","Caneta","Corpo","Corpo","Caneta","Tampa","Tampa");
 double[] custos={2.50,.80,.30,.50,1,.70,.30,.70,.50,.20};
 var itens=new ArrayList<ItemFormula>();for(int i=0;i<nomes.size();i++)itens.add(new ItemFormula(nomes.get(i),1,custos[i]));
 // O método baseado em nomes exige pais unívocos; os dois "Plástico" são folhas, portanto válidos.
 var raiz=ArvoreProduto.montarArvore(itens,pais);assertEquals(2.50,ArvoreProduto.calcularTotal(raiz),.00001);
 itens.get(2).setCustoUnitario(.40);assertEquals(2.60,ArvoreProduto.calcularTotal(raiz),.00001);
 }
 @Test void testeComputador(){String[] n={"Computador","Gabinete","Placa-Mãe","CPU","Memória RAM","Chipset","Fonte de Alimentação","Armazenamento","HDD","SSD","Monitor","Tela LCD","Carcaça","Periféricos","Teclado","Mouse"};
 String[] p={null,"Computador","Gabinete","Placa-Mãe","Placa-Mãe","Placa-Mãe","Gabinete","Gabinete","Armazenamento","Armazenamento","Computador","Monitor","Monitor","Computador","Periféricos","Periféricos"};
 double[] c={2500,800,500,1000,200,150,300,0,250,400,900,600,100,0,100,80};
 var items=new ArrayList<ItemFormula>();for(int i=0;i<n.length;i++)items.add(new ItemFormula(n[i],i==4?2:1,c[i]));
 assertEquals(3380.0,ArvoreProduto.calcularTotal(ArvoreProduto.montarArvore(items,Arrays.asList(p))),.00001);
 }
 @Test void validaArvore(){assertThrows(IllegalArgumentException.class,()->ArvoreProduto.montarArvore(List.of(new ItemFormula("A",1,1),new ItemFormula("B",1,2)),Arrays.asList(null,"Inexistente")));}
}
