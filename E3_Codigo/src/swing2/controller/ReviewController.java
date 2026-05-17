package swing2.controller;

import catalog.NewProduct;
import users.Client;
import utils.Review;
import users.RegisteredUser;

/**
 * Controlador encargado de gestionar el ciclo de vida y la persistencia de las
 * reseñas y valoraciones del catálogo dentro del módulo correspondiente.
 * Actúa como intermediario para garantizar el cumplimiento de las reglas de negocio
 * asociadas a la creación de opiniones sobre los artículos.
 * 
 * @author Taha Ridda
 */
public class ReviewController {

	/**
     * Procesa, valida y adjunta una nueva calificación y comentario a un producto específico.
     * Evalúa que el autor cumpla con el rol de cliente antes de instanciar y añadir 
     * la reseña a la estructura del artículo.
     * 
     * @param producto   El artículo del catálogo (NewProduct) que recibe la calificación.
     * @param autor      El usuario del sistema (RegisteredUser) que intenta publicar la opinión.
     * @param estrellas  La puntuación numérica otorgada al artículo.
     * @param comentario El texto descriptivo con el cuerpo de la opinión o valoración.
     */
    public static void guardarReseña(NewProduct producto, RegisteredUser autor, int estrellas, String comentario) {

        if (autor instanceof Client) {
            Client cliente = (Client) autor;



            Review nuevaReview = new Review(estrellas, comentario, producto, cliente);


            producto.addReview(nuevaReview);

            System.out.println("[Sistema] Reseña añadida a: " + producto.getName());
        } else {
            System.err.println("[Error] Solo los clientes pueden dejar reseñas.");
        }
    }
}