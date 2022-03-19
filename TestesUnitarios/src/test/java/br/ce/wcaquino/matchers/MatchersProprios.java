package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaESemanMatcher caiEm(Integer diaSemana) {
		return new DiaESemanMatcher(diaSemana);
	}
	
	public static DiaESemanMatcher caiNumaSegunda() {
		return new DiaESemanMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaDiasMatcher ehHojeComDiferencaDias(Integer qtdDias) {
		return new DataDiferencaDiasMatcher(qtdDias);
	}
	
	public static DataDiferencaDiasMatcher ehHoje() {
		return new DataDiferencaDiasMatcher(0);
	}
}
