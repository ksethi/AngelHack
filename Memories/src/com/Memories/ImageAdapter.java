package com.Memories;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Bitmap[] pmap;
    // Keep all Images in array
    /*
    public Integer[] mThumbIds = {
           
    		
    		R.drawable.logo,R.drawable.logo,
    		R.drawable.logo,R.drawable.logo,
    		R.drawable.logo,R.drawable.logo,
    		R.drawable.logo,R.drawable.logo,
    		R.drawable.logo,R.drawable.logo

            
    };
 */
    // Constructor
    public ImageAdapter(Context c, Bitmap[] map){
    	pmap = map; 
        mContext = c;
    }

    @Override
    public int getCount() {
        return pmap.length;
    }
 
    @Override
    public Object getItem(int position) {
        return pmap[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(pmap[position]);
        //imageView.setImageResource(pmap[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;
    }
}
 