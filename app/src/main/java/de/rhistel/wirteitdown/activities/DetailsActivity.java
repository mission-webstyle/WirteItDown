package de.rhistel.wirteitdown.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import de.rhistel.wirteitdown.R;
import de.rhistel.wirteitdown.logic.db.DbManager;
import de.rhistel.wirteitdown.model.MyNote;

/**
 * Zeigt alle Note Details an
 */
public class DetailsActivity extends AppCompatActivity {
	//region 0. Konstanten
	private static final String TAG = DetailsActivity.class.getSimpleName();
	//endregion
	
	//region 1. Decl. and Init Attribute
	private EditText txtNoteName;
	private EditText txtNoteDate;
	private EditText txtNoteContent;
	
	private Long editDeleteId;
	//endregion
	
	//region 2. Lebenszyklus
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		//1. Layout setzen
		this.setContentView(R.layout.details_activity_layout);
		
		//2. Widgets generieren
		this.txtNoteName    = this.findViewById(R.id.txtNoteName);
		this.txtNoteDate    = this.findViewById(R.id.txtNoteDate);
		this.txtNoteContent = this.findViewById(R.id.txtNoteContent);
		
	}
	
	
	/**
	 * Teilweise Sichtbarkeit
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
	}
	
	/**
	 * Sichtbar Anlaufpunkt zur Datenaktualsierung
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
		//ID Auswertung
		this.checkForIntentExtras();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
	//endregion
	
	//region 3. MenuHandling
	
	/**
	 * Baut das Menu zur Laufzeit auf
	 *
	 * @param detailsMenu : {@link Menu} : Hauptmenu
	 *
	 * @return true - sichtbar - false nicht sichtbar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu detailsMenu) {
		this.getMenuInflater().inflate(R.menu.details_menu, detailsMenu);
		return true;
	}
	
	public void submitMyNote(MenuItem item) {
		
		MyNote myNoteFromGui = this.getMyNoteFromGui();
		
		if (myNoteFromGui != null) {
			if (this.editDeleteId > MyNote.DEF_VALUE_ID) {
				//Id des augewählten Objektes uebernehemen
				myNoteFromGui.setId(this.editDeleteId.intValue());
				
				//Update
				DbManager.getInstance(this).updateMyNote(myNoteFromGui);
			} else {
				//INSERT
				DbManager.getInstance(this).insertMyNote(myNoteFromGui);
				
				this.clearGuiWidgets();
			}
			
			Toast.makeText(this, R.string.strUserMsgSubmitSuccess, Toast.LENGTH_SHORT).show();
			
		} else {
			Toast.makeText(this, R.string.strUserMsgFillInEverything, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void deleteMyNote(MenuItem item) {
		if (this.editDeleteId > MyNote.DEF_VALUE_ID) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setMessage(R.string.strUserMsgDeleteNote)
			       .setTitle(R.string.strDelete)
			       .setPositiveButton(android.R.string.ok, (dialog, which) -> {
				
				       //DELETE
				       DbManager.getInstance(this).deleteMyNote(this.editDeleteId);
				
				       this.clearGuiWidgets();
				
				       Toast.makeText(this, R.string.strUserMsgDeleteSuccess, Toast.LENGTH_SHORT).show();
				
				       //Activity schliessen
				       this.finish();
			       })
			       .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
			       })
			       .create()
			       .show();
		} else {
			Toast.makeText(this, R.string.strUserMsgSelectANoteToDelete, Toast.LENGTH_SHORT).show();
		}
	}
	
	//TODO 2. ShareNote implementieren eingentliches teilen
	public void shareNote(MenuItem mnuItem) {
	
		//1. Eingabedaten auslesen
		MyNote myNoteFromGui = this.getMyNoteFromGui();
		
		if (myNoteFromGui != null) {
			//Implizite Intent
			Intent shareMyNoteInfoIntent = new Intent();
			
			//Hey ich brauche den OS-ShareDialog
			shareMyNoteInfoIntent.setAction(Intent.ACTION_SEND);
			
			//Hey OS hier sind die Daten die ich teilen moechte
			shareMyNoteInfoIntent.putExtra(Intent.EXTRA_TEXT,myNoteFromGui.getShareData());
			
			//Hey OS folgender Datentyp habe ich vor zu teilen
			shareMyNoteInfoIntent.setType("text/plain");
			
			//Hey OS hier sind alle Infos die du brauchst gib mir ein startbares Intent und damit den konfigurierten ShareDialog
			Intent myNoteShareIntentToStart = Intent.createChooser(shareMyNoteInfoIntent, this.getString(R.string.app_name));
			
			//Hey OS starte mir den von dir aufbereiteten Dialog
			this.startActivity(myNoteShareIntentToStart);
		} else {
			Toast.makeText(this, R.string.strUserMsgFillInEverythingToShareYourNote, Toast.LENGTH_SHORT).show();
		}
		
	}
	//endregion
	
	
	//region Hilfsmethoden und Funktion
	
	/**
	 * Extrahiert eine Id des in der {@link MainActivity} ausgewaehlten {@link MyNote}
	 * in der {@link MainActivity}. Setzt einen Standardwert: {@link MyNote#DEF_VALUE_ID} sollte
	 * die Activity blanko uber den Hinzufuegen - Menue - Button aufegrufen worden sein
	 */
	private void checkForIntentExtras() {
		
		//Checken ob ein Extra vorliegt
		this.editDeleteId = this.getIntent().getLongExtra(
				this.getString(R.string.strIntentExtraKeyForId),
				MyNote.DEF_VALUE_ID
		);
		
		if (this.editDeleteId > MyNote.DEF_VALUE_ID) {
			MyNote myNoteFromDb = DbManager.getInstance(this).getMyNoteFromDbById(this.editDeleteId);
			
			this.showMyNoteDataInWidgets(myNoteFromDb);
		}
		
	}
	
	/**
	 * Zeigt alle Daten des Models in den passenden Textfeldern an
	 *
	 * @param myNoteFromDb : {@link MyNote} : Notiz die anzeigt werden soll
	 */
	private void showMyNoteDataInWidgets(MyNote myNoteFromDb) {
		this.txtNoteName.setText(myNoteFromDb.getName());
		this.txtNoteDate.setText(myNoteFromDb.getDate());
		this.txtNoteContent.setText(myNoteFromDb.getContent());
	}
	
	/**
	 * Leert alle Felder und {@link DetailsActivity#txtNoteName}
	 * kriegt den Fokus
	 */
	private void clearGuiWidgets() {
		this.txtNoteName.setText("");
		this.txtNoteDate.setText("");
		this.txtNoteContent.setText("");
		
		this.txtNoteName.requestFocus();
	}
	
	
	/**
	 * Gibt ein generiertes {@link MyNote}-Objekt zurueck,
	 * sollten die Eingabedaten korrekt(nicht leer) sein.
	 * Ist das nicht dall so wird <b>null</b> zurueck geliefert.
	 *
	 * @return myNoteFromGui : {@link MyNote} - oder null
	 */
	private MyNote getMyNoteFromGui() {
		//MyNoteFromGui
		MyNote myNoteFromGui = null;
		
		//Eingabedaten auslesen
		String strNoteName    = this.txtNoteName.getText().toString();
		String strNoteDate    = this.txtNoteDate.getText().toString();
		String strNoteContent = this.txtNoteContent.getText().toString();
		
		String[] strInputData = {
				strNoteName,
				strNoteDate,
				strNoteContent
		};
		
		boolean isInputDataValid = true;
		
		for (String s : strInputData) {
			if (s.isEmpty()) {
				isInputDataValid = false;
			}
		}
		
		if (isInputDataValid) {
			myNoteFromGui = new MyNote(strNoteName, strNoteDate, strNoteContent);
		}
		
		return myNoteFromGui;
	}
	//endregion
}