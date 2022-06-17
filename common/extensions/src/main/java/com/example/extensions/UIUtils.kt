package com.example.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

object UIUtils {

    fun TextView.networkCheckByUI(): Boolean = !this.isVisible

    fun ProgressBar.updateProgressBar(visible: Boolean) {
        when (visible) {
            true -> this.visibility = View.VISIBLE
            false -> this.visibility = View.INVISIBLE
        }
    }

    fun Fragment.closeNavigationViewOnBackPressed(drawerLayout: DrawerLayout) {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
            })
    }

    fun Fragment.getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun Fragment.getNavigationBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun View.addMargins(left: Int, top: Int, right: Int, bottom: Int) {
        this.apply {
            post {
                updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(left, top, right, bottom)
                }
            }
        }
    }
}