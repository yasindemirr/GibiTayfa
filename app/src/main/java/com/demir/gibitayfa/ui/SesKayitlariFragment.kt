package com.demir.gibitayfa.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.demir.gibitayfa.R
import com.demir.gibitayfa.adapter.GibiAdapter
import com.demir.gibitayfa.databinding.FragmentSesKayitlariBinding
import com.demir.gibitayfa.model.gibiModel
import com.demir.gibitayfa.util.Resource
import com.demir.gibitayfa.viewmodel.RecordViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class SesKayitlariFragment : Fragment() {
    private lateinit var binding: FragmentSesKayitlariBinding
    private lateinit var viewModel: RecordViewModel
    private lateinit var gibiAdapter:GibiAdapter
    private var mInterstitialAd: InterstitialAd? = null
    private var addcount=0
    private val arg by navArgs<SesKayitlariFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSesKayitlariBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireContext()) {
            loadAdd()
        }
        viewModel= ViewModelProvider(this)[RecordViewModel::class.java]
        setAdapter()
        val owner=arg.owner
        when(owner){
            "yilmaz"->{
                binding.ownerImage.setImageResource(R.drawable.yilmaz_yeni)
                binding.userWords.setText("Yılmaz'ın sözleri")
                lifecycleScope.launchWhenStarted {
                    viewModel.yilmaz.collectLatest{
                        when(it){
                            is Resource.Success->{
                                gibiAdapter.differ.submitList(it.data)
                                binding.progresss.visibility=View.GONE
                            }
                            is Resource.Error->{
                                binding.progresss.visibility=View.GONE
                                Log.d(TAG,it.message.toString())

                            }
                            is Resource.loading-> {
                                binding.progresss.visibility=View.VISIBLE
                            }
                            else ->Unit

                        }
                    }
                }
            }
            "ilkkan"->{
                binding.ownerImage.setImageResource(R.drawable.ilkkan_yeni)
                binding.userWords.setText("İlkkan'ın sözleri")
                lifecycleScope.launchWhenStarted {
                    viewModel.ilkkan.collectLatest{
                        when(it){
                            is Resource.Success->{
                                gibiAdapter.differ.submitList(it.data)
                                binding.progresss.visibility=View.GONE
                            }
                            is Resource.Error->{
                                binding.progresss.visibility=View.GONE
                                Log.d(TAG,it.message.toString())

                            }
                            is Resource.loading-> {
                                binding.progresss.visibility=View.VISIBLE

                            }
                            else ->Unit

                        }
                    }
                }
            }
            "ersoy"->{
                binding.ownerImage.setImageResource(R.drawable.ersoy)
                binding.userWords.setText("Ersoy'ın sözleri")
                lifecycleScope.launchWhenStarted {
                    viewModel.ersoy.collectLatest{
                        when(it){
                            is Resource.Success->{
                                gibiAdapter.differ.submitList(it.data)
                                binding.progresss.visibility=View.GONE
                            }
                            is Resource.Error->{
                                binding.progresss.visibility=View.GONE
                                Log.d(TAG,it.message.toString())

                            }
                            is Resource.loading-> {
                                binding.progresss.visibility=View.VISIBLE

                            }
                            else ->Unit

                        }
                    }
                }
            }
            else-> Unit

        }
    gibiAdapter.onlickShare={
        addcount+=1
        showAdd(it)

    }
    }
    private fun showAdd(gibi:gibiModel) {
        if (mInterstitialAd != null) {
            if (addcount % 3 == 0) {
                mInterstitialAd?.show(requireActivity())
            }else{
                shareLink(gibi.url,gibi.title)
            }
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    loadAdd()
                    shareLink(gibi.url,gibi.title)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    mInterstitialAd = null
                    loadAdd()
                    shareLink(gibi.url,gibi.title)
                }
            }

            }else{
            shareLink(gibi.url,gibi.title)
        }
    }
    private fun loadAdd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-6081284993501190/3618873660", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }
    private fun shareLink(url:String,title:String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gibitayfa:${title}")
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, "Gibitayfa"))
    }

    private fun setAdapter() {
        gibiAdapter= GibiAdapter()
        binding.gibiRec.apply {
            this.adapter=gibiAdapter
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        gibiAdapter.mediaPlayer?.stop()

    }

}