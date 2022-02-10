package de.rhistel.wirteitdown.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.rhistel.wirteitdown.R;
import de.rhistel.wirteitdown.model.MyNote;

/**
 * Handelt die Ihm uebergebene Liste und
 * baut passend dazu soviele neue ListViewItems
 * auf wie Platz dafuer ist. Und verwendet diese
 * dann immer wieder im den neusten Inhalte anzuzeigen.
 * Bei jedem Scrollvorgang geschiet das automatisch
 */
public class ListViewAdapter extends BaseAdapter {
	
	//region 0. Konstanten
	private static final String TAG = ListViewAdapter.class.getSimpleName();
	//endregion
	
	//region 1. Decl and Init Attribute
	private Context        context;
	private LayoutInflater layoutInflater;
	private List<MyNote>   myNotes;
	//endregion
	
	//region 2. Konstruktoren
	
	/**
	 * Zum direkten setzen der Hauptattribute
	 *
	 * @param context : {@link Context} : Akutelle Activity in der sich die {@link android.widget.ListView} dieses Adapters befindet
	 * @param myNotes : {@link List} - {@link MyNote} : Meine Notizliste
	 */
	public ListViewAdapter(Context context, List<MyNote> myNotes) {
		this.context = context;
		this.myNotes = myNotes;
		
		this.layoutInflater = LayoutInflater.from(this.context);
	}
	
	//endregion
	
	//region 3. ListenFunktionen
	
	@Override
	public int getCount() {
		return this.myNotes.size();
	}
	
	@Override
	public Object getItem(int index) {
		return this.myNotes.get(index);
	}
	
	@Override
	public long getItemId(int index) {
		return this.myNotes.get(index).getId();
	}
	
	
	//endregion
	
	//region 4. getView - ListViewItem aufbauen
	
	/**
	 * Baut ein neues ListViewItem auf und befuellt ein bereits vorhandenes mit neuen Daten.
	 * Diese Funktion wird nie direkt aufgerufen. Das Passiert alles automatisch, nach der zuordnung
	 * des {@link ListViewAdapter}s zu {@link android.widget.ListView}.
	 *
	 * @param index          : int : Index des  akutellen ListViewItems, welches entweder neu generiert werden soll oder aktualisiert wird
	 * @param listViewItem   : {@link View}: ListViewItem
	 * @param parentListView : {@link ViewGroup} : ListView in der akutelle Activity
	 *
	 * @return listViewItem : {@link View} : Neue generierte oder akutlaisierte ListViewItem
	 * <p>
	 */
	@Override
	public View getView(int index, View listViewItem, ViewGroup parentListView) {
		
		ViewHolder viewHolder = null;
		
		if (listViewItem == null) {
			//1. Neues ListViewItem generieren
			listViewItem = this.layoutInflater.inflate(R.layout.list_view_item_layout, parentListView, false);
			
			//2. Widgets des ListViewItems generieren
			TextView txtvNoteInfo = listViewItem.findViewById(R.id.txtvNoteInfo);
			
			//3. ViewHolder mit TextView geneirieren
			viewHolder = new ViewHolder(txtvNoteInfo);
			
			//4. ListViewItem bekommt den ViewHodler als Zusatzobjekt mitgegeben
			listViewItem.setTag(viewHolder);
			
			Log.d(TAG, "neu generiert: " + listViewItem);
			
		} else {
			//1. ViewHolder aus Tag rauscasten
			viewHolder = (ViewHolder) listViewItem.getTag();
			Log.d(TAG, "wiederverwendet: " + listViewItem);
		}
		
		//5. / 2. Akutelles NoteElement beschafften
//		MyNote myCurrentNote = (MyNote)this.getItem(index);
		MyNote myCurrentNote = this.myNotes.get(index);
		
		//6. / 3. Daten in das TextView des ListViewItems eintragen
		viewHolder.getTxtvNoteInfo().setText(myCurrentNote.getNoteInfo());
		
		
		return listViewItem;
	}
	//endregion
}
