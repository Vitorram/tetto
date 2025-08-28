/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.SaldoInsuficienteException;
import model.Conta;
import model.ContaCorrente;

import java.io.IOException;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * @author Vitor
 */
public class ContaService {


    private List<ContaCorrente> contas = new ArrayList<>();
    //por saldo
     public List<ContaCorrente> ordenarPorSaldoDecrescente() {
        return contas.stream()
                     .sorted((c1, c2) -> Double.compare(c2.getSaldo(), c1.getSaldo()))
                     .toList();
    }
    public List<ContaCorrente> ordenarPorNome() {
        return contas.stream()
                     .sorted((c1, c2) -> c1.getTitular().compareToIgnoreCase(c2.getTitular()))
                     .toList();
    }
     public List<ContaCorrente> ordenarPorSaldoCrescente() {
        return contas.stream()
                     .sorted(Comparator.comparingDouble(ContaCorrente::getSaldo))
                     .toList();
    }


    public List<ContaCorrente> maiores5000() {
    Predicate<ContaCorrente> maior5000Predicate = f -> f.getSaldo() > 5000;
    
    return contas.stream()
                 .filter(maior5000Predicate)
                 .collect(Collectors.toList());
    }

    public List<ContaCorrente> ePar() {
    return contas.stream()
                 .filter(c -> c.getNumero() % 2 == 0)
                 .toList();
    }


    public void carregarContas(String caminho) throws IOException {
        
        List<String> linhas = Files.readAllLines(Paths.get(caminho));
      for (String linha : linhas) {
    if (linha.trim().isEmpty()) continue; 

    String[] dados = linha.split(",");
    if (dados.length < 3) continue; 

    int numero = Integer.parseInt(dados[0].trim());
    String titular = dados[1].trim();
    double saldo = Double.parseDouble(dados[2].trim());
    contas.add(new ContaCorrente(numero, titular, saldo));
}

    }

    public List<ContaCorrente> filtrarContasAcimaDe10000() {
    return contas.stream()
                 .filter(c -> c.getSaldo() > 10000.0)
                 .toList();
    }
    public Map<String, List<ContaCorrente>> agruparPorFaixaSaldo() {
    return contas.stream()
            .collect(Collectors.groupingBy(c -> {
                double saldo = c.getSaldo();
                if (saldo <= 5000) return "Até R$ 5000";
                else if (saldo <= 10000) return "De R$ 5001 a R$ 10000";
                else return "Acima de R$ 10000";
            }));
    }

    public double calcularSaldoTotal() {
    return contas.stream()
                 .map(ContaCorrente::getSaldo)
                 .reduce(0.0, Double::sum);
    }

    public void salvarContas(String caminho) throws IOException{
        List<String> linhas = new ArrayList<>();
        for (ContaCorrente conta : contas){
            linhas.add(conta.getNumero()+ "," +conta.getTitular()+","+conta.getSaldo());
        }
        Files.write(Paths.get(caminho), linhas);
    }
    public ContaCorrente buscarConta(int numero){
        for (ContaCorrente conta : contas){
            if (conta.getNumero() == numero){
                return conta;
            }
        }
        return null;
    }

    public void sacarValor(int numero, double valor) throws SaldoInsuficienteException{
        ContaCorrente conta = buscarConta(numero);
        if (conta != null){
            conta.sacar(valor);
        }

    }

    public List <ContaCorrente> getCopContaCorrentes(){
        return contas;
    }
   

    public void atualizarConta(ContaCorrente conta, String caminho) throws IOException {
        String dados = conta.getNumero() + "," + conta.getTitular() + "," + conta.getSaldo() + "\n";
         Files.write(Paths.get(caminho), (dados + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    }
    
}
