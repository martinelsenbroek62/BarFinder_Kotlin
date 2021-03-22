package com.lemust.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ofog.promoter.common.base.OnBackPressedListener

    
abstract class BaseFragment : Fragment(), OnBackPressedListener {

    override fun onBackPressed(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    fun putFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.putFragment(fragment)
    }

    fun addFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.addFragment(fragment)
    }


    fun putFragmentForSetNewStackAfter(fragment: BaseFragment) {
        (activity as? BaseActivity)?.putFragmentForSetNewStackAfter(fragment)
    }

    fun setFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.setFragment(fragment)
    }

    fun replaceFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.replaceFragment(fragment)
    }

    fun putRootFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.putRootFragment(fragment)
    }

    fun popFragment() {
        (activity as? BaseActivity)?.onBackPressed()
    }

    fun popFragment(count: Int) {
        (activity as? BaseActivity)?.popFragment(count)
    }

    fun popFragment(tag: String, invalidate: Boolean = false) {
        (activity as? BaseActivity)?.popFragment(tag, invalidate)
    }

    fun popBackStack() {
        (activity as? BaseActivity)?.popBackStack()
    }

    fun setNewStack(rootFragment: BaseFragment, fragments: List<BaseFragment>) {
        (activity as? BaseActivity)?.setNewStack(rootFragment, fragments)
    }


}