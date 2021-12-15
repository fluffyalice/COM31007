package uk.ac.shef.oak.com4510.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.shef.oak.com4510.databinding.FragmentDashboardBinding;
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBinding;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.mydatabase.CacheEntity;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyImage;
import uk.ac.shef.oak.com4510.ui.history.HistoryFragment;
import uk.ac.shef.oak.com4510.ui.history.HistoryViewModel;
import uk.ac.shef.oak.com4510.ui.recyclerview.GalleryRecyclerViewAdapter;
import uk.ac.shef.oak.com4510.ui.recyclerview.RecyclerViewAdapter;

public class GalleryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FragmentDashboardBinding binding;

    //    RecyclerView mRecyclerView ;
    LinearLayoutManager mLayoutManager;
    GalleryRecyclerViewAdapter mRecyclerViewAdapter;

    int IS_TITLE_OR_NOT =1;
    int MESSAGE = 2;

    int key=3;
    int title=4;

    List<Map<Integer, String>> mData =new ArrayList<>();
    Map<Integer, String> map = new HashMap<Integer, String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initUi();
        return view;
    }

    private void initUi() {
        findView();
        init();
        mRecyclerViewAdapter = new GalleryRecyclerViewAdapter(this.getActivity(), mData, 3,launcher);
        binding.recyclerView.setAdapter(mRecyclerViewAdapter);

    }

    void findView(){
        //2 means the column number is 2
        mLayoutManager = new GridLayoutManager(this.getContext(), 3, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.addItemDecoration(new GalleryFragment.SpaceItemDecoration(12));//distance between items
        binding.recyclerView.setLayoutManager(mLayoutManager);
    }

    void init(){

        CacheEntity[] ces= CacheService.getAll();
        mData.clear();
        for(int n=0;n<ces.length;n++)
        {

            List<MyImage> myImageLists= ces[n].getImagebean();
            if(myImageLists!=null&&myImageLists.size()>0)
            {
                map = new HashMap<Integer, String>();
                map.put(IS_TITLE_OR_NOT , "false");
                map.put(MESSAGE , myImageLists.get(0).imageUrl);

                map.put(key , ces[n].key);
                map.put(title , ces[n].title);
                mData.add(map);
            }



            //Initialise titles
            map = new HashMap<Integer, String>();
            map.put(IS_TITLE_OR_NOT , "true");
            SimpleDateFormat dateFormat = null;
            String titleDate="";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("EEE, d MMM yyyy . h:m:s", Locale.ENGLISH);
                titleDate = dateFormat.format(new Date(ces[n].startTime));

                map.put(MESSAGE ,ces[n].title+"\n"+ myImageLists.size()+" photos "+titleDate);
                mData.add(map);
            }

        }


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        init();
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            });

    //Set spacing between items in RecyclerView
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildViewHolder(view).getItemViewType() == 0) {
                outRect.bottom = 0;
                outRect.top = space / 3;
            } else {
                outRect.bottom = space;
                outRect.top = space;
            }
            outRect.right = space;
            outRect.left = space;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}