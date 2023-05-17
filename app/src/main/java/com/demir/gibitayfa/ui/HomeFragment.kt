package com.demir.gibitayfa.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.demir.gibitayfa.R
import com.demir.gibitayfa.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewList:List<View>
    private lateinit var ownerList:List<String>
    private var mInterstitialAd: InterstitialAd? = null
    private var addcount=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireContext()) {
            loadAdd()
        }
        viewList= listOf(binding.cons1,binding.cons2,binding.cons3)
        ownerList= listOf("yilmaz","ilkkan","ersoy")

        for (item in viewList){
            val i= viewList.indexOf(item)
            val bundle=Bundle().apply {
                putString("owner",ownerList[i])
            }
            item.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_sesKayitlariFragment,bundle)
                addcount+=1
                showAdd()

            }
        }

    }
    private fun showAdd() {
        if (mInterstitialAd != null) {
            if (addcount % 4 == 0) {
                mInterstitialAd?.show(requireActivity())
            }
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
}