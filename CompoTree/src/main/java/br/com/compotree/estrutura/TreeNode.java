
package br.com.compotree.estrutura;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

    private T dado;
    private List<TreeNode<T>> filhos;

    public TreeNode(T dado) {
        this.dado = dado;
        this.filhos = new ArrayList<>();
    }

    public T getDado() {
        return dado;
    }

    public List<TreeNode<T>> getFilhos() {
        return filhos;
    }

    public void adicionarFilho(TreeNode<T> filho) {
        filhos.add(filho);
    }

    public void removerFilho(TreeNode<T> filho) {
        filhos.remove(filho);
    }
}
