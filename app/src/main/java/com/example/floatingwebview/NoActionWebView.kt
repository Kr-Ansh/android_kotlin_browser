package com.example.floatingwebview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView

// Preserve text-selection handles while suppressing the floating action menu.
class NoActionWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    override fun startActionMode(callback: ActionMode.Callback?): ActionMode? {
        return super.startActionMode(wrapCallback(callback))
    }

    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && type == ActionMode.TYPE_FLOATING) {
            return super.startActionMode(wrapCallback(callback), type)
        }
        return super.startActionMode(callback, type)
    }

    private fun wrapCallback(callback: ActionMode.Callback?): ActionMode.Callback {
        return object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                callback?.onCreateActionMode(mode, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                callback?.onPrepareActionMode(mode, menu)
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                callback?.onDestroyActionMode(mode)
            }
        }
    }
}
