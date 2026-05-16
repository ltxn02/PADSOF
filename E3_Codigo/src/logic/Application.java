package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;
import discounts.*;

public class Application {
    private static HashMap<String, RegisteredUser> users = new HashMap<>();
    private static HashMap<String, Notification> notifications = new HashMap<>();
    private static ArrayList<SecondHandProduct> secondHandProducts = new ArrayList<>();
    private static ArrayList<NewProduct> catalog = new ArrayList<>();
    private static ArrayList<Category> globalCategories = new ArrayList<>();
    private static ArrayList<IDiscount> globalDiscounts = new ArrayList<>();
    private static int EdadMinimaRegistro = 12;
    // --- BLOQUE DE INICIALIZACIÓN ESTÁTICA ---
    static {
        // 1. INICIALIZACIÓN DE USUARIOS POR DEFECTO
        try {
            Manager lidia = new Manager("lidia", "lidia123", "Lidia Martin Teres", "12345678A", "01/01/2002", "lidia@rongero.es", "600000000", 10000.00);
            Manager taha = new Manager("taha", "taha123", "Taha Ridda", "12345678A", "01/01/2002", "taha@rongero.es", "600000000", 10000.00);
            Manager ivan = new Manager("ivan", "ivan123", "Ivan Sanchez", "12345678A", "01/01/2002", "ivan@rongero.es", "600000000", 10000.00);

            Employee empleadoDefecto = new Employee("empleado", "empleado123", "Empleado de Prueba", "87654321B", "15/05/1995", "empleado@rongero.es", "600000000", 1200.00, true);

            users.put(lidia.getUsername(), lidia);
            users.put(taha.getUsername(), taha);
            users.put(ivan.getUsername(), ivan);
            users.put(empleadoDefecto.getUsername(), empleadoDefecto);

            System.out.println("[Sistema] Cuentas de gestor y empleado creadas por defecto.");
            
            // ========================================================
            // 11 EMPLEADOS ADICIONALES PARA PRUEBAS
            // ========================================================
            Employee emp1 = new Employee("mgarcia", "12345", "María García López", "45678901A", "15/03/1990", "maria.garcia@rongero.es", "612345678", 1400.00, true);
            Employee emp2 = new Employee("cmartinez", "12345", "Carlos Martínez Ruiz", "12345678B", "22/07/1988", "carlos.martinez@rongero.es", "623456789", 1500.00, true);
            Employee emp3 = new Employee("efernandez", "12345", "Elena Fernández Sánchez", "87654321C", "08/11/1992", "elena.fernandez@rongero.es", "634567890", 1350.00, true);
            Employee emp4 = new Employee("jlopez", "12345", "Juan López Díaz", "23456789D", "30/05/1995", "juan.lopez@rongero.es", "645678901", 1300.00, true);
            Employee emp5 = new Employee("rjimenez", "12345", "Rosa Jiménez Moreno", "56789012E", "14/01/1991", "rosa.jimenez@rongero.es", "656789012", 1400.00, true);
            Employee emp6 = new Employee("arodriguez", "12345", "Antonio Rodríguez Pérez", "34567890F", "09/09/1987", "antonio.rodriguez@rongero.es", "667890123", 1600.00, true);
            Employee emp7 = new Employee("lsanchez", "12345", "Laura Sánchez Torres", "78901234G", "27/04/1993", "laura.sanchez@rongero.es", "678901234", 1380.00, true);
            Employee emp8 = new Employee("fgonzalez", "12345", "Francisco González Herrera", "90123456H", "11/12/1989", "francisco.gonzalez@rongero.es", "689012345", 1450.00, true);
            Employee emp9 = new Employee("iramirez", "12345", "Isabel Ramírez Castro", "01234567I", "19/06/1994", "isabel.ramirez@rongero.es", "690123456", 1320.00, true);
            Employee emp10 = new Employee("pvazquez", "12345", "Pedro Vázquez López", "67890123J", "03/10/1986", "pedro.vazquez@rongero.es", "691234567", 1550.00, true);
            Employee emp11 = new Employee("snunez", "12345", "Sofía Núñez Gómez", "89012345K", "25/02/1996", "sofia.nunez@rongero.es", "692345678", 1300.00, true);

            emp1.permissions.add(Permission.EXCH_VALIDATE);

            users.put(emp1.getUsername(), emp1);
            users.put(emp2.getUsername(), emp2);
            users.put(emp3.getUsername(), emp3);
            users.put(emp4.getUsername(), emp4);
            users.put(emp5.getUsername(), emp5);
            users.put(emp6.getUsername(), emp6);
            users.put(emp7.getUsername(), emp7);
            users.put(emp8.getUsername(), emp8);
            users.put(emp9.getUsername(), emp9);
            users.put(emp10.getUsername(), emp10);
            users.put(emp11.getUsername(), emp11);

            System.out.println("[Sistema] 11 empleados adicionales creados para pruebas.");

            
        } catch (Exception e) {
            System.err.println("Error al crear usuarios por defecto: " + e.getMessage());
        }

        // 2. INICIALIZACIÓN DE PRODUCTOS POR DEFECTO
        try {
            ArrayList<Review> emptyReviews = new ArrayList<>();
            ArrayList<Item> emptyItems = new ArrayList<>();

            Category catComic = new Category("Cómics y manga", emptyItems);
            Category catFigura = new Category("Figuras de colección", emptyItems);
            Category catJuego = new Category("Juegos de mesa", emptyItems);

            ArrayList<Category> comicCategories = new ArrayList<>(Arrays.asList(catComic));
            ArrayList<Category> figuraCategories = new ArrayList<>(Arrays.asList(catFigura));
            ArrayList<Category> juegoCategories = new ArrayList<>(Arrays.asList(catJuego));

// 1
            catalog.add(new Comic("One Piece Vol. 1", "Acompaña a Monkey D. Luffy en el épico comienzo de su viaje para convertirse en el Rey de los Piratas. En este volumen, conocemos los orígenes de su fruta del diablo y su inquebrantable determinación por reunir una tripulación única. La narrativa de Eiichiro Oda mezcla magistralmente el humor, la acción frenética y momentos profundamente emotivos que han definido a toda una generación de lectores. Descubre cómo se forja la amistad con Zoro y los primeros pasos en el East Blue, enfrentando peligros inimaginables y estableciendo las bases de un mundo vasto lleno de misterios, tesoros legendarios y la búsqueda constante de la libertad absoluta en los mares más peligrosos del mundo.", 7.95, new ArrayList<>(Arrays.asList("src/imgProductos/op1.jpg")), 50, comicCategories, emptyReviews, null, 208, "Planeta Cómic", 1997, new ArrayList<>(Arrays.asList("Eiichiro Oda"))));
// 2
            catalog.add(new Comic("Dragon Ball Vol. 1", "Revive el inicio de la leyenda de las esferas del dragón con el primer encuentro entre el pequeño y fuerte Goku y la brillante Bulma. Este tomo marca el inicio de una búsqueda que cambiará el destino del universo, llena de artes marciales, personajes pintorescos como el Maestro Roshi y Oolong, y un sentido de la aventura inigualable. Akira Toriyama logra crear un equilibrio perfecto entre la comedia y la acción, sentando las bases de uno de los mangas más influyentes de la historia. Acompaña a estos jóvenes aventureros mientras exploran tierras desconocidas, se enfrentan a villanos torpes pero peligrosos y descubren que el verdadero poder reside en la amistad y el entrenamiento constante.", 8.50, new ArrayList<>(Arrays.asList("src/imgProductos/db1.jpg")), 40, comicCategories, emptyReviews, null, 192, "Planeta Cómic", 1984, new ArrayList<>(Arrays.asList("Akira Toriyama"))));
// 3
            catalog.add(new Comic("Tintín en el Tíbet", "Considerada por muchos como la obra maestra de Hergé, esta aventura lleva al reportero Tintín hasta las cumbres nevadas del Himalaya en una búsqueda desesperada por rescatar a su amigo Chang tras un accidente aéreo. Lejos de las tramas políticas habituales, esta historia se centra en la pureza de la lealtad y el valor humano frente a la naturaleza implacable. Con el Capitán Haddock a su lado, Tintín enfrenta tormentas de nieve, la fatiga extrema y el misterio del mítico Yeti. Es un viaje visualmente deslumbrante que utiliza el espacio en blanco de la nieve para crear una atmósfera de soledad y misticismo, culminando en un reencuentro que conmueve a lectores de todas las edades.", 14.90, new ArrayList<>(Arrays.asList("src/imgProductos/tintin.jpg")), 25, comicCategories, emptyReviews, null, 64, "Juventud", 1960, new ArrayList<>(Arrays.asList("Hergé"))));
// 4
            catalog.add(new Comic("Batman: Año Uno", "Frank Miller redefine los orígenes del Caballero Oscuro en esta obra cruda y realista que narra el primer año de Bruce Wayne intentando limpiar Gotham de la corrupción sistémica. Paralelamente, seguimos el ascenso del teniente James Gordon en un departamento de policía podrido hasta la médula. La narrativa es asfixiante y urbana, alejándose de los superpoderes para centrarse en la fragilidad humana y la voluntad de acero. Es la historia de dos hombres justos tratando de marcar la diferencia en una ciudad que parece rechazar la justicia. Imprescindible para entender la psicología moderna de Batman y su relación simbiótica con la ciudad que juró proteger por encima de su propia vida.", 18.00, new ArrayList<>(Arrays.asList("src/imgProductos/batman.jpg")), 15, comicCategories, emptyReviews, null, 144, "ECC Ediciones", 1987, new ArrayList<>(Arrays.asList("Frank Miller"))));
// 5
            catalog.add(new Comic("Naruto Vol. 1", "Naruto Uzumaki es un joven aprendiz de ninja marginado por su aldea que sueña con alcanzar el título de Hokage para ganar el respeto de todos. Este primer volumen nos introduce en un mundo rico en mitología, técnicas de combate espectaculares y conflictos emocionales profundos. Acompañado por Sasuke y Sakura bajo la tutela de Kakashi, Naruto debe aprender que el camino del ninja es mucho más que fuerza bruta; es sacrificio y persistencia. La historia explora temas de soledad, superación y el vínculo inquebrantable de los equipos. Es el inicio de una saga épica que ha cautivado a millones de personas gracias a su carismático protagonista y su mensaje de esperanza constante.", 7.50, new ArrayList<>(Arrays.asList("src/imgProductos/naruto1.jpg")), 60, comicCategories, emptyReviews, null, 192, "Planeta Cómic", 1999, new ArrayList<>(Arrays.asList("Masashi Kishimoto"))));
// 6
            catalog.add(new Comic("Spiderman: Kraven", "En 'La última cacería de Kraven', el icónico villano Kraven el Cazador decide que para derrotar finalmente a Spider-Man, no basta con matarlo; debe suplantarlo y demostrar ser un héroe superior. Esta historia es una de las más oscuras y psicológicas de Marvel, explorando la obsesión, la locura y la mortalidad. Peter Parker es enterrado vivo mientras Kraven recorre las calles de Nueva York impartiendo su propia y retorcida versión de la justicia. Con un arte atmosférico y un guion poético, esta obra trasciende el género de superhéroes para convertirse en un estudio sobre el miedo y la redención final. Es un relato inolvidable que desafía las convenciones del cómic convencional y profundiza en el alma del héroe.", 22.00, new ArrayList<>(Arrays.asList("src/imgProductos/spiderman.jpg")), 10, comicCategories, emptyReviews, null, 160, "Panini Comics", 1987, new ArrayList<>(Arrays.asList("J.M. DeMatteis"))));
// 7
            catalog.add(new Comic("Akira Vol. 1", "En un Neo-Tokyo post-apocalíptico construido sobre las ruinas de una explosión nuclear, las bandas de motociclistas dominan las calles mientras el gobierno realiza experimentos secretos con niños con poderes psíquicos. Kaneda y Tetsuo, dos amigos de la infancia, se ven envueltos en una conspiración militar cuando Tetsuo desarrolla habilidades incontrolables que amenazan con repetir la catástrofe del pasado. El dibujo de Katsuhiro Otomo es detallado hasta la obsesión, con escenas de acción dinámicas y una crítica social punzante sobre el poder y la tecnología. Akira es un pilar del género ciberpunk y una obra monumental que cambió la percepción del manga en Occidente para siempre por su crudeza y ambición visual.", 25.00, new ArrayList<>(Arrays.asList("src/imgProductos/akira1.jpg")), 20, comicCategories, emptyReviews, null, 360, "Norma Editorial", 1982, new ArrayList<>(Arrays.asList("Katsuhiro Otomo"))));
// 8
            catalog.add(new Comic("Watchmen", "Alan Moore y Dave Gibbons deconstruyen el mito del superhéroe en esta obra maestra que cuestiona quién vigila a los vigilantes. En una realidad alternativa donde Estados Unidos ganó la guerra de Vietnam y los héroes disfrazados son parte de la sociedad, el asesinato de uno de ellos desencadena una investigación que revela una conspiración global. Con personajes complejos y moralmente ambiguos como Rorschach o el Dr. Manhattan, Watchmen explora la política de la Guerra Fría, la ética y la naturaleza del tiempo. Su narrativa innovadora y su estructura de 'cómic dentro de un cómic' la han elevado a ser la única novela gráfica presente en la lista de las mejores novelas del siglo XX, desafiando cualquier etiqueta previa.", 35.00, new ArrayList<>(Arrays.asList("src/imgProductos/watchmen.jpg")), 12, comicCategories, emptyReviews, null, 416, "DC Comics", 1986, new ArrayList<>(Arrays.asList("Alan Moore"))));
// 9
            catalog.add(new Comic("Death Note Vol. 1", "Light Yagami es un estudiante brillante pero aburrido que encuentra un cuaderno perteneciente a un Dios de la Muerte, capaz de matar a cualquier persona cuyo nombre sea escrito en él. Decidido a crear un mundo sin criminales donde él sea el dios, comienza una limpieza global bajo el seudónimo de Kira. Sin embargo, se encuentra con la oposición del enigmático detective L, iniciando un juego del gato y el ratón basado en el intelecto, la deducción y la manipulación psicológica. Esta historia es un thriller trepidante que pone a prueba la moralidad del lector: ¿es justo matar a los malvados para salvar a los buenos? Un duelo mental legendario donde un solo error significa la muerte definitiva.", 8.00, new ArrayList<>(Arrays.asList("src/imgProductos/deathnote1.jpg")), 45, comicCategories, emptyReviews, null, 200, "Norma Editorial", 2003, new ArrayList<>(Arrays.asList("Tsugumi Ohba"))));
// 10
            catalog.add(new Comic("Berserk Vol. 1", "Guts, el Guerrero Negro, vaga por un mundo medieval oscuro plagado de demonios y horrores inimaginables, llevando consigo una espada gigante llamada Matadragones. Este primer volumen nos introduce en un viaje de venganza contra los seres que marcaron su destino. La obra de Kentaro Miura es célebre por su arte increíblemente detallado y su narrativa visceral que no teme explorar la depravación humana y la lucha contra la fatalidad. A pesar de su violencia, Berserk es una historia profundamente humana sobre el trauma, la resiliencia y la voluntad de sobrevivir en un mundo que parece diseñado para aplastarte. Una obra de culto indispensable para los amantes de la fantasía oscura y el drama épico sin concesiones.", 10.00, new ArrayList<>(Arrays.asList("src/imgProductos/berserk1.jpg")), 30, comicCategories, emptyReviews, null, 224, "Panini Comics", 1989, new ArrayList<>(Arrays.asList("Kentaro Miura"))));
            // --- 10 FIGURAS ---
            // 11
            catalog.add(new Figurine("Figura Gon Freecss", "Esta figura dinámica captura a Gon Freecss en su pose característica de batalla, concentrando su aura para el ataque Jajanken. Con un esculpido que resalta su determinación y espíritu indomable, la pieza muestra el diseño fiel del anime Hunter x Hunter. La calidad del PVC y los detalles en su cabello y ropa reflejan el estándar de Banpresto para coleccionistas. Es el complemento perfecto para cualquier estantería, simbolizando la búsqueda incansable de Gon por encontrar a su padre y su crecimiento constante como cazador profesional en un mundo lleno de peligros y retos sobrenaturales.", 45.00, new ArrayList<>(Arrays.asList("src/imgProductos/gon.jpg")), 10, figuraCategories, emptyReviews, null, 15.0, 5.0, 5.0, "PVC", "Banpresto"));
// 12
            catalog.add(new Figurine("Funko Pop Zoro", "Roronoa Zoro, el legendario espadachín de los Piratas de Sombrero de Paja, llega en formato Funko Pop con su icónica cicatriz en el ojo y sus tres katanas preparadas para el combate. A pesar de su tamaño reducido, la figura logra transmitir la seriedad y el carisma del personaje. Pintada con colores vibrantes y fabricada en vinilo de alta durabilidad, es una pieza esencial para los fans de One Piece. Ya sea que lo coloques en tu escritorio o en tu vitrina de colección, este Zoro te recordará siempre que el camino hacia el mejor espadachín del mundo está lleno de esfuerzo y honor.", 16.99, new ArrayList<>(Arrays.asList("src/imgProductos/zoro_pop.jpg")), 25, figuraCategories, emptyReviews, null, 10.0, 6.0, 6.0, "Vinilo", "Funko"));
// 13
            catalog.add(new Figurine("Figura Eren Titán", "Esta impresionante figura de colección captura el momento exacto de la transformación de Eren Yeager en el imponente Titán de Ataque. Con un nivel de detalle asombroso, cada músculo y fibra del titán ha sido esculpido para reflejar la ferocidad y el poder bruto del personaje principal de Shingeki no Kyojin. La base texturizada simula los escombros de las murallas de Shiganshina, aportando un realismo cinematográfico a tu vitrina. Fabricada con resina de alta calidad y pintada a mano, esta pieza es indispensable para cualquier fan que desee conmemorar la lucha desesperada de la humanidad contra los titanes y el destino trágico de sus protagonistas en esta obra maestra.", 89.00, new ArrayList<>(Arrays.asList("src/imgProductos/eren.jpg")), 5, figuraCategories, emptyReviews, null, 25.0, 15.0, 15.0, "Resina", "Good Smile"));
// 14
            catalog.add(new Figurine("Nendoroid Link", "Acompaña a Link en su exploración por las tierras de Hyrule con este Nendoroid inspirado en Breath of the Wild. Incluye múltiples accesorios como el arco, flechas ancestrales, la espada maestra y la tableta Sheikah para recrear tus momentos favoritos del juego. Su diseño articulado permite una gran variedad de poses, desde una posición de combate hasta un descanso frente a una hoguera. La calidad de los acabados y la expresividad de sus rostros intercambiables capturan la esencia de la aventura más grande de Nintendo. Una joya para los seguidores de la saga Zelda que buscan versatilidad y detalle en una figura pequeña pero potente.", 55.00, new ArrayList<>(Arrays.asList("src/imgProductos/link.jpg")), 8, figuraCategories, emptyReviews, null, 10.0, 4.0, 4.0, "ABS", "Good Smile"));
// 15
            catalog.add(new Figurine("Figura Nezuko", "Nezuko Kamado se presenta en esta figura de alta calidad saliendo de su caja protectora, lista para proteger a su hermano Tanjiro. Los detalles de su kimono rosa y el brillo en sus ojos están cuidadosamente aplicados para reflejar su naturaleza dual como demonio y humana con alma pura. La pose es fluida y natural, transmitiendo una sensación de movimiento y ternura a la vez. Fabricada por Sega, esta pieza destaca por su resistencia y fidelidad al diseño original de Kimetsu no Yaiba, convirtiéndola en una de las representaciones favoritas de los fans que buscan capturar la magia de esta emotiva historia de supervivencia familiar.", 39.90, new ArrayList<>(Arrays.asList("src/imgProductos/nezuko.jpg")), 15, figuraCategories, emptyReviews, null, 12.0, 7.0, 7.0, "PVC", "Sega"));
// 16
            catalog.add(new Figurine("Funko Pop Iron Man", "El genio, millonario, playboy y filántropo Tony Stark inmortalizado en su armadura Mark 85. Este Funko Pop destaca por el acabado metalizado de su traje rojo y dorado, reflejando el sacrificio final visto en Avengers: Endgame. Con su postura firme y lista para la acción, es un recordatorio del héroe que inició el universo cinematográfico de Marvel. El nivel de detalle en el casco y los propulsores lo convierte en una pieza destacada dentro de la línea de Marvel. Fabricado en vinilo de alta calidad, es el regalo perfecto para cualquier entusiasta de los Vengadores que quiera tener un pedazo de historia del cine en su habitación.", 15.00, new ArrayList<>(Arrays.asList("src/imgProductos/ironman.jpg")), 30, figuraCategories, emptyReviews, null, 10.0, 6.0, 6.0, "Vinilo", "Funko"));
// 17
            catalog.add(new Figurine("Figura Darth Vader", "Siente el poder del lado oscuro con esta figura premium de Lord Darth Vader. Con una capa de tela real que fluye con elegancia y una armadura detallada hasta el más mínimo interruptor de su panel de soporte vital, esta pieza de Hot Toys es el pináculo del coleccionismo de Star Wars. La escala y el peso de la figura le otorgan una presencia imponente en cualquier habitación. Cada articulación está diseñada para recrear los gestos amenazantes del Sith más temido de la galaxia. Incluye manos intercambiables y un sable de luz para que puedas personalizar la exhibición de este icono cinematográfico que ha definido la ciencia ficción durante décadas.", 120.00, new ArrayList<>(Arrays.asList("src/imgProductos/vader.jpg")), 3, figuraCategories, emptyReviews, null, 30.0, 12.0, 10.0, "PVC", "Hot Toys"));
// 18
            catalog.add(new Figurine("Figura Sailor Moon", "Usagi Tsukino, la guerrera que lucha por el amor y la justicia, llega en esta figura de Bandai con su báculo lunar y su clásica pose de transformación. Los colores pasteles y el acabado brillante de su uniforme capturan la estética mágica del anime de los noventa que revolucionó el género de las Magical Girls. Su expresión es una mezcla de valentía y dulzura, fiel al carácter del personaje. Con una base decorativa que asegura su estabilidad, esta figura de Sailor Moon es una pieza de nostalgia pura para quienes crecieron viendo sus aventuras y un objeto de deseo para las nuevas generaciones de coleccionistas de anime clásico.", 42.00, new ArrayList<>(Arrays.asList("src/imgProductos/sailormoon.jpg")), 12, figuraCategories, emptyReviews, null, 18.0, 6.0, 6.0, "PVC", "Bandai"));
// 19
            catalog.add(new Figurine("Figura Geralt Rivia", "Inspirada en el universo de The Witcher, esta figura muestra a Geralt de Rivia en plena cacería, con su espada de plata desenfundada y su rostro concentrado en el rastro de un monstruo. Los detalles en su armadura de cuero, las pociones que lleva al cinto y la textura de su cabello blanco son excepcionales. Dark Horse ha logrado plasmar la atmósfera madura y oscura de los libros y videojuegos en esta pieza de colección. La base temática simula un terreno boscoso, añadiendo profundidad a la escena. Es una representación fiel del Lobo Blanco, ideal para decorar el espacio de cualquier aficionado a la fantasía épica y las historias de Geralt.", 65.00, new ArrayList<>(Arrays.asList("src/imgProductos/geralt.jpg")), 7, figuraCategories, emptyReviews, null, 24.0, 10.0, 10.0, "PVC", "Dark Horse"));
// 20
            catalog.add(new Figurine("Funko Pop Pikachu", "El Pokémon más famoso del mundo llega en su versión Funko Pop para electrificar tu colección. Con sus mejillas rojas y su cola en forma de rayo, esta figura de Pikachu es la definición de ternura y nostalgia. Su diseño minimalista pero reconocible lo hace apto tanto para niños como para coleccionistas veteranos de la franquicia. Fabricado en vinilo resistente, captura perfectamente la alegría del compañero inseparable de Ash Ketchum. Es la puerta de entrada ideal para quienes comienzan a coleccionar figuras de Pokémon o una adición obligatoria para quienes ya tienen a todo el equipo listo para combatir en el gimnasio más cercano de su ciudad.", 14.50, new ArrayList<>(Arrays.asList("src/imgProductos/pikachu.jpg")), 40, figuraCategories, emptyReviews, null, 10.0, 5.0, 5.0, "Vinilo", "Funko"));
            // --- 10 JUEGOS ---
            // 21
            catalog.add(new Game("Catan", "Conviértete en el primer colono en dominar la isla de Catan. Este juego de mesa moderno es un clásico absoluto de la estrategia donde la gestión de recursos como madera, arcilla y trigo es clave para construir pueblos y carreteras. El comercio con otros jugadores es el corazón del juego, creando situaciones de negociación tensas y divertidas. El tablero modular asegura que cada partida sea completamente diferente, desafiando tu capacidad de adaptación y planificación a largo plazo. Ideal para reuniones familiares o con amigos, Catan es el juego que inició la fiebre de los juegos de mesa modernos y sigue siendo una experiencia imprescindible y adictiva para todos los públicos.", 45.00, new ArrayList<>(Arrays.asList("src/imgProductos/catan.jpg")), 20, juegoCategories, emptyReviews, null, 4, new ArrayList<>(Arrays.asList("Gestión")), new AgeRange(10, 99)));
// 22
            catalog.add(new Game("Dixit", "Sumérgete en un mundo de fantasía y poesía visual con Dixit, el juego de cartas donde la imaginación es tu mejor herramienta. Cada carta presenta una ilustración onírica y surrealista que los jugadores deben describir con una frase o palabra. El objetivo es que algunos jugadores adivinen tu carta, pero no todos, creando un juego sutil de pistas y engaños. Sus reglas sencillas permiten que cualquier persona pueda jugar de inmediato, fomentando la creatividad y la empatía entre los participantes. Con un arte gráfico deslumbrante que parece sacado de un libro de cuentos, cada partida es una experiencia única que estimula la mente y relaja el espíritu en un ambiente de juego inigualable.", 32.00, new ArrayList<>(Arrays.asList("src/imgProductos/dixit.jpg")), 15, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Cartas")), new AgeRange(8, 99)));
// 23
            catalog.add(new Game("Carcassonne", "Transporta a tus amigos a la Francia medieval mientras construyen juntos un paisaje lleno de ciudades fortificadas, monasterios, campos y caminos. En Carcassonne, los jugadores colocan losetas por turnos para expandir el tablero y despliegan a sus seguidores o 'meeples' para ganar puntos de control. La sencillez de sus mecánicas esconde una profundidad estratégica sorprendente donde cada loseta puede beneficiarte a ti o bloquear el avance de tus oponentes. Es un juego dinámico, rápido de aprender y visualmente gratificante conforme el mapa crece en la mesa. Perfecto para quienes buscan un juego de mesa relajado pero competitivo que ha ganado numerosos premios internacionales por su brillante diseño.", 28.00, new ArrayList<>(Arrays.asList("src/imgProductos/carcassonne.jpg")), 25, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Losetas")), new AgeRange(7, 99)));
// 24
            catalog.add(new Game("Exploding Kittens", "Prepárate para una ruleta rusa llena de adrenalina, estrategia y, sobre todo, gatos explosivos. Exploding Kittens es un juego de cartas de ritmo frenético donde el objetivo es simple: no explotes. Los jugadores roban cartas hasta que alguien saca un gatito explosivo y queda eliminado, a menos que tenga una carta de desactivación. El juego permite sabotear a tus oponentes, espiar el mazo o saltarte turnos para evitar el desastre. Con un humor irreverente y un diseño artístico único, es el juego de cartas ideal para fiestas y grupos que buscan partidas cortas llenas de risas y traiciones inesperadas. ¡Un éxito de ventas mundial que garantiza diversión explosiva en cada ronda!", 20.00, new ArrayList<>(Arrays.asList("src/imgProductos/kittens.jpg")), 50, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Azar")), new AgeRange(7, 99)));
// 25
            catalog.add(new Game("Pandemic", "El destino de la humanidad está en tus manos. En este emocionante juego cooperativo, los jugadores forman un equipo de especialistas (médicos, científicos, logistas) que deben viajar por el mundo frenando brotes de enfermedades mientras investigan las curas antes de que sea demasiado tarde. A diferencia de otros juegos, aquí no compites contra tus amigos, sino contra el propio sistema del juego que lanza epidemias constantes. La comunicación y la planificación conjunta son vitales para ganar. Pandemic ofrece una tensión constante y una satisfacción inmensa cuando logras salvar el planeta en el último segundo. Un desafío estratégico inteligente que pone a prueba la capacidad de liderazgo y trabajo en equipo de cualquier grupo.", 40.00, new ArrayList<>(Arrays.asList("src/imgProductos/pandemic.jpg")), 12, juegoCategories, emptyReviews, null, 4, new ArrayList<>(Arrays.asList("Cooperación")), new AgeRange(10, 99)));
// 26
            catalog.add(new Game("Zombicide", "Prepárate para la experiencia definitiva de supervivencia cooperativa en un mundo infestado por hordas incontrolables de no muertos. En Zombicide, tú y tus amigos asumen el papel de supervivientes con habilidades únicas que deben trabajar en equipo para completar misiones peligrosas a través de una ciudad devastada. El juego destaca por sus miniaturas detalladas y un sistema de reglas dinámico donde la adrenalina sube conforme aparecen más zombis. Cada decisión cuenta: buscar armas, subir de nivel o arriesgarse para salvar a un compañero. La tensión es constante, la estrategia es vital y la diversión está garantizada en cada partida de este juego que se ha convertido en un referente absoluto del género de terror y miniaturas.", 95.00, new ArrayList<>(Arrays.asList("src/imgProductos/zombicide.jpg")), 6, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Dados")), new AgeRange(14, 99)));
// 27
            catalog.add(new Game("Aventureros Tren", "Embárcate en un viaje ferroviario por toda Europa en este juego de mesa elegante y familiar. El objetivo es conectar ciudades icónicas mediante rutas de tren recogiendo cartas de diferentes tipos de vagones. Cuanto más largas sean tus rutas y más misiones de destino completes, más puntos obtendrás. Sin embargo, debes tener cuidado de que tus oponentes no bloqueen tus vías estratégicas. Con un tablero precioso y mecánicas fluidas, Aventureros al Tren logra un equilibrio perfecto entre la planificación a largo plazo y la competencia directa. Es fácil de enseñar a nuevos jugadores pero ofrece capas de estrategia para los veteranos, lo que lo convierte en un fijo en cualquier colección de juegos de mesa.", 44.00, new ArrayList<>(Arrays.asList("src/imgProductos/tren.jpg")), 18, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Rutas")), new AgeRange(8, 99)));
// 28
            catalog.add(new Game("Risk", "El clásico juego de conquista militar mundial te invita a liderar tus ejércitos hacia la victoria definitiva. Despliega tus tropas con astucia, planea ataques sorpresa y refuerza tus fronteras mientras intentas conquistar continentes enteros. Las batallas se resuelven mediante dados, añadiendo un factor de azar que puede cambiar el rumbo de la guerra en un instante. Las alianzas temporales y las traiciones son moneda común en este juego donde la diplomacia es tan importante como la fuerza militar. Risk es un desafío de resistencia y visión global que ha entretenido a estrategas de todo el mundo durante décadas, siendo el escenario perfecto para demostrar quién es el mejor general en el campo de batalla.", 35.00, new ArrayList<>(Arrays.asList("src/imgProductos/risk.jpg")), 22, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Dados")), new AgeRange(10, 99)));
// 29
            catalog.add(new Game("Cluedo", "Se ha cometido un asesinato en la Mansión Tudor y tú eres el encargado de resolver el misterio. ¿Fue el Coronel Mostaza en el salón con el candelabro? ¿O la Srta. Amapola con el puñal en la cocina? Los jugadores deben explorar las habitaciones, hacer sugerencias y usar el proceso de eliminación para descubrir al culpable, el arma y el lugar del crimen. Cluedo es el juego de deducción por excelencia que pone a prueba tu capacidad lógica y tu atención a los detalles. Cada pista es una pieza del rompecabezas que debes encajar antes que los demás investigadores. Un clásico de intriga que sigue siendo tan emocionante y desafiante como el primer día que salió al mercado.", 29.90, new ArrayList<>(Arrays.asList("src/imgProductos/cluedo.jpg")), 20, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Deducción")), new AgeRange(8, 99)));
// 30
            catalog.add(new Game("Uno", "El juego de cartas más famoso del mundo es sinónimo de diversión rápida y competencia feroz. El objetivo es ser el primero en quedarse sin cartas en la mano igualando números o colores con la carta del mazo central. Pero no es tan fácil: las cartas especiales como el 'Chupa 4', el 'Cambio de sentido' o el 'Salto de turno' pueden arruinar tu estrategia en el último momento. Y lo más importante, ¡no olvides gritar UNO cuando te quede solo una carta! Es un juego universal, fácil de transportar y perfecto para cualquier edad o lugar, garantizando risas, piques sanos y una rejugabilidad infinita que lo hace indispensable en cualquier hogar del planeta.", 10.00, new ArrayList<>(Arrays.asList("src/imgProductos/uno.jpg")), 100, juegoCategories, emptyReviews, null, 10, new ArrayList<>(Arrays.asList("Cartas")), new AgeRange(6, 99)));
            globalCategories.addAll(Arrays.asList(catComic, catFigura, catJuego));
            System.out.println("[Sistema] Catálogo inicializado con 30 productos reales.");

        } catch (Exception e) {
            System.err.println("Error al crear los productos por defecto: " + e.getMessage());
            e.printStackTrace();
        }

        // =========================================================
        // 3. INICIALIZACIÓN DE INTERCAMBIOS POR DEFECTO (PRUEBAS)
        // =========================================================
        try {
            // a) Creamos un par de clientes "falsos" para que sean los dueños
            Client cliente1 = new Client("Gon67", "pass", "Gon Freecss", "11111111A", "05/05/2005", "gon@mail.com", "600111222");
            Client cliente2 = new Client("asus09", "pass", "Asus Gamer", "22222222B", "10/10/2000", "asus@mail.com", "600333444");
            Client cliente3 = new Client("akatsuki9", "pass", "Itachi Uchiha", "33333333C", "09/06/1998", "itachi@mail.com", "600555666");

            users.put(cliente1.getUsername(), cliente1);
            users.put(cliente2.getUsername(), cliente2);
            users.put(cliente3.getUsername(), cliente3);

            // c) Creamos los productos de Segunda Mano usando ArrayList
            SecondHandProduct sh1 = new SecondHandProduct(
                    "Figura Killua (HXH)", "Figura de la guerra con las hormigas",
                    new ArrayList<>(Arrays.asList("gon.jpg")), 100.0, true, ItemType.FIGURINE, Condition.PERFECTO, cliente1);

            SecondHandProduct sh2 = new SecondHandProduct(
                    "Funko de Po (Kung Fu Panda)", "Tercera película",
                    new ArrayList<>(Arrays.asList("pikachu.jpg")), 15.0, true, ItemType.FIGURINE, Condition.USO_LIGERO, cliente2);

            SecondHandProduct sh3 = new SecondHandProduct(
                    "Manga Naruto volumen 23", "Edición antigua Glénat",
                    new ArrayList<>(Arrays.asList("naruto1.jpg")), 21.0, true, ItemType.COMIC, Condition.USO_EVIDENTE, cliente3);

            // d) Los añadimos a la lista global de intercambios de la aplicación
            secondHandProducts.add(sh1);
            secondHandProducts.add(sh2);
            secondHandProducts.add(sh3);

            System.out.println("[Sistema] Intercambios de prueba inicializados.");

            // =========================================================
            // 4. INICIALIZACIÓN DE PEDIDOS POR DEFECTO (PRUEBAS)
            // =========================================================
            try {
                // Usamos a cliente1 (Gon67) que ya está creado arriba
                // Metemos productos en su carrito
                cliente1.addToCart(catalog.get(0), 1); // 1x One Piece Vol 1
                cliente1.addToCart(catalog.get(10), 1); // 1x Figura Gon

                // Creamos el pedido a mano copiando los items del carrito para no usar la pasarela del profesor
                Order pedido1 = new Order(cliente1, new ArrayList<>(cliente1.getShoppingCart().getCartItems()), cliente1.getShoppingCart().getPrice());
                pedido1.setOrderStatus(OrderStatus.EN_PREPARACION); // Le forzamos el estado

                // Lo guardamos en el historial del cliente y vaciamos el carrito
                cliente1.getOrders().add(pedido1);
                cliente1.getOrderHistoric().addOrder(pedido1);
                cliente1.getShoppingCart().clearCart();

                // Usamos a cliente2 (asus09) para otro pedido
                cliente2.addToCart(catalog.get(20), 1); // 1x Juego Catan

                Order pedido2 = new Order(cliente2, new ArrayList<>(cliente2.getShoppingCart().getCartItems()), cliente2.getShoppingCart().getPrice());
                pedido2.setOrderStatus(OrderStatus.SIN_PAGAR);

                cliente2.getOrders().add(pedido2);
                cliente2.getOrderHistoric().addOrder(pedido2);
                cliente2.getShoppingCart().clearCart();

                System.out.println("[Sistema] Pedidos de prueba inicializados.");

            } catch (Exception e) {
                System.err.println("Error al crear pedidos por defecto: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error al crear intercambios por defecto: " + e.getMessage());
        }
    }

    public static int getEdadMinimaRegistro() {
        return EdadMinimaRegistro;
    }
    public boolean setEdadMinimaRegistro(int edad) {
        if (edad > 0 && edad <= 99){
            EdadMinimaRegistro = edad;
        return true;
    } return false;
    }


    // --- RESTO DE MÉTODOS DE LA CLASE (Login, Registro, Persistencia, etc.) ---
    public static RegisteredUser login(String username, String password) throws IOException {
        RegisteredUser user = Application.users.get(username);
        if (user == null || !(user.login(username, password))) {
            throw new IOException("Incorrect username or password.");
        }
        return user;
    }

    public static void registerClient(Client client) throws IOException {
        if (users.containsKey(client.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }
        Application.users.put(client.getUsername(), client);
    }

    public static void registerEmployee(Employee employee) throws IOException {
        if (users.containsKey(employee.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }
        users.put(employee.getUsername(), employee);
    }

    public static ArrayList<NewProduct> getCatalog() { return catalog; }
    public static ArrayList<RegisteredUser> getUsers() { return new ArrayList<>(users.values()); }
    public static ArrayList<SecondHandProduct> getSecondHandProducts() { return secondHandProducts; }
    public static List<ExchangeOffer> getoffersmade(Client c){ return new ArrayList<>(c.getOffersMade()); }
    public static List<ExchangeOffer> getoffersreceived(Client c){ return new ArrayList<>(c.obtenerMisOfertasRecibidos()); }
    public static void addSecondHandProduct(SecondHandProduct p) { secondHandProducts.add(p); }
    public static ArrayList<Category> getGlobalCategories() { return globalCategories; }
    public static void addCategory(Category c) { globalCategories.add(c); }
    public static ArrayList<IDiscount> getGlobalDiscounts() { return globalDiscounts; }
    public static void addDiscount(IDiscount d) { globalDiscounts.add(d); }

    public static void guardarDatos(String rutaArchivo) {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(rutaArchivo))) {
            oos.writeObject(users);
            oos.writeObject(notifications);
            oos.writeObject(secondHandProducts);
            oos.writeObject(catalog);
            oos.writeObject(globalCategories);
            oos.writeObject(globalDiscounts);
        } catch (java.io.IOException e) {
            System.out.println("[!] Error al guardar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarDatos(String rutaArchivo) {
        java.io.File archivo = new java.io.File(rutaArchivo);
        if (!archivo.exists()) return;
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(rutaArchivo))) {
            users = (java.util.HashMap<String, RegisteredUser>) ois.readObject();
            notifications = (java.util.HashMap<String, Notification>) ois.readObject();
            secondHandProducts = (java.util.ArrayList<SecondHandProduct>) ois.readObject();
            catalog = (java.util.ArrayList<NewProduct>) ois.readObject();
            globalCategories = (java.util.ArrayList<Category>) ois.readObject();
            globalDiscounts = (java.util.ArrayList<IDiscount>) ois.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("[!] Error al cargar: " + e.getMessage());
        }
    }
}
