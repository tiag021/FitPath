package com.example.fithpath.ui.runs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fithpath.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> listStats;
    private HashMap<String, String> listPhotos;

    public ExpandableListViewAdapter(Context context, ArrayList<String> listStats, HashMap<String, String> listPhotos) {
        this.context = context;
        this.listStats = listStats;
        this.listPhotos = listPhotos;
    }


    @Override
    public int getGroupCount() {
        return listStats.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listPhotos.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listStats.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listPhotos.get(listStats.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_runs_parentrow, null);
        }

        TextView runStats = convertView.findViewById(R.id.runStat);
        runStats.setText((String)getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_runs_childrow, null);
        }
        File imgFile = new  File(getChild(groupPosition, childPosition).toString());

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        ImageView runStats = convertView.findViewById(R.id.runPhoto);

        runStats.setImageBitmap(myBitmap);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}