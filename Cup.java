/**
 * Una taza que se puede apilar en la torre.
 * Visualmente está hecha de tres rectángulos: base, pared izquierda y pared derecha.
 * Puede tener una tapa, y cuando la tiene ambas se mueven juntas.
 *
 * @author Samuel Mena
 * @version 2.0
 */
public class Cup {
    private int number;
    private int xPos = 70;
    private int yPos = 15;
    private int cupWidth = 40;
    private int cupHeight = 30;
    private boolean hasLid = false;
    private Lid lid;
    private Rectangle base;
    private Rectangle leftWall;
    private Rectangle rightWall;

    // inicializa la taza con su número y arma la estructura visual
    public Cup(int number) {
        this.number = number;
        this.base = new Rectangle();
        this.leftWall = new Rectangle();
        leftWall.moveVertical(-cupHeight);
        this.rightWall = new Rectangle();
        rightWall.moveHorizontal(cupWidth - 10);
        rightWall.moveVertical(-cupHeight);
    }

    // retorna el número de la taza
    public int getNumber() {
        return number;
    }

    public int getYPos()    { return yPos; }
    public int getXPos()    { return xPos; }
    public int getWidth()   { return cupWidth; }

    // dice si la taza tiene tapa en este momento
    public boolean hasLid() {
        return hasLid;
    }

    public void makeVisible() {
        base.makeVisible();
        leftWall.makeVisible();
        rightWall.makeVisible();
    }

    public void makeInvisible() {
        base.makeInvisible();
        leftWall.makeInvisible();
        rightWall.makeInvisible();
    }

    public void setColor(String color) {
        base.changeColor(color);
        leftWall.changeColor(color);
        rightWall.changeColor(color);
    }

    // mueve toda la taza a la nueva posición calculando el desplazamiento
    public void setPosition(int x, int y) {
        int dx = x - xPos;
        int dy = y - yPos;
        this.xPos = x;
        this.yPos = y;

        base.moveHorizontal(dx);
        base.moveVertical(dy);
        leftWall.moveHorizontal(dx);
        leftWall.moveVertical(dy);
        rightWall.moveHorizontal(dx);
        rightWall.moveVertical(dy);
    }

    // ajusta el tamaño y reposiciona las paredes según el nuevo ancho/alto
    public void setSize(int height, int width) {
        int oldHeight = this.cupHeight;
        int oldWidth = this.cupWidth;
        this.cupWidth = width;
        this.cupHeight = height;
        base.changeSize(10, width);
        leftWall.changeSize(height, 10);
        rightWall.changeSize(height, 10);
        leftWall.moveVertical(oldHeight - height);
        rightWall.moveHorizontal(width - oldWidth);
        rightWall.moveVertical(oldHeight - height);
    }

    // asocia una tapa a esta taza
    public void putLid(Lid lid) {
        this.lid = lid;
        this.hasLid = true;
    }

    // quita la tapa y la retorna
    public Lid removeLid() {
        Lid temp = this.lid;
        this.lid = null;
        this.hasLid = false;
        return temp;
    }

    public Lid getLid() {
        return lid;
    }
}
