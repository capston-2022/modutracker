package com.example.modutracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Choreographer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.modutracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //jwt 받아오기
        var getIntent = getIntent()
        var jwt = getIntent.getStringExtra("jwt")

//viewPager
        val list= listOf(CalendarFragment(jwt.toString()),DiaryFragment(jwt.toString()),SettingFragment(jwt.toString()))

        val pagerAdapter=FragmentPagerAdapter(list,this)

        binding.viewPager.adapter=pagerAdapter
//tablayout
        val titles= listOf("Calendar","Diary","Setting")
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text=titles.get(position)
        }.attach()
    }
}


class FragmentPagerAdapter(val fragmentList:List<Fragment> ,FragmentActivity:FragmentActivity)
    :FragmentStateAdapter(FragmentActivity){
    override fun getItemCount() = fragmentList.size
    override fun createFragment(position: Int) = fragmentList.get(position)
}
