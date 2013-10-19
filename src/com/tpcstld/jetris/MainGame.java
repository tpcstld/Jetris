package com.tpcstld.jetris;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

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

	// Internal Options
	static int test = 0;
	static final int numSquaresX = 16; // Total number of columns
	static final int numSquaresY = 22; // total number of rows
	static final double textScaleSize = 0.85; // Text scaling
	static final int FPS = 1000 / 30;
	static int ORANGE;
	static Context mContext;

	// External Options
	public static double defaultGravity = 0.05; // The default gravity of the
												// game value
	public static int flickSensitivity = 30; // How sensitive is the flick
												// gesture
	public static int slackLength = 1000; // How long the stack goes on for in
	public static double softDropSpeed = 0.45; // How fast soft dropping is
	public static int dragSensitivity = 100;
	public static long countDownTime = 120;
	public static int linesPerLevel = 10;
	public static double gravityAddPerLevel = 0.025;
	public static int textColor = Color.BLACK;

	// Game Mode
	public static String gameMode = "";

	// Timers
	static Timer time = new Timer(true); // This is the slack timer
	static long currentCountDownTime = countDownTime * 1000; // Time remaining
																// on time
																// attack mode
	static CountDownTimer countDown = new CustomCountDownTimer(
			currentCountDownTime, FPS);

	static String auxText = ""; // Text for displaying the time left
	static boolean slack = false; // Whether or not slack is currently active
	public static boolean pause = false; // Whether or not the pause is
											// currently paused
	static boolean lose = false; // Whether or not the game is still in progress
	static boolean win = false; // Whether or not the game is finished
	static boolean holdOnce = false; // Whether or not the block has already
										// been held once
	static boolean hardDrop = false; // Whether or not harddropping is active
	static boolean slackOnce = false; // Whether or not slack as already been
										// activated
	static boolean turnSuccess = false; // Whether or not a turn was successful
	static boolean softDrop = false; // Whether or not softdropping is active
	static boolean kick = false; // Whether or not the block was kicked to
									// location
	static boolean difficult = false; // Whether or not a clear was considered
										// to be "hard"
	static boolean lastDifficult = false; // Whether or not the last clear was
											// considered to be "hard"
	static int score = 0; // The current score
	static int highScore = 0;
	static int level = 0;
	static int linesCleared = 0;
	static int linesClearedFloor = 0;
	static int mainFieldShiftX; // How much the screen is shifted to the right
	static int mainFieldShiftY; // How much the screen is shifted downwards
	static int squareSide; // The size of one square
	static int numberOfBlocksWidth = 10; // The number of columns of blocks in
											// the main field
	static int numberOfBlocksLength = 22; // The number of rows of blocks in the
											// main field
	static int numberOfHoldShapeWidth = 4; // The width of the auxiliary boxes
	static int numberOfHoldShapeLength = 2; // The length of the auxiliary boxes
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
	static int thisShape = -1; // The NEXT shape on the playing field.
	static int nextShape = -1; // The NEXT2 shape on the playing field.
	static int nextShape1 = -1; // The NEXT3 shaped on the playing field.
	static int holdShape = -1; // The shape currently being held.
	static int lastShape = -1; // The CURRENT shape on the playing field.
	// A list containing the shapes left in the current bag
	static ArrayList<Integer> shapeList = new ArrayList<Integer>();
	static int shape = 0; // The current shape of the block
	static int combo = 0; // The current combo
	static Random r = new Random(); // The randomizer
	// Array detailing the type of block in each square
	static int[][] blocks = new int[numberOfBlocksWidth][numberOfBlocksLength];
	// Array detailing the colour in each square
	static int[][] colors = new int[numberOfBlocksWidth][numberOfBlocksLength];
	// Array displaying the block in hold
	static int[][] holdBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next block
	static int[][] nextBlocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next2 block
	static int[][] next2Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	// Array displaying the next3 block
	static int[][] next3Blocks = new int[numberOfHoldShapeWidth][numberOfHoldShapeLength];
	static int[] playLocationX = new int[4]; // X-coords of the blocks in play
	static int[] playLocationY = new int[4]; // Y-coords of the blocks in play
	static double gravity = defaultGravity; // The current gravity of the game
	static double gravityAdd = 0;
	static double totalGrav = 0; // The current gravity ticker
	static long clock = System.currentTimeMillis(); // Tracks the real time for
													// fps
	static boolean getScreenSize = false; // Initial getting screen size
											// variable
	public static boolean startNewGame = true; // Whether it should be a new
												// game or not
	static ArrayList<String> clearInfo = new ArrayList<String>();

	// Blocks Data:
	// 0 = empty space
	// 1 = active space
	// 2 = locked space
	// 3 = ghost space

	public MainGame(Context context) {
		super(context);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		getSettings(settings);

		mContext = context;
		// Setting the orange color since there is no default
		ORANGE = getResources().getColor(R.color.orange);

		// Create the object to receive touch input
		setOnTouchListener(getOnTouchListener());
		if (startNewGame) {
			newGame();
		}

	}

	@Override
	public void onDraw(Canvas canvas) {

		// Get the screen size and adjust the game screen proportionally if
		// needed.
		if (getScreenSize) {
			int width = this.getMeasuredWidth();
			int height = this.getMeasuredHeight();
			getLayout(width, height);
			paint.setTextSize((float) (squareSide * textScaleSize));
		}

		paint.setColor(textColor);
		paint.setTextSize((float) (squareSide * textScaleSize * textScaleSize));
		// Drawing "Hold: " text box
		canvas.drawText("Hold: ", holdShapeXStarting + mainFieldShiftX,
				holdTextYStarting + mainFieldShiftY, paint);

		// Drawing "Next: " text box
		canvas.drawText("Next: ", holdShapeXStarting + mainFieldShiftX,
				nextTextYStarting + mainFieldShiftY, paint);

		canvas.drawText("High Score: " + highScore, mainFieldShiftX,
				highScoreYStarting + mainFieldShiftY, paint);

		// paint.setTextSize((float) (squareSide * textScaleSize));

		// Drawing Score text box
		canvas.drawText("Score: " + score, auxInfoXStarting + mainFieldShiftX,
				scoreInfoYStarting + mainFieldShiftY, paint);

		// Drawing aux text box
		if (gameMode.equals(Constants.MARATHON_MODE)) {
			String tempText = auxText + " ("
					+ (linesPerLevel - linesCleared + linesClearedFloor) + ")";
			canvas.drawText(tempText, auxInfoXStarting + mainFieldShiftX,
					auxInfoYStarting + mainFieldShiftY, paint);
		} else if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			canvas.drawText(auxText, auxInfoXStarting + mainFieldShiftX,
					auxInfoYStarting + mainFieldShiftY, paint);
		}

		// Drawing clearInfo text box
		for (int xx = 0; xx < clearInfo.size(); xx++) {
			canvas.drawText(clearInfo.get(xx), holdShapeXStarting
					+ mainFieldShiftX - squareSide / 2, clearInfoYStarting
					+ mainFieldShiftY + squareSide * xx, paint);
		}

		gravity();
		coloring();
		ghostShape();
		for (int xx = mainFieldShiftX; xx <= squareSide * numberOfBlocksWidth
				+ mainFieldShiftX; xx += squareSide) {
			canvas.drawLine(xx, mainFieldShiftY, xx, squareSide
					* (numberOfBlocksLength - 2) + mainFieldShiftY, paint);
		}

		for (int xx = mainFieldShiftY; xx <= squareSide
				* (numberOfBlocksLength - 2) + mainFieldShiftY; xx += squareSide)
			canvas.drawLine(mainFieldShiftX, xx, squareSide
					* numberOfBlocksWidth + mainFieldShiftX, xx, paint);

		for (int x = 0; x < 8; x++) {
			// Setting the color of the blocks
			paint.setColor(chooseColor(x));

			for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
				for (int yy = 2; yy < numberOfBlocksLength; yy++) {
					if (blocks[xx][yy] != 0 & blocks[xx][yy] != 3
							& colors[xx][yy] == x) {
						canvas.drawRect(xx * squareSide + mainFieldShiftX,
								(yy - 2) * squareSide + mainFieldShiftY, xx
										* squareSide + squareSide
										+ mainFieldShiftX, (yy - 2)
										* squareSide + squareSide
										+ mainFieldShiftY, paint);
					}
				}
			}

			// Ghost shape drawing
			for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
				for (int yy = 0; yy < numberOfBlocksLength; yy++) {
					if (blocks[xx][yy] == 3 & lastShape == x) {
						canvas.drawCircle((float) (xx * squareSide + squareSide
								* 0.5 + mainFieldShiftX),
								(float) ((yy - 2) * squareSide + squareSide
										* 0.5 + mainFieldShiftY),
								(float) (squareSide * 0.5), paint);
					}
				}
			}

			for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
				for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
					if (holdBlocks[xx][yy] == 1 & holdShape == x) {
						canvas.drawRect(xx * squareSide + holdShapeXStarting
								+ mainFieldShiftX, yy * squareSide
								+ mainFieldShiftY + holdShapeYStarting, xx
								* squareSide + holdShapeXStarting + squareSide
								+ mainFieldShiftX, yy * squareSide + squareSide
								+ mainFieldShiftY + holdShapeYStarting, paint);
					}
				}
			}

			for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
				for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
					if (nextBlocks[xx][yy] == 1 & thisShape == x) {
						canvas.drawRect(xx * squareSide + holdShapeXStarting
								+ mainFieldShiftX, yy * squareSide
								+ nextShapeYStarting + mainFieldShiftY, xx
								* squareSide + holdShapeXStarting + squareSide
								+ mainFieldShiftX, yy * squareSide
								+ nextShapeYStarting + squareSide
								+ mainFieldShiftY, paint);
					}
				}
			}

			for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
				for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
					if (next2Blocks[xx][yy] == 1 & nextShape == x) {
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

			for (int xx = 0; xx < numberOfHoldShapeWidth; xx++) {
				for (int yy = 0; yy < numberOfHoldShapeLength; yy++) {
					if (next3Blocks[xx][yy] == 1 & nextShape1 == x) {
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
		}
		// Displaying the big text if needed
		if (lose) {
			// Change the font settings
			paint.setColor(Color.RED);
			paint.setTextSize((float) (squareSide * 4));
			paint.setShadowLayer((float) 5, 0, 0, Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("GAME", width / 2, mainFieldShiftY + length / 3,
					paint);
			canvas.drawText("OVER", width / 2,
					mainFieldShiftY + length * 2 / 3, paint);
			// Revert text settings to normal
			paint.setShadowLayer((float) 0, 0, 0, Color.BLACK);
			paint.setTextSize((float) (squareSide * textScaleSize));
			paint.setTextAlign(Paint.Align.LEFT);
		} else if (win) {
			// Change the font settings
			paint.setColor(Color.GREEN);
			paint.setTextSize((float) (squareSide * 4));
			paint.setShadowLayer((float) 5, 0, 0, Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("TIME'S", width / 2, mainFieldShiftY + length / 3,
					paint);
			canvas.drawText("UP", width / 2, mainFieldShiftY + length * 2 / 3,
					paint);
			// Revert text settings to normal
			paint.setShadowLayer((float) 0, 0, 0, Color.BLACK);
			paint.setTextSize((float) (squareSide * textScaleSize));
			paint.setTextAlign(Paint.Align.LEFT);
		} else if (pause) {
			// Change the font settings
			paint.setColor(textColor);
			paint.setTextSize((float) (squareSide * 4));
			paint.setShadowLayer((float) 5, 0, 0, Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			int length = this.getMeasuredHeight();
			int width = this.getMeasuredWidth();
			// Display and align the needed text
			canvas.drawText("PAUSED", width / 2, mainFieldShiftY + length / 2,
					paint);
			// Revert text settings to normal
			paint.setShadowLayer((float) 0, 0, 0, Color.BLACK);
			paint.setTextSize((float) (squareSide * textScaleSize));
			paint.setTextAlign(Paint.Align.LEFT);
		}
		invalidate();
	}

	public void getSettings(SharedPreferences settings) {
		defaultGravity = getDoubleFromSettings(defaultGravity,
				"defaultGravity", settings);
		flickSensitivity = getIntFromSettings(flickSensitivity,
				"flickSensitivity", settings);
		slackLength = getIntFromSettings(slackLength, "slackLength", settings);
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
	// thisShape = -1 means that it is a new game
	// Affects:
	// lastShape == The CURRENT shape on the playing field.
	// thisShape == The NEXT shape on the playing field.
	// nextShape == The NEXT2 shape on the playing field.
	// nextShape1 == The NEXT3 shape on the playing field.
	public static void pickShape() {
		time.cancel();
		time = new Timer();
		if (thisShape == -1) {
			thisShape = shapeList.remove(r.nextInt(shapeList.size()));
			nextShape = shapeList.remove(r.nextInt(shapeList.size()));
			nextShape1 = shapeList.remove(r.nextInt(shapeList.size()));
		}
		displayShape(thisShape);
		lastShape = thisShape;
		thisShape = nextShape;
		nextShape = nextShape1;
		nextShape1 = shapeList.remove(r.nextInt(shapeList.size()));

		if (shapeList.size() <= 0) {
			for (int xx = 0; xx < 7; xx++) {
				shapeList.add(xx);
			}
		}

		displayBoxShape(nextBlocks, thisShape);
		displayBoxShape(next2Blocks, nextShape);
		displayBoxShape(next3Blocks, nextShape1);
		detectShape();
		holdOnce = false;
	}

	public static void holdShape() {
		if (holdShape == -1) {
			holdShape = lastShape;
			pickShape();
		} else {
			int lastShape1 = holdShape;
			holdShape = lastShape;
			lastShape = lastShape1;
			displayShape(lastShape);
		}
		holdOnce = true;
		displayBoxShape(holdBlocks, holdShape);
	}

	public static void displayShape(int thisShape) {
		for (int xx = 0; xx < numberOfBlocksWidth; xx++)
			for (int yy = 0; yy < numberOfBlocksLength; yy++)
				if (blocks[xx][yy] == 1)
					blocks[xx][yy] = 0;
		if (thisShape == 0) {
			for (int xx = 3; xx <= 6; xx++)
				blocks[xx][0] = 1;
			shape = 1;
		} else if (thisShape == 1) {
			for (int xx = 3; xx <= 5; xx++)
				blocks[xx][1] = 1;
			blocks[3][0] = 1;
			shape = 5;
		} else if (thisShape == 2) {
			for (int xx = 3; xx <= 5; xx++)
				blocks[xx][1] = 1;
			blocks[5][0] = 1;
			shape = 9;
		} else if (thisShape == 3) {
			for (int xx = 3; xx <= 5; xx++)
				blocks[xx][1] = 1;
			blocks[4][0] = 1;
			shape = 13;
		} else if (thisShape == 4) {
			blocks[4][0] = 1;
			blocks[5][0] = 1;
			blocks[3][1] = 1;
			blocks[4][1] = 1;
			shape = 15;
		} else if (thisShape == 5) {
			blocks[3][0] = 1;
			blocks[4][0] = 1;
			blocks[4][1] = 1;
			blocks[5][1] = 1;
			shape = 17;
		} else if (thisShape == 6) {
			blocks[4][0] = 1;
			blocks[5][0] = 1;
			blocks[4][1] = 1;
			blocks[5][1] = 1;
			shape = 19;
		}
	}

	public static void shapeDown() {
		if (!lose & !pause & !win) {
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
				totalGrav = 0.0;
				gravity = defaultGravity;

				int addScore = scoring(currentDrop, tSpin);

				score = score + addScore;
				linesCleared = linesCleared + currentDrop;
				if (gameMode.equals(Constants.MARATHON_MODE)) {
					changeGravity();
				}
				// Scoring System end.
				detectLose();
				pickShape();
			} else if (move) {
				// If slack is still activated
				if (slack) {
					time.cancel();
					time = new Timer();
				}
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
		}
	}

	public static void activateSlack() {
		slackOnce = true;
		slack = true;
		time.schedule(new Slack(), slackLength);
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
				clearInfo.add("Single");
			} else if (currentDrop == 2) {
				clearInfo.add("Double");
			} else if (currentDrop == 3) {
				clearInfo.add("Triple");
			} else if (currentDrop == 4) {
				clearInfo.add("Tetris");
			}
			if (tSpin) {
				clearInfo.add("T-spin");
				if (kick) {
					clearInfo.add("Kicked");
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
			clearInfo.add("Back to Back");
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

	public static void changeGravity() {
		while (linesCleared >= linesClearedFloor + linesPerLevel) {
			level = level + 1;
			linesClearedFloor = linesClearedFloor + linesPerLevel;
			if (gravityAdd < 20) {
				gravityAdd = level * gravityAddPerLevel;
			}
		}
		auxText = "Level: " + (level + 1);
	}

	public static void detectLose() {
		lose = false;
		for (int xx = 0; xx < numberOfBlocksWidth; xx++) {
			if (blocks[xx][1] == 2) {
				lose = true;
				countDown.cancel();
				break;
			}
		}
		if (lose) {
			updateHighScore();
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
		for (int xx = 0; xx < playLocationY.length; xx++)
			if (playLocationX[xx] - 1 < 0)
				move = false;
		if (move)
			for (int xx = 0; xx < playLocationY.length; xx++)
				if (blocks[playLocationX[xx] - 1][playLocationY[xx]] == 2)
					move = false;
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
		for (int xx = 0; xx < playLocationY.length; xx++)
			if (playLocationX[xx] + 1 >= numberOfBlocksWidth)
				move = false;
		if (move)
			for (int xx = 0; xx < playLocationY.length; xx++)
				if (blocks[playLocationX[xx] + 1][playLocationY[xx]] == 2)
					move = false;
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
		if (shape == 1) {
			turnShape(-1, 0, 1, 2, 2, 1, 0, -1);
			if (turnSuccess)
				shape = 2;
		} else if (shape == 2) {
			turnShape(-2, -1, 0, 1, -2, -1, 0, 1);
			if (turnSuccess)
				shape = 1;
		} else if (shape == 3) {
			turnShape(-2, -1, 0, 1, 0, 1, 0, -1);
			if (turnSuccess)
				shape = 4;
		} else if (shape == 4) {
			turnShape(-1, 0, 0, 1, -1, -2, 0, 1);
			if (turnSuccess)
				shape = 5;
		} else if (shape == 5) {
			turnShape(-1, 0, 1, 2, 1, 0, -1, 0);
			if (turnSuccess)
				shape = 6;
		} else if (shape == 6) {
			turnShape(-1, 0, 0, 1, -1, 0, 2, 1);
			if (turnSuccess)
				shape = 3;
		} else if (shape == 7) {
			turnShape(0, -1, 0, 1, -2, 1, 0, -1);
			if (turnSuccess)
				shape = 8;
		} else if (shape == 8) {
			turnShape(-1, 0, 1, 2, -1, 0, 1, 0);
			if (turnSuccess)
				shape = 9;
		} else if (shape == 9) {
			turnShape(-1, 0, 1, 0, 1, 0, -1, 2);
			if (turnSuccess)
				shape = 10;
		} else if (shape == 10) {
			turnShape(-2, -1, 0, 1, 0, -1, 0, 1);
			if (turnSuccess)
				shape = 7;
		} else if (shape == 11) {
			turnShape(0, -1, 0, 0, 0, -1, 0, 0);
			if (turnSuccess)
				shape = 12;
		} else if (shape == 12) {
			turnShape(1, 0, 0, 0, -1, 0, 0, 0);
			if (turnSuccess)
				shape = 13;
		} else if (shape == 13) {
			turnShape(0, 0, 1, 0, 0, 0, 1, 0);
			if (turnSuccess)
				shape = 14;
		} else if (shape == 14) {
			turnShape(0, 0, 0, -1, 0, 0, 0, 1);
			if (turnSuccess)
				shape = 11;
		} else if (shape == 15) {
			turnShape(0, 0, -2, 0, 0, -1, -1, 0);
			if (turnSuccess)
				shape = 16;
		} else if (shape == 16) {
			turnShape(0, 0, 0, 2, 0, 0, 1, 1);
			if (turnSuccess)
				shape = 15;
		} else if (shape == 17) {
			turnShape(-1, -1, 0, 0, -2, 0, 0, 0);
			if (turnSuccess)
				shape = 18;
		} else if (shape == 18) {
			turnShape(1, 0, 0, 1, 0, 0, 0, 2);
			if (turnSuccess)
				shape = 17;
		}
		// Reset slack if turn is successful
		if (turnSuccess) {
			slackOnce = false;
			time.cancel();
			time = new Timer();
		}
	}

	public static void shapeTurnCC() {
		turnSuccess = false;
		detectShape();
		if (shape == 1) {
			turnShape(-2, -1, 0, 1, 2, 1, 0, -1);
			if (turnSuccess)
				shape = 2;
		} else if (shape == 2) {
			turnShape(-1, 0, 1, 2, -2, -1, 0, 1);
			if (turnSuccess)
				shape = 1;
		} else if (shape == 3) {
			turnShape(0, -1, 0, 1, -2, -1, 0, 1);
			if (turnSuccess)
				shape = 6;
		} else if (shape == 4) {
			turnShape(1, 2, 0, -1, -1, 0, 0, 1);
			if (turnSuccess)
				shape = 3;
		} else if (shape == 5) {
			turnShape(-1, 0, 1, 0, -1, 0, 1, 2);
			if (turnSuccess)
				shape = 4;
		} else if (shape == 6) {
			turnShape(1, 0, -2, -1, -1, 0, 1, 0);
			if (turnSuccess)
				shape = 5;
		} else if (shape == 7) {
			turnShape(2, -1, 0, 1, 0, -1, 0, 1);
			if (turnSuccess)
				shape = 10;
		} else if (shape == 8) {
			turnShape(1, 0, -1, 0, -1, 0, 1, 2);
			if (turnSuccess)
				shape = 7;
		} else if (shape == 9) {
			turnShape(-1, 0, 1, -2, -1, 0, 1, 0);
			if (turnSuccess)
				shape = 8;
		} else if (shape == 10) {
			turnShape(0, 1, 0, -1, -2, -1, 0, 1);
			if (turnSuccess)
				shape = 9;
		} else if (shape == 11) {
			turnShape(0, 0, 0, 1, 0, 0, 0, -1);
			if (turnSuccess)
				shape = 14;
		} else if (shape == 12) {
			turnShape(0, 0, 0, 1, 0, 0, 0, 1);
			if (turnSuccess)
				shape = 11;
		} else if (shape == 13) {
			turnShape(-1, 0, 0, 0, 1, 0, 0, 0);
			if (turnSuccess)
				shape = 12;
		} else if (shape == 14) {
			turnShape(-1, 0, 0, 0, -1, 0, 0, 0);
			if (turnSuccess)
				shape = 13;
		} else if (shape == 15) {
			turnShape(0, 0, -2, 0, 0, -1, -1, 0);
			if (turnSuccess)
				shape = 16;
		} else if (shape == 16) {
			turnShape(0, 0, 0, 2, 0, 0, 1, 1);
			if (turnSuccess)
				shape = 15;
		} else if (shape == 17) {
			turnShape(-1, -1, 0, 0, -2, 0, 0, 0);
			if (turnSuccess)
				shape = 18;
		} else if (shape == 18) {
			turnShape(1, 0, 0, 1, 0, 0, 0, 2);
			if (turnSuccess)
				shape = 17;
		}
		// Reset slack if turn is successful
		if (turnSuccess) {
			slackOnce = false;
			time.cancel();
			time = new Timer();
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
			colors[playLocationX[xx]][playLocationY[xx]] = lastShape;
	}

	public static void displayBoxShape(int[][] x, int y) {
		for (int xx = 0; xx < numberOfHoldShapeWidth; xx++)
			for (int yy = 0; yy < numberOfHoldShapeLength; yy++)
				x[xx][yy] = 0;

		if (y == 0) {
			for (int xx = 0; xx <= 3; xx++)
				x[xx][0] = 1;
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

	public static void pauseGame(boolean changeTo) {
		pause = changeTo;
		if (pause && gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown.cancel();
		} else if (!pause && gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown = new CustomCountDownTimer(currentCountDownTime, FPS);
			countDown.start();
		}
		clock = System.currentTimeMillis();
	}

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
								if (!holdOnce) {
									holdShape();
								}
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
							if (x < squareSide * numberOfBlocksWidth * 0.5) {
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
		mainFieldShiftY = 0;
		getScreenSize = false;
	}

	// Gravity falling mechanic.
	// totalGrav is incremented by gravity every tick.
	// when totalGrav is higher than 1, the shape moves down 1 block.
	public static void gravity() {
		if (!lose & !pause & !win) {
			if (thisShape >= 0) {
				long temp = System.currentTimeMillis();
				long dtime = temp - clock;
				if (dtime > FPS) {
					totalGrav = totalGrav + gravity + gravityAdd;
					clock = clock + FPS;
				}
				while (totalGrav >= 1) {
					shapeDown();
					totalGrav = totalGrav - 1;
				}
			}
		}
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
		thisShape = -1;
		nextShape = -1;
		nextShape1 = -1;
		holdShape = -1;
		shape = -1;
		lastShape = -1;
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
		time.cancel();
		time = new Timer();
		clock = System.currentTimeMillis();
		auxText = "";
		currentCountDownTime = countDownTime * 1000;
		countDown.cancel();
		if (gameMode.equals(Constants.TIME_ATTACK_MODE)) {
			countDown = new CustomCountDownTimer(currentCountDownTime, FPS);
			countDown.start();
		} else if (gameMode.equals(Constants.MARATHON_MODE)) {
			auxText = "Level: " + (level + 1);
		}
		// Pick the new shape
		pickShape();
	}

}
