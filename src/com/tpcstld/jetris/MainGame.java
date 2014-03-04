package com.tpcstld.jetris;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

public class MainGame extends View {

	Paint paint = new Paint();

	// INTERNAL OPTIONS:
	static int debug = 0;
	static final int numSquaresX = 16; // Total number of columns
	static final int numSquaresY = 22; // total number of rows
	static final double textScaleSize = 0.8; // Text scaling
	static final double textScaleSizeAux = 0.7; // Text scaling for auxiliary
												// text
	static final int FPS = 1000000000 / 30; // The nanoseconds per frame at which the game
										// runs at
	static int ORANGE;
	static Context mContext;

	// EXTERNAL OPTIONS:
	public static boolean tapToHold = true;
	public static double defaultGravity = 0.05;
	// The default gravity of the game value
	public static int flickSensitivity = 30;
	// The Sensitivity is the flick gesture
	public static long slackLength = 1000000000; // How long the stack goes on for in milliseconds
	public static double softDropSpeed = 0.45; // How fast soft dropping is
	public static int dragSensitivity = 100;
	// The sensitivity of the drag gesture
	public static long countDownTime = 120;
	public static int linesPerLevel = 10;
	public static double gravityAddPerLevel = 0.025;
	public static int textColor = Color.BLACK;

	// GAME STATICS
	static final int numberOfBlocksWidth = 10; // The number of columns of
												// blocks in
												// the main field
	static final int numberOfBlocksLength = 22; // The number of rows of blocks
												// in the
	// main field
	static final int numberOfHoldShapeWidth = 4; // The width of the auxiliary
													// boxes
	static final int numberOfHoldShapeLength = 2; // The length of the auxiliary
													// boxes

	// Game Mode
	public static String gameMode = "";

	// TIMERS:
	static long currentCountDownTime = countDownTime * 1000;
	// Time remaining on time attack mode
	static CountDownTimer countDown = new CustomCountDownTimer(
			currentCountDownTime, FPS);
	static long clock = System.nanoTime();
	static long slackTime = slackLength;

	// Tracks the real time for fps

	// POSITIONING VARIABLES:
	static int mainFieldShiftX; // How much the screen is shifted to the right
	static int mainFieldShiftY; // How much the screen is shifted downwards
	static int mainFieldStartingY; // At what Y-value do the vertical lines
									// start at
	static int squareSide; // The size of one square
	static int holdShapeXStarting; // Where the hold box starts (x)
	static int holdShapeYStarting; // Where the hold box starts (y)
	static int nextShapeYStarting; // Where the first next box starts (y)
	static int nextShapeY2Starting; // Where the second next box starts (y)
	static int nextShapeY3Starting; // Where the third next box starts (y)
	static int clearInfoYStarting; // Where the clear info text starts (y)
	static int scoreInfoYStarting; // Where the score box starts (y)
	static int holdTextYStarting; // Where the "Hold: " text starts (y)
	static int nextTextYStarting; // Where the "Next: " text starts (y)
	static int auxInfoXStarting; // Where the aux box starts (x)
	static int auxInfoYStarting; // Where the aux box starts (y)
	static int highScoreYStarting; // Where the highscore text box starts(y)

	// SHAPE INFORMATION:
	static int nextShape = -1; // The NEXT shape on the playing field.
	static int next2Shape = -1; // The NEXT2 shape on the playing field.
	static int next3Shape = -1; // The NEXT3 shaped on the playing field.
	static int holdShape = -1; // The shape currently being held.
	static int currentShape = -1; // The CURRENT shape on the playing field.
	static ArrayList<Integer> shapeList = new ArrayList<Integer>();
	// A list containing the shapes left in the current bag
	static int specificCurrentShape = 0;
	// The current, specific shape of the block

	// FIELD INFORMATION:
	static int[][] blocks = new int[numberOfBlocksWidth][numberOfBlocksLength];
	// Array detailing the type of block in each square
	static int[][] colors = new int[numberOfBlocksWidth][numberOfBlocksLength];
	// Array detailing the colour in each square
	static int[][] holdBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the block in hold
	static int[][] nextBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next block
	static int[][] next2Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next2 block
	static int[][] next3Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next3 block
	static int[] playLocationX = new int[4]; // X-coords of the blocks in play
	static int[] playLocationY = new int[4]; // Y-coords of the blocks in play

	// MISC VARIABLES:
	static String auxText = ""; // Text for displaying the time left
	static boolean slack = false; // Whether or not slack is currently active
	public static boolean pause = false;
	// Whether or not the game is currently paused
	static boolean lose = false; // Whether or not the game is still in progress
	static boolean win = false; // Whether or not the game is finished
	static boolean holdOnce = false;
	// Whether or not the block has already been held once
	static boolean hardDrop = false; // Whether or not harddropping is active
	static boolean slackOnce = false;
	// Whether or not slack as already been activated
	static boolean turnSuccess = false; // Whether or not a turn was successful
	static boolean softDrop = false; // Whether or not softdropping is active
	static boolean kick = false;
	// Whether or not the block was kicked to its current location
	static boolean difficult = false;
	// Whether or not a clear was considered to be "hard"
	static boolean lastDifficult = false;
	// Whether or not the last clear was considered to be "hard"
	static int score = 0; // The current score
	static int highScore = 0; // The highscore of the current gamemode
	static int level = 0; // The current level (marathon mode)
	static int linesCleared = 0; // The total number of lines cleared
	static int linesClearedFloor = 0;
	// The number of lines cleared in at the current speed (Marathon Mode)
	static int combo = 0; // The current combo
	static Random r = new Random(); // The randomizer
	static double gravity = defaultGravity; // The current base gravity
	static double gravityAdd = 0; // The amount of gravity to add onto the base
	static double gravityTicker = 0; // The current gravity ticker
	static boolean getScreenSize = false;
	// Initial getting screen size variable
	public static boolean startNewGame = true;
	// Whether it should be a new game or not
	static ArrayList<String> clearInfo = new ArrayList<String>();

	// Blocks Data:
	// 0 = empty space
	// 1 = active space
	// 2 = locked space
	// 3 = ghost space

	// Loading the game
	public MainGame(Context context) {
		super(context);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		getSettings(settings);
		gravity = defaultGravity;
		mContext = context;
		// Setting the orange color since there is no default
		ORANGE = getResources().getColor(R.color.orange);
		// Create the object to receive touch input
		setOnTouchListener(getOnTouchListener());
		if (startNewGame) {
			newGame();
		}

	}

	public void tick() {
		if (!lose && !pause && !win) {
			long temp = System.nanoTime();
			long dtime = temp - clock;
			if (dtime > FPS) {
				clock = clock + FPS;
				slack();
				gravity();
			}
			coloring();
			ghostShape();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		// Get the screen size and adjust the game screen proportionally
		if (getScreenSize) {
			int width = this.getMeasuredWidth();
			int height = this.getMeasuredHeight();
			getLayout(width, height);
			paint.setTextSize((float) (squareSide * textScaleSize));
		}

		paint.setColor(textColor);
		paint.setTextSize((float) (squareSide * textScaleSizeAux));
		// Drawing "Hold: " text box
		canvas.drawText("Hold: ", holdShapeXStarting + mainFieldShiftX,
				holdTextYStarting + mainFieldShiftY, paint);

		// Drawing "Next: " text box
		canvas.drawText("Next: ", holdShapeXStarting + mainFieldShiftX,
				nextTextYStarting + mainFieldShiftY, paint);

		// Drawing "High Score: " text box
		canvas.drawText("High Score: " + highScore, mainFieldShiftX,
				highScoreYStarting + mainFieldShiftY, paint);

		// Drawing Score text box
		canvas.drawText("Score:", auxInfoXStarting + mainFieldShiftX,
				scoreInfoYStarting + mainFieldShiftY, paint);
		changePaintSettings("info");
		canvas.drawText("" + score, numSquaresX * squareSide,
				scoreInfoYStarting + mainFieldShiftY, paint);
		changePaintSettings("normal");

		// Drawing aux text box
		if (gameMode.equals(Constants.MARATHON_MODE)) {
			String tempText = auxText + " ("
					+ (linesPerLevel - linesCleared + linesClearedFloor) + ")";
			canvas.drawText("Level:", auxInfoXStarting + mainFieldShiftX,
					auxInfoYStarting + mainFieldShiftY, paint);
			changePaintSettings("info");
			canvas.drawText(tempText, numSquaresX * squareSide,
					auxInfoYStarting + mainFieldShiftY, paint);
			changePaintSettings("normal");
		} else if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			canvas.drawText("Time:", auxInfoXStarting + mainFieldShiftX,
					auxInfoYStarting + mainFieldShiftY, paint);
			changePaintSettings("info");
			canvas.drawText(auxText, numSquaresX * squareSide, auxInfoYStarting
					+ mainFieldShiftY, paint);
			changePaintSettings("normal");
		}

		// Drawing clearInfo text box
		for (int xx = 0; xx < clearInfo.size(); xx++) {
			canvas.drawText(clearInfo.get(xx), holdShapeXStarting
					+ mainFieldShiftX - squareSide / 2, clearInfoYStarting
					+ mainFieldShiftY + squareSide * xx, paint);
		}

		tick();

		// Drawing columns for the main field
		for (int xx = mainFieldShiftX; xx <= squareSide * numberOfBlocksWidth
				+ mainFieldShiftX; xx += squareSide) {
			canvas.drawLine(xx, mainFieldStartingY, xx, squareSide
					* (numberOfBlocksLength - 2) + mainFieldShiftY, paint);
		}

		// Drawing rows for the main field
		for (int xx = mainFieldShiftY; xx <= squareSide
				* (numberOfBlocksLength - 2) + mainFieldShiftY; xx += squareSide) {
			canvas.drawLine(mainFieldShiftX, xx, squareSide
					* numberOfBlocksWidth + mainFieldShiftX, xx, paint);
		}

		// Coloring the main field
		for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
			for (int yy = 0; yy < numberOfBlocksLength; yy++) {
				if (blocks[xx][yy] != 0 & blocks[xx][yy] != 3) {
					paint.setColor(chooseColor(colors[xx][yy]));
					canvas.drawRect(xx * squareSide + mainFieldShiftX, (yy - 2)
							* squareSide + mainFieldShiftY, xx * squareSide
							+ squareSide + mainFieldShiftX, (yy - 2)
							* squareSide + squareSide + mainFieldShiftY, paint);
				}
			}
		}

		// Coloring the ghost shape
		paint.setColor(chooseColor(currentShape));
		for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
			for (int yy = 0; yy < numberOfBlocksLength; yy++) {
				if (blocks[xx][yy] == 3) {
					canvas.drawCircle((float) (xx * squareSide + squareSide
							* 0.5 + mainFieldShiftX), (float) ((yy - 2)
							* squareSide + squareSide * 0.5 + mainFieldShiftY),
							(float) (squareSide * 0.5), paint);
				}
			}
		}

		// Coloring the blocks for the held shape.
		paint.setColor(chooseColor(holdShape));
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
				if (holdBlocks[xx][yy] == 1) {
					canvas.drawRect(xx * squareSide + holdShapeXStarting
							+ mainFieldShiftX, yy * squareSide
							+ mainFieldShiftY + holdShapeYStarting, xx
							* squareSide + holdShapeXStarting + squareSide
							+ mainFieldShiftX, yy * squareSide + squareSide
							+ mainFieldShiftY + holdShapeYStarting, paint);
				}
			}
		}

		// Coloring the next shape
		paint.setColor(chooseColor(nextShape));
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
				if (nextBlocks[xx][yy] == 1) {
					canvas.drawRect(xx * squareSide + holdShapeXStarting
							+ mainFieldShiftX, yy * squareSide
							+ nextShapeYStarting + mainFieldShiftY, xx
							* squareSide + holdShapeXStarting + squareSide
							+ mainFieldShiftX,
							yy * squareSide + nextShapeYStarting + squareSide
									+ mainFieldShiftY, paint);
				}
			}
		}

		// Coloring the second next shape
		paint.setColor(chooseColor(next2Shape));
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
				if (next2Blocks[xx][yy] == 1) {
					canvas.drawRect(xx * squareSide + holdShapeXStarting
							+ mainFieldShiftX, yy * squareSide
							+ nextShapeY2Starting + mainFieldShiftY, xx
							* squareSide + holdShapeXStarting + squareSide
							+ mainFieldShiftX, yy * squareSide
							+ nextShapeY2Starting + squareSide
							+ mainFieldShiftY, paint);
				}
			}
		}

		// Coloring the third next shape
		paint.setColor(chooseColor(next3Shape));
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
				if (next3Blocks[xx][yy] == 1) {
					canvas.drawRect(xx * squareSide + holdShapeXStarting
							+ mainFieldShiftX, yy * squareSide
							+ nextShapeY3Starting + mainFieldShiftY, xx
							* squareSide + holdShapeXStarting + squareSide
							+ mainFieldShiftX, yy * squareSide
							+ nextShapeY3Starting + squareSide
							+ mainFieldShiftY, paint);
				}
			}
		}

		// Displaying the big text if needed
		// When the game is lost
		// When the game is won
		// When the game is paused
		if (lose) {
			// Change the font settings
			paint.setColor(Color.RED);
			changePaintSettings("big on");
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("GAME", width / 2, mainFieldShiftY + length / 3,
					paint);
			canvas.drawText("OVER", width / 2,
					mainFieldShiftY + length * 2 / 3, paint);
			// Revert text settings to normal
			changePaintSettings("big off");
		} else if (win) {
			// Change the font settings
			paint.setColor(Color.GREEN);
			changePaintSettings("big on");
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("TIME'S", width / 2, mainFieldShiftY + length / 3,
					paint);
			canvas.drawText("UP", width / 2, mainFieldShiftY + length * 2 / 3,
					paint);
			// Revert text settings to normal
			changePaintSettings("big off");
		} else if (pause) {
			// Change the font settings
			paint.setColor(textColor);
			changePaintSettings("big on");
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("PAUSED", width / 2, mainFieldShiftY + length / 2,
					paint);
			// Revert text settings to normal
			changePaintSettings("big off");
		}
		invalidate();
	}

	public void changePaintSettings(String setting) {
		if (setting.equals("info")) {
			paint.setTextSize((float) (squareSide * textScaleSize));
			paint.setTextAlign(Paint.Align.RIGHT);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
		} else if (setting.equals("normal")) {
			paint.setTextAlign(Paint.Align.LEFT);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setTextSize((float) (squareSide * textScaleSizeAux));
		} else if (setting.equals("big on")) {
			paint.setTextSize((float) (squareSide * 4));
			paint.setShadowLayer((float) 5, 0, 0, Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
		} else if (setting.equals("big off")) {
			paint.setShadowLayer((float) 0, 0, 0, Color.BLACK);
			paint.setTextSize((float) (squareSide * textScaleSize));
			paint.setTextAlign(Paint.Align.LEFT);
		}
	}

	public void getSettings(SharedPreferences settings) {
		tapToHold = settings.getBoolean("tapToHold", tapToHold);
		defaultGravity = getDoubleFromSettings(defaultGravity,
				"defaultGravity", settings);
		flickSensitivity = getIntFromSettings(flickSensitivity,
				"flickSensitivity", settings);
		slackLength = getIntFromSettings((int) (slackLength / 1000000), "slackLength", settings) * 1000000;
		softDropSpeed = getDoubleFromSettings(softDropSpeed, "softDropSpeed",
				settings);
		dragSensitivity = getIntFromSettings(dragSensitivity,
				"dragSensitivity", settings);
		countDownTime = getIntFromSettings((int) countDownTime,
				"countDownTime", settings);
		linesPerLevel = getIntFromSettings(linesPerLevel, "linesPerLevel",
				settings);
		gravityAddPerLevel = getDoubleFromSettings(gravityAddPerLevel,
				"gravityAddPerLevel", settings);

		if (startNewGame) {
			getScreenSize = true;
		}

		if (gameMode.equals(Constants.MARATHON_MODE)) {
			highScore = settings.getInt(Constants.MARATHON_SCORE, 0);
		} else if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			highScore = settings.getInt(Constants.TIME_ATTACK_SCORE, 0);
		}

		// Get the theme to set the textcolor
		int theme = Constants.getTheme(settings);
		if (theme == R.style.LightTheme) {
			textColor = Color.BLACK;
		} else if (theme == R.style.DarkTheme) {
			textColor = Color.WHITE;
		}
	}

	public int getIntFromSettings(int variable, String text,
			SharedPreferences settings) {
		try {
			return Integer.parseInt(settings.getString(text,
					String.valueOf(variable)));
		} catch (Exception e) {
			System.err.println("Error getting " + text
					+ ". Reverting to default value.");
		}
		return variable;
	}

	public double getDoubleFromSettings(double variable, String text,
			SharedPreferences settings) {
		try {
			return Double.parseDouble(settings.getString(text,
					String.valueOf(variable)));
		} catch (Exception e) {
			System.err.println("Error getting " + text
					+ ". Reverting to default value.");
		}
		return variable;
	}

	public static void detectShape() {
		int counter = 0;
		for (int yy = numberOfBlocksLength - 1; yy >= 0; yy--) {
			for (int xx = numberOfBlocksWidth - 1; xx >= 0; xx--) {
				if (blocks[xx][yy] == 1) {
					playLocationX[counter] = xx;
					playLocationY[counter] = yy;
					counter++;
				}
			}
		}
	}

	// Generates new minos.
	// Follows the 7-bag system, which means that there will be ALL the blocks..
	// ...found in each "bag" of 7 pieces.
	public static void pickShape() {
		if (nextShape == -1) {
			nextShape = shapeList.remove(r.nextInt(shapeList.size()));
			next2Shape = shapeList.remove(r.nextInt(shapeList.size()));
			next3Shape = shapeList.remove(r.nextInt(shapeList.size()));
		}
		displayShape(nextShape);
		currentShape = nextShape;
		nextShape = next2Shape;
		next2Shape = next3Shape;
		next3Shape = shapeList.remove(r.nextInt(shapeList.size()));
		
		if (!lose) {
			if (shapeList.size() <= 0) {
				for (int xx = 0; xx < 7; xx++) {
					shapeList.add(xx);
				}
			}

			displayBoxShape(nextBlocks, nextShape);
			displayBoxShape(next2Blocks, next2Shape);
			displayBoxShape(next3Blocks, next3Shape);
			detectShape();
			shapeDown();
			holdOnce = false;
		}
	}

	public static void holdShape() {
		// Clear all active shapes
		if (holdOnce) {
			return;
		}
		for (int xx = 0; xx < numberOfBlocksWidth; xx++)
			for (int yy = 0; yy < numberOfBlocksLength; yy++)
				if (blocks[xx][yy] == 1)
					blocks[xx][yy] = 0;

		if (holdShape == -1) {
			holdShape = currentShape;
			pickShape();
		} else {
			int lastShape1 = holdShape;
			holdShape = currentShape;
			currentShape = lastShape1;
			displayShape(currentShape);
		}
		holdOnce = true;
		displayBoxShape(holdBlocks, holdShape);
	}

	public static void displayShape(int thisShape) {
		if (thisShape == 0) {
			placeShapeAndDetectLose(new int[] { 3, 4, 5, 6 }, new int[] { 1, 1,
					1, 1 });
			specificCurrentShape = 1;
		} else if (thisShape == 1) {
			placeShapeAndDetectLose(new int[] { 3, 3, 4, 5 }, new int[] { 0, 1,
					1, 1 });
			specificCurrentShape = 5;
		} else if (thisShape == 2) {
			placeShapeAndDetectLose(new int[] { 5, 3, 4, 5 }, new int[] { 0, 1,
					1, 1 });
			specificCurrentShape = 9;
		} else if (thisShape == 3) {
			placeShapeAndDetectLose(new int[] { 4, 3, 4, 5 }, new int[] { 0, 1,
					1, 1 });
			specificCurrentShape = 13;
		} else if (thisShape == 4) {
			placeShapeAndDetectLose(new int[] { 4, 5, 3, 4 }, new int[] { 0, 0,
					1, 1 });
			specificCurrentShape = 15;
		} else if (thisShape == 5) {
			placeShapeAndDetectLose(new int[] { 3, 4, 4, 5 }, new int[] { 0, 0,
					1, 1 });
			specificCurrentShape = 17;
		} else if (thisShape == 6) {
			placeShapeAndDetectLose(new int[] { 4, 5, 4, 5 }, new int[] { 0, 0,
					1, 1 });
			specificCurrentShape = 19;
		}
	}

	public static void displayBoxShape(int[][] x, int y) {
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++)
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++)
				x[xx][yy] = 0;

		if (y == 0) {
			for (int xx = 0; xx <= 3; xx++)
				x[xx][1] = 1;
		} else if (y == 1) {
			for (int xx = 0; xx <= 2; xx++)
				x[xx][1] = 1;
			x[0][0] = 1;
		} else if (y == 2) {
			for (int xx = 0; xx <= 2; xx++)
				x[xx][1] = 1;
			x[2][0] = 1;
		} else if (y == 3) {
			for (int xx = 0; xx <= 2; xx++)
				x[xx][1] = 1;
			x[1][0] = 1;
		} else if (y == 4) {
			x[1][0] = 1;
			x[2][0] = 1;
			x[0][1] = 1;
			x[1][1] = 1;
		} else if (y == 5) {
			x[0][0] = 1;
			x[1][0] = 1;
			x[1][1] = 1;
			x[2][1] = 1;
		} else if (y == 6) {
			x[0][0] = 1;
			x[1][0] = 1;
			x[0][1] = 1;
			x[1][1] = 1;
		}
	}

	public static void placeShapeAndDetectLose(int[] x, int[] y) {
		lose = false;
		for (int xx = 0; xx < x.length; xx++) {
			if (blocks[x[xx]][y[xx]] == 2) {
				lose = true;
				countDown.cancel();
				break;
			}
		}
		if (lose) {
			updateHighScore();
		}
		for (int xx = 0; xx < x.length; xx++) {
			blocks[x[xx]][y[xx]] = 1;
		}
	}

	// Checks if there is anything below the active shape.
	// If there is: Freeze the active shape and initiate scoring
	// If there isn't: Move the shape down.
	public static void shapeDown() {
		detectShape();
		coloring();

		boolean move = canFallDown(playLocationX, playLocationY);

		if (!move && !hardDrop && !slackOnce) {
			activateSlack();
		}

		// Detect
		if (!move && !slack) {
			int currentDrop; // The number of lines cleared
			boolean tSpin = checkTSpin();

			// Set current squares to inactive
			for (int xx = 0; xx < playLocationY.length; xx++) {
				blocks[playLocationX[xx]][playLocationY[xx]] = 2;
			}

			currentDrop = clearLines();
			hardDrop = false;
			softDrop = false;
			gravityTicker = 0.0;
			gravity = defaultGravity;

			int addScore = scoring(currentDrop, tSpin);

			score = score + addScore;
			linesCleared = linesCleared + currentDrop;
			if (gameMode.equals(Constants.MARATHON_MODE)) {
				changeGravity();
			}
			// Scoring System end.
			pickShape();
		} else if (move) {
			// If slack is still activated, cancel the slack
			slackOnce = false;
			for (int xx = 0; xx < playLocationX.length; xx++) {
				blocks[playLocationX[xx]][playLocationY[xx]] = 0;
				blocks[playLocationX[xx]][playLocationY[xx] + 1] = 1;
			}
		}
		if (move && !slack) {
			if (hardDrop) {
				score = score + 2;
			} else if (softDrop) {
				score = score + 1;
			}
		}
		highScore = Math.max(highScore, score);
	}

	public static void activateSlack() {
		slackOnce = true;
		slack = true;
		slackTime = slackLength;
	}

	public static boolean canFallDown(int[] locationX, int[] locationY) {
		for (int xx = 0; xx < locationY.length; xx++) {
			if (locationY[xx] + 1 >= numberOfBlocksLength) {
				return false;
			}
		}
		for (int xx = 0; xx < locationY.length; xx++) {
			if (blocks[locationX[xx]][locationY[xx] + 1] == 2) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkTSpin() {
		boolean moveUp = true;
		boolean moveLeft = true;
		boolean moveRight = true;

		for (int xx = 0; xx < playLocationY.length; xx++) {
			try {
				if (blocks[playLocationX[xx] - 1][playLocationY[xx]] == 2) {
					moveLeft = false;
				}
			} catch (Exception e) {
				moveLeft = false;
			}
			try {
				if (blocks[playLocationX[xx] + 1][playLocationY[xx]] == 2) {
					moveRight = false;
				}
			} catch (Exception e) {
				moveRight = false;
			}
			try {
				if (blocks[playLocationX[xx]][playLocationY[xx] - 1] == 2) {
					moveUp = false;
				}
			} catch (Exception e) {
				moveUp = false;
			}
		}

		if (!moveLeft & !moveRight & !moveUp) {
			return true;
		}
		return false;
	}

	public static int clearLines() {
		int counter = 0;
		for (int yy = numberOfBlocksLength - 1; yy > 0; yy--) {
			boolean line = true;
			do {
				for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
					if (blocks[xx][yy] == 0) {
						line = false;
						break;
					}
				}
				if (line) {
					for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
						blocks[xx][yy] = 0;
					}
					for (int xy = yy; xy > 0; xy--) {
						for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
							blocks[xx][xy] = blocks[xx][xy - 1];
							colors[xx][xy] = colors[xx][xy - 1];
						}
					}
					for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
						blocks[xx][0] = 0;
					}
					counter++;
				}
			} while (line);
		}
		return counter;
	}

	public static int scoring(int currentDrop, boolean tSpin) {
		// Scoring Information System Startup
		if (currentDrop > 0 || tSpin) {
			clearInfo.clear();
			if (currentDrop == 1) {
				clearInfo.add(Constants.singleText);
			} else if (currentDrop == 2) {
				clearInfo.add(Constants.doubleText);
			} else if (currentDrop == 3) {
				clearInfo.add(Constants.tripleText);
			} else if (currentDrop == 4) {
				clearInfo.add(Constants.tetrisText);
			}
			if (tSpin) {
				clearInfo.add(Constants.twistText);
				if (kick) {
					clearInfo.add(Constants.kickText);
				}
			}
		}

		// Scoring System
		int addScore = 0;

		if (currentDrop > 0) {
			lastDifficult = difficult;
			difficult = false;
		}

		if (currentDrop == 1 & tSpin & !kick) {
			addScore = 800;
			difficult = true;
		} else if (currentDrop == 1 & tSpin & kick) {
			addScore = 200;
			difficult = true;
		} else if (currentDrop == 2 & tSpin) {
			addScore = 1200;
			difficult = true;
		} else if (currentDrop == 3 & tSpin) {
			addScore = 1600;
			difficult = true;
		} else if (currentDrop == 0 & tSpin & kick) {
			addScore = 100;
		} else if (currentDrop == 0 & tSpin) {
			addScore = 400;
		} else if (currentDrop == 1) {
			addScore = 100;
		} else if (currentDrop == 2) {
			addScore = 300;
		} else if (currentDrop == 3) {
			addScore = 500;
		} else if (currentDrop == 4) {
			addScore = 800;
			difficult = true;
		}

		if (lastDifficult & difficult & currentDrop > 0) {
			addScore = (int) (addScore * 1.5);
			clearInfo.add(Constants.backToBackText);
		}

		if (currentDrop > 0) {
			if (combo > 0) {
				clearInfo.add(combo + " Chain");
				addScore = addScore + 50 * combo;
			}
			combo = combo + 1;
		} else {
			combo = 0;
		}

		if (addScore > 0) {
			clearInfo.add("+" + addScore);
		}

		return addScore;
	}

	// Gravity falling mechanic.
	// totalGrav is incremented by gravity every tick.
	// when totalGrav is higher than 1, the shape moves down 1 block.
	public static void gravity() {
		gravityTicker = gravityTicker + gravity + gravityAdd;
		while (gravityTicker >= 1) {
			gravityTicker = gravityTicker - 1;
			shapeDown();
		}
	}

	public static void changeGravity() {
		while (linesCleared >= linesClearedFloor + linesPerLevel) {
			level = level + 1;
			linesClearedFloor = linesClearedFloor + linesPerLevel;
			if (gravityAdd < 20) {
				gravityAdd = level * gravityAddPerLevel;
			}
		}
		auxText = "" + (level + 1);
	}

	public static void slack() {
		if (slack) {
			slackTime = slackTime - FPS;
			if (slackTime < 0) {
				slack = false;
			}
		}
	}

	public static void updateHighScore() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		if (gameMode.equals(Constants.MARATHON_MODE)) {
			editHighScore(settings, Constants.MARATHON_SCORE);
		} else if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			editHighScore(settings, Constants.TIME_ATTACK_SCORE);
		}
	}

	public static void editHighScore(SharedPreferences settings,
			String scoreType) {
		if (score > settings.getInt(scoreType, 0)) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(scoreType, score);
			editor.commit();
			highScore = score;
		}
	}

	public static void ghostShape() {
		boolean move = true;
		detectShape();
		int[] tempPlayLocationX = new int[4];
		int[] tempPlayLocationY = new int[4];
		for (int xx = 0; xx < numberOfBlocksWidth; xx++)
			for (int yy = 0; yy < numberOfBlocksLength; yy++)
				if (blocks[xx][yy] == 3)
					blocks[xx][yy] = 0;
		for (int xx = 0; xx < playLocationY.length; xx++) {
			tempPlayLocationX[xx] = playLocationX[xx];
			tempPlayLocationY[xx] = playLocationY[xx];
		}
		do {
			move = canFallDown(tempPlayLocationX, tempPlayLocationY);
			if (!move) {
				for (int xx = 0; xx < playLocationY.length; xx++) {
					if (blocks[tempPlayLocationX[xx]][tempPlayLocationY[xx]] != 1
							& blocks[tempPlayLocationX[xx]][tempPlayLocationY[xx]] != 2) {
						blocks[tempPlayLocationX[xx]][tempPlayLocationY[xx]] = 3;
					}
				}
			} else {
				for (int xx = 0; xx < playLocationY.length; xx++) {
					tempPlayLocationY[xx] = tempPlayLocationY[xx] + 1;
				}
			}
		} while (move);
	}

	public static void moveLeft() {
		boolean move = true;
		detectShape();
		// Shape cannot go out of bounds
		for (int xx = 0; xx < playLocationY.length; xx++)
			if (playLocationX[xx] - 1 < 0)
				move = false;
		// Shape cannot overlap another shape
		if (move)
			for (int xx = 0; xx < playLocationY.length; xx++)
				if (blocks[playLocationX[xx] - 1][playLocationY[xx]] == 2)
					move = false;
		// Move and reset slack
		if (move) {
			for (int xx = 0; xx < playLocationY.length; xx++)
				blocks[playLocationX[xx]][playLocationY[xx]] = 0;
			for (int xx = 0; xx < playLocationY.length; xx++)
				blocks[playLocationX[xx] - 1][playLocationY[xx]] = 1;
		}
	}

	public static void moveRight() {
		boolean move = true;
		detectShape();
		// Shape cannot go out of bounds
		for (int xx = 0; xx < playLocationY.length; xx++)
			if (playLocationX[xx] + 1 >= numberOfBlocksWidth)
				move = false;
		// Shape cannot overlap another shape
		if (move)
			for (int xx = 0; xx < playLocationY.length; xx++)
				if (blocks[playLocationX[xx] + 1][playLocationY[xx]] == 2)
					move = false;
		// Move and reset slack
		if (move) {
			for (int xx = 0; xx < playLocationY.length; xx++)
				blocks[playLocationX[xx]][playLocationY[xx]] = 0;
			for (int xx = 0; xx < playLocationY.length; xx++)
				blocks[playLocationX[xx] + 1][playLocationY[xx]] = 1;
		}
	}

	public static void shapeTurn() {
		turnSuccess = false;
		detectShape();
		if (specificCurrentShape == 1) {
			turnShape(-1, 0, 1, 2, 2, 1, 0, -1);
			if (turnSuccess)
				specificCurrentShape = 2;
		} else if (specificCurrentShape == 2) {
			turnShape(-2, -1, 0, 1, -2, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 1;
		} else if (specificCurrentShape == 3) {
			turnShape(-2, -1, 0, 1, 0, 1, 0, -1);
			if (turnSuccess)
				specificCurrentShape = 4;
		} else if (specificCurrentShape == 4) {
			turnShape(-1, 0, 0, 1, -1, -2, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 5;
		} else if (specificCurrentShape == 5) {
			turnShape(-1, 0, 1, 2, 1, 0, -1, 0);
			if (turnSuccess)
				specificCurrentShape = 6;
		} else if (specificCurrentShape == 6) {
			turnShape(-1, 0, 0, 1, -1, 0, 2, 1);
			if (turnSuccess)
				specificCurrentShape = 3;
		} else if (specificCurrentShape == 7) {
			turnShape(0, -1, 0, 1, -2, 1, 0, -1);
			if (turnSuccess)
				specificCurrentShape = 8;
		} else if (specificCurrentShape == 8) {
			turnShape(-1, 0, 1, 2, -1, 0, 1, 0);
			if (turnSuccess)
				specificCurrentShape = 9;
		} else if (specificCurrentShape == 9) {
			turnShape(-1, 0, 1, 0, 1, 0, -1, 2);
			if (turnSuccess)
				specificCurrentShape = 10;
		} else if (specificCurrentShape == 10) {
			turnShape(-2, -1, 0, 1, 0, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 7;
		} else if (specificCurrentShape == 11) {
			turnShape(0, -1, 0, 0, 0, -1, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 12;
		} else if (specificCurrentShape == 12) {
			turnShape(1, 0, 0, 0, -1, 0, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 13;
		} else if (specificCurrentShape == 13) {
			turnShape(0, 0, 1, 0, 0, 0, 1, 0);
			if (turnSuccess)
				specificCurrentShape = 14;
		} else if (specificCurrentShape == 14) {
			turnShape(0, 0, 0, -1, 0, 0, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 11;
		} else if (specificCurrentShape == 15) {
			turnShape(0, 0, -2, 0, 0, -1, -1, 0);
			if (turnSuccess)
				specificCurrentShape = 16;
		} else if (specificCurrentShape == 16) {
			turnShape(0, 0, 0, 2, 0, 0, 1, 1);
			if (turnSuccess)
				specificCurrentShape = 15;
		} else if (specificCurrentShape == 17) {
			turnShape(-1, -1, 0, 0, -2, 0, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 18;
		} else if (specificCurrentShape == 18) {
			turnShape(1, 0, 0, 1, 0, 0, 0, 2);
			if (turnSuccess)
				specificCurrentShape = 17;
		}
		// Reset slack if turn is successful
		if (turnSuccess) {
			slackOnce = false;
		}
	}

	public static void shapeTurnCC() {
		turnSuccess = false;
		detectShape();
		if (specificCurrentShape == 1) {
			turnShape(-2, -1, 0, 1, 2, 1, 0, -1);
			if (turnSuccess)
				specificCurrentShape = 2;
		} else if (specificCurrentShape == 2) {
			turnShape(-1, 0, 1, 2, -2, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 1;
		} else if (specificCurrentShape == 3) {
			turnShape(0, -1, 0, 1, -2, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 6;
		} else if (specificCurrentShape == 4) {
			turnShape(1, 2, 0, -1, -1, 0, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 3;
		} else if (specificCurrentShape == 5) {
			turnShape(-1, 0, 1, 0, -1, 0, 1, 2);
			if (turnSuccess)
				specificCurrentShape = 4;
		} else if (specificCurrentShape == 6) {
			turnShape(1, 0, -2, -1, -1, 0, 1, 0);
			if (turnSuccess)
				specificCurrentShape = 5;
		} else if (specificCurrentShape == 7) {
			turnShape(2, -1, 0, 1, 0, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 10;
		} else if (specificCurrentShape == 8) {
			turnShape(1, 0, -1, 0, -1, 0, 1, 2);
			if (turnSuccess)
				specificCurrentShape = 7;
		} else if (specificCurrentShape == 9) {
			turnShape(-1, 0, 1, -2, -1, 0, 1, 0);
			if (turnSuccess)
				specificCurrentShape = 8;
		} else if (specificCurrentShape == 10) {
			turnShape(0, 1, 0, -1, -2, -1, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 9;
		} else if (specificCurrentShape == 11) {
			turnShape(0, 0, 0, 1, 0, 0, 0, -1);
			if (turnSuccess)
				specificCurrentShape = 14;
		} else if (specificCurrentShape == 12) {
			turnShape(0, 0, 0, 1, 0, 0, 0, 1);
			if (turnSuccess)
				specificCurrentShape = 11;
		} else if (specificCurrentShape == 13) {
			turnShape(-1, 0, 0, 0, 1, 0, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 12;
		} else if (specificCurrentShape == 14) {
			turnShape(-1, 0, 0, 0, -1, 0, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 13;
		} else if (specificCurrentShape == 15) {
			turnShape(0, 0, -2, 0, 0, -1, -1, 0);
			if (turnSuccess)
				specificCurrentShape = 16;
		} else if (specificCurrentShape == 16) {
			turnShape(0, 0, 0, 2, 0, 0, 1, 1);
			if (turnSuccess)
				specificCurrentShape = 15;
		} else if (specificCurrentShape == 17) {
			turnShape(-1, -1, 0, 0, -2, 0, 0, 0);
			if (turnSuccess)
				specificCurrentShape = 18;
		} else if (specificCurrentShape == 18) {
			turnShape(1, 0, 0, 1, 0, 0, 0, 2);
			if (turnSuccess)
				specificCurrentShape = 17;
		}
		// Reset slack if turn is successful
		if (turnSuccess) {
			slackOnce = false;
		}
	}

	public static void turnShape(int x1, int x2, int x3, int x4, int y1,
			int y2, int y3, int y4) {
		kick = false;
		int[] x = new int[4];
		int[] y = new int[4];
		x[0] = x1;
		x[1] = x2;
		x[2] = x3;
		x[3] = x4;
		y[0] = y1;
		y[1] = y2;
		y[2] = y3;
		y[3] = y4;

		for (int yy = 0; yy <= 2; yy++) {

			if (yy == 1)
				yy = -1;
			else if (yy != 0)
				yy = yy - 1;
			for (int xy = 0; xy <= 3; xy++) {

				if (xy == 1)
					xy = -1;
				else if (xy != 0)
					xy = xy - 1;

				boolean ok = true;

				for (int xx = 0; xx < playLocationX.length; xx++) {
					if (playLocationX[xx] + x[xx] + xy > numberOfBlocksWidth - 1
							| playLocationX[xx] + x[xx] + xy < 0) {
						ok = false;
					}
				}
				for (int xx = 0; xx < playLocationY.length; xx++) {
					if (playLocationY[xx] + y[xx] - yy >= 22
							| playLocationY[xx] + y[xx] - yy < 0) {
						ok = false;
					}
				}
				try {
					if (ok) {
						if (blocks[playLocationX[0] + x[0] + xy][playLocationY[0]
								+ y[0] - yy] != 2
								& blocks[playLocationX[1] + x[1] + xy][playLocationY[1]
										+ y[1] - yy] != 2
								& blocks[playLocationX[2] + x[2] + xy][playLocationY[2]
										+ y[2] - yy] != 2
								& blocks[playLocationX[3] + x[3] + xy][playLocationY[3]
										+ y[3] - yy] != 2) {
							for (int xx = 0; xx < playLocationY.length; xx++)
								blocks[playLocationX[xx]][playLocationY[xx]] = 0;
							for (int xx = 0; xx < playLocationY.length; xx++)
								blocks[playLocationX[xx] + x[xx] + xy][playLocationY[xx]
										+ y[xx] - yy] = 1;
							turnSuccess = true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (xy == -1)
					xy = 1;
				else if (xy != 0)
					xy = xy + 1;
				if (turnSuccess) {
					if (xy != 0) {
						kick = true;
					}
					break;
				}
			}
			if (yy == -1)
				yy = 1;
			else if (yy != 0)
				yy = yy + 1;
			if (turnSuccess) {
				if (yy != 0) {
					kick = true;
				}
				break;
			}
		}
	}

	public static void coloring() {
		detectShape();
		for (int xx = 0; xx < playLocationX.length; xx++)
			colors[playLocationX[xx]][playLocationY[xx]] = currentShape;

	}

	public static void pauseGame(boolean changeTo) {
		pause = changeTo;
		if (pause && gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown.cancel();
		} else if (!pause && gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown = new CustomCountDownTimer(currentCountDownTime, FPS);
			countDown.start();
		}
		clock = System.nanoTime();
		updateHighScore();
	}

	// Controls
	public static OnTouchListener getOnTouchListener() {
		return new OnTouchListener() {
			float x;
			float y;
			float prevY;
			boolean turn;
			boolean ignoreInputs;
			float startingX;
			float startingY;

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (!pause & !lose) {
					switch (arg1.getAction()) {

					case MotionEvent.ACTION_DOWN:
						x = arg1.getX();
						y = arg1.getY();
						startingX = x;
						startingY = y;
						prevY = y;
						turn = true;
						ignoreInputs = false;
						return true;
					case MotionEvent.ACTION_MOVE:
						x = arg1.getX();
						y = arg1.getY();
						float dy = y - prevY;
						if (!ignoreInputs) {
							if (dy > flickSensitivity) {
								hardDrop = true;
								slack = false;
								gravity = 20.0;
								turn = false;
								ignoreInputs = true;
							} else if (dy < -flickSensitivity) {
								holdShape();
								turn = false;
								ignoreInputs = true;
							} else if (x - startingX > squareSide) {
								startingX = x;
								moveRight();
								turn = false;
							} else if (x - startingX < -squareSide) {
								startingX = x;
								moveLeft();
								turn = false;
							} else if (y - startingY > dragSensitivity
									& !ignoreInputs) {
								gravity = softDropSpeed;
								softDrop = true;
								turn = false;
								// ignoreInputs = true;
							}
						}
						prevY = y;
						coloring();
						return true;
					case MotionEvent.ACTION_UP:
						x = arg1.getX();
						y = arg1.getY();
						if (turn) {
							if (mainFieldShiftX + holdShapeXStarting < x
									&& x < mainFieldShiftX + holdShapeXStarting
											+ numberOfHoldShapeWidth
											* squareSide
									&& mainFieldShiftY < y
									&& y < mainFieldShiftY + holdShapeYStarting
											+ numberOfHoldShapeLength
											* squareSide && tapToHold) {
								holdShape();
							} else if (x < squareSide * numberOfBlocksWidth
									* 0.5) {
								shapeTurnCC();
							} else {
								shapeTurn();
							}
						}
						if (softDrop) {
							gravity = defaultGravity;
							softDrop = false;
						}
						coloring();
						return true;
					}
				}
				return false;
			}

		};
	}

	public static void getLayout(int width, int height) {
		squareSide = (int) Math.min(width / numSquaresX, height / numSquaresY);
		holdShapeXStarting = squareSide * (numberOfBlocksWidth + 1);
		holdShapeYStarting = squareSide * 1;
		nextShapeYStarting = squareSide * 7;
		nextShapeY2Starting = squareSide * 10;
		nextShapeY3Starting = squareSide * 13;
		clearInfoYStarting = squareSide * 16;
		scoreInfoYStarting = squareSide * 4;
		auxInfoYStarting = squareSide * 5;
		auxInfoXStarting = holdShapeXStarting - squareSide * 2 / 3;
		holdTextYStarting = (int) (squareSide * 0.6);
		nextTextYStarting = squareSide * 6;
		highScoreYStarting = (int) (squareSide * 21);

		mainFieldShiftX = squareSide / 2;
		mainFieldShiftY = squareSide / 2;
		getScreenSize = false;
	}

	public static int chooseColor(int x) {
		if (x == 0) {
			// @@@@
			return Color.CYAN;
		} else if (x == 1) {
			return Color.BLUE;
		} else if (x == 2) {
			// @@@
			// @
			return ORANGE;
		} else if (x == 3) {
			return Color.MAGENTA;
		} else if (x == 4) {
			// @@
			// @@
			return Color.GREEN;
		} else if (x == 5) {
			return Color.RED;
		} else {
			// @@
			// @@
			return Color.YELLOW;
		}
	}

	public static void newGame() {
		updateHighScore();
		// Reset Variables
		for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
			for (int yy = 0; yy < numberOfBlocksLength; yy++) {
				blocks[xx][yy] = 0;
				colors[xx][yy] = 0;
				if (xx < numberOfHoldShapeWidth & yy < numberOfHoldShapeLength) {
					holdBlocks[xx][yy] = 0;
					nextBlocks[xx][yy] = 0;
					next2Blocks[xx][yy] = 0;
					next3Blocks[xx][yy] = 0;
				}
			}
		}
		blocks = new int[numberOfBlocksWidth][numberOfBlocksLength];
		colors = new int[numberOfBlocksWidth][numberOfBlocksLength];
		holdBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
		nextBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
		next2Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
		next3Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
		playLocationX = new int[4];
		playLocationY = new int[4];
		nextShape = -1;
		next2Shape = -1;
		next3Shape = -1;
		holdShape = -1;
		specificCurrentShape = -1;
		currentShape = -1;
		shapeList = new ArrayList<Integer>();
		for (int xx = 0; xx < 7; xx++) {
			shapeList.add(xx);
		}
		score = 0;
		gravityAdd = 0;
		linesCleared = 0;
		linesClearedFloor = 0;
		level = 0;
		clearInfo.clear();
		difficult = false;
		lastDifficult = false;
		pause = false;
		win = false;
		lose = false;
		clock = System.nanoTime();
		auxText = "";
		currentCountDownTime = countDownTime * 1000;
		countDown.cancel();
		if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown = new CustomCountDownTimer(currentCountDownTime, FPS);
			countDown.start();
		} else if (gameMode.equals(Constants.MARATHON_MODE)) {
			auxText = "" + (level + 1);
		}
		// Pick the new shape
		pickShape();
	}

}
