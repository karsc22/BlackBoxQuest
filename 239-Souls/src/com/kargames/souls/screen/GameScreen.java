package com.kargames.souls.screen;

import box2dLight.Light;
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.kargames.souls.App;
import com.kargames.souls.actor.Bubble;
import com.kargames.souls.actor.Part;
import com.kargames.souls.actor.Player;
import com.kargames.souls.actor.Sub;
import com.kargames.souls.manager.Axis;
import com.kargames.souls.util.Resource;
import com.kargames.souls.widget.FillTable;

public class GameScreen extends BaseScreen {
	
	private static final float WORLD_SIZE = 150;

	ShapeRenderer fullScreenSR;
	ShapeRenderer sr;
	public static boolean drawDebug = false;
	Box2DDebugRenderer debugRenderer;
	World world;
	
	Stage gameStage;
	public Player player;
	PositionalLight playerLight;
	
	RayHandler rayHandler;
	OrthographicCamera cam;
	

	Color topColor = new Color(0, 0.5f, 1f, 1f);
	Color botColor = new Color(0, 0, 1f, 1f);

	Label energyLabel;
	Label depthLabel;
	Label partsLabel;
	Label helpLabel;
	
	public Resource numParts;
	Image image;
	Image surface;
	
	public int helpStage;
	
	Array<Body> toRemove;
	
	UpgradeScreen upgradeScreen;
	
	int numBubbles = 100;
	Bubble[] bubbles;
	int currentBubble = 0;
	
	
	// b to buy upgrades
	// b to go back
	// a/x/y to buy speed/light/battery
	
	public GameScreen(App app) {
		super(app);
		toRemove = new Array<Body>();
		
		
		
		helpStage = 2;
		numParts = new Resource(0);
		fullScreenSR = new ShapeRenderer();
		upgradeScreen = new UpgradeScreen(app);
		FillTable hud = new FillTable();
		hud.top();
		energyLabel = new Label("Energy: 0 / 0", app.skin);
		depthLabel = new Label("Depth: 0", app.skin);
		partsLabel = new Label("Parts: 0", app.skin);
		helpLabel = new Label("Collect airplane parts", app.skin);
		hud.add(energyLabel).row();
		hud.add(depthLabel).row();
		hud.add(partsLabel);
		stage.addActor(hud);
		stage.addActor(upgradeScreen);
		
		FillTable helpTable = new FillTable();
		helpTable.add(helpLabel).padBottom(100);
		stage.addActor(helpTable);
		

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
				if (ch == 'b') {
					upgradeScreen.setVisible(!upgradeScreen.isVisible());
				}
				return super.keyTyped(event, ch);
			}
		});
		
		

		bubbles = new Bubble[numBubbles];
		
		for (int i = 0; i < numBubbles; i++) {
			bubbles[i] = new Bubble(app);
			gameStage.addActor(bubbles[i]);
		}
		
	}
	
	
	@Override
	public void show() {
		super.show();
		image = new Image(new Texture(Gdx.files.internal("levels/level2.png")));
		image.setSize(WORLD_SIZE, WORLD_SIZE);
		image.setPosition(-image.getWidth()/2, -image.getHeight());
		
//		gameStage.addActor(image);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, gameStage));
		
		debugRenderer = new Box2DDebugRenderer();
		RubeSceneLoader loader = new RubeSceneLoader();
		
		RubeScene scene = loader.loadScene(Gdx.files.internal("levels/level1.json"));
		world = scene.getWorld();
		Body playerBody = scene.getNamed(Body.class, "playerBody").first();
		scene.getImages();

		surface = new Image(app.textures.pixel);
		surface.setSize(WORLD_SIZE, 0.1f);
		surface.setPosition(-WORLD_SIZE/2, 0);
		player = new Player(app, playerBody); 
		gameStage.addActor(surface);
		gameStage.addActor(player);
		

		Body subBody = scene.getNamed(Body.class, "sub").first();
		Sub sub = new Sub(app, subBody);
		gameStage.addActor(sub);
		
		rayHandler = new RayHandler(world);
		playerLight = new PointLight(rayHandler, 1024);
		Light.setContactFilter((short)1, (short)0, (short)1);
		playerLight.setSoft(true);
		playerLight.setSoftnessLength(5);
		playerLight.setStaticLight(false);
		
		Array<Body> parts = scene.getNamed(Body.class, "part");
		
		for (Body body : parts) {
			Part p = new Part(app, body, 1);
			numParts.max++;
			gameStage.addActor(p);
		}

		parts = scene.getNamed(Body.class, "bigPart");
		
		for (Body body : parts) {
			Part p = new Part(app, body, 5);
			numParts.max+= 5;
			p.setColor(Color.RED);
			gameStage.addActor(p);
		}
		numParts.setMax();
		
		

		ContactListener cl = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Object udA = contact.getFixtureA().getBody().getUserData();
				Object udB = contact.getFixtureB().getBody().getUserData();

				Part part = null;
				if (udA instanceof Part) part = (Part)udA;
				if (udB instanceof Part) part = (Part)udB;
				
				Player player = null;
				if (udA instanceof Player) player = (Player)udA;
				if (udB instanceof Player) player = (Player)udB;
				
				Image sub = null;
				if (udA instanceof Sub) sub = (Sub)udA;
				if (udB instanceof Sub) sub = (Sub)udB;
				
				if (player != null && part != null) {
					part.remove();
					numParts.value+=part.val;
					toRemove.add(part.body);
					if (helpStage == 0) {
						helpStage = 1;
						helpLabel.setText("Return to surface to recharge battery and upgrade ship");
					}
				}
				

				if (player != null && sub != null) {
					player.setTouchingSub(true);
					System.out.println("start contact");
				}
				
				
			}

			@Override
			public void endContact(Contact contact) {
				Object udA;
				Object udB;
				try {
					udA = contact.getFixtureA().getBody().getUserData();
					udB = contact.getFixtureB().getBody().getUserData();
				} catch (NullPointerException e) {
					return;
				}
					
				Player player = null;
				if (udA instanceof Player) player = (Player)udA;
				if (udB instanceof Player) player = (Player)udB;
				
				Image sub = null;
				if (udA instanceof Sub) sub = (Sub)udA;
				if (udB instanceof Sub) sub = (Sub)udB;

				if (player != null && sub != null) {
					player.setTouchingSub(false);
					System.out.println("end contact");
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			
		};
		world.setContactListener(cl);
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

		cam.position.set(player.body.getPosition(), 0); 
		cam.update();
		fullScreenSR.begin(ShapeType.Filled);
		
		
		float depth = -player.body.getPosition().y / WORLD_SIZE;
		if (depth > 1) depth = 1;

		float blue = 1;
		botColor.set(0, 0, blue, 1);
		topColor.set(0, blue/2f, blue, 1);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		fullScreenSR.rect(0, 0, w, h, botColor, botColor, topColor, topColor);
		fullScreenSR.end();
		
		
		
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
		

		gameStage.getSpriteBatch().begin();
		image.draw(gameStage.getSpriteBatch(), 1);
		gameStage.getSpriteBatch().end();
		gameStage.draw();
		rayHandler.render();

		gameStage.getSpriteBatch().begin();
		player.draw(gameStage.getSpriteBatch(), 1);
		gameStage.getSpriteBatch().end();

//		fullScreenSR.begin(ShapeType.Line);
//		Gdx.gl20.glEnable(GL20.GL_BLEND);
//		fullScreenSR.setColor(0f, 1, 0, 0.6f);
//		fullScreenSR.circle(w/2, h/2, h/2, 60);
//		fullScreenSR.circle(w/2, h/2, h/4, 40);
//		fullScreenSR.line(w/2, 0, w/2, h);
//		fullScreenSR.line(0, h/2, w, h/2);
//		fullScreenSR.line(w/2,  h/2, w/2+h/2*MathUtils.sinDeg(deg), h/2+h/2*MathUtils.cosDeg(deg));
//		deg++;
//		fullScreenSR.end();

		if (drawDebug) {
			debugRenderer.render(world, gameStage.getCamera().combined);
		}
		super.draw();
	}

	float deg = 0;

	@Override
	public void update(float delta) {
		super.update(delta);
		
		while (toRemove.size > 0) {
			world.destroyBody(toRemove.pop());
		}
		
		world.step(1/60f, 8, 3);
		gameStage.act();
		rayHandler.setCombinedMatrix(gameStage.getCamera().combined);
		playerLight.setPosition(player.body.getPosition());
//		playerLight.setColor(0.5f, 0.7f, 1, 1);

		float y = player.body.getPosition().y;
		float light = player.light;
		float depth = -y / (WORLD_SIZE + 50 + light*10);
		if (depth > 1) depth = 1;
		if (depth < 0) depth = 0;
		playerLight.setColor(1, 1, 1, 0.9f-depth);
//		playerLight.setColor(1, 1, 1, 0.9f);
		playerLight.setDistance((1-depth) * 25 + 20);
		rayHandler.update();
		

		energyLabel.setText("Energy: " + player.energy.toString());
		depthLabel.setText("Depth: " + (int)y);
		partsLabel.setText("Parts: " + numParts.toString());
		

		if (player.body.getPosition().y > -0.5f && helpStage == 1){
			helpStage++;
			helpLabel.setVisible(false);
		}
		
		float val = Axis.getControllerValue(2, false);
		cam.zoom += val*delta;
		
	}

	@Override
	public void resize(int width, int height) {
		float zoom = 3;
		gameStage.setViewport(16*zoom, 9*zoom, true);
		super.resize(width, height);
	}


	public void makeBubbles(float h, float v) {
		Bubble b = bubbles[currentBubble];
		currentBubble++;
		if (currentBubble >= numBubbles) {
			currentBubble = 0;
		}
		b.init(player.body.getPosition(), -h*3, -v*3);
	}

}
