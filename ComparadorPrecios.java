import java.util.*;

class Producto {
    private String nombre;
    private String supermercado;
    private double precio;

    public Producto(String nombre, String supermercado, double precio) {
        this.nombre = nombre;
        this.supermercado = supermercado;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre + " (" + supermercado + ", €" + precio + ")";
    }
}

public class ComparadorPrecios {

    public static void main(String[] args) {
        // Productos precargados
        List<Producto> productos = Arrays.asList(
                new Producto("Leche", "Supermercado A", 1.20),
                new Producto("Leche", "Supermercado B", 1.10),
                new Producto("Pan", "Supermercado A", 0.85),
                new Producto("Pan", "Supermercado B", 0.80),
                new Producto("Huevos", "Supermercado B", 2.50),
                new Producto("Huevos", "Supermercado A", 2.40)
        );

        // Lista de compras del usuario
        List<Producto> listaDeCompras = seleccionarProductos(productos);

        if (!listaDeCompras.isEmpty()) {
            // Mostrar precios disponibles para cada producto seleccionado
            System.out.println("\nLista final de compras con los precios más baratos:");
            listaDeCompras.forEach(System.out::println);

            // Determinar el supermercado más barato para la lista completa
            String supermercadoMasBarato = determinarSupermercadoMasBarato(listaDeCompras);
            System.out.println("\nEl supermercado más barato para la lista completa es: " + supermercadoMasBarato);

            // Mostrar el costo total en cada supermercado
            System.out.println("\nCosto total por supermercado:");
            Map<String, Double> totalPorSupermercado = calcularTotalPorSupermercado(listaDeCompras);
            totalPorSupermercado.forEach((supermercado, total) ->
                    System.out.println(supermercado + ": €" + String.format("%.2f", total))
            );
        } else {
            System.out.println("No se seleccionaron productos.");
        }
    }

    private static List<String> obtenerNombresUnicos(List<Producto> productos) {
        return productos.stream()
                .map(Producto::getNombre)
                .distinct()
                .toList();
    }

    private static void mostrarMenuDeProductos(List<String> nombresUnicos) {
        System.out.println("\nLista de productos disponibles:");
        for (int i = 0; i < nombresUnicos.size(); i++) {
            System.out.println((i + 1) + ". " + nombresUnicos.get(i));
        }
        System.out.print("\nSeleccione un producto por número (0 para finalizar): ");
    }

    private static List<Producto> seleccionarProductos(List<Producto> productos) {
        Scanner scanner = new Scanner(System.in);
        List<Producto> listaDeCompras = new ArrayList<>();
        List<String> nombresUnicos = obtenerNombresUnicos(productos);

        while (true) {
            mostrarMenuDeProductos(nombresUnicos);
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                continue;
            }

            if (opcion == 0) {
                break;
            }

            if (opcion > 0 && opcion <= nombresUnicos.size()) {
                String nombreSeleccionado = nombresUnicos.get(opcion - 1);
                Producto opcionMasBarata = productos.stream()
                        .filter(p -> p.getNombre().equals(nombreSeleccionado))
                        .min(Comparator.comparingDouble(Producto::getPrecio))
                        .orElse(null);

                if (opcionMasBarata != null) {
                    listaDeCompras.add(opcionMasBarata);
                    System.out.println("Producto seleccionado: " + opcionMasBarata);
                }
            } else {
                System.out.println("Opción inválida.");
            }
        }

        return listaDeCompras;
    }

    private static String determinarSupermercadoMasBarato(List<Producto> listaDeCompras) {
        Map<String, Double> totalPorSupermercado = calcularTotalPorSupermercado(listaDeCompras);

        return totalPorSupermercado.entrySet().stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("No disponible");
    }

    private static Map<String, Double> calcularTotalPorSupermercado(List<Producto> listaDeCompras) {
        Map<String, Double> totalPorSupermercado = new HashMap<>();

        for (Producto producto : listaDeCompras) {
            totalPorSupermercado.merge(producto.getSupermercado(), producto.getPrecio(), Double::sum);
        }

        return totalPorSupermercado;
    }
}
