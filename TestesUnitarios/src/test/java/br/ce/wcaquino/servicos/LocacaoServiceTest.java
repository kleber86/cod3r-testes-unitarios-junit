package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTest {
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@SuppressWarnings("deprecation")
	@Test
	public void testeLocacao() {
		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		// A��o
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		// Verifica��o
		error.checkThat(locacao.getValor(), is(equalTo(6.0)));
		error.checkThat(isMesmaData(
				locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(
				locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));
	}
}
