package com.example.samples.fragments;


/**
 * Created by hassan on 14-09-15.
 */
import java.util.List;

import com.androidquery.AQuery;
import com.example.samples.R;
import com.example.samples.activity.MainActivity;
import com.example.samples.adapters.ImagesListAdapter;
import com.example.samples.db.DbUris;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;



public class FragmentImageList extends android.app.Fragment {
	
	AQuery aq;
	static final int SELECT_FILE = 2;
	GridView gridView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_imagelist, container, false);
        aq = new AQuery(getActivity(), view);
        initUi();
        return view;
    	
    }
    
    
void initUi(){
	
	
	
	List<DbUris> listItems=DbUris.listAll(DbUris.class);
if(listItems.size()>0){
	Log.e("LIST ITEM SIZE",""+listItems.size());
	ImagesListAdapter imagesListAdapter= new ImagesListAdapter(getActivity(), listItems);
	aq.id(R.id.gridImageList).getGridView().setAdapter(imagesListAdapter);
}else{
	Log.e("EMPTY",""+listItems.size());
}
	
	
	
	
	
}
   

}
