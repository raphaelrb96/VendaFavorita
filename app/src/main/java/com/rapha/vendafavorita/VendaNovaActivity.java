package com.rapha.vendafavorita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public class VendaNovaActivity extends AppCompatActivity {

    private AdView mAdView;
    private LinearLayout container_ad_venda_nova;
    private ImageView img_venda_nova;
    private RewardedAd rewardedAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_nova);

        findViews();
        ativeAds();
        showContent();
    }

    private void findViews() {
        mAdView = (AdView) findViewById(R.id.adView_venda_nova);
        container_ad_venda_nova = (LinearLayout) findViewById(R.id.container_ad_venda_nova);
        img_venda_nova = (ImageView) findViewById(R.id.img_venda_nova);
    }
    private void showContent() {
        Glide.with(this).asGif().diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(R.drawable.vendanova).into(img_venda_nova);
    }
    public void voltar(View view) {
        if(rewardedAd != null) {
            listenerRewarded();
            return;
        }
        onBackPressed();
        finish();
    }

    public void voltarProInicio(View view) {
        if(rewardedAd != null) {
            listenerRewarded();
            return;
        }

        startMainActivity();

    }

    public void listenerRewarded() {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                rewardedAd = null;
            }
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                rewardedAd = null;
            }
        });
        rewardedAd.show(this, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                //startMainActivity();
                rewardedAd = null;
            }
        });
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void detalhesDoPedido(View view) {
        Intent intent = new Intent(this, HistoricoRevendasActivity.class);
        startActivity(intent);
        finish();
    }

    private void ativeAds() {

        MobileAds.initialize(this, initializationStatus -> {});


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                container_ad_venda_nova.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@androidx.annotation.NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //Log.d("ADSTESTE", "initializationStatus: " + loadAdError.getMessage());
                container_ad_venda_nova.setVisibility(View.GONE);
            }
        });

        AdRequest adVideoRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-8108209584809121/7084212664", adVideoRequest, new RewardedAdLoadCallback() {
            @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        listenerRewarded();
                    }
                });

    }

}