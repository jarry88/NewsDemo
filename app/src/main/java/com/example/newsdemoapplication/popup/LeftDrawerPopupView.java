package com.example.newsdemoapplication.popup;

import android.content.Context;
import android.icu.text.CaseMap;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.view.ChapterDragView;
import com.example.newsdemoapplication.view.DragView;
import com.example.newsdemoapplication.vo.ChapterVo;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.core.DrawerPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 自定义带列表的Drawer弹窗
 * Create by dance, at 2019/1/9
 */
public class LeftDrawerPopupView extends DrawerPopupView {
    DragView dragView;
    private List<ChapterVo> list;
    private ChapterDragView chapterDragView;
    public LeftDrawerPopupView(@NonNull Context context) {
        super(context);
    }
    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;
    @Override
    protected int getImplLayoutId() {
        return R.layout.left_list_drawer;
    }
//    final ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onCreate() {
        dragView =findViewById(R.id.drag_view);
        dragView.setLineLayoutManager();
        List<String> listStr=new ArrayList<String>();
        for(ChapterVo chapterVo:list){
            listStr.add(chapterVo.getChapterName());
        }
        dragView.addAll(listStr);
        findViewById(R.id.textView).setOnClickListener(v -> {
            if(mOnClickListener!=null)
                mOnClickListener.onClick(v);
        });
        findViewById(R.id.btn_close_left).setOnClickListener(v -> dismiss());
        findViewById(R.id.btn_add).setOnClickListener(v -> {
            dismiss();
            if(mOnClickListener!=null)
                mOnClickListener.onClick(v);
        });

    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        if(mOnDismissListener!=null&&!list.isEmpty()){
            mOnDismissListener.dismiss(list.get(dragView.getSelectedPosition()).getId());
        }
    }

    public void setData(@NotNull List<ChapterVo> list) {
        this.list=list;
    }
    public void setmOnDismissListener(OnDismissListener onClickListener){
        mOnDismissListener=onClickListener;
    }
    public void setmOnClickListener(OnClickListener onClickListener){
        mOnClickListener=onClickListener;
    }
    public interface OnDismissListener{
        void dismiss(int position);
    }
}
