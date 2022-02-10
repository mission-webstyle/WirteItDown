package de.rhistel.wirteitdown.testData;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;

import java.util.List;

import de.rhistel.wirteitdown.R;
import de.rhistel.wirteitdown.logic.CsvFileHandler;
import de.rhistel.wirteitdown.logic.db.DbManager;
import de.rhistel.wirteitdown.model.MyNote;

/**
 * TestDinge
 */
public class TestActivity extends AppCompatActivity {
	
	private EditText txtOutput;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//1. Layout setzen
		this.setContentView(R.layout.test_activity_layout);
		
		this.txtOutput = this.findViewById(R.id.txtOutput);
	}
	
	public void testThis(View v){
		
		List<MyNote> myNotesFromDb = DbManager.getInstance(this).getAllMyNotes();
		
		for(MyNote mn : myNotesFromDb) {
			this.txtOutput.append(mn.getAllAttributesAsCsvLine());
		}
		
		this.txtOutput.append("\n#######################\n");
		
//		DbManager.getInstance(this).insertMyNote(new MyNote("Einkaufen","09.02.2022","Tomaten"));
//		DbManager.getInstance(this).updateMyNote(new MyNote(27,"Segeln","09.02.2022","Am Strand"));
//		DbManager.getInstance(this).deleteMyNote(new MyNote(27,"Segeln","09.02.2022","Am Strand"));
		
		myNotesFromDb = DbManager.getInstance(this).getAllMyNotes();
		
		for(MyNote mn : myNotesFromDb) {
			this.txtOutput.append(mn.getAllAttributesAsCsvLine());
		}
		
		
	}
}