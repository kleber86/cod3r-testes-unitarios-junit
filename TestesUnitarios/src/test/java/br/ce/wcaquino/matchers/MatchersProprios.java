package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaESemanMatcher caiEm(Integer diaSemana) {
		return new DiaESemanMatcher(diaSemana);
	}
	
	public static DiaESemanMatcher caiNumaSegunda() {
		return new DiaESemanMatcher(Calendar.MONDAY);
	}
}
