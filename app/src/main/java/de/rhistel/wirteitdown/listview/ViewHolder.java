package de.rhistel.wirteitdown.listview;

import android.widget.TextView;

/**
 * ListViewItemLayout Gui Bean
 * Stellt eine permanete REferenz zum ListViewItem her.
 * Und kann auch im {@link android.widget.AdapterView.OnItemClickListener} verwendet werden
 * um zustandsveraendernte Methoden und Funktionen fuer die hier integrierten Widgets
 * zu implmentieren.
 *
 */
public class ViewHolder {
	
	//region 0. Konstanten
	//endregion
	
	//region 1. Decl and Init Attribute
	private TextView txtvNoteInfo;
	//endregion
	
	//region 2. Konstruktoren
	
	/**
	 * Setzt die Referenz eines ListViewItems generiert im ListViewAdapter
	 *
	 * @param txtvNoteInfo : {@link TextView} : Zum anzeigen der Note infos
	 */
	public ViewHolder(TextView txtvNoteInfo) {
		this.txtvNoteInfo = txtvNoteInfo;
	}
	
	//endregion
	
	//region 3. Getter und Setter
	
	public TextView getTxtvNoteInfo() {
		return txtvNoteInfo;
	}
	
	public void setTxtvNoteInfo(TextView txtvNoteInfo) {
		this.txtvNoteInfo = txtvNoteInfo;
	}
	
	//endregion
	
	//region 4. Methoden und Funktionen
	//endregion
}
