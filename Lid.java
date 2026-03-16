/**
 * La tapa de una taza en la torre.
 * Cada tapa tiene una altura fija de 1 cm y está ligada a su taza por el número.
 * Si la taza se mueve, la tapa va con ella.
 *
 * @author Samuel Mena
 * @version 2.0
 */
public class Lid {
    private Rectangle visual;
    private int cupNumber;

    // recibe el número de la taza a la que pertenece
    public Lid(int cupNumber) {
        this.cupNumber = cupNumber;
        this.visual = new Rectangle();
    }

    public void makeVisible() {
        visual.makeVisible();
    }

    public void makeInvisible() {
        visual.makeInvisible();
    }

    public void setColor(String color) {
        visual.changeColor(color);
    }

    // retorna el número de la taza asociada
    public int getCupNumber() {
        return cupNumber;
    }

    // mueve la tapa a la posición indicada
    public void setPosition(int x, int y) {
        visual.moveHorizontal(x - 70);
        visual.moveVertical(y - 15);
    }

    public void setSize(int width) {
        visual.changeSize(5, width);
    }

    public Rectangle getVisual() {
        return visual;
    }
}