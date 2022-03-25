package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private LocacaoDAO dao;
	
	@InjectMocks @Spy
	private LocacaoService service;
	
	@Mock
	private EmailService emailService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		System.out.println("Iniciando 2");
	}

	@After
	public void tearDown(){
		System.out.println("Finalizando 2");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void deveAlugarFilme() throws Exception {

		// Cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =Arrays.asList(umFilme().comValor(5.0).agora());

		Mockito.doReturn(DataUtils.obterData(24, 3, 2022))
				.when(service).obterData();

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		//error.checkThat(locacao.getDataLocacao(), ehHoje());
		//error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(23, 3, 2022)), is(false));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(24, 3, 2022)), is(false));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExecaoAoAlugarFilmeSemEstoque() throws Exception {
		// Cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =Arrays.asList(umFilmeSemEstoque().agora());

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws Exception {
		// Cenario
		List<Filme> filmes =Arrays.asList(umFilme().agora());

		// Ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws Exception {
		// Cenario
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// Ação
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Mockito.doReturn(DataUtils.obterData(26, 3, 2022))
				.when(service).obterData();

		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
	
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado"));
		}
		
		verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadas() {
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().atrasada().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		service.notificarAtrasos();
		
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(emailService).notificarAtraso(usuario);
		verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		verify(emailService, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spcService.possuiNegativacao(usuario))
		.thenThrow(new Exception("Falha Cadastrofica"));
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problema com SPC, tente novamente");
		
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		service.prorrogarLocacao(locacao, 3);
		
		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argumentCaptor.capture());
		Locacao locacaoRetornada = argumentCaptor.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
				
	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {

		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(service, filmes);

		assertThat(valor, is(4.0));
	}
}
