package de.rhistel.wirteitdown.testData;

import java.util.ArrayList;
import java.util.List;

import de.rhistel.wirteitdown.model.MyNote;

/**
 * Stellt Testdaten zur Verfuegung
 */
public class TestData {
	
	//region 0. Konstanten
	private static final int MAX_AMOUNT_OF_NOTES = 25;
	//endregion
	
	//region 1. Decl and Init Attribute
	//endregion
	
	//region 2. Konstruktoren
	
	/**
	 * Standardkonstruktor
	 */
	private TestData() {
		//Nichts zu tun ausser privat zu sein
	}
	
	//endregion
	
	//region 3. GetTestNotes
	
	/**
	 * Gibt eine {@link List} - {@link MyNote} zum testen zurueck.
	 * Die Liste ist {@link TestData#MAX_AMOUNT_OF_NOTES} gross
	 *
	 * @return myTestNotes : {@link List} - {@link MyNote}s : Testnotizen
	 */
	public static synchronized List<MyNote> getMyTestNotes() {
		List<MyNote> myTestNotes = new ArrayList<>();
		
		for (int index = 0; index < MAX_AMOUNT_OF_NOTES; index++) {
			myTestNotes.add(new MyNote("Name " + index, "08.02.2022", "Content " + index));
		}
		
		return myTestNotes;
	}
	//endregion
	
	//region 4. Methoden und Funktionen
	//endregion
}
