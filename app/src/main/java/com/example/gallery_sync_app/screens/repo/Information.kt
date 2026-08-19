package com.example.gallery_sync_app.screens.repo

import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.data.SongsList

class Information {
private var bio="I will say, the next record will probably be the most upfront, the most clear […] about what the story is. And it will be the last record of this story, and so it will, like, tie it all up. And in order to do that, I want to make sure that we’re very clear and explaining what’s going on. I think some of the hunches for where it’s going are probably correct and some of them are way off. This story has been written for a while, and […] I’ll be very proud [of its ending]. […] I am proud of it, and that is what it is."
private val songList=listOf<SongsList>(
        SongsList("payPhone", R.drawable.clancy, bio),
        SongsList("Chlorine", R.drawable.c, bio),
        SongsList("SelfCare", R.drawable.clancy, bio),
        SongsList("Dont Wanna Know", R.drawable.pp, bio),
        SongsList("Safarnama", R.drawable.t, bio),
        SongsList("Doomed", R.drawable.bmth, bio),
        SongsList("LunchBox_theme", R.drawable.pp, bio),
        SongsList("OldenStation", R.drawable.clancy, bio),

        )
    fun getList()=songList
}