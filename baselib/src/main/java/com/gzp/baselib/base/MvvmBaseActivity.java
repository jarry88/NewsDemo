package com.gzp.baselib.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class MvvmBaseActivity<U extends BaseViewModel, T extends ViewDataBinding> extends BaseActivity<U> {

    private T binding;

    @Override
    protected void setupContentView(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutResId(), null, false);
        setContentView(binding.getRoot());
    }

    public T getBinding() {
        return binding;
    }
}
