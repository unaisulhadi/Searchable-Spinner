package com.hadi.searchablespinner.spinner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.hadi.searchablespinner.R
import com.hadi.searchablespinner.model.User
import java.util.*

class SpinnerDialog(activity: Activity, var items: ArrayList<User>) {

    var context: Activity = activity
    private var onSpinnerItemClick: SpinnerClickListener? = null
    private var alertDialog: AlertDialog? = null

    private var pos = 0
    private var style = 0

    private var dTitle = "Select"
    private var closeTitle = "Close"

    private var cancellable = false
    private var showKeyboard = false
    private var useContainsFilter: Boolean = false

    var titleColor: Int = 0
    var searchIconColor = 0
    var searchTextColor = 0
    var itemColor = 0
    var itemDividerColor = 0
    var closeColor = 0
    var contentBgColor = 0

    private fun initColor(context: Context) {
        titleColor = R.color.colorBlack
        searchIconColor =  R.color.colorBlack
        searchTextColor = R.color.colorBlack
        itemColor = R.color.colorBlack
        closeColor = R.color.colorBlack
        itemDividerColor = R.color.colorLightGray
        contentBgColor =  R.color.md_white_1000
    }

    init {
        initColor(context)
    }

    fun showSpinnerDialog() {
        val adb = AlertDialog.Builder(context)
        val v = context.layoutInflater.inflate(R.layout.dialog_layout, null)
        val mainView = v.findViewById<LinearLayout>(R.id.mainView) as LinearLayout
        val rippleViewClose = v.findViewById<View>(R.id.close) as TextView
        val title = v.findViewById<View>(R.id.spinerTitle) as TextView
        val searchIcon = v.findViewById<View>(R.id.searchIcon) as ImageView
        rippleViewClose.text = closeTitle
        title.text = dTitle
        val listView = v.findViewById<View>(R.id.list) as ListView
        val sage = ColorDrawable(itemDividerColor)
        listView.divider = sage
        listView.dividerHeight = 1
        val searchBox = v.findViewById<View>(R.id.searchBox) as EditText
        if (isShowKeyboard()) {
            showKeyboard(searchBox)
        }

        mainView.background.setColorFilter(Color.parseColor(context.getString(contentBgColor)), PorterDuff.Mode.SRC_ATOP)
        title.setTextColor(ContextCompat.getColor(context,titleColor))
        searchBox.setTextColor(ContextCompat.getColor(context,searchTextColor))
        rippleViewClose.setTextColor(ContextCompat.getColor(context,closeColor))
        searchIcon.setColorFilter(ContextCompat.getColor(context,searchIconColor),PorterDuff.Mode.SRC_IN)


//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.items_view, items);
        val adapter: SpinnerAdapter<User> =
            object : SpinnerAdapter<User>(context, R.layout.items_view, items) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val text1 = view.findViewById<TextView>(R.id.tvItem)
                    text1.setTextColor(ContextCompat.getColor(context,itemColor))
                    return view
                }
            }
        listView.adapter = adapter
        adb.setView(v)
        alertDialog = adb.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        alertDialog?.window?.attributes?.windowAnimations = style
        listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val t = view.findViewById<View>(R.id.tvItem) as TextView
            for (j in items.indices) {
                if (t.text.toString().equals(items[j].toString(), ignoreCase = true)) {
                    pos = j
                }
            }
            onSpinnerItemClick?.onItemClick(items[pos], pos)
            closeSpinnerDialog()
        }
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (isUseContainsFilter()) {
                    adapter.getContainsFilter(searchBox.text.toString())
                } else {
                    adapter.filter.filter(searchBox.text.toString())
                }
            }
        })
        rippleViewClose.setOnClickListener { closeSpinnerDialog() }
        alertDialog?.setCancelable(isCancellable())
        alertDialog?.setCanceledOnTouchOutside(isCancellable())
        alertDialog?.show()
    }

    fun setDialogStyle(style: Int) {
        this.style = style
    }

    fun setCancelTitle(closeTitle: String) {
        this.closeTitle = closeTitle
    }

    fun setDialogTitle(dialogTitle: String) {
        this.dTitle = dialogTitle
    }

    fun setOnSpinnerItemSelectListener(onSpinnerItemClick: SpinnerClickListener?) {
        this.onSpinnerItemClick = onSpinnerItemClick
    }

    private fun isCancellable(): Boolean {
        return cancellable
    }

    fun setCancellable(cancel: Boolean) {
        cancellable = cancel
    }

    private fun isShowKeyboard(): Boolean {
        return showKeyboard
    }

    private fun isUseContainsFilter(): Boolean {
        return useContainsFilter
    }


    fun setShowKeyboard(showKeyboard: Boolean) {
        this.showKeyboard = showKeyboard
    }

    fun useContainsFilter(containsFilter: Boolean) {
        this.useContainsFilter = containsFilter
    }

    fun setDialogTitleColor(titleColor: Int) {
        this.titleColor = titleColor
    }

    fun setSearchSearchIconColor(searchIconColor: Int) {
        this.searchIconColor = searchIconColor
    }

    fun setSearchItemTextColor(searchTextColor: Int) {
        this.searchTextColor = searchTextColor
    }

    fun setListItemColor(itemColor: Int) {
        this.itemColor = itemColor
    }

    fun setCloseTextColor(closeColor: Int) {
        this.closeColor = closeColor
    }

    fun setItemsDividerColor(itemDividerColor: Int) {
        this.itemDividerColor = itemDividerColor
    }

    fun setDialogBackgroundColor(bgColor:Int){
        this.contentBgColor = bgColor
    }

    private fun closeSpinnerDialog() {
        hideKeyboard()
        alertDialog?.dismiss()
    }

    private fun hideKeyboard() {
        try {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                context.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
        }
    }

    private fun showKeyboard(ettext: EditText) {
        ettext.requestFocus()
        ettext.postDelayed({
            val keyboard =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(ettext, 0)
        }, 200)
    }

    open class SpinnerAdapter<S>(
        context: Activity, items_view: Int, items: ArrayList<User>
    ) : ArrayAdapter<User>(context, items_view, items) {
        private var items: MutableList<User>? = null
        private var arraylist: ArrayList<User>

        // Filter Class
        fun getContainsFilter(charText: String) {
            var filterText = charText.toLowerCase(Locale.getDefault())
            items?.clear()
            if (filterText.isEmpty()) {
                items?.addAll(arraylist)
            } else {
                for (item in arraylist) {
                    if (item.name.toLowerCase(Locale.getDefault()).contains(filterText)) {
                        items?.add(item)
                    }
                }
            }
            notifyDataSetChanged()
        }

        init {
            this.items = items
            arraylist = ArrayList()
            arraylist.addAll(items)
        }
    }


}

interface SpinnerClickListener {
    fun onItemClick(item: User, position: Int)
}