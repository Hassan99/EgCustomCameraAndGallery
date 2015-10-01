package com.example.samples.adapters;

/**
 * Created by hassan on 14-09-15.
 */

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samples.R;
import com.example.samples.db.DbUris;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

public class ImagesListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<DbUris> listItems;

    public ImagesListAdapter(Context c, List<DbUris> listItems) {
        mContext = c;
        this.listItems = listItems;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder vh = null;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_image_items, null);
            vh = new Viewholder();
            vh.imgPreview = (ImageView) convertView.findViewById(R.id.imgImage);
            vh.txtLabel=(TextView)convertView.findViewById(R.id.txtLable);
            


            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200, 200);
            vh.imgPreview.setLayoutParams(lp);
            vh.imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            vh.imgPreview.setPadding(2, 2, 2, 2);

            convertView.setTag(vh);
        } else {
            vh = (Viewholder) convertView.getTag();
        }


        vh.txtLabel.setText(listItems.get(position).label);
        loadImageFromUrl(listItems.get(position).imgPath, vh.imgPreview);
        return convertView;
    }

    void loadImageFromUrl(String url, ImageView imageView) {
    	
    	File file= new File(url);
    	if(file.exists()){
    		Picasso.with(mContext)
                .load(file)
                .placeholder(R.drawable.ic_launcher)
                .resize(300, 300)
                .error(R.drawable.ic_launcher)
                .into(imageView);
    		
    	}
    	else{
    		imageView.setImageResource(R.drawable.ic_launcher);
    	}
    }

    public class Viewholder {
        public ImageView imgPreview;
        public TextView txtLabel;
        
    }


}