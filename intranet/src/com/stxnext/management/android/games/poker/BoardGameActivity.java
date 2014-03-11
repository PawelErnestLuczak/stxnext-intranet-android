
package com.stxnext.management.android.games.poker;

import java.io.IOException;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongOut;

import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;

import com.stxnext.management.android.games.poker.DeckFactory.DeckType;
import com.stxnext.management.android.games.poker.OSDMenu.OSDMenuListener;

public class BoardGameActivity extends SimpleBaseGameActivity implements OSDMenuListener {
    // ===========================================================
    // Constants
    // ===========================================================

    public static int CAMERA_HEIGHT = 720;
    public static int CAMERA_WIDTH = 480;

    // ===========================================================
    // Fields
    // ===========================================================

    private Camera mCamera;
    // private BitmapTextureAtlas mCardDeckTexture;
    private Scene mScene;
    // private HashMap<Card, ITextureRegion> mCardTotextureRegionMap;
    private BitmapTextureAtlas mBitmapTextureAtlas;
    private TextureRegion mFaceTextureRegion;
    private Font mFont;
    private Sound cardPickSound;
    private Sound cardPutSound;
    private RepeatingSpriteBackground tableTexture;
    private OSDMenu osdMenu;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        CAMERA_WIDTH = size.x;
        CAMERA_HEIGHT = size.y;

        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        final EngineOptions engineOptions = new EngineOptions(true,
                ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH,
                        CAMERA_HEIGHT), this.mCamera);
        engineOptions.getRenderOptions().setMultiSampling(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);

        return engineOptions;
    }

    public Engine getEngine() {
        return mEngine;
    }

    @Override
    public void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
                CardSprite.CARD_WIDTH, CardSprite.CARD_HEIGHT,
                TextureOptions.BILINEAR);
        
        this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                this.mBitmapTextureAtlas, this, "card_blank.png", 0, 0);
        this.mBitmapTextureAtlas.load();
        
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
                200, 200,
                TextureOptions.BILINEAR);
        this.tableTexture = new RepeatingSpriteBackground(CAMERA_WIDTH, CAMERA_HEIGHT, this.getTextureManager(), AssetBitmapTextureAtlasSource.create(this.getAssets(), "gfx/dark_texture.png"), this.getVertexBufferObjectManager());
        mBitmapTextureAtlas.load();
        
        
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
                32, 32,
                TextureOptions.BILINEAR);
        TextureRegion alignMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                this.mBitmapTextureAtlas, this, "arrow_refresh.png", 0, 0);
        this.osdMenu = new OSDMenu(this,this,alignMenu);
        this.mBitmapTextureAtlas.load();

        this.mFont = FontFactory.create(getFontManager(),
                getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 22);
        mFont.load();
        osdMenu.prepareTextures();
        
        SoundFactory.setAssetBasePath("mfx/");
        try {
            this.cardPickSound = SoundFactory.createSoundFromAsset(getEngine()
                    .getSoundManager(), this, "card_pick.wav");
            this.cardPutSound = SoundFactory.createSoundFromAsset(getEngine()
                    .getSoundManager(), this, "card_put.wav");
        } catch (final IOException e) {
            Debug.e(e);
        }
        
        CardSprite.setCardPickSound(cardPickSound);
        CardSprite.setCardPutSound(cardPutSound);
        CardSprite.setFont(mFont);

    }

    @Override
    public Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.mScene = new Scene();
        this.mScene.setOnAreaTouchTraversalFrontToBack();
        // this.mScene.setScale(CardSprite.cardGlobalScale);

        this.cards = DeckFactory.produce(DeckType.DEFAULT, mFaceTextureRegion, this);
        for (CardSprite sprite : this.cards) {
            addCard(sprite);
        }
        
        this.osdMenu.prepareScene(mScene);

        //new Background(0.09804f, 0.6274f, 0.8784f)
        this.mScene.setBackground(this.tableTexture);
        this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

        return this.mScene;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private List<CardSprite> cards;

    private void addCard(CardSprite sprite) {
        this.mScene.attachChild(sprite);
        this.mScene.registerTouchArea(sprite);
    }

    public void clearCardsZIndex() {
        for (CardSprite card : cards) {
            card.setZIndex(0);
        }
    }

    @Override
    public void onAlignDeck() {
        for(int i=0;i<cards.size();i++){
            cards.get(i).registerEntityModifier(new MoveModifier(1f, cards.get(i).getX(), cards.get(i).getOriginalX(), cards.get(i).getY(), cards.get(i).getOriginalY(), EaseStrongOut.getInstance()));
            cards.get(i).setZIndex(i);
        }
        mScene.sortChildren();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
