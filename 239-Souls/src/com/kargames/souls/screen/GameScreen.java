package com.kargames.souls.screen;

import com.kargames.souls.App;
import com.kargames.souls.widget.FillTable;

public class GameScreen extends BaseScreen {

	public GameScreen(App app) {
		super(app);
		
		FillTable table = new FillTable();
		table.setSkin(app.skin);
		table.add("<Insert gameplay here>");
		stage.addActor(table);
	}

}
