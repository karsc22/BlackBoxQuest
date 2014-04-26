package com.kargames.souls.screen;

import com.kargames.souls.App;
import com.kargames.souls.widget.FillTable;

public class CreditsScreen extends BaseScreen{

	public CreditsScreen(App app) {
		super(app);
		FillTable table = new FillTable();
		table.setSkin(app.skin);
		
	}
}
