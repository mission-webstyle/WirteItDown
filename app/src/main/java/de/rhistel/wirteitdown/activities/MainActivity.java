package de.rhistel.wirteitdown.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import de.rhistel.wirteitdown.R;
import de.rhistel.wirteitdown.listview.ListViewAdapter;
import de.rhistel.wirteitdown.logic.CsvFileHandler;
import de.rhistel.wirteitdown.logic.db.DbManager;
import de.rhistel.wirteitdown.model.MyNote;
import de.rhistel.wirteitdown.testData.TestData;

/**
 * Einstiegspunkt in die App
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
	
	//region 0. Konstanten
	private static final String TAG             = MainActivity.class.getSimpleName();
	private static final long   DEF_VALUE_NO_ID = -1;
	//endregion
	
	//region 1. Decl. and Init Attribute
	private ListView        listViewNotes;
	private ListViewAdapter listViewAdapter;
	
	//endregion
	
	//region 2. Lebenszyklus
	
	/**
	 * Keien Sichtbarkeit
	 *
	 * @param savedInstanceState : {@link Bundle} : Kapselobjekt speichern der GUI-Infos
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		//1. Layout setzen
		this.setContentView(R.layout.main_activity_layout);
		
		//2. Widgets generieren
		this.listViewNotes = this.findViewById(R.id.listViewNotes);
		this.listViewNotes.setOnItemClickListener(this::onItemClick);
		
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
		
		this.updateListView();
		
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
	
	//region 2. MenuHandling
	
	/**
	 * Baut das Menu zur Laufzeit auf
	 *
	 * @param mainMenu : {@link Menu} : Hauptmenu
	 *
	 * @return true - sichtbar - false nicht sichtbar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu mainMenu) {
		this.getMenuInflater().inflate(R.menu.main_menu, mainMenu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		if (item.getItemId() == R.id.mnuItemAdd) {
			this.startDetailsActivity(DEF_VALUE_NO_ID);
		}
		return false;
	}
	
	//endregion
	
	//region 3. ListViewClickHandling
	
	/**
	 * Springt nach zuweisung immer dann an wenn ein ListViewItem innerhalb der {@link ListView}
	 * geklickt wird.
	 *
	 * @param parentListView             : {@link ListView} : {@link MainActivity#listViewNotes}
	 * @param currentClickedListViewItem : {@link View} : Aktuelle gekklickte ListViewItem
	 * @param index                      : int : index des akutelle Datenelementes was an dem ListViewItem dran haengt
	 * @param id                         : long : Id des Datenelementes aus dem {@link ListViewAdapter#getItemId(int)}
	 */
	@Override
	public void onItemClick(AdapterView<?> parentListView, View currentClickedListViewItem, int index, long id) {
		this.startDetailsActivity(id);
	}
	
	//endregion
	
	//region Hilfsfunktionen und Methoden
	private void updateListView() {
		
		//CsvFileHandler nutzen und Notes auslesen
//		List<MyNote> myNotesToSave = CsvFileHandler.getInstance().readMyNotesFromCsvFile(this);
		
		//DbManger Main nutzen und Notes auslesen um diese in der Lv anzuzeigen
		List<MyNote> myNotesToSave = DbManager.getInstance(this).getAllMyNotes();
		
		//Apdapter generierern
		this.listViewAdapter = new ListViewAdapter(this,  myNotesToSave);
		
		//Adapter zuweisen
		this.listViewNotes.setAdapter(this.listViewAdapter);
		
	}
	
	private void startDetailsActivity(long idOfCurrentSelectedNote) {
		//1. Explizites Intent Activity von uns aufrufen (Start,Ziel)
		Intent intentToStartDetailsActivity = new Intent(this, DetailsActivity.class);
		
		if(idOfCurrentSelectedNote > DEF_VALUE_NO_ID){
			String strIntentExtraKeyForId = this.getString(R.string.strIntentExtraKeyForId);
			intentToStartDetailsActivity.putExtra(strIntentExtraKeyForId,idOfCurrentSelectedNote);
		}
		
		//2. MainActivity->DetailsActivity
		this.startActivity(intentToStartDetailsActivity);
	}
	//endregion
}