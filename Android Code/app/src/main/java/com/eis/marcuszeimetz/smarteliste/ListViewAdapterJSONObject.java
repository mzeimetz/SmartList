package com.eis.marcuszeimetz.smarteliste;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;


class ListViewAdapterJSONObject extends BaseAdapter {

        public JSONObject [] jsonlist;
        int type;
        Activity activity;
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;

        public ListViewAdapterJSONObject(Activity activity, JSONObject [] list, int type){
            super();
            this.activity=activity;
            this.jsonlist=list;
            this.type=type;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jsonlist.length;
        }

        @Override
        public Object getItem(int position) {

            return null;
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



                    //HashMap<String, String> map=jsonlist.get(position);
                   // txtFirst.setText(map.get(FIRST_COLUMN_Route));
                    //txtSecond.setText(map.get(SECOND_COLUMN_Date));
                    //txtThird.setText(map.get(FOUR_COLUMN_ROLE));

                }

            }




            return convertView;
        }

    }

