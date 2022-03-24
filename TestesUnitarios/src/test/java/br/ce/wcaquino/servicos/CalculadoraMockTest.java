package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.entidades.Locacao;

public class CalculadoraMockTest {

	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Mock
	private EmailService emailService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void deveMostrarDiferencaEntreMockSpy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(5);
		Mockito.doReturn(5).when(calcSpy).somar(1, 2);
		Mockito.doNothing().when(calcSpy).imprimir();
		
		System.out.println(calcMock.somar(1, 2));
		System.out.println(calcSpy.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprimir();
		
		System.out.println("Spy");
		calcSpy.imprimir();
	}
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);
	
		assertEquals(5, calc.somar(1, 8));
		System.out.println(argumentCaptor.getAllValues());
	}
}
