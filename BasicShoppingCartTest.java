import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// 假设 Supermarket, Member, Book, Food 等已正确初始化

public class BasicShoppingCartTest {

    private Supermarket s;
    private Member m;
    private Product p1;
    private Product p2;

    @BeforeEach
    void setupCartEnv() {
        s = new Supermarket();
        m = new Member("Grace");
        p1 = new Book("Test Book", 50, 10);
        p2 = new Food("Test Food", 5, 20);
        s.addProduct(p1);
        s.addProduct(p2);
    }

    @Test
    void test10_CartAddAndCheckoutNoPoints() {
        ShoppingCart cart = new ShoppingCart(m, s);

        cart.add("Test Book", 2); // $100
        cart.add("Test Food", 5); // $25

        assertEquals(2, cart.getItems().size());

        int initialPoints = m.getPoints();
        int initialStock1 = s.getStock("Test Book"); // 10

        cart.checkout(); // checkout(false)

        assertEquals(initialPoints + 125, m.getPoints());
        assertEquals(0, cart.getItems().size());
        assertEquals(initialStock1 - 2, s.getStock("Test Book")); // 8
    }
}