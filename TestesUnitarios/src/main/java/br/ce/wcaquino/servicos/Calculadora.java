package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZero;

public class Calculadora {

	public int somar(int a, int b) {
		System.out.println("Estou executando o metodo: somar");
		return a + b;
	}

	public int subracao(int a, int b) {
		return a - b;
	}

	public int dividir(int a, int b) throws NaoPodeDividirPorZero {
		if(b == 0) {
			throw new NaoPodeDividirPorZero();
		}
		return a / b;
	}

	public int divide(String a, String b) {
		return Integer.valueOf(a) / Integer.valueOf(b);
	}
	
	public void imprimir() {
		System.out.println("Passei aqui");
	}
}
