package uk.ac.shef.oak.com4510.ui.recyclerview;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Map;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.ui.history.MyImageDialog;
import uk.ac.shef.oak.com4510.ui.map.MapActivity;
import uk.ac.shef.oak.com4510.ui.mapdetail.MapDetailActivity;

/**
 * Gallery view adapter class
 *
 * @author Feng Li, Ruiqing Xu
 *
 */

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder>{
    private Activity mContext;
    private LayoutInflater mInflater;
    private static final int VIEW_TYPE_TITLE= 0;
    private static final int VIEW_TYPE_ITEM = 1;
    int IS_TITLE_OR_NOT =1;
    int MESSAGE = 2;

    int key=3;
    int title=4;

    int ColumnNum;
    List<Map<Integer, String>> mData;
    ActivityResultLauncher<Intent> launcher;

    public GalleryRecyclerViewAdapter(Activity context , List<Map<Integer, String>> mData , int ColumnNum,ActivityResultLauncher<Intent> launcher ) {
        this.mContext=context;
        this.ColumnNum=ColumnNum;
        this.mData= mData;
        this.launcher=launcher;
    }

    @Override
    public GalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder  vh = null;
        mInflater = LayoutInflater.from(mContext);
        //Determine the view type to return a different Viewholder
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                vh = new HolderOne(mInflater.inflate(R.layout.gallery_recyclerview_title, parent, false));
                break;
            case VIEW_TYPE_ITEM:
                vh = new HolderTwo(mInflater.inflate(R.layout.recyclerview_item, parent,false));
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if("true".equals(mData.get(position).get(IS_TITLE_OR_NOT))){
            holder.mTitle.setText(mData.get(position).get(MESSAGE));
        }else {
            Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).get(MESSAGE));
            //Set Bitmap to imageview
            holder.mImageView.setImageBitmap(bitmap);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, MapDetailActivity.class);//MapActivity

                    intent.putExtra("title",mData.get(position).get(title));
                    intent.putExtra("key",mData.get(position).get(key));
                    launcher.launch(intent);



                }
            });
        }

    }



    //Determines the style of the RecyclerView's child items, returning an int value indicating
    @Override
    public int getItemViewType(int position) {
        if ("true".equals(mData.get(position).get(IS_TITLE_OR_NOT))) {
            return VIEW_TYPE_TITLE;
        }
        return VIEW_TYPE_ITEM;
    }

    //determine whether it is a title, if it is, then the title fills all the sub-items of a line,
    // if it is an item, then it fills a sub-item
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //If it's a title, it takes up 2 cells
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if("false".equals(mData.get(position).get(IS_TITLE_OR_NOT))){
                    return 1;
                }else {
                    return ColumnNum;
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position , List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        } else {
            onBindViewHolder(holder,position);
        }
    }

    //initialise for different layouts of sub-items
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImageView;
        public  View mView;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public class HolderOne extends ViewHolder {

        public HolderOne(View viewHolder) {
            super(viewHolder);
            mTitle= (TextView) viewHolder.findViewById(R.id.title);
        }
    }

    public class HolderTwo extends ViewHolder{

        public HolderTwo(final View viewHolder) {
            super(viewHolder);
            mView=viewHolder;
            mImageView =(ImageView) viewHolder.findViewById(R.id.image);
        }
    }
}