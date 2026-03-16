/**
 * Resuelve y simula el problema de la maratón de Stacking Cups.
 * La lógica de resolución es independiente de Tower, solo se usa para simular visualmente.
 *
 * @author Samuel Mena
 */
public class TowerContest {

    private static Tower currentTower = null;

    // verifica si se puede armar una torre de altura h con n tazas
    public static String solve(int n, int h) {
        // mínimo posible: 1 (una tapa), máximo: 2*n (todo apilado)
        if (h < 1 || h > 2 * n) {
            return "IMPOSSIBLE";
        }

        // altura impar siempre se puede con (h+1)/2 tazas sin tapas
        if (h % 2 == 1) {
            return "POSSIBLE";
        }

        // altura par necesita h/2 tazas con tapas
        if (n >= h / 2) {
            return "POSSIBLE";
        }

        return "IMPOSSIBLE";
    }

    // arma la simulación visual usando Tower
    public static void simulate(int n, int h) {
        if (currentTower != null) {
            currentTower.exit();
            currentTower = null;
        }

        String result = solve(n, h);

        if (result.equals("IMPOSSIBLE")) {
            System.out.println("IMPOSSIBLE - No se puede simular una solución que no existe.");
            return;
        }

        if (h > 30) {
            System.out.println("POSSIBLE - Pero la altura es demasiado grande para graficar.");
            return;
        }

        currentTower = new Tower(n, h);

        int cupsNeeded = (h % 2 == 1) ? (h + 1) / 2 : h / 2;
        // escala el ancho para que quepan en el canvas
        int maxWidth = Math.min(250, cupsNeeded * 20);

        for (int i = cupsNeeded; i >= 1; i--) {
            int width = (cupsNeeded == 1) ? maxWidth :
                (int)((2.0 * i - 1) / (2.0 * cupsNeeded - 1) * maxWidth);
            if (width < 10) width = 10;
            currentTower.pushCup(i, width);
        }

        if (h % 2 == 0) {
            currentTower.pushLidOnTop();
        }

        currentTower.makeVisible();
        System.out.println("POSSIBLE - Solución simulada con altura " + currentTower.height());
    }
}