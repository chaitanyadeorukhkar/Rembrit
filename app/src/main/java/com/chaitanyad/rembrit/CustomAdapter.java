package com.chaitanyad.rembrit;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaitanyad.rembrit.R;

/**
 * Created by deoru on 9/2/2016.
 */
public class CustomAdapter extends SimpleCursorAdapter {
    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;

    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        /*TextView titleS=(TextView)view.findViewById(R.id.upcomingID);
        TextView titleS=(TextView)view.findViewById(R.id.upcomingtext);
        TextView titleS=(TextView)view.findViewById(R.id.upcomingtime);

        int Title_index=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        int Artist_index=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

        titleS.setText(cursor.getString(Title_index));
        artistS.setText(cursor.getString(Artist_index));*/

    }

    public void refreshListView(Context context,Cursor cursor,ViewGroup view)
    {
        newView(context,cursor,view);
    }

}
