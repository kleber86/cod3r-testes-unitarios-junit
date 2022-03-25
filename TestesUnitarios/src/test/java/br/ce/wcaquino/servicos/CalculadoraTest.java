package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import br.ce.wcaquino.runners.ParallelRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZero;
import org.junit.runner.RunWith;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("Iniciando...");
	}

	@After
	public void tearDown(){
		System.out.println("Finalizando...");
	}

	@Test
	public void deveSomarDoisValores() {
		// Cenario
		int a = 5;
		int b = 3;
		
		// A��o
		int resultado = calc.somar(a, b);
		
		// Verifica��o
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
