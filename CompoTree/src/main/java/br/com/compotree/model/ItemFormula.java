package br.com.compotree.model;

public class ItemFormula {
    private String nome;
    private int quantidade;
    private double custoUnitario;

    public ItemFormula(String nome, int quantidade, double custoUnitario) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do item é obrigatório.");
        }
        if (quantidade < 1) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (custoUnitario < 0) {
            throw new IllegalArgumentException("O custo não pode ser negativo.");
        }

        this.nome = nome;
        this.quantidade = quantidade;
        this.custoUnitario = custoUnitario;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(double custoUnitario) {
        if (custoUnitario < 0) {
            throw new IllegalArgumentException("O custo não pode ser negativo.");
        }
        this.custoUnitario = custoUnitario;
    }
}
