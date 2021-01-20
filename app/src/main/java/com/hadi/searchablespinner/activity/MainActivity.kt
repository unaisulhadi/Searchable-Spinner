package com.hadi.searchablespinner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hadi.searchablespinner.R
import com.hadi.searchablespinner.model.User
import com.hadi.searchablespinner.spinner.SpinnerClickListener
import com.hadi.searchablespinner.spinner.SpinnerDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.*

class MainActivity : AppCompatActivity() {

    var users = arrayListOf<User>()
    lateinit var spinnerDialog: SpinnerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        users.add(User("Ajin","ajin@gmail.com","23","9"))
        users.add(User("Ashik","ashik@gmail.com","24","9"))
        users.add(User("Jaseel","jaseel@gmail.com","23","9"))
        users.add(User("Arjun","arjun@gmail.com","24","8"))
        users.add(User("Hamnas","hamnas@gmail.com","25","7"))
        users.add(User("Ajith","ajith@gmail.com","24","9"))
        users.add(User("Athira","athira@gmail.com","25","8"))
        users.add(User("Sudheesh","sudheesh@gmail.com","28","9"))


        spinnerDialog = SpinnerDialog(this,users)
        spinnerDialog.setDialogTitle("Select or Search")
        spinnerDialog.setCancelTitle("Cancel")
        spinnerDialog.setCancellable(true)


        //Setting Values
//        spinnerDialog.setDialogBackgroundColor(R.color.md_red_700)
//        spinnerDialog.setCloseTextColor(R.color.md_white_1000)
//        spinnerDialog.setSearchItemTextColor(R.color.md_white_1000)
//        spinnerDialog.setDialogTitleColor(R.color.md_white_1000)
//        spinnerDialog.setListItemColor(R.color.md_white_1000)
//        spinnerDialog.setItemsDividerColor(R.color.md_black_1000)

        spinnerDialog.setOnSpinnerItemSelectListener(object : SpinnerClickListener {
            override fun onItemClick(item: User, position: Int) {
                tvSelected.text = "Name : ${item.name}\nEmail : ${item.email}\nScore : ${item.score}\nPosition : ${position}"
            }
        })

        btnShowSpinner.setOnClickListener {
            spinnerDialog.showSpinnerDialog()
        }
    }

}