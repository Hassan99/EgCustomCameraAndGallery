package com.example.samples.fragments;

/**
 * Created by hassan on 14-09-15.
 */
import com.androidquery.AQuery;
import com.example.samples.R;
import com.example.samples.activity.MainActivity;
import com.example.samples.additionalclasses.FragmentStateMaintainClass;
import com.example.samples.db.DbUris;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public class FragmentHome extends android.app.Fragment {
	
	AQuery aq;
	static final int SELECT_FILE = 2;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	
    	View view = inflater.inflate(R.layout.fragment_home, container, false);
        aq = new AQuery(getActivity(), view);
        initUi();
        return view;
    	
    }
    
    
void initUi(){
	aq.id(R.id.btnLoadFromCamera).getButton().setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			FragmentCamera fragment = new FragmentCamera();
			((MainActivity)getActivity()).replaceFragment(fragment);
			((MainActivity)getActivity()).fragmentStateClear();
			FragmentStateMaintainClass.fragmentState.add(FragmentStateMaintainClass.CAMERA);
			
			
          
			
		}
	});
	aq.id(R.id.btnLoadFromGallery).getButton().setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			loadFromGallery();
			
		}
	});
aq.id(R.id.btnListSavedImages).getButton().setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			FragmentImageList fragment = new FragmentImageList();
			((MainActivity)getActivity()).replaceFragment(fragment);
			((MainActivity)getActivity()).fragmentStateClear();
			FragmentStateMaintainClass.fragmentState.add(FragmentStateMaintainClass.IMAGELIST);
			
			
		}
	});
	
	
}
   

void loadFromGallery(){
	  Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      startActivityForResult(i, SELECT_FILE); 
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == getActivity().RESULT_OK) {
        if (requestCode == SELECT_FILE) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            String selectedImagePath = cursor.getString(column_index);

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            String imageUri = ((MainActivity) getActivity()).getRealPathFromURI(data.getData());
            
          DbUris db= new DbUris();
          db.imgPath=imageUri;
          db.save();
          Toast.makeText(getActivity(), "File Saved To Database", Toast.LENGTH_LONG).show();
        }
    }
}
    
}
