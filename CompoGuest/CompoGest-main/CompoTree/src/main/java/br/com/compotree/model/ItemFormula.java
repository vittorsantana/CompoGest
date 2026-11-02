package br.com.compotree.model;
public class ItemFormula {
 private String nome; private int quantidade; private double custoUnitario;
 public ItemFormula(String nome,int quantidade,double custoUnitario){if(nome==null||nome.isBlank()||quantidade<1||!Double.isFinite(custoUnitario)||custoUnitario<0)throw new IllegalArgumentException("Item inválido.");this.nome=nome;this.quantidade=quantidade;this.custoUnitario=custoUnitario;}
 public String getNome(){return nome;} public int getQuantidade(){return quantidade;} public double getCustoUnitario(){return custoUnitario;}
 public void setCustoUnitario(double custo){if(!Double.isFinite(custo)||custo<0)throw new IllegalArgumentException("Custo inválido.");custoUnitario=custo;}
}
