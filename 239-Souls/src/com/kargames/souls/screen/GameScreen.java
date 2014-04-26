package com.kargames.souls.screen;

import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.kargames.souls.App;
import com.kargames.souls.Player;
import com.kargames.souls.util.Resource;
import com.kargames.souls.widget.FillTable;

public class GameScreen extends BaseScreen {

	ShapeRenderer sr;
	public static boolean drawDebug = true;
	Box2DDebugRenderer debugRenderer;
	World world;
	
	Stage gameStage;
	Player player;
	PositionalLight playerLight;
	
	RayHandler rayHandler;
	OrthographicCamera cam;
	

	Color topColor = new Color(0, 0.5f, 1f, 1f);
	Color botColor = new Color(0, 0, 1f, 1f);
	
	Label breathLabel;
	
	Resource breath;
	
	public GameScreen(App app) {
		super(app);
		sr = new ShapeRenderer();
		FillTable hud = new FillTable();
		hud.top();
		breathLabel = new Label("Breath: 0 / 0", app.skin);
		hud.add(breathLabel);
		stage.addActor(hud);
		
		breath = new Resource(15);
		breath.regen = -1;
		

		gameStage = new Stage();
		cam = (OrthographicCamera) gameStage.getCamera();

		
		gameStage.addListener(new InputListener(){
			@Override
			public boolean scrolled(InputEvent event, float x, float y,
					int amount) {
				cam.zoom += cam.zoom * (amount / 10f);
				return super.scrolled(event, x, y, amount);
			}

			@Override
			public boolean keyTyped(InputEvent event, char ch) {
				if (ch == 'c') {
					cam.zoom = 1;
				} 
				if (ch == ' ') {
					drawDebug = !drawDebug;
				}
				return super.keyTyped(event, ch);
			}
		});
		
		
	}
	
	Image image;
	
	@Override
	public void show() {
		super.show();
		player = new Player(app); 
//		gameStage.addActor(world);
		image = new Image(new Texture(Gdx.files.internal("levels/level1.png")));
		image.setSize(image.getWidth()/13.1f, image.getHeight()/13.1f);
		image.setPosition(-image.getWidth()/2, -image.getHeight());
//		image.setOrigin(image.getWidth()/2, image.getHeight());
		
		gameStage.addActor(player);
//		gameStage.addActor(image);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, gameStage));
		
		debugRenderer = new Box2DDebugRenderer();
		RubeSceneLoader loader = new RubeSceneLoader();
		
		RubeScene scene = loader.loadScene(Gdx.files.internal("levels/level1.json"));
		world = scene.getWorld();
		player.body = scene.getNamed(Body.class, "playerBody").first();
		scene.getImages();

		rayHandler = new RayHandler(world);
		playerLight = new PointLight(rayHandler, 2048);
//		PositionalLight pointSun = new PointLight(rayHandler, 2048);
//
//		Body sunBody = scene.getNamed(Body.class, "sun").first();
//		pointSun.setPosition(sunBody.getPosition());
//		pointSun.setColor(1, 1, 1, 1);
//		pointSun.setSoft(false);
//		pointSun.setDistance(50);
		

//		DirectionalLight light2 = new DirectionalLight(rayHandler, 256, new Color(1,1,1,0.5f), 2048);
//		Body light2Body = scene.getNamed(Body.class, "light2").first();
//		light2.setPosition(light2Body.getPosition());
		
	}
	
	@Override
	public void draw() {

//		cam.position.set(player.body.getPosition(), 0); 
		cam.position.set(player.getCenterX(), player.getCenterY(), 0); 
		cam.update();
		sr.begin(ShapeType.Filled);
		
		
		float depth = -player.getCenterY() / 70f;
		if (depth > 1) depth = 1;

		float blue = 1;
		botColor.set(0, 0, blue, 1);
		topColor.set(0, blue/2f, blue, 1);
		
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 
				botColor, botColor, topColor, topColor);
		sr.end();
		gameStage.getSpriteBatch().begin();
		image.draw(gameStage.getSpriteBatch(), 1);
		gameStage.getSpriteBatch().end();
		rayHandler.render();
//		sr.begin(ShapeType.Filled);
//		botColor.set(0, 0, blue, 1);
//		topColor.set(0, blue/2f, blue, 1);
//		Vector3 temp = new Vector3(player.body.getPosition(), 0);
//		cam.unproject(temp);
//		float y = temp.y;
//		System.out.println(y);
//		sr.rect(0, y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 
//				botColor, botColor, topColor, topColor);
//		sr.end();
		gameStage.draw();
		


		if (drawDebug) {
			debugRenderer.render(world, gameStage.getCamera().combined);
		}
		super.draw();
	}


	@Override
	public void update(float delta) {
		super.update(delta);
		
		world.step(1/60f, 8, 3);
		gameStage.act();
		rayHandler.setCombinedMatrix(gameStage.getCamera().combined);
		playerLight.setPosition(player.body.getPosition());
//		playerLight.setColor(0.5f, 0.7f, 1, 1);

		float depth = -player.getCenterY() / 150f;
		if (depth > 1) depth = 1;
		if (depth < 0) depth = 0;
		playerLight.setColor(1, 1, 1, 0.9f-depth);
//		playerLight.setColor(1, 1, 1, 0.9f);
		playerLight.setDistance((1-depth) * 25 + 20);
		playerLight.setSoft(true);
		playerLight.setSoftnessLength(5);
//		light.setSoftnessLenght(0.5f);
		playerLight.setStaticLight(false);
		rayHandler.update();
		
		if (player.getCenterY() > -0.5f){
			breath.regen = 2;
		} else {
			breath.regen = -1;
		}
		
		breath.update(delta);
		breathLabel.setText("Breath: " + breath.toString());
		
	}

	@Override
	public void resize(int width, int height) {
		gameStage.setViewport(16, 9, true);
		super.resize(width, height);
	}

}
