/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Jackson Woodruff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package edu.coass.table;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

/**
 * Created by Jackson on 1/28/2015.
 *
 * This is an adapter for use with the TableList.
 *
 * It is especially designed to use String arrays
 * in such a ay that it can take the width of each
 * column from the TableList after it has been
 * added to it (as opposed to someone having to
 * specify the size of each column).
 */
public abstract class TableAdapter<T> extends ArrayAdapter<T> {
	float[] columnWidths = new float[0];
	int numberOfColumns = 0;
	int unusableWidth = 0;

	// These are some numbers used for storing various settings
	// to do with the appearence of the list
	int spaceColor = Color.WHITE;
	int columnSpacing = 0;
	int rowSpacing = 0;
	int cellPadding = 0;

	int cellBackgroundColor = Color.TRANSPARENT;
	
	public TableAdapter(Context context, T[] values) {
		// 0 is okay to pass here because we are going to the the the 
		super(context, 0, values);
	}

	public abstract View getView(int position, View convertView, ViewGroup parent, int usableWidth);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// We need to update all the floats for the width etc. here
		// in case the size of the view has changed since the last
		// getView call.

		int usableWidth = parent.getWidth() - unusableWidth;


		// This layout is necessary in case we need to put the
		// line in above the row.
		LinearLayout tableRow = new LinearLayout(getContext());

		tableRow.setOrientation(LinearLayout.VERTICAL);

		if (position != 0) {
			View rowDivider = new View(getContext());
			LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, rowSpacing
			);
			rowDivider.setBackgroundColor(spaceColor);
			tableRow.addView(rowDivider, dividerParams);
		}

		View row = getView(position, convertView, parent, usableWidth);
		row.setBackgroundColor(cellBackgroundColor);

		tableRow.addView(row);

		return tableRow;
	}

	/*
	 * This method returns the default layout params for a view in column n.
	 */
	public LinearLayout.LayoutParams getLayoutParamsAt(int n, int maxWidth) {
		int usableWidth = maxWidth - unusableWidth;

		return new LinearLayout.LayoutParams(
				(int) (columnWidths[n] * usableWidth), ViewGroup.LayoutParams.WRAP_CONTENT
		);
	}

	protected View getRowSeparator() {
		View rowSeparator = new View(getContext());
		rowSeparator.setBackgroundColor(spaceColor);

		ViewGroup.LayoutParams separatorParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, rowSpacing
		);
		rowSeparator.setLayoutParams(separatorParams);

		return rowSeparator;
	}

	protected View getColumnSeparator() {
		View rowSeparator = new View(getContext());
		rowSeparator.setBackgroundColor(spaceColor);

		ViewGroup.LayoutParams separatorParams = new ViewGroup.LayoutParams(
				columnSpacing, ViewGroup.LayoutParams.MATCH_PARENT
		);
		rowSeparator.setLayoutParams(separatorParams);

		return rowSeparator;
	}

	public void setCellBackgroundColor(int color) {
		this.cellBackgroundColor = color;
	}

	public void setSpaceColor(int color) {
		this.spaceColor = color;
	}

	public void setColumnSpacing(int spacing) {
		this.columnSpacing = spacing;
		updateDimensions();
	}

	public void setRowSpacing(int spacing) {
		this.rowSpacing = spacing;
		updateDimensions();
	}
	
	public void setColumnWidths(float[] widths) {
		this.columnWidths = widths;
		updateDimensions();
	}

	public void setCellPadding(int padding) {
		this.cellPadding = padding;
		updateDimensions();
	}

	/*
	 * This is a small method used for keeping the values up to date.
	 *
	 * It is private because it is called automatically when it has to be.
	 */
	private void updateDimensions() {
		if (columnWidths == null) {
			return;
		}
		this.numberOfColumns = columnWidths.length;
		this.unusableWidth = (numberOfColumns * columnSpacing) + (numberOfColumns * 2 * cellPadding);
	}
}
