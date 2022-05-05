package com.example.extensions

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
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
}