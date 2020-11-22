package com.example.newsdemoapplication.ui.notifications;

import android.annotation.SuppressLint;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.Util;
import com.example.newsdemoapplication.adapter.ListDragAdapter;
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack;
import com.example.newsdemoapplication.callback.OnItemClickListener;
import com.example.newsdemoapplication.ui.dashboard.ItemHelper;
import com.example.newsdemoapplication.view.DragRecycleView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;

import static com.example.newsdemoapplication.Util.getData;

public class NotificationsFragment extends Fragment {
    private NotificationsViewModel notificationsViewModel;
    GridLayoutManager mGridLayoutManager;
    ListDragAdapter listDragAdapter;
    RecyclerView mRecycleView;
    BottomSheetDialog bottomSheetDialog;

    private DragRecycleView dragRecycleView;
    private int currSelectedPosition;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView = view.findViewById(R.id.rv_list);
        listDragAdapter =new ListDragAdapter(getActivity(),getData(50));
        mRecycleView.setAdapter(listDragAdapter);
        mGridLayoutManager = new GridLayoutManager(getContext(), 4);
        dragRecycleView =view.findViewById(R.id.rv_drag);
        dragRecycleView.setAdapter(listDragAdapter);
        dragRecycleView.setLayoutManager(new GridLayoutManager(getContext(),4));
        mRecycleView.setLayoutManager(mGridLayoutManager);

        bottomSheetDialog =new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.botton_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); }
        );
        listDragAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currSelectedPosition = position;
                Util.Loge(String.format("%d",currSelectedPosition));
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallBack(new ItemHelper() {
            @Override
            public void itemMoved(int oldPosition, int newPosition) {
                Util.Loge("move");
                //交换变换位置的集合数据
                Collections.swap(listDragAdapter.getData(), oldPosition, newPosition);
                listDragAdapter.notifyItemMoved(oldPosition, newPosition);
            }

            @Override
            public void itemDismiss(int position) {

            }

            @Override
            public void itemSelected(int position) {
                Vibrator vib = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(150);
            }

            @Override
            public void itemClear(int position) {

            }
        }));
        //关联RecyclerView
//        itemTouchHelper.attachToRecyclerView(mRecycleView);
        itemTouchHelper.attachToRecyclerView(dragRecycleView);

    }
}