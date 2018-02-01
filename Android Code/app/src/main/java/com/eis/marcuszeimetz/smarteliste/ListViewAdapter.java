package com.eis.marcuszeimetz.smarteliste;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.eis.marcuszeimetz.smarteliste.MainActivity.FIRST_COLUMN_Route;
import static com.eis.marcuszeimetz.smarteliste.MainActivity.FOUR_COLUMN_ROLE;
import static com.eis.marcuszeimetz.smarteliste.MainActivity.SECOND_COLUMN_Date;


class ListViewAdapter extends BaseAdapter {

        public ArrayList<HashMap<String, String>> list;
        int type;
        Activity activity;
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;

        public ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list, int type){
            super();
            this.activity=activity;
            this.list=list;
            this.type=type;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub



            LayoutInflater inflater=activity.getLayoutInflater();

            if(convertView == null){


                if (type == 1) {


                    convertView = inflater.inflate(R.layout.list_item, null);

                    txtFirst = (TextView) convertView.findViewById(R.id.name);
                    txtSecond = (TextView) convertView.findViewById(R.id.gender);
                    txtThird = (TextView) convertView.findViewById(R.id.textview_role);

                    HashMap<String, String> map=list.get(position);
                    txtFirst.setText(map.get(FIRST_COLUMN_Route));
                    txtSecond.setText(map.get(SECOND_COLUMN_Date));
                    txtThird.setText(map.get(FOUR_COLUMN_ROLE));

                } else {
                    convertView = inflater.inflate(R.layout.list_item_empty, null);
                    txtFirst = (TextView) convertView.findViewById(R.id.name);

                    HashMap<String, String> map=list.get(position);
                    txtFirst.setText(map.get(FIRST_COLUMN_Route));

                }

            }




            return convertView;
        }

    }

