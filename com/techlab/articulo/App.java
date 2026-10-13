package com.techlab.articulo;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Articulo> articulos = new ArrayList<>();
        ArrayList<Categoria> categorias = new ArrayList<>();

        precargarCategorias(categorias);

        int opcion;
        do {
            System.out.println("\n======================================================");
            System.out.println(" SISTEMA DE ARTÍCULOS - CLASE 4 (HERENCIA Y TO_STRING)");
            System.out.println("======================================================");
            System.out.println("1 - Ingresar artículo");
            System.out.println("2 - Listar artículos");
            System.out.println("3 - Consultar un artículo");
            System.out.println("4 - Modificar un artículo");
            System.out.println("5 - Eliminar un artículo");
            System.out.println("6 - Listar categorías");
            System.out.println("0 - Salir");
            System.out.println("======================================================");

            opcion = leerEntero(scanner, "Ingrese una opción: ");

            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, articulos, categorias);
                    break;
                case 2:
                    listarArticulos(articulos);
                    break;
                case 3:
                    consultarArticulo(scanner, articulos);
                    break;
                case 4:
                    modificarArticulo(scanner, articulos, categorias);
                    break;
                case 5:
                    eliminarArticulo(scanner, articulos);
                    break;
                case 6:
                    listarCategorias(categorias);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente nuevamente.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void precargarCategorias(ArrayList<Categoria> categorias) {
        categorias.add(new Categoria(1, "Electrónica", "Productos tecnológicos y electrónicos"));
        categorias.add(new Categoria(2, "Periféricos", "Accesorios para computadora"));
        categorias.add(new Categoria(3, "Alimentos", "Productos alimenticios"));
        categorias.add(new Categoria(4, "Limpieza", "Artículos de limpieza del hogar"));
    }

    public static void ingresarArticulo(Scanner scanner, ArrayList<Articulo> articulos, ArrayList<Categoria> categorias) {
        System.out.println("\n--- INGRESAR ARTÍCULO ---");
        System.out.println("1 - Artículo electrónico");
        System.out.println("2 - Artículo alimenticio");

        int tipo;
        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de artículo (1 o 2): ");
            if (tipo != 1 && tipo != 2) {
                System.out.println("Tipo de artículo inválido. Ingrese 1 o 2.");
            }
        } while (tipo != 1 && tipo != 2);

        int codigo = leerEnteroNoNegativo(scanner, "Ingrese el código del artículo: ");

        if (buscarArticuloPorCodigo(articulos, codigo) != null) {
            System.out.println("Error: Ya existe un artículo con el código " + codigo + ". Operación cancelada.");
            return;
        }

        String nombre = leerTextoNoVacio(scanner, "Ingrese el nombre del artículo: ");
        double precio = leerDoubleNoNegativo(scanner, "Ingrese el precio del artículo: ");

        listarCategorias(categorias);
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);

        Articulo articulo;
        if (tipo == 1) {
            int garantia = leerEnteroNoNegativo(scanner, "Ingrese la garantía en meses: ");
            articulo = new ArticuloElectronico(codigo, nombre, precio, categoria, garantia);
        } else {
            int diasVencimiento = leerEnteroNoNegativo(scanner, "Ingrese los días restantes para el vencimiento: ");
            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoria, diasVencimiento);
        }

        articulos.add(articulo);
        System.out.println("\nArtículo ingresado correctamente.");
        System.out.println("Resumen del objeto creado: " + articulo);
    }

    public static void listarArticulos(ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("\nNo hay artículos cargados.");
            return;
        }
        System.out.println("\n--- LISTADO DE ARTÍCULOS ---");
        for (Articulo articulo : articulos) {
            System.out.println(articulo);
        }
    }

    public static void consultarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("\nNo hay artículos cargados.");
            return;
        }
        int codigo = leerEnteroNoNegativo(scanner, "Ingrese el código del artículo a consultar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
        } else {
            System.out.println("\n--- DATOS DEL ARTÍCULO ---");
            System.out.println(articulo);
            System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
        }
    }

    public static void modificarArticulo(Scanner scanner, ArrayList<Articulo> articulos, ArrayList<Categoria> categorias) {
        if (articulos.isEmpty()) {
            System.out.println("\nNo hay artículos cargados.");
            return;
        }
        int codigo = leerEnteroNoNegativo(scanner, "Ingrese el código del artículo a modificar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        System.out.println("Artículo encontrado: " + articulo.getNombre());
        String nuevoNombre = leerTextoNoVacio(scanner, "Ingrese el nuevo nombre: ");
        double nuevoPrecio = leerDoubleNoNegativo(scanner, "Ingrese el nuevo precio: ");

        listarCategorias(categorias);
        Categoria nuevaCategoria = pedirCategoriaExistente(scanner, categorias);

        articulo.setNombre(nuevoNombre);
        articulo.setPrecio(nuevoPrecio);
        articulo.setCategoria(nuevaCategoria);

        if (articulo instanceof ArticuloElectronico) {
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;
            int nuevaGarantia = leerEnteroNoNegativo(scanner, "Ingrese la nueva garantía en meses: ");
            electronico.setGarantiaMeses(nuevaGarantia);
        } else if (articulo instanceof ArticuloAlimenticio) {
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;
            int nuevosDias = leerEnteroNoNegativo(scanner, "Ingrese los nuevos días para el vencimiento: ");
            alimenticio.setDiasParaVencimiento(nuevosDias);
        }

        System.out.println("\nArtículo modificado correctamente.");
    }

    public static void eliminarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("\nNo hay artículos cargados.");
            return;
        }
        int codigo = leerEnteroNoNegativo(scanner, "Ingrese el código del artículo a eliminar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
        } else {
            articulos.remove(articulo);
            System.out.println("Artículo eliminado correctamente.");
        }
    }

    public static void listarCategorias(ArrayList<Categoria> categorias) {
        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");
        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
    }

    public static Articulo buscarArticuloPorCodigo(ArrayList<Articulo> articulos, int codigo) {
        for (Articulo articulo : articulos) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }

    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }

    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias) {
        Categoria categoria = null;
        while (categoria == null) {
            int codigoCat = leerEntero(scanner, "Ingrese el código de la categoría: ");
            categoria = buscarCategoriaPorCodigo(categorias, codigoCat);
            if (categoria == null) {
                System.out.println("La categoría ingresada no existe. Intente nuevamente.");
            }
        }
        return categoria;
    }

    public static int leerEntero(Scanner scanner, String mensaje) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                valor = Integer.parseInt(entrada.trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
        return valor;
    }

    public static int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        int valor;
        do {
            valor = leerEntero(scanner, mensaje);
            if (valor < 0) {
                System.out.println("Error: El valor no puede ser negativo.");
            }
        } while (valor < 0);
        return valor;
    }

    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                valor = Double.parseDouble(entrada.trim());
                if (Double.isNaN(valor) || Double.isInfinite(valor) || valor < 0) {
                    System.out.println("Error: El precio no puede ser negativo, NaN o Infinito.");
                } else {
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número decimal válido.");
            }
        }
        return valor;
    }

    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: El texto no puede estar vacío.");
            }
        }
        return texto;
    }
}