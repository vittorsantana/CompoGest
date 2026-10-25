
package br.com.compotree.estrutura;

import br.com.compotree.model.ItemFormula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArvoreProduto {

    public static TreeNode<ItemFormula> montarArvore(
            List<ItemFormula> itens,
            List<String> pais) {

        Map<Integer, TreeNode<ItemFormula>> nos = new HashMap<>();
        TreeNode<ItemFormula> raiz = null;

        // Cria um nó para cada item
        for (int i = 0; i < itens.size(); i++) {
            nos.put(i, new TreeNode<>(itens.get(i)));
        }

        // Relaciona cada item ao seu pai
        for (int i = 0; i < itens.size(); i++) {

            String nomePai = pais.get(i);

            if (nomePai == null) {
                if (raiz != null) {
                    throw new IllegalArgumentException(
                        "Existe mais de um produto principal."
                    );
                }

                raiz = nos.get(i);
                continue;
            }

            boolean encontrouPai = false;

            for (int j = 0; j < itens.size(); j++) {
                if (itens.get(j).getNome().equals(nomePai)) {
                    nos.get(j).adicionarFilho(nos.get(i));
                    encontrouPai = true;
                    break;
                }
            }

            if (!encontrouPai) {
                throw new IllegalArgumentException(
                    "Componente pai não encontrado: " + nomePai
                );
            }
        }

        if (raiz == null) {
            throw new IllegalArgumentException(
                "Nenhum produto principal foi informado."
            );
        }

        return raiz;
    }

    public static void imprimirArvore(
            TreeNode<ItemFormula> no, int nivel) {

        if (no == null) {
            return;
        }

        for (int i = 0; i < nivel; i++) {
            System.out.print("    ");
        }

        ItemFormula item = no.getDado();

        System.out.println(
            item.getNome() + " - R$ " +
            String.format("%.2f", item.getCustoUnitario())
        );

        for (TreeNode<ItemFormula> filho : no.getFilhos()) {
            imprimirArvore(filho, nivel + 1);
        }
    }
}
