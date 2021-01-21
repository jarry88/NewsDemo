package com.gzp.baselib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gzp.baselib.R
import com.lishuaihua.materialdialogs.MaterialDialog
import com.lishuaihua.materialdialogs.customview.customView
import com.lishuaihua.materialdialogs.lifecycle.lifecycleOwner
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<U : BaseViewModel>() : Fragment() {
    protected lateinit var vm: U


    /**
     * Initialize view model according to the generic class type. Override this method to
     * add your owen implementation.
     * @return the view model instance.
     */
    protected fun createViewModel(): U {
        val vmClass: Class<U> = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<U>
        return  ViewModelProvider(activity!!, ViewModelProvider.NewInstanceFactory()).get(vmClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        vm = createViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResId = getLayoutResId()
        require(layoutResId > 0) { "The subclass must provider a valid layout resources id." }
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doCreateView(savedInstanceState)
        doObservable()
    }
    protected open fun doObservable(){
    }
    /**
     * Get the layout resource id from subclass.
     *
     * @return layout resource id.
     */
    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    /**
     * Do create view business.
     *
     * @param savedInstanceState the saved instance state.
     */
    protected abstract fun doCreateView(savedInstanceState: Bundle?)

    private var dialog: MaterialDialog? = null

    protected open fun showLoading() {
        showLoading(context!!.getString(R.string.loading))
    }
    protected open fun showLoading(meesage:String) {
        if (dialog == null) {
            var view = LayoutInflater.from(context).inflate(R.layout.layout_custom_progress_dialog_view, null)
            view.findViewById<TextView>(R.id.tv_loading_message).setText(meesage)
            dialog = requireActivity().let {
                MaterialDialog(it)
                        .cancelable(false)
                        .cancelOnTouchOutside(false)
                        .customView(R.layout.layout_custom_progress_dialog_view)
                        .lifecycleOwner(this)
            }
        }
        if (!requireActivity().isFinishing()) {
            dialog?.show()
        }
    }


    protected open fun hideLoading() {
        if (!requireActivity().isFinishing()) {
            dialog?.dismiss()
        }
    }

    override fun onDestroy() {
        if (!requireActivity().isFinishing()) {
            dialog?.dismiss()
        }
        super.onDestroy()
    }

}