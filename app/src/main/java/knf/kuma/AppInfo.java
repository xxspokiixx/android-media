package knf.kuma;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import knf.kuma.changelog.ChangelogActivity;
import knf.kuma.commons.EAHelper;
import xdroid.toaster.Toaster;

/**
 * Created by jordy on 05/03/2018.
 */

public class AppInfo extends MaterialAboutActivity {
    public static void open(Context context) {
        context.startActivity(new Intent(context, AppInfo.class));
    }

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        MaterialAboutCard.Builder infoCard = new MaterialAboutCard.Builder();
        infoCard.addItem(ConvenienceBuilder.createAppTitleItem(this));
        infoCard.addItem(ConvenienceBuilder.createVersionActionItem(this, getDrawable(R.drawable.ic_version), "Versión", true));
        infoCard.addItem(new MaterialAboutActionItem.Builder().text("Changelog").icon(R.drawable.ic_changelog_get).setOnClickAction(new MaterialAboutItemOnClickAction() {
            @Override
            public void onClick() {
                ChangelogActivity.open(AppInfo.this);
            }
        }).build());
        MaterialAboutCard.Builder authorCard = new MaterialAboutCard.Builder();
        authorCard.title("Autor");
        authorCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_author), "Spokes", true, Uri.parse("https://t.me/UnbarredStream")));
        MaterialAboutCard.Builder donateCard = new MaterialAboutCard.Builder();
        donateCard.title("Donar");
        donateCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_paypal), "Paypal", false, getPaypalUri()));
        donateCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_patreon), "Patreon", false, Uri.parse("https://www.patreon.com/animeflvapp")));
        MaterialAboutCard.Builder extraCard = new MaterialAboutCard.Builder();
        extraCard.title("Extras");
        extraCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_web), "Página web", true, Uri.parse("http://spokes.ga")));
        extraCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_github), "Proyecto en github", true, Uri.parse("https://github.com/jordyamc/spokes")));
        extraCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_facebook), "Facebook", true, Uri.parse("https://www.facebook.com/spokesapp")));
        extraCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_discord), "Discord", false, Uri.parse("https://discord.gg/6hzpua6")));
        extraCard.addItem(ConvenienceBuilder.createWebsiteActionItem(AppInfo.this, getDrawable(R.drawable.ic_beta), "Grupo Beta", false, Uri.parse("https://t.me/joinchat/A3tvqEKOzGVyaZhQPc14_Q")));
        final String ea = EAHelper.getEAMessage(this);
        if (ea != null)
            extraCard.addItem(new MaterialAboutActionItem.Builder().text("Easter egg").icon(R.drawable.ic_egg).setOnClickAction(new MaterialAboutItemOnClickAction() {
                @Override
                public void onClick() {
                    Toaster.toastLong(ea);
                }
            }).build());
        return new MaterialAboutList.Builder()
                .addCard(infoCard.build())
                .addCard(authorCard.build())
                .addCard(donateCard.build())
                .addCard(extraCard.build())
                .build();
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return "Acerca de";
    }

    public Uri getPaypalUri() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
        uriBuilder.appendQueryParameter("cmd", "_donations");
        uriBuilder.appendQueryParameter("business", "jordyamc@hotmail.com");
        uriBuilder.appendQueryParameter("lc", "US");
        uriBuilder.appendQueryParameter("item_name", "Donación");
        uriBuilder.appendQueryParameter("no_note", "1");
        uriBuilder.appendQueryParameter("no_shipping", "1");
        uriBuilder.appendQueryParameter("currency_code", "USD");
        return uriBuilder.build();
    }
}
