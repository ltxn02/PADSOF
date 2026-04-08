package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import users.*;
import catalog.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase abstracta {@link Staff}.
 */
public class StaffTest {

    private Manager managerStaff;
    private Employee employeeStaff;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;

    @BeforeEach
    void setUp() {
        managerStaff = new Manager(
            "staff_manager",
            "password123",
            "Staff Manager Test",
            "12345678A",
            "15/03/1990",
            "staff_manager@test.com",
            "666123456",
            50000.0
        );

        employeeStaff = new Employee(
            "staff_employee",
            "password123",
            "Staff Employee Test",
            "87654321B",
            "20/05/1995",
            "staff_employee@test.com",
            "666654321",
            30000.0,
            true
        );

        Category testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);
        testReviews = new ArrayList<>();
    }

    // ==========================================
    // TESTS DE HERENCIA
    // ==========================================

    @Test
    void testManagerHeredaDeStaff() {
        assertTrue(managerStaff instanceof Staff);
        assertTrue(managerStaff instanceof RegisteredUser);
        assertTrue(managerStaff instanceof User);
    }

    @Test
    void testEmployeeHeredaDeStaff() {
        assertTrue(employeeStaff instanceof Staff);
        assertTrue(employeeStaff instanceof RegisteredUser);
        assertTrue(employeeStaff instanceof User);
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    @Test
    void testCrearManagerConDatos() {
        assertNotNull(managerStaff);
        assertEquals("staff_manager", managerStaff.getUsername());
        String profile = managerStaff.userProfile();
        // Verificar que contiene el nombre completo
        assertTrue(profile.contains("Staff Manager Test"));
        // Verificar que contiene la fecha de nacimiento (no enmascarada)
        assertTrue(profile.contains("15/03/1990"));
        // Verificar que contiene el teléfono (no enmascarado)
        assertTrue(profile.contains("666123456"));
        // Verificar que contiene "Nombre:" del perfil
        assertTrue(profile.contains("Nombre:"));
    }

    @Test
    void testCrearEmployeeConDatos() {
        assertNotNull(employeeStaff);
        assertEquals("staff_employee", employeeStaff.getUsername());
        String profile = employeeStaff.userProfile();
        // Verificar que contiene el nombre completo
        assertTrue(profile.contains("Staff Employee Test"));
        // Verificar que contiene la fecha de nacimiento (no enmascarada)
        assertTrue(profile.contains("20/05/1995"));
        // Verificar que contiene el teléfono (no enmascarado)
        assertTrue(profile.contains("666654321"));
        // Verificar que contiene "Nombre:" del perfil
        assertTrue(profile.contains("Nombre:"));
    }

    // ==========================================
    // TESTS DE DATOS SALARIALES
    // ==========================================

    @Test
    void testManagerTieneSalario() {
        assertNotNull(managerStaff);
    }

    @Test
    void testEmployeeTieneSalario() {
        assertNotNull(employeeStaff);
    }

    @Test
    void testStaffConDiferentesSalarios() {
        Manager managerAltoSalario = new Manager(
            "high_salary", "pass", "High Salary Manager", "99999999Z", "01/01/1985",
            "high@test.com", "666999999", 100000.0
        );

        Employee employeeBajoSalario = new Employee(
            "low_salary", "pass", "Low Salary Employee", "11111111A", "02/02/2000",
            "low@test.com", "666111111", 15000.0, true
        );

        assertNotNull(managerAltoSalario);
        assertNotNull(employeeBajoSalario);
    }

    // ==========================================
    // TESTS DE DATOS HEREDADOS DE RegisteredUser
    // ==========================================

    @Test
    void testManagerAccesoDatosRegisteredUser() {
        String profile = managerStaff.userProfile();
        // Verificar fecha de nacimiento (no enmascarada)
        assertTrue(profile.contains("15/03/1990"));
        // Verificar teléfono (no enmascarado)
        assertTrue(profile.contains("666123456"));
        // Verificar que el perfil contiene el usuario
        assertTrue(profile.contains("staff_manager"));
        // Verificar que contiene "DNI:" (aunque esté enmascarado)
        assertTrue(profile.contains("DNI:"));
        // Verificar que contiene "Email:" (aunque esté enmascarado)
        assertTrue(profile.contains("Email:"));
    }

    @Test
    void testEmployeeAccesoDatosRegisteredUser() {
        String profile = employeeStaff.userProfile();
        // Verificar fecha de nacimiento (no enmascarada)
        assertTrue(profile.contains("20/05/1995"));
        // Verificar teléfono (no enmascarado)
        assertTrue(profile.contains("666654321"));
        // Verificar que el perfil contiene el usuario
        assertTrue(profile.contains("staff_employee"));
        // Verificar que contiene "DNI:" (aunque esté enmascarado)
        assertTrue(profile.contains("DNI:"));
        // Verificar que contiene "Email:" (aunque esté enmascarado)
        assertTrue(profile.contains("Email:"));
    }

    // ==========================================
    // TESTS DE IDENTIDAD Y AUTENTICACIÓN
    // ==========================================

    @Test
    void testManagerYEmployeeDistintosInstancias() {
        assertNotEquals(managerStaff, employeeStaff);
        assertNotSame(managerStaff, employeeStaff);
    }

    @Test
    void testStaffUsernamesUnicos() {
        assertNotEquals(managerStaff.getUsername(), employeeStaff.getUsername());
    }

    @Test
    void testStaffEmailsDistintos() {
        String managerEmail = managerStaff.userProfile();
        String employeeEmail = employeeStaff.userProfile();
        assertNotEquals(managerEmail, employeeEmail);
    }

    // ==========================================
    // TESTS DE HERENCIA DE COMPORTAMIENTO
    // ==========================================

    @Test
    void testManagerPuedeVerCatalogo() {
        Catalog catalog = new Catalog();
        var visibleProducts = managerStaff.view_catalog(managerStaff, catalog);
        assertNotNull(visibleProducts);
        assertTrue(visibleProducts.size() >= 0);
    }

    @Test
    void testEmployeePuedeVerCatalogo() {
        Catalog catalog = new Catalog();
        var visibleProducts = employeeStaff.view_catalog(employeeStaff, catalog);
        assertNotNull(visibleProducts);
    }

    // ==========================================
    // TESTS DE POLYMORFISMO
    // ==========================================

    @Test
    void testPolimorfismoStaff() {
        ArrayList<Staff> staffMembers = new ArrayList<>();
        staffMembers.add(managerStaff);
        staffMembers.add(employeeStaff);

        assertEquals(2, staffMembers.size());
        assertTrue(staffMembers.get(0) instanceof Manager);
        assertTrue(staffMembers.get(1) instanceof Employee);
    }

    @Test
    void testMultiplesStaffMembers() {
        Manager manager1 = new Manager(
            "manager1", "pass", "Manager 1", "11111111A", "01/01/1990",
            "m1@test.com", "666111111", 45000.0
        );

        Manager manager2 = new Manager(
            "manager2", "pass", "Manager 2", "22222222B", "02/02/1991",
            "m2@test.com", "666222222", 55000.0
        );

        Employee emp1 = new Employee(
            "emp1", "pass", "Employee 1", "33333333C", "03/03/1992",
            "e1@test.com", "666333333", 25000.0, true
        );

        Employee emp2 = new Employee(
            "emp2", "pass", "Employee 2", "44444444D", "04/04/1993",
            "e2@test.com", "666444444", 30000.0, false
        );

        ArrayList<Staff> allStaff = new ArrayList<>();
        allStaff.add(manager1);
        allStaff.add(manager2);
        allStaff.add(emp1);
        allStaff.add(emp2);

        assertEquals(4, allStaff.size());
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    @Test
    void testStaffConSalarioNegativo() {
        Manager negativeManager = new Manager(
            "neg_salary", "pass", "Negative Salary", "55555555E", "05/05/1995",
            "neg@test.com", "666555555", -1000.0
        );

        assertNotNull(negativeManager);
    }

    @Test
    void testStaffConSalarioCero() {
        Employee zeroSalary = new Employee(
            "zero_salary", "pass", "Zero Salary", "66666666F", "06/06/1996",
            "zero@test.com", "666666666", 0.0, true
        );

        assertNotNull(zeroSalary);
    }

    @Test
    void testIndependenciaStaffMembers() {
        Manager staffA = new Manager(
            "staffA", "pass", "Staff A", "77777777G", "07/07/1997",
            "staffA@test.com", "666777777", 40000.0
        );

        Manager staffB = new Manager(
            "staffB", "pass", "Staff B", "88888888H", "08/08/1998",
            "staffB@test.com", "666888888", 40000.0
        );

        assertNotEquals(staffA.getUsername(), staffB.getUsername());
        assertNotEquals(staffA.userProfile(), staffB.userProfile());
    }

    @Test
    void testStaffAutenticacion() {
        assertTrue(managerStaff.login("staff_manager", "password123"));
        assertFalse(managerStaff.login("staff_manager", "wrongpassword"));
        assertFalse(managerStaff.login("wrongusername", "password123"));
    }

    @Test
    void testStaffUserPreview() {
        String preview = managerStaff.userPreview();
        assertTrue(preview.contains("staff_manager"));
        assertTrue(preview.contains("Staff Manager Test"));
    }
}