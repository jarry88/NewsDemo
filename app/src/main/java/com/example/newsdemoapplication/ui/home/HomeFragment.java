package com.example.newsdemoapplication.ui.home;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.NewsVo;
import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.Util;
import com.example.newsdemoapplication.adapter.ListDragAdapter;
import com.example.newsdemoapplication.adapter.NewsContentAdapter;
import com.example.newsdemoapplication.callback.OnItemClickListener;
import com.example.newsdemoapplication.ui.dashboard.ItemHelper;
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack;
import com.example.newsdemoapplication.util.CommonUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.newsdemoapplication.Util.getData;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ImageView btnExpand;
    ImageView btnFold;
    RecyclerView mTitleRecycleView;

    GridLayoutManager titleLayoutManager ;
    ListDragAdapter titleAdapter;
    RecyclerView mContentRecycleView;
    BottomSheetDialog bottomSheetDialog;
    //标题栏是否支持局部展开
    private boolean supportHalfExpand=false;
    private int currSelectedPosition;
    private int titleMaxHeight;//像素


    List<NewsVo> mData =new ArrayList<>();
    List<String> list=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.btn_login);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        titleMaxHeight=CommonUtils.dp2px(getActivity(),200);
        list =getData(45);
        initTopContainer();
        initTitleRecycleView();
        initContentRecycleView();
        initBottomSheetDiaLog();
    }

    private void initTopContainer() {
        btnExpand =getView().findViewById(R.id.btn_expand);
        btnFold =getView().findViewById(R.id.btn_expand);
    }


    private void initContentRecycleView() {
        mContentRecycleView = getView().findViewById(R.id.rv_content_list);
        mContentRecycleView.setAdapter(new NewsContentAdapter(getContext(),list));
    }
    private void initBottomSheetDiaLog() {
        bottomSheetDialog =new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.botton_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); }
        );
    }

    private void initTitleRecycleView() {
        mTitleRecycleView =getView().findViewById(R.id.rv_title);
        titleLayoutManager =new GridLayoutManager(requireContext(),4);
        titleAdapter =new ListDragAdapter(getActivity(),list);
        titleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currSelectedPosition = position;
                Util.Loge(String.format("%d",currSelectedPosition));
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mTitleRecycleView.setLayoutManager(titleLayoutManager);
        mTitleRecycleView.setAdapter(titleAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallBack(new ItemHelper() {
            @Override
            public void itemMoved(int oldPosition, int newPosition) {
                Util.Loge("move");
                //交换变换位置的集合数据
                Collections.swap(titleAdapter.getData(), oldPosition, newPosition);
                titleAdapter.notifyItemMoved(oldPosition, newPosition);
            }

            @Override
            public void itemDismiss(int position) {

            }

            @Override
            public void itemSelected() {
                Vibrator vib = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(150);
            }
        }));
        itemTouchHelper.attachToRecyclerView(mTitleRecycleView);
    }
}