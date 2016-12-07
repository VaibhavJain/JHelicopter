import javafx.scene.shape.Rectangle;

public class Wall extends Rectangle {

	public Wall(double layoutX, double layoutY, int height) {
		super();
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setFill(JHelicopter.fillColor);
		setHeight(height);
		setWidth(JHelicopter.hurdleWidth);
		setCache(true);
	}

	public Wall() {
		super();
	}

	public static Wall[] getWall() {
		int range = JHelicopter.wallBlockHeight - 10;
		//int cutY = (int)JHelicopter.RANDOM.nextGaussian() * range;
		int cutY = JHelicopter.RANDOM.nextInt(range);
		cutY = cutY - ( cutY % 10 ) + 10;
		int bottomY = JHelicopter.wallBlockHeight - cutY;
// second cahnage
		Wall top = new Wall(JHelicopter.width, 0, cutY);
		Wall bottom = new Wall(JHelicopter.width,JHelicopter.height
				- bottomY, bottomY );
		Wall[] ret = { top, bottom };

		return ret;
	}

}
