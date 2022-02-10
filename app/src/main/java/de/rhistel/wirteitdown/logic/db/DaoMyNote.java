package de.rhistel.wirteitdown.logic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.rhistel.wirteitdown.model.MyNote;

/**
 * Implementierrt die CRUD Operationen fuer die Tabelle notes
 */
public class DaoMyNote extends ASQLiteKeyWords {
	
	//region 0. Konstanten
	
	private static final String TABLE_NAME       = "notes";
	private static final String COL_NAME_ID      = "_id";
	private static final String COL_NAME_NAME    = "name";
	private static final String COL_NAME_DATE    = "date";
	private static final String COL_NAME_CONTENT = "content";
	//endregion
	
	//region 1. Decl. and Init Attribute
	//endregion
	
	//region 2. Konstruktor
	
	/**
	 * Standardkonstruktor
	 */
	public DaoMyNote() {
		//Nichts zun tun
	}
	
	//endregion
	
	//region 3. Create and drop Table
	
	public String getCreateTableStatement() {
		return CREATE_TBL + TABLE_NAME
				+ CHAR_OPEN_BRACKET
				+ COL_NAME_ID + PRIMARY_KEY_AUTO_INCREMENT_INC_COMMA
				+ COL_NAME_NAME + DATA_TYPE_TEXT_INC_COMMA
				+ COL_NAME_DATE + DATA_TYPE_TEXT_INC_COMMA
				+ COL_NAME_CONTENT + DATA_TYPE_TEXT
				+ CHAR_CLOSE_BRACKET_SEMICOLON;
	}
	
	/**
	 * Baut das fertige DROP Table Statement zusammen und gibt dieses zurueck.
	 *
	 * @return strDropTableStatement: {@link String} : Create Statement der Tabelle
	 */
	public String getDropTableStatement() {
		
		//DROP Tabele - Statement zusammebauen
		return DROP_TBL + TABLE_NAME;
		
	}
	//endregion
	
	//region 4. Read
	
	/**
	 * Liest alle Datensaeatze aus der Tabelle
	 *
	 * @param db : {@link SQLiteDatabase} : Datenbankverbindungsobjekt
	 *
	 * @return myNotesFromDb : {@link List} - {@link MyNote} -: Alle Notizen
	 */
	public List<MyNote> readAllMyNotesFromDbTable(SQLiteDatabase db) {
		//Decl. and Init
		List<MyNote> myNotesFromDb = new ArrayList<>();
		
		try {
			
			//1. Datenbankobjekt ist bereits geoffnet
			
			//2. Abfrage and die Db stellen SELECT * FROM und speichern
			Cursor cQueryResult = db.query(TABLE_NAME,
					null,
					null,
					null,
					null,
					null,
					null);
			
			//3. Ergebnismenge auswerten
			while (cQueryResult.moveToNext()) {
				
				//4. Model von Cursor anfertigen
				MyNote myNoteFromDb = this.createMyNoteFromCursor(cQueryResult);
				
				//5. In Liste speichern
				myNotesFromDb.add(myNoteFromDb);
			}
			
		} catch (SQLiteException sqlEx) {
			sqlEx.getMessage();
		} finally {
			//3. Schliessen
			db.close();
		}
		return myNotesFromDb;
	}
	
	/**
	 * Liest einen bestimmten Datensatz aus der Tabelle aus.
	 * Welcher bestimmt die Id
	 *
	 * @param db : {@link SQLiteDatabase} : Datenbankverbindungsobjekt
	 * @param id : long : id des Notizens welches gesucht wird
	 *
	 * @return myNoteFromDb : {@link MyNote} : Notiz mit der uebergebenen Id oder null
	 */
	@Nullable
	public MyNote readMyNoteFromDbTableById(SQLiteDatabase db, long id) {
		//Decl. and Init
		MyNote myNoteFromDb = null;
		
		try {
			String strSelectionArgs[] = {String.valueOf(id)};
			
			//2. Abfrage and die Db stellen SELECT * FROM und speichern
			Cursor cQueryResult = db.query(TABLE_NAME,
					null,
					COL_NAME_ID + EQUALS_OPERATOR_INC_PLACE_HOLDER,
					strSelectionArgs,
					null,
					null,
					null);
			
			//3. Ergebnismenge auswerten
			cQueryResult.moveToFirst();
			
			//4. Model von Cursor anfertigen
			myNoteFromDb = this.createMyNoteFromCursor(cQueryResult);
			
			
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			//3. Schliessen
			db.close();
		}
		return myNoteFromDb;
	}
	
	//endregion
	
	//region 5 Insert
	
	/**
	 * Fuegt ein Notiz in die Datenbank ein
	 *
	 * @param db             : {@link SQLiteDatabase} : Datenbankverbindungsobjekt
	 * @param myNoteToInsert : {@link MyNote} : Objekt das eingefuegt werden soll
	 *
	 * @return lngRowId : long : id des eingefuegten Objektkes. Oder -1 sollte das
	 * einfuegen nicht funktioniert haben
	 */
	public long insertMyNoteIntoDbTable(SQLiteDatabase db, MyNote myNoteToInsert) {
		
		//Decl. and Init
		long lngRowId = -1;
		
		try {
			//1. Datenbankobjekt ist bereits geoffnet
			
			//2. ContentValues Spaltennamen und Werte mappen
			ContentValues cvMyNotes = createContentValuesFromMyNote(myNoteToInsert);
			
			
			//3. Insert
			lngRowId = db.insert(TABLE_NAME, null, cvMyNotes);
			
			
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			//3. Schliessen
			db.close();
		}
		
		return lngRowId;
	}
	//endregion
	
	//region 6. Update
	
	/**
	 * Aendert ein Notiz in der Datenbanktabelle
	 *
	 * @param db             : {@link SQLiteDatabase} : Datenbankverbindungsobjekt
	 * @param myNoteToUpdate : {@link MyNote} : Objekt das eingefuegt werden soll
	 *
	 * @return iNumberOfAffectedRows : int : Anzahl der veraenderten Datensaetze
	 */
	public int updateMyNoteInDbTable(SQLiteDatabase db, MyNote myNoteToUpdate) {
		
		//Decl. and Init
		int iNumberOfAffectedRows = -1;
		
		try {
			//1. Datenbankobjekt ist bereits geoffnet
			
			//3. WhereStatement generieren
			String strSelection = COL_NAME_ID + EQUALS_OPERATOR_INC_PLACE_HOLDER;
			
			//4. Argumente generieren
			String[] strSelectionArgs = {String.valueOf(myNoteToUpdate.getId())};
			
			//5. ContentValues Werte und Spaltennamen mappen
			ContentValues cvMyNote = createContentValuesFromMyNote(myNoteToUpdate);
			//5. Update
			iNumberOfAffectedRows = db.update(TABLE_NAME, cvMyNote,
					strSelection, strSelectionArgs);
			
			
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			//3. Schliessen
			db.close();
		}
		return iNumberOfAffectedRows;
	}
	//endregion
	
	//region 7. Delete
	
	/**
	 * Loescht ein Notiz aus der Datenbanktabelle
	 *
	 * @param db             : {@link SQLiteDatabase} : Datenbankverbindungsobjekt
	 *  @param myNoteId : long : NotizId die geloescht werden soll
	 *
	 * @return iNumberOfAffectedRows : int : Anzahl der geloeschten Datensaetze
	 */
	public int deleteMyNoteInDbTable(SQLiteDatabase db, Long myNoteId) {
		//Decl. and Init
		int             iNumberOfAffectedRows = -1;
		String          strStmtDeleteModel    = "";
		SQLiteStatement stmtDeleteModel       = null;
		
		try {
			//1. Datenbankobjekt ist bereits geoffnet
			
			//3. String Statement generieren
			strStmtDeleteModel = DELETE_FROM_TBL + TABLE_NAME;
			strStmtDeleteModel += WHERE_CONDITION + COL_NAME_ID;
			strStmtDeleteModel += EQUALS_OPERATOR_INC_PLACE_HOLDER;
			
			stmtDeleteModel = db.compileStatement(strStmtDeleteModel);
			
			//4. Argumente generieren
			String[] strSelectionArgs = {myNoteId.toString()};
			
			//5. Argumente binden
			stmtDeleteModel.bindAllArgsAsStrings(strSelectionArgs);
			
			//5. Update
			iNumberOfAffectedRows = stmtDeleteModel.executeUpdateDelete();
			
			
		} catch (SQLiteException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			
			//Wenn Statement korrket generiert wurde schliessen
			if (stmtDeleteModel != null) {
				stmtDeleteModel.close();
			}
			//3. Schliessen der Datenbank
			db.close();
		}
		return iNumberOfAffectedRows;
	}
	//endregion
	
	//region 8. Hilfsmethoden und Funktionen
	
	/**
	 * Mappen der Spaltennamen zu den konkreten Werten di
	 * eingefuegt oder geaendert werden sollen
	 *
	 * @param myNote : {@link MyNote} : MyNote dessen werte mit den Spaltennamen gemappt wird
	 *
	 * @return cvMyNote : {@link ContentValues} : Map die Spaltenname und Werte mappt
	 */
	private ContentValues createContentValuesFromMyNote(MyNote myNote) {
		ContentValues cvMyNote = new ContentValues();
		
		cvMyNote.put(COL_NAME_NAME, myNote.getName());
		cvMyNote.put(COL_NAME_DATE, myNote.getDate());
		cvMyNote.put(COL_NAME_CONTENT, myNote.getContent());
		
		
		return cvMyNote;
		
	}
	
	/**
	 * Generiert ein fertiges Model aus der mitgelieferten Ergebnismenge
	 *
	 * @param queryResult : {@link Cursor} : Ergenismenge
	 *
	 * @return myNote : {@link MyNote} :  Gefertigtes Notiz
	 */
	private MyNote createMyNoteFromCursor(Cursor queryResult) {
		
		
		final int indexId      = queryResult.getColumnIndex(COL_NAME_ID);
		final int indexName    = queryResult.getColumnIndex(COL_NAME_NAME);
		final int indexDate    = queryResult.getColumnIndex(COL_NAME_DATE);
		final int indexContent = queryResult.getColumnIndex(COL_NAME_CONTENT);
		
		int    iId        = queryResult.getInt(indexId);
		String strName    = queryResult.getString(indexName);
		String strDate    = queryResult.getString(indexDate);
		String strContent = queryResult.getString(indexContent);
		
		
		return new MyNote(iId, strName, strDate, strContent);
	}
	
	//endregion
}
