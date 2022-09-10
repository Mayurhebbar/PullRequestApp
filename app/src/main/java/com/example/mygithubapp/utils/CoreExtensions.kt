package com.example.mygithubapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragmentWithBackStack(layoutId: Int, fragment: Fragment, tag: String?) {
    this.supportFragmentManager.beginTransaction().add(layoutId, fragment)
        .addToBackStack(tag).commitAllowingStateLoss()
}

fun Fragment.addFragmentBackStack(layoutId: Int, fragment: Fragment, tag: String?) {
    this.fragmentManager?.beginTransaction()?.replace(layoutId, fragment)?.addToBackStack(tag)
        ?.commit()
}