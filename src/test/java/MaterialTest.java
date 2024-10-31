import com.example.demoplswork.model.Material;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaterialTest {
    private Material material;

    @BeforeEach
    public void setUp() {
        material = new Material("Screws", 50, 3.5);
    }

    @Test
    public void getName() {
        assertEquals("Screws", material.getName());
    }
    @Test
    public void testString() { assertInstanceOf(String.class, material.getName()); }

    @Test
    public void getQuantity() {
        assertEquals(50, material.getQuantity());
    }
    @Test
    public void testPositive1() { assertTrue(material.getQuantity() > 0); }

    @Test
    public void getPrice() { assertEquals(3.5, material.getPrice()); }
    @Test
    public void testPositive2() { assertTrue(material.getPrice() > 0); }

}
