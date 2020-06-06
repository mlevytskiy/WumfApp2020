package com.core.wumfapp2020

import com.library.core.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun getNavRes(): Int {
        return R.id.main_nav_host
    }

}
