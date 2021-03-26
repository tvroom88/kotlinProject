package com.kotlinapplications.MyGallery


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    // 뷰페이저가 표시할 프래그먼트 목록
    private val items = ArrayList<Fragment>()

    // position 위치의 프래그먼트
    override fun createFragment(position: Int): Fragment {
        val n : Fragment= (items[position] ?:  null) as Fragment
        return n

    }

    // 아이템의 갯수
    override fun getItemCount(): Int {
        return items.size
    }

    // 아이템 갱신
    fun updateFragments(items: List<Fragment>) {
        this.items.addAll(items)
    }
}
