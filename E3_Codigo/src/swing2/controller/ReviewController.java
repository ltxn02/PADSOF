package swing2.controller;

import catalog.NewProduct;
import users.Client;
import utils.Review;
import users.RegisteredUser;

public class ReviewController {

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