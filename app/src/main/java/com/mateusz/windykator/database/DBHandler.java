package com.mateusz.windykator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mateusz.windykator.pojos.Debt;
import com.mateusz.windykator.pojos.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2015-10-12.
 */
public class DBHandler extends SQLiteOpenHelper {

    //Static variables
    //DB version
    private static final int DATABASE_VERSION = 2;

    //DB name
    private static final String DATABASE_NAME = "windykator";

    //Table name for People handling
    private static final String TABLE_PEOPLE = "people";
    private static final String TABLE_DEBTS = "debts";

    //Multi use table column names
    private static final String KEY_ID = "id";

    //Person table column names
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_PHONE_NO = "phoneNo";
    private static final String KEY_SEND_MESSAGE = "sendMessage";

    //Debts table column names
    private static final String KEY_VALUE = "value";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PERSON_ID = "personid";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_SURNAME + " TEXT, "
                + KEY_PHONE_NO + " TEXT, "
                + KEY_SEND_MESSAGE + " INTEGER "
                + ")";

        String CREATE_DEBTS_TABLE = "CREATE TABLE " + TABLE_DEBTS +
                "("
                + KEY_VALUE + " REAL, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_PERSON_ID + " TEXT, "
                + KEY_ID + " INTEGER PRIMARY KEY "
                + ")";

        db.execSQL(CREATE_PEOPLE_TABLE);
        db.execSQL(CREATE_DEBTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);

        // Create tables again
        onCreate(db);
    }

    /*
    ------------------------------------- Person's methods ------------------------------------
     */

    // Adding new person
    public void addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName());
        values.put(KEY_SURNAME, person.getSurname());
        values.put(KEY_PHONE_NO, person.getPhoneNo());
        values.put(KEY_SEND_MESSAGE, person.isSendMessage() ? 1 : 0);

        // Inserting Row
        db.insert(TABLE_PEOPLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single person
    public Person getPerson(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PEOPLE, new String[]{KEY_ID,
                        KEY_NAME, KEY_SURNAME, KEY_PHONE_NO, KEY_SEND_MESSAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        db.close();

        return new Person(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4) == 1, cursor.getInt(0));
    }

    // Getting All persons
    public List<Person> getAllPersons() {
        List<Person> personsList = new ArrayList();
        // Select All Query
        final String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(0));
                person.setName(cursor.getString(1));
                person.setSurname(cursor.getString(2));
                person.setPhoneNo(cursor.getString(3));
                person.setSendMessage(cursor.getInt(4) == 1 ? true : false);

                // Adding contact to list
                personsList.add(person);
            } while (cursor.moveToNext());
        }

        db.close();
        // return contact list
        return personsList;
    }

    // Updating single person
    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName());
        values.put(KEY_SURNAME, person.getSurname());
        values.put(KEY_PHONE_NO, person.getPhoneNo());
        values.put(KEY_SEND_MESSAGE, person.isSendMessage() ? 1 : 0);

        // updating row
        int integer = db.update(TABLE_PEOPLE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(person.getId())});

        db.close();

        return integer;
    }

    // Deleting single person
    public void deletePerson(Person person) {

        deletePersonDebts(person);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEOPLE, KEY_ID + " = ?",
                new String[]{String.valueOf(person.getId())});
        db.close();
    }

    /*
    ----------------------------------- Debt's methods --------------------------------------
     */

    public void addDebt(Debt debt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, debt.getValue());
        values.put(KEY_DESCRIPTION, debt.getDescription());
        values.put(KEY_PERSON_ID, debt.getPersonID());

        db.insert(TABLE_DEBTS, null, values);
        db.close();

    }

    public List<Debt> getPersonDebts(int id) {

        List<Debt> debts = new ArrayList();
        final String selectQuery = "SELECT * FROM " + TABLE_DEBTS + " WHERE " + KEY_PERSON_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {

            do {
                Debt debt = new Debt();
                debt.setValue(cursor.getDouble(0));
                debt.setDescription(cursor.getString(1));
                debt.setPersonID(cursor.getLong(2));
                debt.setID(cursor.getInt(3));

                debts.add(debt);
            } while (cursor.moveToNext());

        }
        return debts;
    }

    public List<Debt> getAllDebts() {

        List<Debt> debts = new ArrayList();
        final String selectQuery = "SELECT * FROM " + TABLE_DEBTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Debt debt = new Debt();
                debt.setValue(cursor.getDouble(0));
                debt.setDescription(cursor.getString(1));
                debt.setPersonID(cursor.getLong(2));
                debt.setID(cursor.getInt(3));

                debts.add(debt);
            } while (cursor.moveToNext());
        }
        return debts;
    }

    public int updateDebt(Debt debt) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, debt.getValue());
        values.put(KEY_DESCRIPTION, debt.getDescription());

        int integer = db.update(TABLE_DEBTS, values, KEY_ID + " = ? AND " + KEY_PERSON_ID + " =?",
                new String[]{String.valueOf(debt.getID()), String.valueOf(debt.getPersonID())});

        db.close();
        return integer;
    }

    public void deleteDebt(Debt debt) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DEBTS,KEY_ID + " = ?", new String[]{String.valueOf(debt.getID())});
        db.close();
    }

    public void deletePersonDebts(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DEBTS,KEY_PERSON_ID + " = ? ", new String[]{String.valueOf(person.getId())});
        db.close();
    }

}
