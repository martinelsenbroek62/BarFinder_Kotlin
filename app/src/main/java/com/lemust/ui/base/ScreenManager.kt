package com.lemust.ui.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.ofog.promoter.common.base.OnBackPressedListener
import com.ofog.promoter.common.base.OnInvalidateSelfListener


class ScreenManager(private val manager: FragmentManager) {

    fun putRootFragment(fragment: Fragment, container: Int) {
        val tag = generateFragmentTag(fragment)
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val transaction = manager.beginTransaction()
        transaction.replace(container, fragment, tag).commit()
        manager.executePendingTransactions()
    }

    fun setFragment(fragment: Fragment?, container: Int) {
        fragment?.also {
            val tag = generateFragmentTag(fragment)
            manager.beginTransaction().replace(container, fragment, tag).commit()
        }
    }

    fun setNewStack(rootFragment: Fragment, fragments: List<Fragment>, container: Int) {
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val tag = generateFragmentTag(rootFragment)
        manager.beginTransaction().replace(container, rootFragment, tag).commit()

        for (fragment in fragments) {
            val tag = generateFragmentTag(fragment)
            manager.beginTransaction().replace(container, fragment, tag).addToBackStack(tag).commit()
        }
        manager.executePendingTransactions()
    }

    fun putFragment(fragment: Fragment, animSet: Array<Int>?, container: Int) {
        val tag = generateFragmentTag(fragment)
        val transaction = manager.beginTransaction()
        if (animSet != null)
            transaction.setCustomAnimations(animSet[0], animSet[1], animSet[2], animSet[3])

        transaction.replace(container, fragment, tag).addToBackStack(tag).commit()
    }


    fun addFragment(fragment: Fragment, animSet: Array<Int>?, container: Int) {
        val tag = generateFragmentTag(fragment)
        val transaction = manager.beginTransaction()
        if (animSet != null)
            transaction.setCustomAnimations(animSet[0], animSet[1], animSet[2], animSet[3])

        transaction.add(container, fragment, tag).addToBackStack(tag).commit()
    }

    fun replaceFragment(fragment: Fragment, container: Int) {
        manager.popBackStack()
        val tag = generateFragmentTag(fragment)
        val transaction = manager.beginTransaction()
        transaction.replace(container, fragment, tag).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
    }

    fun putFragments(container: Int, vararg fragments: Fragment) {
        val transaction = manager.beginTransaction()
        for (fragment in fragments) {
            val tag = generateFragmentTag(fragment)
            transaction.replace(container, fragment, tag).addToBackStack(tag)
        }
        transaction.commit()
        manager.executePendingTransactions()
    }

    fun popFragment() {
        if (hasPoppedFragments()) manager.popBackStack()
    }

    fun popFragment(count: Int) {
        if (count < 1) throw IllegalArgumentException()
        if (manager.backStackEntryCount >= count) {
            val tag = manager.getBackStackEntryAt(manager.backStackEntryCount - count).name
            manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else popFragment()
    }

    fun popFragment(tag: String, invalidate: Boolean) {
        for (index in manager.backStackEntryCount - 1 downTo 0) {
            val name = manager.getBackStackEntryAt(index).name
            if (name.contains(tag)) {
                if (invalidate) (manager.findFragmentByTag(name) as? OnInvalidateSelfListener)?.onInvalidateSelf()
                manager.popBackStack(name, 0)
                return
            }
        }
        popBackStack()
    }

    fun popBackStack() {
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        manager.executePendingTransactions()
    }

    fun hasPoppedFragments(): Boolean {
        return manager.backStackEntryCount > 0
    }

    fun performOnBackPressed(): Boolean {
        if (manager.backStackEntryCount > 0) {
            val tag = manager.getBackStackEntryAt(manager.backStackEntryCount - 1).name
            val topFragment = tag?.let { manager.findFragmentByTag(it) }
            (topFragment as? OnBackPressedListener)?.also { return it.onBackPressed() }
        }
        return false
    }

    private fun generateFragmentTag(fragment: Fragment): String {
        return "${fragment.javaClass.name}${'$'}${System.currentTimeMillis()}"
    }

}