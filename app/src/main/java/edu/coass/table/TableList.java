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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Jackson on 1/28/2015.
 */
public class TableList extends LinearLayout {
	ListView table;

	LinearLayout tableHeaders;
	String[] tableHeaderContents;

	private TableAdapter tableAdapter;
	float[] columnWidths;

	int headerTextColor = Color.BLACK;
	int headerBackground = Color.TRANSPARENT;

	private boolean headersNeedRefresh = true;

	public TableList(Context context) {
		super(context);
		init();
	}
	
	public TableList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		tableAdapter = new SimpleTableAdapter(getContext(), new CharSequence[0][0]);
		table = new ListView(getContext());
		tableHeaders = new LinearLayout(getContext());

		table.setDivider(null);
		table.setDividerHeight(0);


		if (!isInEditMode()) {
			ViewTreeObserver observer = this.getViewTreeObserver();
			observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					// This was causing serious performance issues
					// because it was constantly called. Putting it in
					// this structure fixes that
					if (headersNeedRefresh) {
						setupTableHeaders();
						headersNeedRefresh = false;
					}
				}
			});
		}
		this.setOrientation(VERTICAL);
		refreshUI();
	}

	/*
	 * This is so the columns resize when the orientation of the
	 * device is changed
	 */
	@Override
	public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		refreshUI();
	}

	/*
	 * The length of the array is the number of columns
	 * that will be used. The contents of every slot in
	 * this array is the size of each respective column
	 */
	public void setColumnWidths(float[] widths) {
		tableAdapter.setColumnWidths(widths);
		this.columnWidths = widths;
		refreshUI();
	}

	/*
	 * This is a helper method for setting the column
	 * widths. It is useful when you want the columns
	 * to have equal widths.
	 *
	 * Note that this uses the View.getWidth() method
	 * and so must be called after the view has been
	 * added to a layout
	 */
	public void setColumnWidths(int numberOfColumns) {
		columnWidths = new float[numberOfColumns];
		Arrays.fill(columnWidths, 1f / (float)numberOfColumns);
		tableAdapter.setColumnWidths(columnWidths);
		refreshUI();
	}

	/*
	 * This is for setting the headers of the table.
	 * It will remove all previously added headers
	 */
	public void setTableHeaders(View view) {
		tableHeaders.removeAllViewsInLayout();
		tableHeaders.addView(view);
		this.tableHeaderContents = null;
	}

	public void setTableHeaders(String[] headers, int textColor, int background) {
		this.tableHeaderContents = headers;
		this.headerTextColor = textColor;
		this.headerBackground = background;
		headersNeedRefresh = true;
	}

	private void setupTableHeaders() {
		if (tableHeaderContents == null || columnWidths == null || getWidth() == 0) {
			// if the headers are null, then we will have to draw the
			// stuff at a later date
			return;
		}


		tableHeaders.removeAllViews();
		tableHeaders.setOrientation(HORIZONTAL);
		tableHeaders.setBackgroundColor(headerBackground);
		tableHeaders.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
		));

		for (int i = 0; i < tableHeaderContents.length; i ++) {
			if (i != 0) {
				View rowSpacer = tableAdapter.getColumnSeparator();
				tableHeaders.addView(rowSpacer);
			}

			TextView item = new TextView(getContext());
			item.setText(tableHeaderContents[i]);
			item.setTextColor(headerTextColor);
			item.setPadding(tableAdapter.cellPadding, tableAdapter.cellPadding,
					tableAdapter.cellPadding, tableAdapter.cellPadding);
			item.setLayoutParams(tableAdapter.getLayoutParamsAt(i, getWidth()));

			tableHeaders.addView(item);
		}
	}


	/*
	 * This method simply takes the layout and resets it.
	 *
	 * It is called automatically when one of the important parameters
	 * changes
	 */
	private void refreshUI() {
		this.removeAllViews();
		this.setOrientation(VERTICAL);
		this.addView(tableHeaders);
		this.addView(tableAdapter.getRowSeparator());
		this.addView(table);
		this.addView(tableAdapter.getRowSeparator());

		headersNeedRefresh = true;
	}

	public void setAdapter(TableAdapter adapter) {
		adapter.setColumnWidths(columnWidths);
		table.setAdapter(adapter);
		this.tableAdapter = adapter;
		refreshUI();
	}
}