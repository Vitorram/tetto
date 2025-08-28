/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app;

import model.Conta;
import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vitor
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ContaService cs = new ContaService();

        try{
            cs.carregarContas("conta.txt");

             List<ContaCorrente> crescente = cs.ordenarPorSaldoCrescente();
            List<ContaCorrente> porNome = cs.ordenarPorNome();
            List<ContaCorrente> corinthians = cs.ordenarPorSaldoDecrescente();

            
            System.out.println("Saldo decrescente: ");
            corinthians.forEach(c -> System.out.println(c.getNumero() + " - " + c.getTitular() + " - " + c.getSaldo()));
            System.out.println("Lista por nome: ");
            porNome.forEach(c -> System.out.println(c.getNumero() + " - " + c.getTitular() + " - " + c.getSaldo()));
            System.out.println("Saldo crescente: ");
            crescente.forEach(c -> System.out.println(c.getNumero() + " - " + c.getTitular() + " - " + c.getSaldo()));











            var contasAltas = cs.filtrarContasAcimaDe10000();
            StringBuilder sb = new StringBuilder("Contas acima de 5000:\n");
            List<ContaCorrente> filt = cs.maiores5000();
            System.out.println("\nContas com saldo acima de 5000");
            filt.forEach(c->
                System.out.println(c.getNumero() + " - " + c.getTitular() + " - " + c.getSaldo())
            );
            List<ContaCorrente> filtPar = cs.ePar();
            System.out.println("Contas pares: ");
            filtPar.forEach(c->
                            System.out.println(c.getNumero() + " - " + c.getTitular() + " - " + c.getSaldo())
            );




            for (ContaCorrente c : contasAltas) {
            sb.append(c.getNumero()).append(" - ").append(c.getTitular())
             .append(" - R$ ").append(c.getSaldo()).append("\n");
}
            JOptionPane.showMessageDialog(null, sb.toString());

            Map<String, List<ContaCorrente>> contasPorFaixa = cs.agruparPorFaixaSaldo();

            for (String faixa : contasPorFaixa.keySet()) {
             sb.append(faixa).append(":\n");
                for (ContaCorrente c : contasPorFaixa.get(faixa)) {
                  sb.append("  ").append(c.getNumero())
                  .append(" - ").append(c.getTitular())
                  .append(" - R$ ").append(c.getSaldo()).append("\n");
                }
        }
                JOptionPane.showMessageDialog(null, sb.toString());







            double saldoTotal = cs.calcularSaldoTotal();
            JOptionPane.showMessageDialog(null, "Saldo total: R$ " + saldoTotal);
            

            String numeroStr = JOptionPane.showInputDialog("Digite o numero da conta: ");
            int numero = Integer.parseInt(numeroStr);


            ContaCorrente conta = cs.buscarConta(numero);

            if (conta == null){
                JOptionPane.showMessageDialog(null, "Conta nao encontrda");
                return;
            }

            JOptionPane.showMessageDialog(null, +conta.getNumero()+ " - " +conta.getTitular()+ " - " + conta.getSaldo());
            String valoString = JOptionPane.showInputDialog("Informe o valor para saque: ");
            double valor = Double.parseDouble(valoString);

            try{
                cs.sacarValor(numero, valor);
                JOptionPane.showMessageDialog(null, "Saque realizado ✔");
            }catch(SaldoInsuficienteException e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "erro", numero);
            }

            cs.salvarContas("conta.txt");
            cs.atualizarConta(conta, "conta_atualizada.txt");
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "erro ao acessar arquivo");
        }
        
    }
    
}
