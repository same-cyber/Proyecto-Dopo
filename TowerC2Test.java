import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas unitarias del Ciclo 2.
 * Se verifican los métodos nuevos de Tower en modo invisible.
 *
 * @author Samuel Mena
 * @version Ciclo 2
 */
public class TowerC2Test {

    private boolean containsCup(int[] arr, int value) {
        for (int x : arr) {
            if (x == value) return true;
        }
        return false;
    }

    private boolean sameIdentifier(String[] id, String type, String num) {
        return id != null && id.length == 2 && type.equals(id[0]) && num.equals(id[1]);
    }

    // --------------------------
    // Requisito 10: Tower(int cups)
    // --------------------------

    @Test
    public void shouldCreateTowerWithCorrectOrder() {
        Tower t = new Tower(3);

        String[][] items = t.stackingItems();
        assertNotNull(items);
        assertEquals("Debe haber 3 tazas", 3, items.length);

        assertTrue("La base debería ser cup 3", sameIdentifier(items[0], "cup", "3"));
        assertTrue("El medio debería ser cup 2", sameIdentifier(items[1], "cup", "2"));
        assertTrue("Arriba debería ser cup 1", sameIdentifier(items[2], "cup", "1"));
    }

    @Test
    public void shouldStartWithNoLids() {
        Tower t = new Tower(5);

        int[] lided = t.lidedCups();
        assertNotNull(lided);
        assertEquals("No debería haber tapas al crear la torre", 0, lided.length);
    }

    // --------------------------
    // Requisito 12: cover()
    // --------------------------

    @Test
    public void shouldCoverOnlyMatchingCups() {
        Tower t = new Tower(4);
        t.pushLid(1);
        t.pushLid(3);

        t.cover();

        int[] lided = t.lidedCups();
        assertEquals("Solo deberían taparse 2 tazas", 2, lided.length);
        assertTrue("Cup 1 debería estar tapada", containsCup(lided, 1));
        assertTrue("Cup 3 debería estar tapada", containsCup(lided, 3));
        assertFalse("Cup 2 no debería estar tapada", containsCup(lided, 2));
    }

    @Test
    public void shouldRemoveLooseLidsAfterCover() {
        Tower t = new Tower(2);
        t.pushLid(2);

        t.cover();

        String[][] items = t.stackingItems();
        for (String[] item : items) {
            assertFalse("La lid 2 no debería quedar suelta", sameIdentifier(item, "lid", "2"));
        }
    }

    // --------------------------
    // Requisito 11: swap(o1,o2)
    // --------------------------

    @Test
    public void shouldSwapCupAndLid() {
        Tower t = new Tower(2);
        t.pushLid(1);

        t.swap(new String[]{"cup", "2"}, new String[]{"lid", "1"});

        String[][] after = t.stackingItems();
        assertTrue("Después del swap, lid 1 debería estar primero",
                sameIdentifier(after[0], "lid", "1"));
    }

    @Test
    public void shouldKeepSameSizeAfterInvalidSwap() {
        Tower t = new Tower(2);
        String[][] before = t.stackingItems();

        t.swap(new String[]{"cup", "10"}, new String[]{"cup", "1"});

        String[][] after = t.stackingItems();
        assertEquals("El tamaño no debería cambiar con un swap inválido",
                before.length, after.length);
        for (int i = 0; i < before.length; i++) {
            assertEquals(before[i][0], after[i][0]);
            assertEquals(before[i][1], after[i][1]);
        }
    }

    // --------------------------
    // Requisito 13: swapToReduce()
    // --------------------------

    @Test
    public void shouldSuggestSwapWhenLidIsOnTop() {
        Tower t = new Tower(2);
        t.pushLid(2);
        t.swap(new String[]{"lid", "2"}, new String[]{"cup", "1"});

        String[][] suggestion = t.swapToReduce();
        assertNotNull("Debería sugerir un swap cuando hay una lid arriba", suggestion);
        assertEquals(2, suggestion.length);
    }

    @Test
    public void shouldReduceHeightAfterApplyingSuggestion() {
        Tower t = new Tower(3);
        t.pushLid(3);

        int hBefore = t.height();
        String[][] suggestion = t.swapToReduce();

        assertNotNull(suggestion);
        t.swap(suggestion[0], suggestion[1]);

        assertTrue("La altura debería bajar tras aplicar el swap sugerido",
                t.height() < hBefore);
    }

    @Test
    public void shouldReturnNullWithSingleItem() {
        Tower t = new Tower(1);

        assertNull("Con una sola taza no hay swap posible", t.swapToReduce());
    }
}
