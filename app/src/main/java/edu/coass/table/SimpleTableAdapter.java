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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jackson on 1/29/2015.
 *
 * This is a table adapter that just puts strings into cells.
 *
 * For more complex views, you have to implement the ComplexTableAdapter
 */
public class SimpleTableAdapter extends TableAdapter<CharSequence[]> {
	public SimpleTableAdapter(Context context, CharSequence[][] values) {
		super(context, values);
	}

	@Override
	public LinearLayout getView(int position, View convertView, ViewGroup parent, int usableWidth) {
		CharSequence[] contents = getItem(position);

		LinearLayout itemContainer = new LinearLayout(getContext());

		for (int i = 0; i < Math.min(contents.length, columnWidths.length); i ++) {
			if (i != 0) {
				View columnDivider = new View(getContext());
				LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
						columnSpacing, ViewGroup.LayoutParams.MATCH_PARENT
				);
				columnDivider.setBackgroundColor(spaceColor);
				itemContainer.addView(columnDivider, dividerParams);
			}
			TextView cell = new TextView(getContext());
			LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(
					(int) (columnWidths[i] * usableWidth), ViewGroup.LayoutParams.MATCH_PARENT
			);
			cell.setBackgroundColor(cellBackgroundColor);
			cell.setText(contents[i]);
			itemContainer.addView(cell, cellParams);
		}

		return itemContainer;
	}
}
