package com.kargames.bbq.screen;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Value;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.kargames.bbq.App;
import com.kargames.bbq.Renderer;
import com.kargames.bbq.Upgrade;
import com.kargames.bbq.actor.BlackBox;
import com.kargames.bbq.actor.Bubble;
import com.kargames.bbq.actor.Part;
import com.kargames.bbq.actor.Player;
import com.kargames.bbq.actor.Sub;
import com.kargames.bbq.manager.Axis;
import com.kargames.bbq.manager.Control;
import com.kargames.bbq.util.Resource;
import com.kargames.bbq.widget.FillTable;

public class GameScreen extends BaseScreen {
	
	private static final float WORLD_SIZE = 150;
	private static final int BIG_PART_VAL = 3;
	
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

	Label energyLabel;
	Label depthLabel;
	Label partsLabel;
	Label helpLabel;
	Label floatingText;
	Label collectedLabel;
	Label warningLabel;

	Vector2 temp = new Vector2();
	
	public Resource partsCollected;
	public int money = 4;
	Image image;
	
	public int helpStage;
	
	Array<Body> toRemove;
	
	UpgradeScreen upgradeScreen;
	
	int numBubbles = 100;
	Bubble[] bubbles;
	int currentBubble = 0;

	boolean releasedB;
	boolean releasedX;
	boolean releasedY;
	
	Sub sub;
	
	Image arrow;
	
	public GameScreen(App app) {
		super(app);
		toRemove = new Array<Body>();
		arrow = new Image(app.textures.arrow); 
		arrow.setSize(1, 1);
		arrow.setOrigin(arrow.getWidth()/2f, arrow.getHeight()/2f);
		arrow.setVisible(false);
		floatingText = new Label("", app.styles.labelStyle);
		stage.addActor(floatingText);
		helpStage = 0;
		partsCollected = new Resource(0);
		fullScreenSR = new ShapeRenderer();
		upgradeScreen = new UpgradeScreen(app);
		FillTable hud = new FillTable();
		hud.top();
		energyLabel = new Label("Energy: 0 / 0", app.styles.labelStyle);
		depthLabel = new Label("Depth: 0", app.styles.labelStyle);
		partsLabel = new Label("Parts: 0", app.styles.labelStyle);
		collectedLabel= new Label("Collected: 0 / 0", app.styles.labelStyle);
		helpLabel = new Label("Collect airplane parts", app.styles.labelStyle);
		warningLabel = new Label("Warning: energy low.  Return to sub", app.styles.labelStyle);
		
		
		hud.add(energyLabel).row();
//		hud.add(depthLabel).row();
		hud.add(partsLabel).row();
//		hud.add(collectedLabel);
		stage.addActor(hud);
		stage.addActor(upgradeScreen);
		
		FillTable warningTable = new FillTable();
		warningTable.add(warningLabel).padBottom(Value.percentHeight(0.1f));
		stage.addActor(warningTable);
		
		FillTable helpTable = new FillTable();
		helpTable.add(helpLabel).padBottom(Value.percentHeight(0.4f));
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
//				if (ch == ' ') {
//					drawDebug = !drawDebug;
//				}
//				if (ch == 'b') {
//					upgradeScreen.setVisible(!upgradeScreen.isVisible());
//				}
				return super.keyTyped(event, ch);
			}
		});
		
		

		bubbles = new Bubble[numBubbles];
		
		for (int i = 0; i < numBubbles; i++) {
			bubbles[i] = new Bubble(app);
			gameStage.addActor(bubbles[i]);
		}
		
		
		
		image = new Image(new Texture(Gdx.files.internal("levels/level2.png")));
		image.setSize(WORLD_SIZE, WORLD_SIZE);
		image.setPosition(-image.getWidth()/2, -image.getHeight());
		
		debugRenderer = new Box2DDebugRenderer();
		RubeSceneLoader loader = new RubeSceneLoader();

		RubeScene scene = loader.loadScene(Gdx.files.internal("levels/level2.json"));
//		RubeScene scene = loader.loadScene(Gdx.files.internal("levels/level2.rube"));
		world = scene.getWorld();
		Body playerBody = scene.getNamed(Body.class, "playerBody").first();
		scene.getImages();

		player = new Player(app, playerBody); 
		gameStage.addActor(player);
		
		gameStage.addActor(arrow);

		Body subBody = scene.getNamed(Body.class, "sub").first();
		sub = new Sub(app, subBody);
		gameStage.addActor(sub);
		
		Body blackBoxBody = scene.getNamed(Body.class, "blackBox").first();
		BlackBox blackBox = new BlackBox(app, blackBoxBody);
		gameStage.addActor(blackBox);
		
		rayHandler = new RayHandler(world);
		playerLight = new PointLight(rayHandler, 1024);
		Light.setContactFilter((short)1, (short)0, (short)1);
		playerLight.setSoft(true);
		playerLight.setSoftnessLength(5);
		playerLight.setStaticLight(false);
		
		Array<Body> parts = scene.getNamed(Body.class, "part");
		
		for (Body body : parts) {
			Part p = new Part(app, body, 1);
			partsCollected.max++;
			gameStage.addActor(p);
		}

		parts = scene.getNamed(Body.class, "bigPart");
		
		for (Body body : parts) {
			Part p = new Part(app, body, BIG_PART_VAL);
			partsCollected.max+= BIG_PART_VAL;
			gameStage.addActor(p);
		}
		
		

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

				Sub sub = null;
				if (udA instanceof Sub) sub = (Sub)udA;
				if (udB instanceof Sub) sub = (Sub)udB;
				
				BlackBox blackBox = null;
				if (udA instanceof BlackBox) blackBox = (BlackBox)udA;
				if (udB instanceof BlackBox) blackBox = (BlackBox)udB;
				
				if (player != null && part != null) {
					setFloatingText("+$" + (int)part.val, part.getX(), part.getY());
					part.remove();
					money += part.val;
					partsCollected.value += part.val;
					toRemove.add(part.body);
					if (helpStage == 0) {
						arrow.setVisible(true);
						helpStage = 1;
						helpLabel.setText("Return to sub to recharge battery and upgrade ship");
					}
				}


				if (player != null && sub != null) {
					player.setTouchingSub(true);
					upgradeScreen.setVisible(true);
				}

				if (player != null && blackBox != null) {
					// show victory screen
					helpLabel.setText("You Win!");
					helpLabel.setVisible(true);
					helpLabel.setColor(Color.WHITE);
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
					upgradeScreen.setVisible(false);
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			
		};
		world.setContactListener(cl);
		
	}
	
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, gameStage));
	}
	
	@Override
	public void draw() {

		cam.position.set(player.body.getPosition(), 0); 

		if (cam.zoom < 0.1f) cam.zoom = 0.1f;
		cam.update();
		
		Renderer.renderBackground(-player.body.getPosition().y / WORLD_SIZE);
		gameStage.getSpriteBatch().begin();
		image.draw(gameStage.getSpriteBatch(), 1);
		gameStage.getSpriteBatch().end();
		gameStage.draw();
		rayHandler.render();

		gameStage.getSpriteBatch().begin();
		player.draw(gameStage.getSpriteBatch(), 1);
		if (arrow.isVisible()) {
			arrow.draw(gameStage.getSpriteBatch(), 1);
		}
		gameStage.getSpriteBatch().end();

		// radar
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
		float y = player.body.getPosition().y;
		float light = player.light;
		float depth = -y / (WORLD_SIZE + 50 + light*10);
		if (depth > 1) depth = 1;
		if (depth < 0) depth = 0;
		playerLight.setColor(1, 1, 1, 0.9f-depth);
		playerLight.setDistance((1-depth) * 25 + 20);
		rayHandler.update();
		

		energyLabel.setText("Energy: " + player.energy.toString());
		depthLabel.setText("Depth: " + (int)-y);
		partsLabel.setText("$" + money);
		collectedLabel.setText("Collected: " + partsCollected.toString());
		

		if (player.isTouchingSub) {
			
			Array<Controller> controllers = Controllers.getControllers();
			if (controllers.size > 0) {
				Controller c = controllers.get(0);
				if (c.getButton(Control.BUTTON_X)) {
					if (releasedX){
						upgradeScreen.upgrade(Upgrade.SPEED);
					}
					releasedX = false;
				} else {
					releasedX = true;
				}
				
				if (c.getButton(Control.BUTTON_B)) {
					if (releasedB){
						upgradeScreen.upgrade(Upgrade.BATTERY);
					}
					releasedB = false;
				} else {
					releasedB = true;
				}
				 
				if (c.getButton(Control.BUTTON_Y)) {
					if (releasedY){
						upgradeScreen.upgrade(Upgrade.LIGHT);
					}
					releasedY = false;
				} else {
					releasedY = true;
				}
			}
			
			if (helpStage == 1){
				helpStage++;
				helpLabel.setText("Continue to collect parts and\nexplore until you find the black box!");
				helpLabel.addAction(Actions.sequence(Actions.delay(5), Actions.fadeOut(5)));
			}
		}
		
		float val = Axis.getControllerValue(2, false);
		cam.zoom += val*delta*0.8f;

		Vector2 p = player.body.getPosition();
		temp.set(sub.body.getPosition());
		temp.sub(p);
		float angle = temp.angle();
		arrow.setPosition(player.getX() + 3*MathUtils.cosDeg(angle), player.getY()+ 3*MathUtils.sinDeg(angle));
		arrow.setRotation(angle-90f);
		
		
		warningLabel.setVisible(false);
		if (player.energy.getRatio() < 0.5f && !player.isTouchingSub) {
			warningLabel.setVisible(true);
			warningLabel.setText("Warning: energy low.  Return to sub");
			warningLabel.setColor(Color.YELLOW);
			if (player.energy.getRatio() < 0.2f) {
				warningLabel.setText("WARNING: ENERGY CRITICAL! RETURN TO SUB!");
				warningLabel.setColor(Color.RED);
			}
		}
		
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
	
	public void setFloatingText(String text, float x, float y) {
		temp.set(x-0.5f, y-0.5f);
		gameStage.stageToScreenCoordinates(temp);
		stage.screenToStageCoordinates(temp);
		System.out.println(temp);
		
		floatingText.setText(text);
		floatingText.clearActions();
		floatingText.setPosition(temp.x, temp.y);

		floatingText.addAction(Actions.fadeIn(0));
		floatingText.addAction(Actions.fadeOut(1));
		floatingText.addAction(Actions.moveBy(0, 100, 1));
	}

}
