package com.example.disasterfasoulisproject;

import android.provider.BaseColumns;

public class ContacsDbContract {

    private ContacsDbContract() { }

    //Χρησιμοποιείται απο την κλάση ContactsDbHelper για να εχω τα final πεδία αντι για καρφωτά string
    public  static class ContacsEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacs";
        public static final String COLUMN_NAME_CONTACT_TITLE = "contacs_name";
        public static final String COLUMN_NAME_CONTACT_PHONE = "contacs_phone";
    }


}
