package br.com.compotree.estrutura;
import br.com.compotree.model.ItemFormula;
import java.util.*;
public class ArvoreProduto {
    public static TreeNode<ItemFormula> montarArvore(List<ItemFormula> itens, List<String> pais) {
        if (itens == null || pais == null || itens.size() != pais.size() || itens.isEmpty()) throw new IllegalArgumentException("Fórmula inválida.");
        Map<Integer, TreeNode<ItemFormula>> nos = new HashMap<>();
        for (int i=0;i<itens.size();i++) nos.put(i,new TreeNode<>(itens.get(i)));
        TreeNode<ItemFormula> raiz=null;
        for (int i=0;i<itens.size();i++) {
            String pai=pais.get(i);
            if(pai==null){if(raiz!=null)throw new IllegalArgumentException("Mais de uma raiz.");raiz=nos.get(i);continue;}
            int encontrado=-1;
            for(int k=0;k<itens.size();k++)if(itens.get(k).getNome().equals(pai)){if(encontrado!=-1)throw new IllegalArgumentException("Pai ambíguo: "+pai);encontrado=k;}
            if(encontrado<0 || encontrado==i)throw new IllegalArgumentException("Pai inválido: "+pai);
            nos.get(encontrado).adicionarFilho(nos.get(i));
        }
        if(raiz==null)throw new IllegalArgumentException("Raiz ausente.");
        Set<TreeNode<ItemFormula>> vistos=Collections.newSetFromMap(new IdentityHashMap<>());
        verificar(raiz,vistos);
        if(vistos.size()!=itens.size())throw new IllegalArgumentException("Existem itens desconectados ou ciclos.");
        return raiz;
    }
    private static void verificar(TreeNode<ItemFormula> n,Set<TreeNode<ItemFormula>> vistos){if(!vistos.add(n))throw new IllegalArgumentException("Ciclo na fórmula.");for(var f:n.getFilhos())verificar(f,vistos);}
    public static double calcularTotal(TreeNode<ItemFormula> no){if(no.getFilhos().isEmpty())return no.getDado().getCustoUnitario();double soma=0;for(var filho:no.getFilhos())soma+=filho.getDado().getQuantidade()*calcularTotal(filho);return soma;}
    public static void imprimirArvore(TreeNode<ItemFormula> no,int nivel){System.out.println("  ".repeat(nivel)+no.getDado().getNome()+" ×"+no.getDado().getQuantidade());for(var f:no.getFilhos())imprimirArvore(f,nivel+1);}
}
