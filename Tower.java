import java.util.ArrayList;
import java.util.Collections;

/**
 * Torre donde se apilan tazas con sus tapas.
 * Simula el problema del maratón de apilamiento de tazas.
 *
 * @author Samuel Mena
 * @version 2
 */
public class Tower {
    private int width;
    private int maxHeight;
    private boolean isVisible;
    private ArrayList<Cup> cups;
    private ArrayList<Lid> lids;
    private ArrayList<Object> towerItems;
    private Rectangle axisX;
    private ArrayList<Rectangle> axisY;

    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.cups = new ArrayList<>();
        this.lids = new ArrayList<>();
        this.towerItems = new ArrayList<>();
        this.isVisible = false;
        this.axisY = new ArrayList<>();
        createAxis();
    }

    // crea la torre con n tazas apiladas directamente
    public Tower(int cupsCount) {
        this(9, 1000);
        for (int i = cupsCount; i >= 1; i--) {
            pushCup(i, (2 * i - 1) * 10);
        }
    }

    // verifica que las tazas estén de mayor a menor desde la base
    public boolean ok() {
        if (cups.isEmpty()) return true;
        for (int i = 0; i < cups.size() - 1; i++) {
            if (cups.get(i).getNumber() < cups.get(i + 1).getNumber()) return false;
        }
        return true;
    }

    // retorna la altura actual de la torre según el primer elemento
    public int height() {
        if (towerItems.isEmpty()) return 0;
        Object first = towerItems.get(0);
        if (first instanceof Cup) {
            int i = ((Cup) first).getNumber();
            return 2 * i - 1;
        }
        return 1;
    }

    public void makeVisible() {
        isVisible = true;
        axisX.makeVisible();
        for (Rectangle mark : axisY) mark.makeVisible();
        for (Cup cup : cups) {
            cup.makeVisible();
            if (cup.hasLid()) cup.getLid().makeVisible();
        }
        updatePositions();
    }

    public void makeInvisible() {
        isVisible = false;
        axisX.makeInvisible();
        for (Rectangle mark : axisY) mark.makeInvisible();
        for (Cup cup : cups) {
            cup.makeInvisible();
            if (cup.hasLid()) cup.getLid().makeInvisible();
        }
    }

    // libera todo y deja la torre limpia
    public void exit() {
        makeInvisible();
        cups.clear();
        lids.clear();
        towerItems.clear();
    }

    public void pushCup(int number, int width) {
        if (cups.size() < maxHeight) {
            Cup cup = new Cup(number);
            String color;
            if (number == 1)      { color = "red"; }
            else if (number == 2) { color = "green"; }
            else if (number == 3) { color = "yellow"; }
            else {
                String[] colors = {"blue", "magenta"};
                color = colors[number % 2];
            }
            cup.setColor(color);
            cup.setSize(30, width);
            cups.add(cup);
            towerItems.add(cup);
            if (isVisible) updatePositions();
        }
    }

    public void pushLid(int i) {
        Lid lid = new Lid(i);
        lid.setColor("black");
        lids.add(lid);
        towerItems.add(lid);
    }

    // pone tapa a todas las tazas que no tengan una
    public void pushLid() {
        for (Cup cup : cups) {
            if (!cup.hasLid()) {
                Lid lid = new Lid(cup.getNumber());
                lid.setColor("black");
                cup.putLid(lid);
            }
        }
        updatePositions();
        if (isVisible) {
            for (Cup cup : cups) {
                if (cup.hasLid()) cup.getLid().makeVisible();
            }
        }
    }

    // pone tapa solo a la taza que está arriba del todo
    public void pushLidOnTop() {
        if (cups.isEmpty()) return;
        Cup top = cups.get(cups.size() - 1);
        if (!top.hasLid()) {
            Lid lid = new Lid(top.getNumber());
            lid.setColor("black");
            top.putLid(lid);
        }
        updatePositions();
        if (isVisible && top.hasLid()) top.getLid().makeVisible();
    }

    public void popCup() {
        if (!cups.isEmpty()) {
            Cup top = cups.get(cups.size() - 1);
            if (top.hasLid()) lids.add(top.removeLid());
            top.makeInvisible();
            cups.remove(cups.size() - 1);
            updatePositions();
        }
    }

    public void popLid() {
        if (!cups.isEmpty()) {
            Cup top = cups.get(cups.size() - 1);
            if (top.hasLid()) {
                Lid lid = top.removeLid();
                lid.makeInvisible();
                lids.add(lid);
            }
        }
    }

    public void removeCup(int number) {
        for (int i = 0; i < cups.size(); i++) {
            if (cups.get(i).getNumber() == number) {
                Cup cup = cups.get(i);
                if (cup.hasLid()) lids.add(cup.removeLid());
                cup.makeInvisible();
                cups.remove(i);
                break;
            }
        }
    }

    public void removeLid(int cupNumber) {
        for (int i = 0; i < lids.size(); i++) {
            if (lids.get(i).getCupNumber() == cupNumber) {
                lids.remove(i);
                break;
            }
        }
    }

    // asocia tapas sueltas a sus tazas correspondientes
    public void cover() {
        ArrayList<Lid> lidsToRemove = new ArrayList<>();
        for (Object obj : towerItems) {
            if (obj instanceof Cup) {
                Cup cup = (Cup) obj;
                if (!cup.hasLid()) {
                    Lid lid = findLooseLid(cup.getNumber());
                    if (lid != null) {
                        lid.setColor("black");
                        cup.putLid(lid);
                        lids.remove(lid);
                        lidsToRemove.add(lid);
                    }
                }
            }
        }
        for (Lid lid : lidsToRemove) towerItems.remove(lid);
        updatePositions();
    }

    public void swap(String[] o1, String[] o2) {
        int index1 = findIndex(o1);
        int index2 = findIndex(o2);
        if (index1 == -1 || index2 == -1) return;
        Collections.swap(towerItems, index1, index2);
        if (isVisible) updatePositions();
    }

    // ordena las tazas de mayor a menor (de abajo hacia arriba)
    public void orderTower() {
        ArrayList<Cup> sortedCups = new ArrayList<>(cups);
        sortedCups.sort((c1, c2) -> Integer.compare(c2.getNumber(), c1.getNumber()));
        towerItems.clear();
        cups.clear();
        for (Cup cup : sortedCups) {
            cups.add(cup);
            towerItems.add(cup);
        }
        // las tapas sueltas van al final
        for (Lid lid : lids) towerItems.add(lid);
        if (isVisible) updatePositions();
    }

    public void reverseTower() {
        Collections.reverse(towerItems);
        if (isVisible) updatePositions();
    }

    // sugiere el swap que más reduciría la altura actual
    public String[][] swapToReduce() {
        if (towerItems.isEmpty()) return null;
        Object first = towerItems.get(0);

        // si arriba hay una lid, buscar una cup para ponerla primero
        if (first instanceof Lid) {
            for (int i = 1; i < towerItems.size(); i++) {
                if (towerItems.get(i) instanceof Cup) {
                    return new String[][]{getIdentifier(0), getIdentifier(i)};
                }
            }
        }

        if (first instanceof Cup) {
            int firstNum = ((Cup) first).getNumber();
            // buscar una cup más pequeña
            for (int i = 1; i < towerItems.size(); i++) {
                Object obj = towerItems.get(i);
                if (obj instanceof Cup && ((Cup) obj).getNumber() < firstNum) {
                    return new String[][]{getIdentifier(0), getIdentifier(i)};
                }
            }
            // si no hay cup más pequeña, buscar una lid
            for (int i = 1; i < towerItems.size(); i++) {
                if (towerItems.get(i) instanceof Lid) {
                    return new String[][]{getIdentifier(0), getIdentifier(i)};
                }
            }
        }
        return null;
    }

    public int[] lidedCups() {
        ArrayList<Integer> temp = new ArrayList<>();
        for (Cup cup : cups) {
            if (cup.hasLid()) temp.add(cup.getNumber());
        }
        int[] result = new int[temp.size()];
        for (int i = 0; i < temp.size(); i++) result[i] = temp.get(i);
        return result;
    }

    public String[][] stackingItems() {
        String[][] result = new String[towerItems.size()][2];
        for (int i = 0; i < towerItems.size(); i++) result[i] = getIdentifier(i);
        return result;
    }

    private void createAxis() {
        axisX = new Rectangle();
        axisX.changeColor("black");
        axisX.changeSize(2, 300);
        axisX.moveHorizontal(-60);
        axisX.moveVertical(265);

        for (int i = 0; i <= 9; i++) {
            Rectangle mark = new Rectangle();
            mark.changeColor("black");
            mark.changeSize(2, 15);
            mark.moveHorizontal(-60);
            mark.moveVertical(-15 + (270 - i * 30));
            axisY.add(mark);
        }
    }

    private Lid findLooseLid(int number) {
        for (Lid lid : lids) {
            if (lid.getCupNumber() == number) return lid;
        }
        return null;
    }

    private int findIndex(String[] id) {
        String type = id[0];
        int number = Integer.parseInt(id[1]);
        for (int i = 0; i < towerItems.size(); i++) {
            Object obj = towerItems.get(i);
            if (type.equals("cup") && obj instanceof Cup && ((Cup) obj).getNumber() == number) return i;
            if (type.equals("lid") && obj instanceof Lid && ((Lid) obj).getCupNumber() == number) return i;
        }
        return -1;
    }

    private String[] getIdentifier(int index) {
        Object obj = towerItems.get(index);
        if (obj instanceof Cup) return new String[]{"cup", "" + ((Cup) obj).getNumber()};
        return new String[]{"lid", "" + ((Lid) obj).getCupNumber()};
    }

    private void updatePositions() {
        // cuenta todos los slots ocupados incluyendo tapas adjuntas
        int totalSlots = 0;
        for (Object obj : towerItems) {
            if (obj instanceof Cup) {
                totalSlots++;
                if (((Cup) obj).hasLid()) totalSlots++;
            } else if (obj instanceof Lid) {
                totalSlots++;
            }
        }
        if (totalSlots == 0) return;

        // el canvas va de y=20 (tope) a y=270 (base)
        int canvasHeight = 250;
        int slotHeight = Math.min(30, canvasHeight / totalSlots);
        int baseY = 270;
        int stackIndex = 0;

        for (Object obj : towerItems) {
            if (obj instanceof Cup) {
                Cup cup = (Cup) obj;
                int cupX = 45 + (width * 10 - cup.getWidth()) / 2;
                int cupY = baseY - (stackIndex * slotHeight);
                cup.setSize(slotHeight, cup.getWidth());
                cup.setPosition(cupX, cupY);
                if (isVisible) cup.makeVisible();
                stackIndex++;

                if (cup.hasLid()) {
                    Lid lid = cup.getLid();
                    int lidY = baseY - (stackIndex * slotHeight);
                    lid.setPosition(cupX, lidY);
                    lid.setSize(cup.getWidth());
                    if (isVisible) lid.makeVisible();
                    stackIndex++;
                }
            } else if (obj instanceof Lid) {
                Lid lid = (Lid) obj;
                int lidX = 45 + (width * 10 - 30) / 2;
                int lidY = baseY - (stackIndex * slotHeight);
                lid.setPosition(lidX, lidY);
                lid.setSize(30);
                if (isVisible) lid.makeVisible();
                stackIndex++;
            }
        }
    }
}