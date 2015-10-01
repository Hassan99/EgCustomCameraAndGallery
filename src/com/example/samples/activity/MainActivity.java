package com.example.samples.activity;

import com.androidquery.AQuery;
import com.example.samples.R;
import com.example.samples.additionalclasses.FragmentStateMaintainClass;
import com.example.samples.fragments.FragmentCamera;
import com.example.samples.fragments.FragmentHome;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;


public class MainActivity extends Activity {
	
	AQuery aq;
	
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq= new AQuery(this);
        FragmentHome fragmentHome= new FragmentHome();
        
        replaceFragment(fragmentHome);
    }

    	  public void replaceFragment(android.app.Fragment fragment) {
    	        getFragmentManager()
    	                .beginTransaction()
    	                .replace(
    	                        R.id.frame_container, fragment).commit();
    	    }

    	  public String getRealPathFromURI(Uri contentURI) {
    	        String result;
    	        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
    	        if (cursor == null) { // Source is Dropbox or other similar local file path
    	            result = contentURI.getPath();
    	        } else {
    	            cursor.moveToFirst();
    	            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    	            result = cursor.getString(idx);
    	            cursor.close();
    	        }
    	        return result;
    	    }
    	  public void fragmentStateClear(){
    		  FragmentStateMaintainClass.fragmentState.clear();
    		  FragmentStateMaintainClass.fragmentState.add(FragmentStateMaintainClass.HOME);
    	  }
    	  
    	  public void backState(){
    		  int frag_size = FragmentStateMaintainClass.fragmentState.size();
  	        frag_size -= 1;
  	        if (frag_size == 0) {
  	            showAlertDialog(this, "Info", "Are you sure to want to exit? ", com.example.samples.helper.Constants.EXIT);
  	            return;
  	        }

  	        FragmentStateMaintainClass.fragmentState.remove(frag_size);
  	        int frag = FragmentStateMaintainClass.fragmentState.get(frag_size - 1);

  	        if (frag == FragmentStateMaintainClass.HOME) {
  	            replaceFragment(new FragmentHome());
  	        } else if (frag == FragmentStateMaintainClass.CAMERA) {
  	            replaceFragment(new FragmentCamera());
  	        }
    	  }
    	  
    	  @Override
    	public void onBackPressed() {
    	backState();
    		  
    	}
    	  
    	  public void showAlertDialog(Activity context, String title, String message,
                  int status) {
final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
alertDialog.setTitle(title);
alertDialog.setMessage(message);
alertDialog.setCancelable(false);


if (status == com.example.samples.helper.Constants.EXIT) {
alertDialog.setButton("OK",
      new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {

              finish();

          }
      });

alertDialog.setButton2("Cancel",
      new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface arg0, int arg1) {
              alertDialog.dismiss();
            
                  fragmentStateClear();
                      }
      });

}

alertDialog.show();
}
   
    
}
