package com.android.App_25.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class StockProvider extends ContentProvider {

    private static final int QUOTE = 100;
    private static final int QUOTE_FOR_SYMBOL = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private com.android.App_25.data.DbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(com.android.App_25.data.Contract.AUTHORITY, com.android.App_25.data.Contract.PATH_QUOTE, QUOTE);
        matcher.addURI(com.android.App_25.data.Contract.AUTHORITY, com.android.App_25.data.Contract.PATH_QUOTE_WITH_SYMBOL, QUOTE_FOR_SYMBOL);
        return matcher;
    }


    //Lifecycle start
    @Override
    public boolean onCreate() {
        dbHelper = new com.android.App_25.data.DbHelper(getContext());
        return true;
    }
//Lifecycle end

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                returnCursor = db.query(
                        com.android.App_25.data.Contract.Quote.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case QUOTE_FOR_SYMBOL:
                returnCursor = db.query(
                        com.android.App_25.data.Contract.Quote.TABLE_NAME,
                        projection,
                        com.android.App_25.data.Contract.Quote.COLUMN_SYMBOL + " = ?",
                        new String[]{com.android.App_25.data.Contract.Quote.getStockFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                db.insert(
                        com.android.App_25.data.Contract.Quote.TABLE_NAME,
                        null,
                        values
                );
                returnUri = com.android.App_25.data.Contract.Quote.uri;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        String selection = s;
        if (selection == null) selection = "1";
        switch (uriMatcher.match(uri)) {
            case QUOTE:
                rowsDeleted = db.delete(
                        com.android.App_25.data.Contract.Quote.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;

            case QUOTE_FOR_SYMBOL:
                String symbol = com.android.App_25.data.Contract.Quote.getStockFromUri(uri);
                rowsDeleted = db.delete(
                        com.android.App_25.data.Contract.Quote.TABLE_NAME,
                        '"' + symbol + '"' + " =" + com.android.App_25.data.Contract.Quote.COLUMN_SYMBOL,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                com.android.App_25.data.Contract.Quote.TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }


    }
}
