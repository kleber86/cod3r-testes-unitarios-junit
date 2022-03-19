package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZero;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		// Cenario
		int a = 5;
		int b = 3;
		
		// Ação
		int resultado = calc.somar(a, b);
		
		// Verificação
		assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		int a = 5;
		int b = 3;
		
		int resultado = calc.subracao(a, b);
		
		assertEquals(2, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZero {
		int a = 5;
		int b = 3;
		
		int resultado = calc.dividir(a, b);
		
		assertEquals(1, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZero.class)
	public void deveLancarExecaoDivirPorZero() throws NaoPodeDividirPorZero {
		int a  = 5;
		int b = 0;
		
		calc.dividir(a, b);
	}
	
	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";
		
		int resultado = calc.divide(a, b);
		
		assertEquals(2, resultado);
	}
}
