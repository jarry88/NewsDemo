package com.example.newsdemoapplication.ui.dashboard;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.NewsVo;
import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.Util;
import com.example.newsdemoapplication.adapter.ListDragAdapter;
import com.example.newsdemoapplication.adapter.NewsContentAdapter;
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack;
import com.example.newsdemoapplication.callback.OnItemClickListener;
import com.example.newsdemoapplication.ui.home.HomeViewModel;
import com.example.newsdemoapplication.util.CommonUtils;
import com.example.newsdemoapplication.view.DragRecycleView;
import com.example.newsdemoapplication.view.DragView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.newsdemoapplication.Util.getData;

public class DashboardFragment extends Fragment  {
    final int SINGLE_LINE =0;
    final int HALF_EXPANDED=1;
    final int COMPLETE_EXPANDED=2;

    final int ColumnNum =4;

    private HomeViewModel homeViewModel;
    ImageView btnExpand;
    ImageView btnFold;
    DragRecycleView mTitleRecycleView;

    GridLayoutManager titleLayoutManager ;
    ListDragAdapter titleAdapter;
    RecyclerView mContentRecycleView;
    BottomSheetDialog bottomSheetDialog;
    //标题栏是否支持局部展开
    private boolean supportHalfExpand=false;
    private int currSelectedPosition;
    private int titleMaxHeight;//像素

    private int currTitleState;
    List<NewsVo> mData =new ArrayList<>();
    List<String> list =new ArrayList<>();
    List<Integer> refreshList =new ArrayList<>();
    private DashboardViewModel dashboardViewModel;
    ItemTouchHelper mTouchHelper ;
    DragView dragView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleMaxHeight= CommonUtils.dp2px(getActivity(),200);
        list =getData(47);
        dragView =view.findViewById(R.id.rv_drag);
        dragView.addAll(getData(47));
        initTitleRecycleView();
        initBottomSheetDiaLog();
    }

    //初始化顶部标题栏
    private void initTitleRecycleView() {
        mTitleRecycleView =getView().findViewById(R.id.rv_title);
        titleLayoutManager =new GridLayoutManager(requireContext(),ColumnNum*3);
        titleLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int end=list.size()%ColumnNum;
                if(list.size()-position<=end)
                    return ColumnNum*3/end;
                else
                    return 3;
            }
        });

        titleAdapter =new ListDragAdapter(getActivity(),list);
        titleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currSelectedPosition =position;
                titleAdapter.setCurrSelectPosition(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mTitleRecycleView.setAdapter(titleAdapter);
        mTitleRecycleView.setLayoutManager(titleLayoutManager);

        mTouchHelper = new ItemTouchHelper(new ToucheCallBack(new ItemHelper() {
            @Override
            public void itemMoved(int oldPosition, int newPosition) {

                Util.Loge("move");
                //交换变换位置的集合数据
                Collections.swap(titleAdapter.getData(), oldPosition, newPosition);
                titleAdapter.notifyItemMoved(oldPosition, newPosition);
                currSelectedPosition =newPosition;
                boolean addOld=true,addNew =true;
                for(int i =0;i<refreshList.size();i++){
                    if(oldPosition ==refreshList.get(i)) addOld =false;
                    if(newPosition ==refreshList.get(i)) addNew =false;
                }
                if(addNew) refreshList.add(newPosition);
                if(addOld) refreshList.add(oldPosition);
//                titleAdapter.setCurrSelectPosition(currSelectedPosition);
            }

            @Override
            public void itemDismiss(int position) {

            }

            @Override
            public void itemSelected(int position) {
                if(position<0) return;

                if(position!=currSelectedPosition){
                    currSelectedPosition =position;
                }
                Vibrator vib = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(150);
                // 记录弹窗信息
//                mTitleRecycleView.setCount(1);
            }

            @Override
            public void itemClear(int position) {

                if(position!=currSelectedPosition){
                    currSelectedPosition=position;
                }
//                for(int i =0;i<refreshList.size();i++){
//                    titleAdapter.notifyItemChanged(refreshList.get(i));
//                }
                Log.e("TAG", "itemClear: 刷新");
                titleAdapter.notifyDataSetChanged();

                refreshList.clear();

            }
        }));
        //attach到RecyclerView中
        mTouchHelper.attachToRecyclerView(mTitleRecycleView);


    }
    //
    private void initBottomSheetDiaLog() {
        bottomSheetDialog =new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.botton_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); }
        );
        bottomSheetDialog.findViewById(R.id.tv_rename).setOnClickListener(v -> {
                    Log.e("TAG", "initBottomSheetDiaLog: "+currSelectedPosition );
                    bottomSheetDialog.dismiss();
                }
        );
        bottomSheetDialog.findViewById(R.id.tv_delete).setOnClickListener(v -> {
            Log.e("TAG", "initBottomSheetDiaLog: "+currSelectedPosition );
            bottomSheetDialog.dismiss(); }
        );
    }
}