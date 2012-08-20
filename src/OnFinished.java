import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class OnFinished implements EventHandler<ActionEvent> {

	public Hurdles group;

	public OnFinished(Hurdles group) {
		super();
		this.group = group;
	}

	@Override
	public void handle(ActionEvent t) {
		ObservableList<Node> children = group.getChildren();

		Helicopter helicopter = Helicopter.getInstance();
		Bounds bound = helicopter.getBoundsInParent();
		for (int i = 0; i < children.size(); i++) {
			Node n = children.get(i);
			if (n instanceof Hurdle) {
				// Check for collision
				if (n.getBoundsInParent().intersects(bound)) {
					Hurdles.stopAll();
				} else {
					n.setTranslateX(n.getTranslateX() - JHelicopter.wallHurdelTranslateX);
					if (n.getLayoutX() + n.getTranslateX()
							+ n.getBoundsInLocal().getWidth() <= 0) {
						// remove and create
						group.getChildren().remove(n);
						group.getChildren().add(
								new Hurdle(JHelicopter.width,
										JHelicopter.RANDOM.nextInt(300) + 50));
					}
				}
			} else if (n instanceof Wall) {
				if (n.getBoundsInParent().intersects(bound)) {
					Hurdles.stopAll();
				} else {
					n.setTranslateX(n.getTranslateX() - JHelicopter.wallHurdelTranslateX);
					if (n.getLayoutX() + n.getTranslateX()
							+ n.getBoundsInLocal().getWidth() <= 0) {
						// remove and create
						group.getChildren().remove(n);
					}

				}
			}  else if (n instanceof Circle) {
				n.setTranslateX(n.getTranslateX() - JHelicopter.wallHurdelTranslateX);
				if (n.getLayoutX() + n.getTranslateX()
						+ n.getBoundsInLocal().getWidth() <= 0) {
					// remove and create
					group.getChildren().remove(n);
			}
		}
	} group.getChildren().add(helicopter.getSmoke());
	}
}
