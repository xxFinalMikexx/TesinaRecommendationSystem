package com.example.xxfin.tesinarecommendationsystem;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.xxfin.tesinarecommendationsystem.Objects.InfoPlace;

/**
 * Created by xxfin on 22/06/2017.
 */

public class ArrayAdapterPlaces extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ArrayAdapterPlaces(Context context, String[] values) {
        super(context, R.layout.list_places, values);
        this.context = context;
        this.values = values;
    }
}
