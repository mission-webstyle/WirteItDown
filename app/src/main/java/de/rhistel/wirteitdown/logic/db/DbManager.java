package de.rhistel.wirteitdown.logic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

import de.rhistel.wirteitdown.model.MyNote;


/**
 * Handelt den Aufbau der datenbank
 * Singleton wegen der Threadsicherheit da die Datenbank nur eine
 * Datei ist.
 * data/data/packagestruturDerApp/database
 */
public class DbManager extends SQLiteOpenHelper {
	
	//region 0. Konstanten
	private static final String DB_NAME    = "myNotes.db";
	private static final int    DB_VERSION = 1;
	//endregion
	
	//region 1. Decl. and Init
	
	/**
	 * Einzige Instanz zur Laufzeit
	 */
	private static DbManager instance;
	
	
	/**
	 * Data Access Object Objekt
	 * welches alle [C]REATE, [R]EAD, [U]PDATE, [D]ELETE-Operationen ausfuehrt.
	 */
	private DaoMyNote daoMyNote;
	
	//endregion
	
	//region 2. Konstruktor
	
	/**
	 * Privater Konstuktor verhindert
	 * das ein Objekt außerhalb der
	 * eigenen Klasse instanziiert werden kann.
	 * <p/>
	 * Es kann unter Android mit SQLite keine
	 * leere Datenbank(leere Tabellen) generiert
	 * werden.
	 * <p/>
	 * <p>
	 * Der Super-Konstruktor der Klasse
	 * {@link SQLiteOpenHelper#SQLiteOpenHelper(Context, String, SQLiteDatabase.CursorFactory, int)}
	 * checkt ob eine Datenabank bereits exisitiert.
	 * Sollte dies der Fall sein wird keine neue Datenbank generiert
	 * <p>
	 * Diese erst dann angelegt wenn
	 * die Funktion getWriteableDatabase() oder
	 * getReadableDatabase() aufgerufen wird.
	 * Sprich wenn ein Datensatz eingefuegt
	 * oder ausgelesen wird.
	 */
	private DbManager(Context context) {
		//Datenbank generieren lassen
		super(context, DB_NAME, null, DB_VERSION);
		
		//DaoKlasse generieren lassen
		this.daoMyNote = new DaoMyNote();
		
		//Schreibzugriff auf Db erhalten->Db real generiert
		this.getWritableDatabase();
		
		
	}
	//endregion
	
	//region 3. Aufbau und Upgrade der Datenbank
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(daoMyNote.getCreateTableStatement());
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		}
		
	}
	
	/**
	 * Wird aufgerufen wenn die Datenbank ubgedatetd werde muss.<br>
	 * Fuer die implementierung des updates, sollten alle Tabellen<br>
	 * mit DROP TABLES entfernt werden und mit CREATE Tabeles<br>
	 * wieder hinzugefuegt werden<br>
	 * <br><br>
	 * Die Tabellen koennen auch mit dem SQL-Befehl ALTER-Tabele geaendert werden.<br>
	 * Soll nur eine Spalte hinzugefuegt werden so kann dies im Live-Betrieb gemacht werden.<br>
	 * Sobal aber eine Spaltennamen veraendert oder eine Spalte geloescht werden soll, muss<br>
	 * eine neue Tabelle angelegt werden und mit den Daten der alten Tabelle befuellt werden.<br>
	 * <br><br>
	 * Danach wird die alte Tabelle geloescht.<br>
	 * <br><br>
	 * Die Dokumentation zur Tabellenaenderungen findet sich<br>
	 * <a href="http://sqlite.org/lang_altertable.html">hier</a><br>
	 * <br>
	 * Diese Methode arbeitet mit Transactions, heist tritt eine Exception auf, wird
	 * alles automatisch wieder auf den Ursprungszustand zurueck gesetzt.
	 *
	 * @param db         : {@link SQLiteDatabase} : Datenbankobjekt
	 * @param oldVersion : int : alte Datenbankversion
	 * @param newVersion : int : neue Datenbankversion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL(daoMyNote.getDropTableStatement());
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		}
		onCreate(db);
	}
	//endregion
	
	//region 4. Get Instance
	
	/**
	 * Gibt die einzige Instanz zur Laufzeit zurueck.
	 * Threadsicher Zugriff ist durch synchronized
	 * gewaehrt
	 *
	 * @return instance : {@link DbManager} : Instanz
	 */
	public static synchronized DbManager getInstance(Context context) {
		
		if (instance == null) {
			instance = new DbManager(context);
		}
		
		return instance;
	}
	//endregion
	
	//region 5. Read
	
	/**
	 * Liest ueber die Klasse {@link DaoMyNote} alle Notizen aus der Datenbanktabelle.
	 *
	 * @return allMyNotes : {@link List} - {@link MyNote} : Alle Datensatze
	 */
	public List<MyNote> getAllMyNotes() {
		return this.daoMyNote.readAllMyNotesFromDbTable(this.getWritableDatabase());
	}
	
	
	/**
	 * Liest ein bestimmtes {@link MyNote} aus der Datenbank. Welches Notiz gelesen wird entscheidet
	 * die mitgelieferte Id.
	 *
	 * @param myNoteId : long : {@link MyNote#getId()}
	 *
	 * @return product : {@link MyNote} : Gesuchtes Notiz oder Null
	 */
	@Nullable
	public MyNote getMyNoteFromDbById(long myNoteId) {
		return this.daoMyNote.readMyNoteFromDbTableById(this.getWritableDatabase(), myNoteId);
	}
	//endregion
	
	
	//region 6. Insert
	
	/**
	 * Fuegt das mitgelieferte Notiz uber die {@link DaoMyNote} Klasse
	 * in die Datenbanktabelle ein
	 *
	 * @param myNoteToInsert : {@link MyNote} : Notiz das eingefuegt werden soll
	 *
	 * @return lngRowId: long: Id des eingefuegten Notizen
	 */
	public long insertMyNote(MyNote myNoteToInsert) {
		return this.daoMyNote.insertMyNoteIntoDbTable(this.getWritableDatabase(), myNoteToInsert);
	}
	//endregion
	
	//region 7. Update
	
	/**
	 * Aendert das mitgelieferte Notiz ueber die {@link DaoMyNote} Klasse
	 * in der Datenbanktabelle
	 *
	 * @param myNoteToUpdate : {@link MyNote} : Notiz das geaendert werden soll
	 *
	 * @return iNumberOfAffectedRows: int: Anzahl der geaenderten Zeilen
	 */
	public int updateMyNote(MyNote myNoteToUpdate) {
		return this.daoMyNote.updateMyNoteInDbTable(this.getWritableDatabase(), myNoteToUpdate);
	}
	//endregion
	
	//region 8. Loeschen
	
	/**
	 * Loescht das mitgelieferte Notiz ueber die {@link DaoMyNote} Klasse
	 * aus der Datenbanktabelle
	 *
	 * @param myNoteId : long : NotizId die geloescht werden soll
	 *
	 * @return iNumberOfAffectedRows: int: Anzahl der geloeschten Zeilen
	 */
	public int deleteMyNote(Long myNoteId) {
		return this.daoMyNote.deleteMyNoteInDbTable(this.getWritableDatabase(), myNoteId);
	}
	//endregion
	
}