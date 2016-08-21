/**
 * Copyright (c) 2015,2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.app.testapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.unidev.app.testapp.R;
import com.unidev.app.testapp.core.Core;
import com.unidev.polydata.domain.Poly;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list);

        final List<? extends Poly> records = new ArrayList<>(Core.getInstance().fetchStorage().list());


        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return records.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    view = inflater.inflate(R.layout.list_item, null);
                }

                TextView item = (TextView) view.findViewById(R.id.item);
                item.setText(records.get(position) + "");

                return view;
            }
        });
        return view;
    }



}
