package knf.kuma.pojos;

import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Recents {
    @Selector("ul.ListEpisodios.AX.Rows.A06.C04.D03 li:not(article)")
    public List<RecentObject.WebInfo> list;

}
