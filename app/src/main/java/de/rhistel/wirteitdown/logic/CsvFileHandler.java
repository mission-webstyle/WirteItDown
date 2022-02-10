package de.rhistel.wirteitdown.logic;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.rhistel.wirteitdown.model.MyNote;

/**
 * Ist ein Singeleton und stellt
 * Zugriff auf die unter data/data/packagestrukturDerApp/files
 * abgelegten Dateien thread sicher zur Verfuegung.
 * Jede App kann nur auf Ihre eigenene Dateien und Prozesse
 * zugreifen, da jede App bei Android als OS-User hinterlegt
 * ist und das Linuxberechtigungsmodell gilt.
 */
public class CsvFileHandler {
	
	//region 0. Konstanten
	private static final String FILE_NAME_MY_NOTES_CSV = "myNotes.csv";
	//endregion
	
	//region 1. Decl and Init Attribute
	private static CsvFileHandler instance;
	//endregion
	
	//region 2. Konstruktoren
	
	private CsvFileHandler() {
		this.instance = instance;
	}
	
	//endregion
	
	//region 3. Getter und Setter
	public static synchronized CsvFileHandler getInstance() {
		if (instance == null) {
			instance = new CsvFileHandler();
		}
		
		return instance;
	}
	//endregion
	
	//region 4. Speichern
	
	/**
	 * Speichert die uebergebene {@link List} von {@link MyNote}s im
	 * Installationsorder der App das ist hier: <b>data/data/de.rhistel.writeitdown/files/</b>{@link CsvFileHandler#FILE_NAME_MY_NOTES_CSV}
	 *
	 * @param context       : {@link Context} : Zugriff auf OS ist eigentlich die Activity / Service die diese Methode nutzt
	 * @param myNotesToSave : {@link List} - {@link MyNote} : Notizen die in einer CSV-Datei gespeichert werden sollen
	 */
	public void saveMyNotesToCsvFile(Context context, List<MyNote> myNotesToSave) {
		
		FileOutputStream   fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter     out = null;
		
		try {
			
			fos = context.openFileOutput(FILE_NAME_MY_NOTES_CSV, Context.MODE_PRIVATE);
			osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			out = new BufferedWriter(osw);
			
			for (MyNote mn : myNotesToSave) {
				out.write(mn.getAllAttributesAsCsvLine());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	//endregion
	
	//region 4. Lesen
	
	/**
	 * Laet eine {@link List} von {@link MyNote}s aus dem
	 * Installationsorder der App das ist hier: <b>data/data/de.rhistel.writeitdown/files/</b>{@link CsvFileHandler#FILE_NAME_MY_NOTES_CSV}
	 *
	 * @param context : {@link Context} : Zugriff auf OS ist eigentlich die Activity / Service die diese Methode nutzt
	 *
	 * @return myNotesFromCsvFile : {@link List} - {@link MyNote} : Notizen aus der CSV-Datei
	 */
	public List<MyNote> readMyNotesFromCsvFile(Context context) {
		
		List<MyNote> myNotesFromCsvFile = new ArrayList<>();
		
		FileInputStream   fis = null;
		InputStreamReader isr = null;
		BufferedReader    in  = null;
		
		try {
			
			fis = context.openFileInput(FILE_NAME_MY_NOTES_CSV);
			isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			in  = new BufferedReader(isr);
			
			
			boolean eof = false;
			
			while (!eof) {
				String strReadLine = in.readLine();
				
				if (strReadLine == null) {
					eof = true;
				} else {
					myNotesFromCsvFile.add(new MyNote(strReadLine));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return myNotesFromCsvFile;
	}
	//endregion
}
