package edu.coass.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jackson on 1/29/2015.
 *
 * This is a cell based adapter, meaning that a
 * method is called for each (i, j) cell.
 *
 * For an adapter that requests a single view per row,
 * use the RowBasedTableAdapter.
 */
public abstract class CellBasedAdapter<T> extends RowBasedTableAdapter<T> {
	public CellBasedAdapter(Context context, T[] values) {
		super(context, values);
	}

	@Override
	public View[] getRow(int position, View convertView, ViewGroup parent,
	                     int[] columnWidths) {
		View[] results = new View[columnWidths.length];

		for (int i = 0; i < results.length; i ++) {
			results[i] = getCell(i, position);
		}

		return results;
	}

	/*
	 * Note when implementing this, (0, 0) is the top
	 * left corner.
	 *
	 *
	 */
	public abstract View getCell(int x, int y);
}
