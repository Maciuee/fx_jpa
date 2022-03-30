import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.model.Student;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjektTest {
 private static EntityManagerFactory entityManagerFactory;
 private static EntityManager entityManager;
 @BeforeAll
 public static void init() throws FileNotFoundException, SQLException {
 entityManagerFactory = Persistence.createEntityManagerFactory("testHsqlManager");
 entityManager = entityManagerFactory.createEntityManager();
 }
 @AfterAll
 public static void afterAll() {
 entityManager.close();
 entityManagerFactory.close();
 }
 // --- URUCHAMIANIE TEST�W ---
 // ABY URUCHOMI� TESTY KLIKNIJ NA NAZWIE KLASY PRAWYM PRZYCISKIEM
 // MYSZY I WYBIERZ Z MENU 'Run As' -> 'Gradle Test' LUB PO USTAWIENIU
 // KURSORA NA NAZWIE KLASY WCI�NIJ SKR�T 'CTRL+ALT+X' A PӏNIEJ 'G'
 // MO�NA R�WNIE� ANALOGICZNIE URUCHAMIA� POJEDYNCZE METODY KLIKAJ�C
 // WCZE�NIEJ NA ICH NAZW�
 
 // PO ZAKO�CZENIU TEST�W W PODKATALOGU PROJEKTU DOST�PNY JEST SZCZEGӣOWY RAPORT
 // project-jpa-client\build\reports\tests\test\index.html,
 // JEGO ADRES DRUKOWANY JEST W KONSOLI, GDY JAKI� TEST ZAKO�CZY SI� B��DEM NP.
 // file:///C:/eclipse-2018-09/workspace/project-jfx-client/build/reports/tests/test/index.html
 @Test
 @Order(1)
 public void dodawanieProjektuZZadaniami() {
 Projekt projekt = new Projekt("Aplikacji webowa", "Aplikacja w Javie", LocalDate.of(2020, 6, 19));
 Zadanie zadanie1 = new Zadanie("Instalacja kontenera serwlet�w", "Instalacja serwera Tomcat 9.0.33", 1);
 Zadanie zadanie2 = new Zadanie("Implementacja aplikacji", "Zgodna z wzorcem MVC", 2);
 //przypisujemy do zada� projekt
 zadanie1.setProjekt(projekt);
 zadanie2.setProjekt(projekt);
 entityManager.getTransaction().begin();
 entityManager.persist(zadanie1); //utrwalanie zawsze dla wszystkich obiekt�w - projektu i jego zada�
 entityManager.persist(zadanie2);
 entityManager.persist(projekt);
 entityManager.getTransaction().commit();
 entityManager.refresh(projekt); //od�wie�enie stanu zarz�dzanej encji 
 //na podstawie informacji z bazy danych
 //sprawdzamy czy w bazie danych do projektu zosta�y przypisane zadania
 List<Zadanie> zadania = projekt.getZadania();
 assertNotNull(zadania);
 assertEquals(2, zadania.size());
 
 System.out.printf("Projekt - Id: %d, Nazwa: %s%n", projekt.getProjektId(), projekt.getNazwa());
 for (Zadanie zad : zadania) {
 System.out.printf(" Zadanie - Id: %d, Nazwa: %s%n", zad.getZadanieId(), zad.getNazwa());
 }
 }
 @Test
 @Order(2)
 public void usuwanieProjektuZZadaniami() {
 // TODO
 // Pami�taj, �e dane w powi�zanych tabelach naszego modelu nie b�d� 
 // automatycznie modyfikowane (ON DELETE NO ACTION, ON UPDATE NO ACTION). 
 // Przy ka�dej pr�bie usuwania, czy modyfikacji projektu, do kt�rego s� 
 // odwo�ania przez warto�ci kluczy obcych, zawsze generowany b�dzie b��d, 
 // a polecenie DELETE lub UPDATE wycofywane.
 // Nale�y zatem zacz�� od usuwania zada�.
 }
 @Test
 @Order(3)
 public void dodawanieProjektuZeStudentamiIZadaniami() {
 // TODO
 }
 
 @Test
 @Order(4)
 public void wyszukiwanieProjektuZeStudentamiIZadaniemTomcat() {
 // TODO
 // Pobierz projekty, do kt�rych zosta�o przypisanych co najmniej dw�ch student�w 
 // i kt�re maja w nazwie lub opisie jakiegokolwiek zadania s�owo 'Tomcat'.
 }
 
 @BeforeEach
 public void before(TestInfo testInfo) {
 System.out.printf("-- METODA -> %s%n", testInfo.getTestMethod().get().getName());
 }
 @AfterEach
 public void after(TestInfo testInfo) {
 System.out.printf("<- KONIEC -- %s%n", testInfo.getTestMethod().get().getName());
 }
