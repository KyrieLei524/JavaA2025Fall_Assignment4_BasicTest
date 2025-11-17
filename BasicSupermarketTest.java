import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class BasicSupermarketTest {

    private Supermarket supermarket;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        supermarket = new Supermarket();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void test4_AddNewProduct() {
        Product p = new Book("Physics", 50, 20);
        supermarket.addProduct(p);

        assertEquals(1, supermarket.getInventory().size());
        assertEquals(20, supermarket.getStock("Physics"));
    }

    @Test
    void test5_MergeStock() {
        supermarket.addProduct(new Food("Tomato", 2, 10));
        supermarket.addProduct(new Food("Tomato", 2, 5));

        assertEquals(1, supermarket.getInventory().size());
        assertEquals(15, supermarket.getStock("Tomato"));
    }

    @Test
    void test6_ReduceStockAndRemoval() {
        supermarket.addProduct(new Food("Cereal", 4, 5)); // Stock 5
        supermarket.reduceStock("Cereal", 10); // Reduce 10

        assertEquals(0, supermarket.getStock("Cereal"));
        assertNull(supermarket.getProduct("Cereal"));
        assertEquals(0, supermarket.getInventory().size());
    }
}