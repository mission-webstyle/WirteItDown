package de.rhistel.wirteitdown.model;

/**
 * Stellt eine Notiz
 */
public class MyNote {
	
	//region 0. Konstanten
	public static final Long DEF_VALUE_ID = -1L;
	
	private static final String DEF_VALUE_STR = ">noValueSetYet<";
	
	private static final String CSV_SPLIT_CHAR = ";";
	
	private static final int SPLIT_INDEX_ID      = 0;
	private static final int SPLIT_INDEX_NAME    = 1;
	private static final int SPLIT_INDEX_DATA    = 2;
	private static final int SPLIT_INDEX_CONTENT = 3;
	public static final String NAME_DATE_SPLITTER = " - ";
	public static final String DATE_CONTENT_SPLITTER = "\n--------------\n";
	//endregion
	
	//region 1. Decl and Init Attribute
	private int    iId;
	private String strName;
	private String strDate;
	private String strContent;
	//endregion
	
	//region 2. Konstruktoren Alt-Einf -> Construtor -> Select None
	
	/**
	 * Standardkonstruktor zum dirkten intialsieren der Attribute
	 */
	public MyNote() {
		this.iId        = DEF_VALUE_ID.intValue();
		this.strName    = DEF_VALUE_STR;
		this.strDate    = DEF_VALUE_STR;
		this.strContent = DEF_VALUE_STR;
	}
	
	/**
	 * Ueberladener Konstruktor zum direkten setzen der wichtigsten Attribute
	 *
	 * @param strName    : {@link String} : Name der Notiz
	 * @param strDate    : {@link String} : Datum der Notiz
	 * @param strContent : {@link String} : Inhalt der Notiz
	 */
	public MyNote(String strName, String strDate, String strContent) {
		this.strName    = strName;
		this.strDate    = strDate;
		this.strContent = strContent;
	}
	
	/**
	 * Ueberladener Konstruktor zum direkten setzen der aller Attribute
	 *
	 * @param iId        : int : DbId
	 * @param strName    : {@link String} : Name der Notiz
	 * @param strDate    : {@link String} : Datum der Notiz
	 * @param strContent : {@link String} : Inhalt der Notiz
	 */
	public MyNote(int iId, String strName, String strDate, String strContent) {
		this.iId        = iId;
		this.strName    = strName;
		this.strDate    = strDate;
		this.strContent = strContent;
	}
	
	/**
	 * Zum direkten setzen aller Attribute ueber
	 * die gelesene CSV-Zeile aus dem {@link de.rhistel.wirteitdown.logic.CsvFileHandler}
	 *
	 * @param strReadCsvLine : {@link String} : Gelesene CsvZeile
	 */
	public MyNote(String strReadCsvLine) {
		this.setAllAttributesFromCsvLine(strReadCsvLine);
	}
	
	//endregion
	
	//region 3. Getter und Setter Alt + Einfg Gettter und Setter
	
	
	public int getId() {
		return iId;
	}
	
	public void setId(int iId) {
		this.iId = iId;
	}
	
	public String getName() {
		return strName;
	}
	
	public void setName(String strName) {
		this.strName = strName;
	}
	
	public String getDate() {
		return strDate;
	}
	
	public void setDate(String strDate) {
		this.strDate = strDate;
	}
	
	
	public String getContent() {
		return strContent;
	}
	
	public void setContent(String strContent) {
		this.strContent = strContent;
	}
	
	public String getNoteInfo() {
		return this.strName + NAME_DATE_SPLITTER + this.strDate;
	}
	
	/**
	 * Gibt einen Formatierten String zum teilen der Daten
	 * mit anderen Apps zurueck:
	 * Beispiel<br>
	 * <p>
	 * Einkaufen gehen - 10.02.2022<br>
	 * --------------<br>
	 * - Tomaten<br>
	 * - Eier<br>
	 * </p>
	 * @return strShareData : {@link String} : Aufgearbeiteter String zur Datenteilung
	 */
	public String getShareData() {
		return this.strName + NAME_DATE_SPLITTER + this.strDate + DATE_CONTENT_SPLITTER + this.strContent;
	}
	
	
	public String getAllAttributesAsCsvLine() {
		return this.iId + CSV_SPLIT_CHAR + this.strName + CSV_SPLIT_CHAR + this.strDate + CSV_SPLIT_CHAR + this.strContent + "\n";
	}
	
	private void setAllAttributesFromCsvLine(String strReadCsvLine) {
		String[] strAllAttributes = strReadCsvLine.split(CSV_SPLIT_CHAR);
		
		this.iId        = Integer.parseInt(strAllAttributes[SPLIT_INDEX_ID]);
		this.strName    = strAllAttributes[SPLIT_INDEX_NAME];
		this.strDate    = strAllAttributes[SPLIT_INDEX_DATA];
		this.strContent = strAllAttributes[SPLIT_INDEX_CONTENT];
	}
	//endregion
	
	//region 4. toString Alt+Einfg to String
	
	@Override
	public String toString() {
		return "Note{" +
				"strName='" + strName + '\'' +
				", strDate='" + strDate + '\'' +
				", strContent='" + strContent + '\'' +
				'}';
	}
	

	//endregion
}
