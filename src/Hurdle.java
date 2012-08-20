import javafx.scene.shape.Rectangle;


	class Hurdle extends Rectangle {
		final int height = 130;
		
		public Hurdle() {
			super();
			setFill(JHelicopter.fillColor);
			setHeight(height);
			setWidth(JHelicopter.hurdleWidth);
			setCache(true);
		}

		public Hurdle(double layoutX, double layoutY) {
			super();
			setLayoutX(layoutX);
			setLayoutY(layoutY);
			setFill(JHelicopter.fillColor);
			setHeight(height);
			setWidth(JHelicopter.hurdleWidth);
			setCache(true);
		}
	}
	
