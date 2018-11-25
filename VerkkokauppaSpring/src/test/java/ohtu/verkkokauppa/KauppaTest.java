package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KauppaTest {
	
	Pankki pankki;
	Viitegeneraattori viite;
	Varasto varasto;

	public KauppaTest() {
	}
	
	@Before
	public void setUp() {
		pankki = mock(Pankki.class);
		viite = mock(Viitegeneraattori.class);
		varasto = mock(Varasto.class);
	}

	@Test
	public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaArvoilla() {
		
		// määritellään että viitegeneraattori palauttaa viitten 42
		when(viite.uusi()).thenReturn(42);

		// määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

		// sitten testattava kauppa 
		Kauppa k = new Kauppa(varasto, pankki, viite);              

		// tehdään ostokset
		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);   
		// toistaiseksi ei välitetty kutsussa käytetyistä parametreista
	}

	@Test
	public void kaksiEriTuotetta() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
		when(varasto.saldo(2)).thenReturn(10); 
		when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "maito2", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(2);     // ostetaan tuotetta numero 1 eli maitoa
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 10);   

	}

	@Test
	public void kaksiTuotettaTilisiirto() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 10);   
	}

	@Test
	public void kaksiEriTuotettaToinenLoppu() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
		when(varasto.saldo(2)).thenReturn(0); 
		when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "maito2", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(2);     // ostetaan tuotetta numero 1 eli maitoa
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);   

	}

	@Test
	public void poistaKorista() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
		when(varasto.saldo(2)).thenReturn(10); 
		when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "maito2", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(2);     // ostetaan tuotetta numero 1 eli maitoa
		k.poistaKorista(2);
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);   

	}

	@Test
	public void aloitaNollaa() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
		when(varasto.saldo(2)).thenReturn(10); 
		when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "maito2", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(2);     // ostetaan tuotetta numero 1 eli maitoa
		k.poistaKorista(2);

		k.aloitaAsiointi();
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 0);   
	}

	@Test
	public void uusiViite() {
		when(viite.uusi()).thenReturn(42);
		when(varasto.saldo(1)).thenReturn(10); 
		when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
		when(varasto.saldo(2)).thenReturn(10); 
		when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "maito2", 5));

		Kauppa k = new Kauppa(varasto, pankki, viite);              

		k.aloitaAsiointi();
		k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
		k.lisaaKoriin(2);     // ostetaan tuotetta numero 1 eli maitoa
		k.poistaKorista(2);

		k.aloitaAsiointi();
		k.tilimaksu("pekka", "12345");

		// sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
		k.tilimaksu("pekka", "12345");
		verify(viite, times(2)).uusi();
	}
	/*
	varmistettava, että metodin aloitaAsiointi kutsuminen nollaa edellisen ostoksen tiedot (eli edellisen ostoksen hinta ei näy uuden ostoksen hinnassa), katso tarvittaessa apua projektin MockitoDemo testeistä!
varmistettava, että kauppa pyytää uuden viitenumeron jokaiselle maksutapahtumalle, katso tarvittaessa apua projektin MockitoDemo testeistä!
	*/
}
